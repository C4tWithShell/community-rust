package org.sonar.rust;

import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.fs.internal.TestInputFileBuilder;

import java.io.File;
import java.nio.file.Files;

import static java.nio.charset.StandardCharsets.UTF_8;

public class TestUtils {
    private TestUtils() {
        // utility class, forbidden constructor
    }

    public static int[] computeLineEndOffsets(int[] lineStartOffsets, int lastValidOffset) {
        int[] lineEndOffsets = new int[lineStartOffsets.length];
        for (int i = 0; i < lineStartOffsets.length - 1; i++) {
            lineEndOffsets[i] = lineStartOffsets[i + 1] - 1;
        }
        lineEndOffsets[lineEndOffsets.length - 1] = lastValidOffset - 1;
        return lineEndOffsets;
    }

    public static InputFile emptyInputFile(String filename) {
        return emptyInputFile(filename, InputFile.Type.MAIN);
    }

    public static InputFile emptyInputFile(String filename, InputFile.Type type) {
        return new TestInputFileBuilder("", filename)
                .setCharset(UTF_8)
                .setLanguage("java")
                .setType(type)
                .build();
    }

    public static InputFile inputFile(String filepath) {
        return inputFile("", new File(filepath));
    }

    public static InputFile inputFile(File file) {
        return inputFile("", file);
    }

    public static InputFile inputFile(String moduleKey, File file) {
        try {
            return new TestInputFileBuilder(moduleKey, file.getPath())
                    .setContents(new String(Files.readAllBytes(file.toPath()), UTF_8))
                    .setCharset(UTF_8)
                    .setLanguage("java")
                    .build();
        } catch (Exception e) {
            throw new IllegalStateException(String.format("Unable to read file '%s", file.getAbsolutePath()));
        }
    }
}
