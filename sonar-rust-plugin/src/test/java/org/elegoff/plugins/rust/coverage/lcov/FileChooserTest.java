package org.elegoff.plugins.rust.coverage.lcov;

import org.junit.Test;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.fs.internal.TestInputFileBuilder;

import java.util.Arrays;
import java.util.Collections;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;

public class FileChooserTest {
    @Test
    public void should_match_suffix() {
        InputFile inputFile = new TestInputFileBuilder("module1", "src/main/java/org/sonar/test/File.rs").build();
        FileChooser locator = new FileChooser(Collections.singleton(inputFile));
        assertThat(locator.getInputFile("org/sonar/test/File.rs")).isEqualTo(inputFile);
    }

    @Test
    public void should_not_match() {
        InputFile inputFile = new TestInputFileBuilder("module1", "src/main/java/org/sonar/test/File.rs").build();
        FileChooser locator = new FileChooser(Collections.singleton(inputFile));
        assertThat(locator.getInputFile("org/sonar/test/File2.rs")).isNull();
        assertThat(locator.getInputFile("org/sonar/test2/File.rs")).isNull();
        assertThat(locator.getInputFile("not/found")).isNull();
    }

    @Test
    public void should_match_first_with_many_options() {
        InputFile inputFile1 = new TestInputFileBuilder("module1", "src/main/java/org/sonar/test/File.rs").build();
        InputFile inputFile2 = new TestInputFileBuilder("module1", "src/test/java/org/sonar/test/File.rs").build();

        FileChooser locator = new FileChooser(Arrays.asList(inputFile1, inputFile2));
        assertThat(locator.getInputFile("org/sonar/test/File.rs")).isEqualTo(inputFile1);
    }

    @Test
    public void should_normalize_paths() {
        InputFile inputFile = new TestInputFileBuilder("module1", "src/main/java/org/sonar/test/File.rs").build();

        FileChooser locator = new FileChooser(singletonList(inputFile));
        assertThat(locator.getInputFile("./org/sonar/test/File.rs")).isEqualTo(inputFile);
        assertThat(locator.getInputFile("./org//sonar/./test/File.rs")).isEqualTo(inputFile);
        assertThat(locator.getInputFile("./org//sonar/../sonar/test/File.rs")).isEqualTo(inputFile);
        assertThat(locator.getInputFile("///a/b/c.txt")).isNull();
    }
}

