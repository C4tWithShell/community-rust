package org.sonar.plugins.rust.api;

import org.sonar.api.batch.fs.InputFile;
import org.sonar.plugins.rust.api.tree.CompilationUnitTree;
import org.sonar.plugins.rust.api.tree.Tree;

import javax.annotation.Nullable;
import java.util.List;

public interface RustFileScannerContext {
    @Nullable
    Object getSemanticModel();


    CompilationUnitTree getTree();

    InputFile getInputFile();
    boolean fileParsed();
    /**
     * Lines of the currently analyzed file.
     * @return list of file lines.
     */
    List<String> getFileLines();

    /**
     * Content of the currently analyzed file.
     * @return the file content as a String.
     */
    String getFileContent();
}
