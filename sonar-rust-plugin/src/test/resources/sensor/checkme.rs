fn get_cache_filename(&self, url: &Url) -> Option<PathBuf> {
    let mut out = PathBuf::new();

    let scheme = url.scheme();
    out.push(scheme);

    match scheme {
        "wasm" => {
            let host = url.host_str().unwrap();
            let host_port = match url.port() {
                // Windows doesn't support ":" in filenames, so we represent port using a
                // special string.
                Some(port) => format!("{}_PORT{}", host, port),
                None => host.to_string(),
            };
            out.push(host_port);

            for path_seg in url.path_segments().unwrap() {
                out.push(path_seg);
            }
        }
        "http" | "https" | "data" => out = url_to_filename(url)?,
        "file" => {
            let path = match url.to_file_path() {
                Ok(path) => path,
                Err(_) => return None,
            };
            let mut path_components = path.components();

            if cfg!(target_os = "windows") {
                if let Some(Component::Prefix(prefix_component)) =
                path_components.next()
                {
                    // Windows doesn't support ":" in filenames, so we need to extract disk prefix
                    // Example: file:///C:/deno/js/unit_test_runner.ts
                    // it should produce: file\c\deno\js\unit_test_runner.ts
                    match prefix_component.kind() {
                        Prefix::Disk(disk_byte) | Prefix::VerbatimDisk(disk_byte) => {
                            let disk = (disk_byte as char).to_string();
                            out.push(disk);
                        }
                        Prefix::UNC(server, share)
                        | Prefix::VerbatimUNC(server, share) => {
                            out.push("UNC");
                            let host = Host::parse(server.to_str().unwrap()).unwrap();
                            let host = host.to_string().replace(":", "_");
                            out.push(host);
                            out.push(share);
                        }
                        _ => unreachable!(),
                    }
                }
            }

            // Must be relative, so strip forward slash
            let mut remaining_components = path_components.as_path();
            if let Ok(stripped) = remaining_components.strip_prefix("/") {
                remaining_components = stripped;
            };

            out = out.join(remaining_components);
        }
        _ => return None,
    };

    Some(out)
}