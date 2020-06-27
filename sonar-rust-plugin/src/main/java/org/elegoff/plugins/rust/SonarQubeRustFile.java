package org.elegoff.plugins.rust;

import org.sonar.api.batch.fs.InputFile;
import org.sonar.rust.RustFile;

import java.io.IOException;
import java.net.URI;

public class SonarQubeRustFile implements RustFile {
    private final InputFile inputFile;

    private SonarQubeRustFile(InputFile inputFile) {
        this.inputFile = inputFile;
    }

    static SonarQubeRustFile create(InputFile inputFile) {
        return new SonarQubeRustFile(inputFile);
    }

    @Override
    public String name() {
        return inputFile.file().getName();
    }

    @Override
    public URI uri() {
        return inputFile.file().toURI();
    }

    private InputFile inputFile() {
        return inputFile;
    }

    @Override
    public String content() {
        try {
            return inputFile().contents();
        } catch (IOException e) {
            throw new IllegalStateException("Could not read content of input file " + inputFile(), e);
        }
    }
}
