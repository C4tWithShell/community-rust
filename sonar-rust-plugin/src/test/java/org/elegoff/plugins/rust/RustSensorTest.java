package org.elegoff.plugins.rust;

import org.assertj.core.api.Condition;
import org.elegoff.plugins.rust.language.RustLanguage;
import org.elegoff.rust.checks.CheckList;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.fs.internal.DefaultFileSystem;
import org.sonar.api.batch.fs.internal.DefaultInputFile;
import org.sonar.api.batch.fs.internal.FileMetadata;
import org.sonar.api.batch.fs.internal.TestInputFileBuilder;
import org.sonar.api.batch.rule.ActiveRules;
import org.sonar.api.batch.rule.CheckFactory;
import org.sonar.api.batch.rule.internal.ActiveRulesBuilder;
import org.sonar.api.batch.sensor.highlighting.TypeOfText;
import org.sonar.api.batch.sensor.internal.DefaultSensorDescriptor;
import org.sonar.api.batch.sensor.internal.SensorContextTester;
import org.sonar.api.batch.sensor.issue.Issue;
import org.sonar.api.internal.SonarRuntimeImpl;
import org.sonar.api.internal.apachecommons.io.FileUtils;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.FileLinesContext;
import org.sonar.api.measures.FileLinesContextFactory;
import org.sonar.api.rule.RuleKey;
import org.sonar.api.utils.Version;
import org.sonar.api.utils.log.LogAndArguments;
import org.sonar.api.utils.log.LogTester;
import org.sonar.api.utils.log.LoggerLevel;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RustSensorTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Rule
    public LogTester logTester = new LogTester();

    private DefaultFileSystem fs;
    private RustSensor sensor;
    private SensorContextTester context;

    private static String FILE1="org/elegoff/plugins/rust/sensor/file1.rs";
    private static String WRONG="org/elegoff/plugins/rust/sensor/wrong.rs";



    @Test(timeout = 600000)
    public void testPerformance() throws Exception {
        initFileSystemWithFile(createRustFile(20000, "smallFile.rs"));
        long timeSmallFile = measureTimeToAnalyzeFile();

        initFileSystemWithFile(createRustFile(40000, "bigFile.rs"));
        long timeBigFile = measureTimeToAnalyzeFile();
        assertThat(timeBigFile < (2.5 * timeSmallFile)).isTrue();
    }

    @Test
    public void test_analysis_cancellation() throws Exception {
        init(false);
        fs.add(createInputFile(FILE1));

        context.setCancelled(true);
        sensor.execute(context);

        assertThat(context.allIssues()).isEmpty();
    }

    @Test
    public void test_nothing_is_executed_if_no_file() throws Exception {
        init(false);

        sensor.execute(context);

        assertThat(context.allIssues()).isEmpty();
    }

    @Test
    public void test_descriptor() throws Exception {
        init(false);
        DefaultSensorDescriptor sensorDescriptor = new DefaultSensorDescriptor();
        sensor.describe(sensorDescriptor);
        assertThat(sensorDescriptor.name()).isEqualTo("RustSensor");
        assertThat(sensorDescriptor.languages()).containsOnly(RustLanguage.KEY);
    }

    /**
     * Expect issue for rule: NewlineCheck
     */
    @Test
    public void test_sensor() throws Exception {
        init(false);
        DefaultInputFile inputFile = createInputFile(FILE1);
        fs.add(inputFile);

        sensor.execute(context);



        // other measures
        assertThat(context.measure(inputFile.key(), CoreMetrics.NCLOC).value()).isEqualTo(0);//FIXME

    }





    @Test
    public void should_not_execute_test_on_corrupted_file_and_should_not_raise_parsing_issue() throws Exception {
        init(false);
        fs.add(createInputFile(WRONG));

        sensor.execute(context);

        assertThat(context.allIssues()).isEmpty();

        assertLog("1 source files to be analyzed", false);
    }

    private void init(boolean activateParsingErrorCheck) throws Exception {
        File moduleBaseDir = new File("src/test/resources");
        context = SensorContextTester.create(moduleBaseDir);

        fs = new DefaultFileSystem(moduleBaseDir);
        fs.setWorkDir(temporaryFolder.newFolder("temp").toPath());

        ActiveRulesBuilder activeRuleBuilder = new ActiveRulesBuilder();




        CheckFactory checkFactory = new CheckFactory(activeRuleBuilder.build());

        FileLinesContextFactory fileLinesContextFactory = mock(FileLinesContextFactory.class);
        when(fileLinesContextFactory.createFor(any(InputFile.class))).thenReturn(mock(FileLinesContext.class));

        sensor = new RustSensor(fs, checkFactory, fileLinesContextFactory);
    }

    private DefaultInputFile createInputFile(String name) throws FileNotFoundException {
        DefaultInputFile inputFile = TestInputFileBuilder.create("modulekey", name)
                .setModuleBaseDir(Paths.get("src/test/resources"))
                .setType(InputFile.Type.MAIN)
                .setLanguage(RustLanguage.KEY)
                .setCharset(StandardCharsets.UTF_8)
                .build();

        inputFile.setMetadata(new FileMetadata().readMetadata(new FileInputStream(inputFile.file()), StandardCharsets.UTF_8, inputFile.absolutePath()));
        return inputFile;
    }

    @Test
    public void should_analyze_file_with_its_own_encoding() throws IOException {
        Charset fileSystemCharset = StandardCharsets.UTF_8;
        Charset fileCharset = StandardCharsets.UTF_16;

        Path moduleBaseDir = temporaryFolder.newFolder().toPath();
        SensorContextTester context = SensorContextTester.create(moduleBaseDir);

        DefaultFileSystem fileSystem = new DefaultFileSystem(moduleBaseDir);
        fileSystem.setEncoding(fileSystemCharset);
        context.setFileSystem(fileSystem);
        String filename = "utf16.xml";
        try (BufferedWriter writer = Files.newBufferedWriter(moduleBaseDir.resolve(filename), fileCharset)) {
            writer.write("fn main() {\n" +
                    "    println!(\"Hello, world!\");\n" +
                    "}");
        }

        String modulekey = "modulekey";
        DefaultInputFile defaultInputFile = TestInputFileBuilder.create(modulekey, filename)
                .setModuleBaseDir(moduleBaseDir)
                .setType(InputFile.Type.MAIN)
                .setLanguage(RustLanguage.KEY)
                .setCharset(fileCharset)
                .build();
        fileSystem.add(defaultInputFile);

        defaultInputFile.setMetadata(new FileMetadata().readMetadata(new FileInputStream(defaultInputFile.file()), StandardCharsets.UTF_8, defaultInputFile.absolutePath()));

        ActiveRules activeRules = new ActiveRulesBuilder().build();
        CheckFactory checkFactory = new CheckFactory(activeRules);

        FileLinesContextFactory fileLinesContextFactory = mock(FileLinesContextFactory.class);
        when(fileLinesContextFactory.createFor(any(InputFile.class))).thenReturn(mock(FileLinesContext.class));
        sensor = new RustSensor(fileSystem, checkFactory, fileLinesContextFactory);
        sensor.execute(context);

        String componentKey = modulekey + ":" + filename;
        assertThat(context.measure(componentKey, CoreMetrics.NCLOC).value()).isEqualTo(0);//FIXME
    }

    private void assertLog(String expected, boolean isRegexp) {
        if (isRegexp) {
            Condition<String> regexpMatches = new Condition<String>(log -> Pattern.compile(expected).matcher(log).matches(), "");
            assertThat(logTester.logs())
                    .filteredOn(regexpMatches)
                    .as("None of the lines in " + logTester.logs() + " matches regexp [" + expected + "], but one line was expected to match")
                    .isNotEmpty();
        } else {
            assertThat(logTester.logs()).contains(expected);
        }
    }

    private void initFileSystemWithFile(File file) throws Exception {
        init(false);

        DefaultInputFile inputFile = TestInputFileBuilder.create("modulekey", file.getName())
                .setModuleBaseDir(Paths.get(file.getParent()))
                .setType(InputFile.Type.MAIN)
                .setLanguage(RustLanguage.KEY)
                .setCharset(StandardCharsets.UTF_8)
                .build();

        inputFile.setMetadata(new FileMetadata().readMetadata(new FileInputStream(inputFile.file()), StandardCharsets.UTF_8, inputFile.absolutePath()));

        fs.add(inputFile);
    }

    private File createRustFile(int nb, String fileName) throws IOException {
        File file = temporaryFolder.newFile(fileName);
        StringBuilder str = new StringBuilder("fn main() {");
        IntStream.range(0, nb).forEach(iteration -> str.append("println!(\"Hello, world! ").append(nb).append(");"));
        str.append("}");
        FileUtils.write(file, str.toString());
        return file;
    }

    private long measureTimeToAnalyzeFile() {
        long t1 = System.currentTimeMillis();
        sensor.execute(context);
        return System.currentTimeMillis() - t1;
    }

}
