// Copyright 2018-2021 the Deno authors. All rights reserved. MIT license.

use crate::ast;
use crate::ast::TokenOrComment;
use crate::colors;
use crate::media_type::MediaType;
use crate::program_state::ProgramState;
use deno_core::error::AnyError;
use deno_core::serde_json::json;
use deno_core::serde_json::Value;
use deno_runtime::inspector::InspectorSession;
use deno_runtime::worker::MainWorker;
use rustyline::completion::Completer;
use rustyline::error::ReadlineError;
use rustyline::highlight::Highlighter;
use rustyline::validate::ValidationContext;
use rustyline::validate::ValidationResult;
use rustyline::validate::Validator;
use rustyline::Context;
use rustyline::Editor;
use rustyline_derive::{Helper, Hinter};
use std::borrow::Cow;
use std::sync::mpsc::channel;
use std::sync::mpsc::sync_channel;
use std::sync::mpsc::Receiver;
use std::sync::mpsc::Sender;
use std::sync::mpsc::SyncSender;
use std::sync::Arc;
use std::sync::Mutex;
use swc_ecmascript::parser::token::{Token, Word};
use tokio::pin;

// Provides helpers to the editor like validation for multi-line edits, completion candidates for
// tab completion.
#[derive(Helper, Hinter)]
struct Helper {
    context_id: u64,
    message_tx: SyncSender<(String, Option<Value>)>,
    response_rx: Receiver<Result<Value, AnyError>>,
}

impl Helper {
    fn post_message(
        &self,
        method: &str,
        params: Option<Value>,
    ) -> Result<Value, AnyError> {
        self.message_tx.send((method.to_string(), params))?;
        self.response_rx.recv()?
    }
}

fn is_word_boundary(c: char) -> bool {
    if c == '.' {
        false
    } else {
        char::is_ascii_whitespace(&c) || char::is_ascii_punctuation(&c)
    }
}

impl Completer for Helper {
    type Candidate = String;

    fn complete(
        &self,
        line: &str,
        pos: usize,
        _ctx: &Context<'_>,
    ) -> Result<(usize, Vec<String>), ReadlineError> {
        let start = line[..pos].rfind(is_word_boundary).map_or_else(|| 0, |i| i);
        let end = line[pos..]
            .rfind(is_word_boundary)
            .map_or_else(|| pos, |i| pos + i);

        let word = &line[start..end];
        let word = word.strip_prefix(is_word_boundary).unwrap_or(word);
        let word = word.strip_suffix(is_word_boundary).unwrap_or(word);

        let fallback = format!(".{}", word);

        let (prefix, suffix) = match word.rfind('.') {
            Some(index) => word.split_at(index),
            None => ("globalThis", fallback.as_str()),
        };

        let evaluate_response = self
            .post_message(
                "Runtime.evaluate",
                Some(json!({
          "contextId": self.context_id,
          "expression": prefix,
          "throwOnSideEffect": true,
          "timeout": 200,
        })),
            )
            .unwrap();

        if evaluate_response.get("exceptionDetails").is_some() {
            let candidates = Vec::new();
            return Ok((pos, candidates));
        }

        if let Some(result) = evaluate_response.get("result") {
            if let Some(object_id) = result.get("objectId") {
                let get_properties_response = self.post_message(
                    "Runtime.getProperties",
                    Some(json!({
            "objectId": object_id,
          })),
                );

                if let Ok(get_properties_response) = get_properties_response {
                    if let Some(result) = get_properties_response.get("result") {
                        let candidates = result
                            .as_array()
                            .unwrap()
                            .iter()
                            .filter_map(|r| {
                                let name = r.get("name").unwrap().as_str().unwrap().to_string();

                                if name.starts_with("Symbol(") {
                                    return None;
                                }

                                if name.starts_with(&suffix[1..]) {
                                    return Some(name);
                                }

                                None
                            })
                            .collect();

                        return Ok((pos - (suffix.len() - 1), candidates));
                    }
                }
            }
        }

        Ok((pos, Vec::new()))
    }
}

