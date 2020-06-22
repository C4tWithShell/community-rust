package org.sonar.plugins.rust.api;

public interface RustFileScanner extends RustCheck {

    /**
     * Method called after parsing and semantic analysis has been done on file.
     * @param context Context of analysis containing the parsed tree.
     */
    void scanFile(RustFileScannerContext context);

}
