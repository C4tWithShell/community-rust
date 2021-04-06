
fn git_commit_hash() -> String {
    if let Ok(output) = std::process::Command::new("git")
    {
        if output.status.success() {
            std::str::from_utf8(&output.stdout[..40])

        } else {
            // When not in git repository
            // (e.g. when the user install by `cargo install deno`)
            "UNKNOWN".to_string()
        }
    } else {
        // When there is no git command for some reason
        "UNKNOWN".to_string()
    }
}
/*
fn main() {
    // Skip building from docs.rs.
    if env::var_os("DOCS_RS").is_some() {
        return;
    }

    // To debug snapshot issues uncomment:
    // op_fetch_asset::trace_serializer();

    println!("cargo:rustc-env=TS_VERSION={}", ts_version());
    println!("cargo:rustc-env=GIT_COMMIT_HASH={}", git_commit_hash());
    println!(
        "cargo:rustc-env=DENO_CONSOLE_LIB_PATH={}",
        deno_console::get_declaration().display()
    );
    println!(
        "cargo:rustc-env=DENO_URL_LIB_PATH={}",
        deno_url::get_declaration().display()
    );
    println!(
        "cargo:rustc-env=DENO_WEB_LIB_PATH={}",
        deno_web::get_declaration().display()
    );
    println!(
        "cargo:rustc-env=DENO_FETCH_LIB_PATH={}",
        deno_fetch::get_declaration().display()
    );
    println!(
        "cargo:rustc-env=DENO_WEBGPU_LIB_PATH={}",
        deno_webgpu::get_declaration().display()
    );
    println!(
        "cargo:rustc-env=DENO_WEBSOCKET_LIB_PATH={}",
        deno_websocket::get_declaration().display()
    );
    println!(
        "cargo:rustc-env=DENO_CRYPTO_LIB_PATH={}",
        deno_crypto::get_declaration().display()
    );

    println!("cargo:rustc-env=TARGET={}", env::var("TARGET").unwrap());
    println!("cargo:rustc-env=PROFILE={}", env::var("PROFILE").unwrap());
    if let Ok(c) = env::var("DENO_CANARY") {
        println!("cargo:rustc-env=DENO_CANARY={}", c);
    }

    let c = PathBuf::from(env::var_os("CARGO_MANIFEST_DIR").unwrap());
    let o = PathBuf::from(env::var_os("OUT_DIR").unwrap());

    // Main snapshot
    let compiler_snapshot_path = o.join("COMPILER_SNAPSHOT.bin");

    let js_files = get_js_files("tsc");
    create_compiler_snapshot(&compiler_snapshot_path, js_files, &c);

    #[cfg(target_os = "windows")]
        {
            let mut res = winres::WindowsResource::new();
            res.set_icon("deno.ico");
            res.set_language(winapi::um::winnt::MAKELANGID(
                winapi::um::winnt::LANG_ENGLISH,
                winapi::um::winnt::SUBLANG_ENGLISH_US,
            ));
            res.compile().unwrap();
        }
}

fn get_js_files(d: &str) -> Vec<PathBuf> {
    let manifest_dir = Path::new(env!("CARGO_MANIFEST_DIR"));
    let mut js_files = std::fs::read_dir(d)
        .unwrap()
        .map(|dir_entry| {
            let file = dir_entry.unwrap();
            manifest_dir.join(file.path())
        })
        .filter(|path| path.extension().unwrap_or_default() == "js")
        .collect::<Vec<PathBuf>>();
    js_files.sort();
    js_files
}


 */