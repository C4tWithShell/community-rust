package org.elegoff.plugins.rust;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.rust.RustFile;

import java.io.FileNotFoundException;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SonarQubeRustFileTest {

    private InputFile inputFile = mock(InputFile.class);

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void knowFile() throws Exception {
        when(inputFile.contents()).thenReturn("Success");
        RustFile rustFile = SonarQubeRustFile.create(inputFile);
        assertThat(rustFile.content()).isEqualTo("Success");
    }

    @Test
    public void unknownFile() throws Exception {
        when(inputFile.contents()).thenThrow(new FileNotFoundException());
        RustFile rustFile = SonarQubeRustFile.create(inputFile);
        thrown.expect(IllegalStateException.class);
        rustFile.content();
    }

}