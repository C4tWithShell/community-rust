package org.elegoff.plugins.rust.coverage.cobertura;


import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;


import org.elegoff.plugins.rust.RustPlugin;
import org.elegoff.plugins.rust.Utils;
import org.elegoff.plugins.rust.language.RustLanguage;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.fs.InputFile.Type;
import org.sonar.api.batch.fs.internal.DefaultInputFile;
import org.sonar.api.batch.fs.internal.TestInputFileBuilder;
import org.sonar.api.batch.sensor.internal.DefaultSensorDescriptor;
import org.sonar.api.batch.sensor.internal.SensorContextTester;
import org.sonar.api.config.internal.MapSettings;
import org.sonar.api.utils.log.LogTester;
import org.sonar.api.utils.log.LoggerLevel;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;


public class CoberturaSensorTest {

    private static final String ABSOLUTE_PATH_PLACEHOLDER = "{CHANGE_ME}";
    private static final String TESTFILE1 = "moduleKey:src/cgroups/common.rs";
    private static final String TESTFILE2 = "moduleKey:src/cgroups/test.rs";
    private static final String TESTFILE3 = "moduleKey:src/process/fork.rs";
    private static final String TESTFILE4 = "moduleKey:src/process/init.rs";
    private SensorContextTester context;
    private MapSettings settings;

    
    private CoberturaSensor coverageSensor;
    private File moduleBaseDir = new File("src/test/resources/org/elegoff/plugins/rust/cobertura").getAbsoluteFile();

    @Rule
    public LogTester logTester = new LogTester();

    @Rule
    public TemporaryFolder tmpDir = new TemporaryFolder();

    @Before
    public void init() {

        coverageSensor = new CoberturaSensor();
        settings = new MapSettings();
        settings.setProperty(RustPlugin.COBERTURA_REPORT_PATHS, "cobertura.xml");
        context = SensorContextTester.create(moduleBaseDir);
        context.setSettings(settings);

        inputFile("src/cgroups/common.rs", Type.MAIN);
        inputFile("src/cgroups/test.rs", Type.MAIN);
        inputFile("src/process/fork.rs", Type.MAIN);
        inputFile("src/process/init.rs", Type.MAIN);
    }

    private InputFile inputFile(String relativePath, Type type) {
        DefaultInputFile inputFile = TestInputFileBuilder.create("moduleKey", relativePath)
                .setModuleBaseDir(moduleBaseDir.toPath())
                .setLanguage(RustLanguage.KEY)
                .setType(type)
                .initMetadata(Utils.fileContent(new File(moduleBaseDir, relativePath), StandardCharsets.UTF_8))
                .build();

        context.fileSystem().add(inputFile);

        return inputFile;
    }

    @Test
    public void report_not_found() {
        settings.setProperty(RustPlugin.COBERTURA_REPORT_PATHS, "/fake/path/report.xml");
        coverageSensor.execute(context);
        assertThat(context.lineHits(TESTFILE1, 1)).isNull();
    }

    @Test
    public void absolute_path() {
        settings.setProperty(RustPlugin.COBERTURA_REPORT_PATHS, new File(moduleBaseDir, "cobertura.xml").getAbsolutePath());

        coverageSensor.execute(context);

        assertThat(context.lineHits(TESTFILE1, 42)).isEqualTo(1);
    }

    @Test
    public void test_coverage() {
        coverageSensor.execute(context);
        Map<Integer, Integer> file1Expected = new HashMap<>();
        file1Expected.put(42,1);
        file1Expected.put(43,3);
        file1Expected.put(48,1);

        checkLineHits(TESTFILE1, file1Expected);

        Map<Integer, Integer> file2Expected = new HashMap<>();
        file2Expected.put(39,1);
        file2Expected.put(40,1);
        file2Expected.put(45,0);
        file2Expected.put(46,0);

        checkLineHits(TESTFILE2, file2Expected);


    }

    private void checkLineHits(String fileKey, Map<Integer, Integer> expect){
        for (Integer k : expect.keySet()) {
            assertThat(context.lineHits(fileKey, k)).isEqualTo(expect.get(k));
        }
    }



