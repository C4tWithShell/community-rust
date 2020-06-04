package org.sonar.plugins.rust.api;

import org.sonar.api.batch.fs.InputFile;

import java.io.IOException;

public class RustFile  {
    private InputFile inputFile;

    private RustFile(){
    //empty
    }

    public RustFile(InputFile inputFile) {
        this.inputFile = inputFile;
    }

    public String contents(){
        try {
            return inputFile.contents();
        } catch (IOException e) {
            throw new IllegalStateException("Could not read content of input file " + inputFile, e);
        }
    }
}
