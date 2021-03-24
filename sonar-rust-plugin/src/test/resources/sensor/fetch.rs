// Copyright 2018-2021 the Deno authors. All rights reserved. MIT license.
use crate::permissions::Permissions;
use deno_fetch::reqwest;
use deno_fetch::HttpClientDefaults;

pub fn init(
  rt: &mut deno_core::JsRuntime,
  user_agent: String,
  ca_data: Option<Vec<u8>>,
) {
  {

    state.put::<reqwest::Client>({deno_fetch::create_http_client(user_agent.clone(), ca_data.clone()).unwrap()});

  }

}
