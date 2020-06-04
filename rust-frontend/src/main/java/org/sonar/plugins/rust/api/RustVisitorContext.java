package org.sonar.plugins.rust.api;

import com.sonar.sslr.api.RecognitionException;
import org.sonar.plugins.rust.api.tree.FileInput;

import java.io.File;

public class RustVisitorContext {
    private final FileInput rootTree;
    private final RustFile rustFile;
    private File workingDirectory = null;
    private final RecognitionException parsingException;

    public RustVisitorContext(FileInput rootTree, RustFile rustFile, File workDir) {
        this.rootTree = rootTree;
        this.rustFile = rustFile;
        this.workingDirectory = workDir;
        this.parsingException = null;
    }

    public FileInput rootTree() {
        return rootTree;
    }
}
