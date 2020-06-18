package org.sonar.rust;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.internal.SensorContextTester;
import org.sonar.api.issue.NoSonarFilter;
import org.sonar.api.utils.PathUtils;
import org.sonar.plugins.rust.api.RustCheck;

import java.io.File;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class MeasurerTest {
    private static final File BASE_DIR = new File("src/test/files/metrics");
    private SensorContextTester context;
    private static final int NB_OF_METRICS = 1;

    @Before
    public void setUp() throws Exception {
        context = SensorContextTester.create(BASE_DIR);
    }

    /*
    @Test
    public void verify_ncloc_metric() {
        checkMetric("LinesOfCode.rs", "ncloc", 2);
        checkMetric("CommentsOnly.rs", "ncloc", 0);
        checkMetric("Empty.rs", "ncloc", 0);
    }
    */


    /**
     * Utility method to quickly get metric out of a file.
     */
    private void checkMetric(String filename, String metric, Number expectedValue) {
        String relativePath = PathUtils.sanitize(new File(BASE_DIR, filename).getPath());
        InputFile inputFile = TestUtils.inputFile(relativePath);
        context.fileSystem().add(inputFile);

        Measurer measurer = new Measurer(context, mock(NoSonarFilter.class));
        RustScanner scanner = new RustScanner(null, measurer, new RustCheck[0]);

        scanner.scan(Collections.singletonList(inputFile));

        assertThat(context.measures(inputFile.key())).hasSize(NB_OF_METRICS);
        assertThat(context.measure(inputFile.key(), metric).value()).isEqualTo(expectedValue);
    }
}
