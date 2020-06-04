package org.sonar.plugins.rust.api;

public interface RustCheck {
    void scanFile(RustVisitorContext visitorContext);
}