    @Test
    public void test_ambigous_locations() {
        settings.setProperty(RustPlugin.COBERTURA_REPORT_PATHS, "coverage-other-2.xml");
        coverageSensor.execute(context);

        assertThat(context.lineHits("moduleKey:src/cgroups/common.rs", 30)).isEqualTo(3);
        //  ambiguity detection
        assertThat(context.lineHits("moduleKey:src/cgroups/test.rs", 1)).isNull();
        assertThat(context.lineHits("moduleKey:src/process]/test.rs", 1)).isNull();
    }



    @Test
    public void test_report_with_absolute_path() throws Exception {
        String reportPath = createReportWithAbsolutePaths();
        settings.setProperty(RustPlugin.COBERTURA_REPORT_PATHS, reportPath);

        coverageSensor.execute(context);

        assertThat(context.lineHits(TESTFILE1, 1)).isEqualTo(1);
        assertThat(context.lineHits(TESTFILE1, 4)).isEqualTo(0);
        assertThat(context.lineHits(TESTFILE1, 6)).isEqualTo(0);
    }

    @Test
    public void test_unresolved_path() {


        String currentFileSeparator = File.separator;

        logTester.clear();

        settings.setProperty(RustPlugin.COBERTURA_REPORT_PATHS, "unresolved.xml");
        coverageSensor.execute(context);

        String expectedLogMessage = String.format(
                "Cannot resolve the file path '/mymodule/src/fake.rs' of the coverage report, the file does not exist in all <source>.",
                currentFileSeparator,
                currentFileSeparator,
                currentFileSeparator);
        assertThat(logTester.logs(LoggerLevel.ERROR)).containsExactly(
                expectedLogMessage,
                "Cannot resolve 2 file paths, ignoring coverage measures for those files");
    }


    @Test
    public void test_multiple_reports() {
        settings.setProperty(RustPlugin.COBERTURA_REPORT_PATHS, "cobertura.xml,coverage-other*.xml");
        coverageSensor.execute(context);

        assertThat(context.conditions(TESTFILE2, 2)).isNull();
        assertThat(context.lineHits(TESTFILE2, 39)).isEqualTo(1);
        assertThat(context.lineHits(TESTFILE2, 45)).isEqualTo(0);
        assertThat(context.lineHits(TESTFILE4, 7)).isNull();
        assertThat(context.lineHits(TESTFILE4, 12)).isEqualTo(0);
    }


    @Test(expected = IllegalStateException.class)
    public void should_fail_on_invalid_report() {
        settings.setProperty(RustPlugin.COBERTURA_REPORT_PATHS, "invalid.xml");
        coverageSensor.execute(context);
    }

    @Test(expected = IllegalStateException.class)
    public void should_fail_on_unexpected_eof() {
        settings.setProperty(RustPlugin.COBERTURA_REPORT_PATHS, "wrong_eof.xml");
        coverageSensor.execute(context);
    }

    @Test
    public void should_do_nothing_on_empty_report() {
        settings.setProperty(RustPlugin.COBERTURA_REPORT_PATHS, "empty.xml");
        coverageSensor.execute(context);

        assertThat(context.lineHits(TESTFILE1, 1)).isNull();
    }

    @Test
    public void default_report_logs() {
        settings.clear();
        CoberturaSensor sensor = new CoberturaSensor();
        sensor.execute(context);
        assertThat(logTester.logs(LoggerLevel.DEBUG)).contains("Using pattern 'cobertura.xml' to find reports");
    }

    @Test
    public void sensor_descriptor() {
        DefaultSensorDescriptor descriptor = new DefaultSensorDescriptor();
        new CoberturaSensor().describe(descriptor);
        assertThat(descriptor.name()).isEqualTo("Cobertura Sensor for Rust coverage");
        assertThat(descriptor.languages()).containsOnly(RustLanguage.KEY);
    }

    private String createReportWithAbsolutePaths() throws Exception {
        Path workDir = tmpDir.newFolder("sonar-rust").toPath().toAbsolutePath();
        String reportName="unresolved.xml";

        String absoluteSourcePath = new File(moduleBaseDir, "src/cgroups/common.rs").getAbsolutePath();
        Path report = new File(moduleBaseDir, reportName).toPath();
        String reportContent = new String(Files.readAllBytes(report), UTF_8);
        reportContent = reportContent.replace(ABSOLUTE_PATH_PLACEHOLDER, absoluteSourcePath);

        Path reportCopy = workDir.resolve(reportName);
        Files.write(reportCopy, reportContent.getBytes(UTF_8));

        return reportCopy.toAbsolutePath().toString();
    }

}