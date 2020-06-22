package org.sonar.rust.model;

import com.google.common.annotations.VisibleForTesting;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.plugins.rust.api.RustFileScannerContext;
import org.sonar.plugins.rust.api.tree.CompilationUnitTree;
import org.sonar.rust.SonarComponents;
import org.sonar.rust.resolve.SemanticModel;

import javax.annotation.Nullable;
import java.util.List;

public class DefaultRustFileScannerContext implements RustFileScannerContext {
    private final CompilationUnitTree tree;
    @VisibleForTesting
    private final SemanticModel semanticModel;
    private final SonarComponents sonarComponents;

    private final InputFile inputFile;
    private final boolean fileParsed;

    @Override
    @Nullable
    public Object getSemanticModel() {
        return semanticModel;
    }

    @Override
    public CompilationUnitTree getTree() {
        return null;
    }

    @Override
    public InputFile getInputFile() {
        return null;
    }


    @Override
    public boolean fileParsed() {
        return fileParsed;
    }

    @Override
    public List<String> getFileLines() {
        return null;
    }

    @Override
    public String getFileContent() {
        return null;
    }

    public DefaultRustFileScannerContext(CompilationUnitTree tree, InputFile inputFile, SemanticModel semanticModel, SonarComponents sonarComponents, boolean fileParsed) {
        this.tree = tree;
        this.inputFile = inputFile;
        this.semanticModel = semanticModel;
        this.sonarComponents = sonarComponents;
        this.fileParsed = fileParsed;
    }
}
