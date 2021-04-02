// Copyright 2018-2021 the Deno authors. All rights reserved. MIT license.

//! This module provides file formatting utilities using
//! [`dprint-plugin-typescript`](https://github.com/dprint/dprint-plugin-typescript).
//!
//! At the moment it is only consumed using CLI but in
//! the future it can be easily extended to provide
//! the same functions as ops available in JS runtime.

use crate::colors;
use crate::diff::diff;
use crate::file_watcher;
use crate::fs_util::{collect_files, get_extension, is_supported_ext_fmt};
use crate::text_encoding;
use deno_core::error::generic_error;
use deno_core::error::AnyError;
use deno_core::futures;
use deno_core::futures::FutureExt;
use std::fs;
use std::io::stdin;
use std::io::stdout;
use std::io::Read;
use std::io::Write;
use std::path::Path;
use std::path::PathBuf;
use std::sync::atomic::{AtomicUsize, Ordering};
use std::sync::{Arc, Mutex};

const BOM_CHAR: char = '\u{FEFF}';

/// Format JavaScript/TypeScript files.
pub async fn format(
    args: Vec<PathBuf>,
    ignore: Vec<PathBuf>,
    check: bool,
    watch: bool,
) -> Result<(), AnyError> {
    let target_file_resolver = || {
        // collect the files that are to be formatted
        collect_files(&args, &ignore, is_supported_ext_fmt).and_then(|files| {
            if files.is_empty() {
                Err(generic_error("No target files found."))
            } else {
                Ok(files)
            }
        })
    };
    let operation = |paths: Vec<PathBuf>| {
        let config = get_typescript_config();
        async move {
            if check {
                check_source_files(config, paths).await?;
            } else {
                format_source_files(config, paths).await?;
            }
            Ok(())
        }
            .boxed_local()
    };

    if watch {
        file_watcher::watch_func(target_file_resolver, operation, "Fmt").await?;
    } else {
        operation(target_file_resolver()?).await?;
    }

    Ok(())
}

/// Formats markdown (using https://github.com/dprint/dprint-plugin-markdown) and its code blocks
/// (ts/tsx, js/jsx).
fn format_markdown(
    file_text: &str,
    ts_config: dprint_plugin_typescript::configuration::Configuration,
) -> Result<String, String> {
    let md_config = get_markdown_config();
    dprint_plugin_markdown::format_text(
        &file_text,
        &md_config,
        Box::new(move |tag, text, line_width| {
            let tag = tag.to_lowercase();
            if matches!(
        tag.as_str(),
        "ts"
          | "tsx"
          | "js"
          | "jsx"
          | "javascript"
          | "typescript"
          | "json"
          | "jsonc"
      ) {
                // It's important to tell dprint proper file extension, otherwise
                // it might parse the file twice.
                let extension = match tag.as_str() {
                    "javascript" => "js",
                    "typescript" => "ts",
                    rest => rest,
                };

                if matches!(extension, "json" | "jsonc") {
                    let mut json_config = get_json_config();
                    json_config.line_width = line_width;
                    dprint_plugin_json::format_text(&text, &json_config)
                } else {
                    let fake_filename =
                        PathBuf::from(format!("deno_fmt_stdin.{}", extension));
                    let mut codeblock_config = ts_config.clone();
                    codeblock_config.line_width = line_width;
                    dprint_plugin_typescript::format_text(
                        &fake_filename,
                        &text,
                        &codeblock_config,
                    )
                }
            } else {
                Ok(text.to_string())
            }
        }),
    )
}

/// Formats JSON and JSONC using the rules provided by .deno()
/// of configuration builder of https://github.com/dprint/dprint-plugin-json.
/// See https://git.io/Jt4ht for configuration.
fn format_json(file_text: &str) -> Result<String, String> {
    let json_config = get_json_config();
    dprint_plugin_json::format_text(&file_text, &json_config)
}

