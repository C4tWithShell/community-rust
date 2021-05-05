// Copyright 2018-2021 the Deno authors. All rights reserved. MIT license.

//! This mod provides functions to remap a `JsError` based on a source map.

use deno_core::error::JsError;
use sourcemap::SourceMap;
use std::collections::HashMap;
use std::str;
use std::sync::Arc;

pub trait SourceMapGetter: Sync + Send {
  /// Returns the raw source map file.
  fn get_source_map(&self, file_name: &str) -> Option<Vec<u8>>;
  fn get_source_line(
    &self,
    file_name: &str,
    line_number: usize,
  ) -> Option<String>;
}
