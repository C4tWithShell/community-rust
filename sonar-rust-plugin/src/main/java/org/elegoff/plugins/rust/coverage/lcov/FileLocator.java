package org.elegoff.plugins.rust.coverage.lcov;

import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.utils.PathUtils;

import javax.annotation.CheckForNull;

public class FileLocator {
    private final ReversePathTree tree = new ReversePathTree();

    FileLocator(Iterable<InputFile> inputFiles) {
        inputFiles.forEach(inputFile -> {
            String[] path = inputFile.relativePath().split("/");
            tree.index(inputFile, path);
        });
    }

    @CheckForNull
    InputFile getInputFile(String filePath) {
        String sanitizedPath = PathUtils.sanitize(filePath);
        if (sanitizedPath == null) {
            return null;
        }
        String[] pathElements = sanitizedPath.split("/");
        return tree.getFileWithSuffix(pathElements);
    }
}
