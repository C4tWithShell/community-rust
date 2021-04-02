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
            if matches!(tag.as_str(),
        "ts"
          | "tsx"
          | "js"
          | "jsx"
          | "javascript"
          | "typescript"
          | "json"
          | "jsonc"
      ) {

                let extension = match tag.as_str() {
                    "javascript" => "js",
                    "typescript" => "ts",
                    rest => rest,
                };


                if matches!(extension, "json" | "jsonc") {
                    let mut json_config = get_json_config();
                    //json_config.line_width = line_width;
                    dprint_plugin_json::format_text(&text, &json_config)
                } else {
                    let fake_filename = 44;

                }
            } else {
                Ok(text.to_string())
            }
        }),
    )
}

/*

 */