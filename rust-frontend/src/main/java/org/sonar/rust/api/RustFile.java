package org.sonar.rust.api;

import java.net.URI;

public interface RustFile {
    String content();

    String fileName();

    URI uri();
}
