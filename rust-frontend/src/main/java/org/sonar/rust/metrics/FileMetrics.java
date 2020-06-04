package org.sonar.rust.metrics;

import org.sonar.plugins.rust.api.RustVisitorContext;
import org.sonar.plugins.rust.api.tree.FileInput;

import java.util.ArrayList;
import java.util.List;

public class FileMetrics {

    private final FileLinesVisitor fileLinesVisitor;
    private List<Integer> functionComplexities = new ArrayList<>();

    public FileMetrics(RustVisitorContext context) {
        FileInput fileInput = context.rootTree();
        fileLinesVisitor = new FileLinesVisitor();
        fileLinesVisitor.scanFile(context);

    }
}