async fn check_source_files(
    config: dprint_plugin_typescript::configuration::Configuration,
    paths: Vec<PathBuf>,
) -> Result<(), AnyError> {
    let not_formatted_files_count = Arc::new(AtomicUsize::new(0));
    let checked_files_count = Arc::new(AtomicUsize::new(0));

    // prevent threads outputting at the same time
    let output_lock = Arc::new(Mutex::new(0));

    run_parallelized(paths, {
        let not_formatted_files_count = not_formatted_files_count.clone();
        let checked_files_count = checked_files_count.clone();
        move |file_path| {
            checked_files_count.fetch_add(1, Ordering::Relaxed);
            let file_text = read_file_contents(&file_path)?.text;
            let ext = get_extension(&file_path).unwrap_or_else(String::new);
            let r = if ext == "md" {
                format_markdown(&file_text, config.clone())
            } else if matches!(ext.as_str(), "json" | "jsonc") {
                format_json(&file_text)
            } else {
                dprint_plugin_typescript::format_text(&file_path, &file_text, &config)
            };
            match r {
                Ok(formatted_text) => {
                    if formatted_text != file_text {
                        not_formatted_files_count.fetch_add(1, Ordering::Relaxed);
                        let _g = output_lock.lock().unwrap();
                        let diff = diff(&file_text, &formatted_text);
                        info!("");
                        info!("{} {}:", colors::bold("from"), file_path.display());
                        info!("{}", diff);
                    }
                }
                Err(e) => {
                    let _g = output_lock.lock().unwrap();
                    eprintln!("Error checking: {}", file_path.to_string_lossy());
                    eprintln!("   {}", e);
                }
            }
            Ok(())
        }
    })
        .await?;

    let not_formatted_files_count =
        not_formatted_files_count.load(Ordering::Relaxed);
    let checked_files_count = checked_files_count.load(Ordering::Relaxed);
    let checked_files_str =
        format!("{} {}", checked_files_count, files_str(checked_files_count));
    if not_formatted_files_count == 0 {
        info!("Checked {}", checked_files_str);
        Ok(())
    } else {
        let not_formatted_files_str = files_str(not_formatted_files_count);
        Err(generic_error(format!(
            "Found {} not formatted {} in {}",
            not_formatted_files_count, not_formatted_files_str, checked_files_str,
        )))
    }
}

async fn format_source_files(
    config: dprint_plugin_typescript::configuration::Configuration,
    paths: Vec<PathBuf>,
) -> Result<(), AnyError> {
    let formatted_files_count = Arc::new(AtomicUsize::new(0));
    let checked_files_count = Arc::new(AtomicUsize::new(0));
    let output_lock = Arc::new(Mutex::new(0)); // prevent threads outputting at the same time

    run_parallelized(paths, {
        let formatted_files_count = formatted_files_count.clone();
        let checked_files_count = checked_files_count.clone();
        move |file_path| {
            checked_files_count.fetch_add(1, Ordering::Relaxed);
            let file_contents = read_file_contents(&file_path)?;
            let ext = get_extension(&file_path).unwrap_or_else(String::new);
            let r = if ext == "md" {
                format_markdown(&file_contents.text, config.clone())
            } else if matches!(ext.as_str(), "json" | "jsonc") {
                format_json(&file_contents.text)
            } else {
                dprint_plugin_typescript::format_text(
                    &file_path,
                    &file_contents.text,
                    &config,
                )
            };
            match r {
                Ok(formatted_text) => {
                    if formatted_text != file_contents.text {
                        write_file_contents(
                            &file_path,
                            FileContents {
                                had_bom: file_contents.had_bom,
                                text: formatted_text,
                            },
                        )?;
                        formatted_files_count.fetch_add(1, Ordering::Relaxed);
                        let _g = output_lock.lock().unwrap();
                        info!("{}", file_path.to_string_lossy());
                    }
                }
                Err(e) => {
                    let _g = output_lock.lock().unwrap();
                    eprintln!("Error formatting: {}", file_path.to_string_lossy());
                    eprintln!("   {}", e);
                }
            }
            Ok(())
        }
    })
        .await?;

    let formatted_files_count = formatted_files_count.load(Ordering::Relaxed);
    debug!(
        "Formatted {} {}",
        formatted_files_count,
        files_str(formatted_files_count),
    );

    let checked_files_count = checked_files_count.load(Ordering::Relaxed);
    info!(
        "Checked {} {}",
        checked_files_count,
        files_str(checked_files_count)
    );

    Ok(())
}