impl Validator for Helper {
    fn validate(
        &self,
        ctx: &mut ValidationContext,
    ) -> Result<ValidationResult, ReadlineError> {
        let mut stack: Vec<Token> = Vec::new();
        let mut in_template = false;

        for item in ast::lex("", ctx.input(), &MediaType::JavaScript) {
            if let TokenOrComment::Token(token) = item.inner {
                match token {
                    Token::BackQuote => in_template = !in_template,
                    Token::LParen
                    | Token::LBracket
                    | Token::LBrace
                    | Token::DollarLBrace => stack.push(token),
                    Token::RParen | Token::RBracket | Token::RBrace => {
                        match (stack.pop(), token) {
                            (Some(Token::LParen), Token::RParen)
                            | (Some(Token::LBracket), Token::RBracket)
                            | (Some(Token::LBrace), Token::RBrace)
                            | (Some(Token::DollarLBrace), Token::RBrace) => {}
                            (Some(left), _) => {
                                return Ok(ValidationResult::Invalid(Some(format!(
                                    "Mismatched pairs: {:?} is not properly closed",
                                    left
                                ))))
                            }
                            (None, _) => {
                                // While technically invalid when unpaired, it should be V8's task to output error instead.
                                // Thus marked as valid with no info.
                                return Ok(ValidationResult::Valid(None));
                            }
                        }
                    }
                    _ => {}
                }
            }
        }

        if !stack.is_empty() || in_template {
            return Ok(ValidationResult::Incomplete);
        }

        Ok(ValidationResult::Valid(None))
    }
}

impl Highlighter for Helper {
    fn highlight_hint<'h>(&self, hint: &'h str) -> Cow<'h, str> {
        hint.into()
    }

    fn highlight_candidate<'c>(
        &self,
        candidate: &'c str,
        _completion: rustyline::CompletionType,
    ) -> Cow<'c, str> {
        self.highlight(candidate, 0)
    }

    fn highlight_char(&self, line: &str, _: usize) -> bool {
        !line.is_empty()
    }

    fn highlight<'l>(&self, line: &'l str, _: usize) -> Cow<'l, str> {
        let mut out_line = String::from(line);

        for item in ast::lex("", line, &MediaType::JavaScript) {
            // Adding color adds more bytes to the string,
            // so an offset is needed to stop spans falling out of sync.
            let offset = out_line.len() - line.len();
            let span = item.span_as_range();

            out_line.replace_range(
                span.start + offset..span.end + offset,
                &match item.inner {
                    TokenOrComment::Token(token) => match token {
                        foo | Token::BackQuote => {
                            colors::green(&line[span]).to_string()
                        }
                        Token::Regex(_, _) => colors::red(&line[span]).to_string(),
                        Token::Num(_) | Token::BigInt(_) => {
                            colors::yellow(&line[span]).to_string()
                        }
                        Token::Word(word) => match word {
                            Word::True | Word::False | Word::Null => {
                                colors::yellow(&line[span]).to_string()
                            }
                            Word::Keyword(_) => colors::cyan(&line[span]).to_string(),
                            Word::Ident(ident) => {
                                if ident == *"undefined" {
                                    colors::gray(&line[span]).to_string()
                                } else if ident == *"Infinity" || ident == *"NaN" {
                                    colors::yellow(&line[span]).to_string()
                                } else if ident == *"async" || ident == *"of" {
                                    colors::cyan(&line[span]).to_string()
                                } else {
                                    line[span].to_string()
                                }
                            }
                        },
                        _ => line[span].to_string(),
                    },
                    TokenOrComment::Comment { .. } => {
                        colors::gray(&line[span]).to_string()
                    }
                },
            );
        }

        out_line.into()
    }
}

async fn post_message_and_poll(
    worker: &mut MainWorker,
    session: &mut InspectorSession,
    method: &str,
    params: Option<Value>,
) -> Result<Value, AnyError> {
    let response = session.post_message(method, params);
    tokio::pin!(response);

    loop {

    }
}

async fn read_line_and_poll(
    worker: &mut MainWorker,
    session: &mut InspectorSession,
    message_rx: &Receiver<(String, Option<Value>)>,
    response_tx: &Sender<Result<Value, AnyError>>,
    editor: Arc<Mutex<Editor<Helper>>>,
) -> Result<String, ReadlineError> {
    let mut line =
        tokio::task::spawn_blocking(move || editor.lock().unwrap().readline("> "));

    let mut poll_worker = true;

    loop {
        for (method, params) in message_rx.try_iter() {
            response_tx
                .send(session.post_message(&method, params).await)
                .unwrap();
        }

        // Because an inspector websocket client may choose to connect at anytime when we have an
        // inspector server we need to keep polling the worker to pick up new connections.
        // TODO(piscisaureus): the above comment is a red herring; figure out if/why
        // the event loop isn't woken by a waker when a websocket client connects.
        let timeout = tokio::time::sleep(tokio::time::Duration::from_millis(100));
        pin!(timeout);


    }
}



async fn inject_prelude(
    worker: &mut MainWorker,
    session: &mut InspectorSession,
    context_id: u64,
) -> Result<(), AnyError> {
    post_message_and_poll(
        worker,
        session,
        "Runtime.evaluate",
        Some(json!({
      "expression": PRELUDE,
      "contextId": context_id,
    })),
    );
      //  .await?;

    Ok(())
}