/// Format stdin and write result to stdout.
/// Treats input as TypeScript or as set by `--ext` flag.
/// Compatible with `--check` flag.
pub fn format_stdin(check: bool, ext: String) -> Result<(), AnyError> {
    let mut source = String::new();
    if stdin().read_to_string(&mut source).is_err() {
        return Err(generic_error("Failed to read from stdin"));
    }
    let config = get_typescript_config();
    let r = if ext.as_str() == "md" {
        format_markdown(&source, config)
    } else if matches!(ext.as_str(), "json" | "jsonc") {
        format_json(&source)
    } else {
        // dprint will fallback to jsx parsing if parsing this as a .ts file doesn't work
        dprint_plugin_typescript::format_text(
            &PathBuf::from("_stdin.ts"),
            &source,
            &config,
        )
    };
    match r {
        Ok(formatted_text) => {
            if check {
                if formatted_text != source {
                    println!("Not formatted stdin");
                }
            } else {
                stdout().write_all(formatted_text.as_bytes())?;
            }
        }
        Err(e) => {
            return Err(generic_error(e));
        }
    }
    Ok(())
}

fn files_str(len: usize) -> &'static str {
    if len <= 1 {
        "file"
    } else {
        "files"
    }
}

fn get_typescript_config(
) -> dprint_plugin_typescript::configuration::Configuration {
    dprint_plugin_typescript::configuration::ConfigurationBuilder::new()
        .deno()
        .build()
}

fn get_markdown_config() -> dprint_plugin_markdown::configuration::Configuration
{
    dprint_plugin_markdown::configuration::ConfigurationBuilder::new()
        .deno()
        .build()
}

fn get_json_config() -> dprint_plugin_json::configuration::Configuration {
    dprint_plugin_json::configuration::ConfigurationBuilder::new()
        .deno()
        .build()
}

struct FileContents {
    text: String,
    had_bom: bool,
}

fn read_file_contents(file_path: &Path) -> Result<FileContents, AnyError> {
    let file_bytes = fs::read(&file_path)?;
    let charset = text_encoding::detect_charset(&file_bytes);
    let file_text = text_encoding::convert_to_utf8(&file_bytes, charset)?;
    let had_bom = file_text.starts_with(BOM_CHAR);
    let text = if had_bom {
        // remove the BOM
        String::from(&file_text[BOM_CHAR.len_utf8()..])
    } else {
        String::from(file_text)
    };

    Ok(FileContents { text, had_bom })
}

fn write_file_contents(
    file_path: &Path,
    file_contents: FileContents,
) -> Result<(), AnyError> {
    let file_text = if file_contents.had_bom {
        // add back the BOM
        format!("{}{}", BOM_CHAR, file_contents.text)
    } else {
        file_contents.text
    };

    Ok(fs::write(file_path, file_text)?)
}

pub async fn run_parallelized<F>(
    file_paths: Vec<PathBuf>,
    f: F,
) -> Result<(), AnyError>
    where
        F: FnOnce(PathBuf) -> Result<(), AnyError> + Send + 'static + Clone,
{
    let handles = file_paths.iter().map(|file_path| {
        let f = f.clone();
        let file_path = file_path.clone();
        tokio::task::spawn_blocking(move || f(file_path))
    });
    let join_results = futures::future::join_all(handles).await;

    // find the tasks that panicked and let the user know which files
    let panic_file_paths = join_results
        .iter()
        .enumerate()
        .filter_map(|(i, join_result)| {
            join_result
                .as_ref()
                .err()
                .map(|_| file_paths[i].to_string_lossy())
        })
        .collect::<Vec<_>>();
    if !panic_file_paths.is_empty() {
        panic!("Panic formatting: {}", panic_file_paths.join(", "))
    }

    // check for any errors and if so return the first one
    let mut errors = join_results.into_iter().filter_map(|join_result| {
        join_result
            .ok()
            .map(|handle_result| handle_result.err())
            .flatten()
    });

    if let Some(e) = errors.next() {
        Err(e)
    } else {
        Ok(())
    }
}
