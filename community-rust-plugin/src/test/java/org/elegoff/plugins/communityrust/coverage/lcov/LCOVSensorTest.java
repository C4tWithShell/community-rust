/**
 * Community Rust Plugin
 * Copyright (C) 2021 Eric Le Goff
 * mailto:community-rust AT pm DOT me
 * http://github.com/elegoff/sonar-rust
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.elegoff.plugins.communityrust.coverage.lcov;

import org.elegoff.plugins.communityrust.CommunityRustPlugin;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.fs.internal.DefaultInputFile;
import org.sonar.api.batch.fs.internal.FileMetadata;
import org.sonar.api.batch.fs.internal.TestInputFileBuilder;
import org.sonar.api.batch.sensor.internal.DefaultSensorDescriptor;
import org.sonar.api.batch.sensor.internal.SensorContextTester;
import org.sonar.api.config.internal.MapSettings;
import org.sonar.api.utils.log.LogTester;
import org.sonar.api.utils.log.LoggerLevel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import static org.assertj.core.api.Assertions.assertThat;

public class LCOVSensorTest {

    private static final String REPORT1 = "reports/report_1.lcov";
    private static final String REPORT2 = "reports/report_2.lcov";
    private static final String TWO_REPORTS = REPORT1 + ", " + REPORT2;

    private SensorContextTester context;
    private MapSettings settings;
    @ClassRule
    public static TemporaryFolder temp = new TemporaryFolder();

    private LCOVSensor coverageSensor = new LCOVSensor();
    private File moduleBaseDir = new File("src/test/resources/lcov/").getAbsoluteFile();

    @org.junit.Rule
    public LogTester logTester = new LogTester();

    @Before
    public void init() throws FileNotFoundException {
        settings = new MapSettings();

        context = SensorContextTester.create(moduleBaseDir);
        context.setSettings(settings);

        inputFile("file1.rs", InputFile.Type.MAIN);
        inputFile("file2.rs", InputFile.Type.MAIN);
    }

    private InputFile inputFile(String relativePath, InputFile.Type type) throws FileNotFoundException {
        DefaultInputFile inputFile = new TestInputFileBuilder("moduleKey", relativePath)
                .setModuleBaseDir(moduleBaseDir.toPath())
                .setLanguage("rust")
                .setType(type)
                .build();

        inputFile.setMetadata(new FileMetadata(s -> {}).readMetadata(new FileInputStream(inputFile.file()), StandardCharsets.UTF_8, inputFile.absolutePath()));
        context.fileSystem().add(inputFile);

        return inputFile;
    }

    @Test
    public void report_not_found() throws Exception {
        settings.setProperty(CommunityRustPlugin.LCOV_REPORT_PATHS, "/wrong/path/lcov_report.dat");
        coverageSensor.execute(context);
        assertThat(context.lineHits("moduleKey:file1.js", 1)).isNull();
    }

    @Test
    public void test_coverage() {
        settings.setProperty(CommunityRustPlugin.LCOV_REPORT_PATHS, TWO_REPORTS);
        coverageSensor.execute(context);
        assertTwoReportsCoverageDataPresent();
    }




    private void assertTwoReportsCoverageDataPresent() {
        Integer[] file1Expected = {3, 3, 1, null};
        Integer[] file2Expected = {5, 5, null, null};

        for (int line = 1; line <= 4; line++) {
            assertThat(context.lineHits("moduleKey:file1.rs", line)).isEqualTo(file1Expected[line - 1]);
            assertThat(context.lineHits("moduleKey:file2.rs", line)).isEqualTo(file2Expected[line - 1]);
            assertThat(context.lineHits("moduleKey:file3.rs", line)).isNull();
            //assertThat(context.lineHits("moduleKey:tests/file1.js", line)).isNull();
        }

        assertThat(context.conditions("moduleKey:file1.rs", 1)).isNull();
        assertThat(context.conditions("moduleKey:file1.rs", 2)).isEqualTo(4);
        assertThat(context.coveredConditions("moduleKey:file1.rs", 2)).isEqualTo(3);
    }

    @Test
    public void should_ignore_and_log_warning_for_invalid_line() {
        settings.setProperty(CommunityRustPlugin.LCOV_REPORT_PATHS, "reports/wrong_line_report.lcov");
        coverageSensor.execute(context);

        assertThat(context.lineHits("moduleKey:file1.rs", 0)).isNull();
        assertThat(context.lineHits("moduleKey:file1.rs", 2)).isEqualTo(1);

        assertThat(context.conditions("moduleKey:file1.rs", 102)).isNull();
        assertThat(context.conditions("moduleKey:file1.rs", 2)).isEqualTo(3);
        assertThat(context.coveredConditions("moduleKey:file1.rs", 2)).isEqualTo(1);

        assertThat(logTester.logs()).contains("Error while parsing LCOV report: can't save DA data for line 3 of coverage report file (java.lang.IllegalArgumentException: Line number 0 doesn't exist in file file1.rs).");

        assertThat(logTester.logs()).contains("Error while parsing LCOV report: can't save BRDA data for line 8 of coverage report file (java.lang.IllegalArgumentException: Line number 102 doesn't exist in file file1.rs).");
    }

    @Test
    public void test_unresolved_path() {
        settings.setProperty(CommunityRustPlugin.LCOV_REPORT_PATHS, "reports/report_with_unresolved_path.lcov");
        coverageSensor.execute(context);
        String fileName = File.separator + "reports" + File.separator + "report_with_unresolved_path.lcov";
        assertThat(logTester.logs(LoggerLevel.WARN))
                .contains("Could not resolve 2 file paths in [" + moduleBaseDir.getAbsolutePath() + fileName + "]")
                .contains("First unresolved path: unresolved/file1.rs (Run in DEBUG mode to get full list of unresolved paths)");
        assertThat(logTester.logs(LoggerLevel.DEBUG))
                .contains("Using pattern 'reports/report_with_unresolved_path.lcov' to find reports");
    }

    @Test
    public void test_unresolved_path_with_debug_log() {
        logTester.setLevel(LoggerLevel.DEBUG);
        settings.setProperty(CommunityRustPlugin.LCOV_REPORT_PATHS, "reports/report_with_unresolved_path.lcov");
        coverageSensor.execute(context);
        String fileName = File.separator + "reports" + File.separator + "report_with_unresolved_path.lcov";
        assertThat(logTester.logs(LoggerLevel.WARN))
                .contains("Could not resolve 2 file paths in [" + moduleBaseDir.getAbsolutePath() + fileName + "]");
        assertThat(logTester.logs(LoggerLevel.DEBUG))
                .contains("Unresolved paths:\n" +
                        "unresolved/file1.rs\n" +
                        "unresolved/file2.rs");
    }

    @Test
    public void should_log_warning_when_wrong_data() throws Exception {
        settings.setProperty(CommunityRustPlugin.LCOV_REPORT_PATHS, "reports/wrong_data_report.lcov");
        coverageSensor.execute(context);

        assertThat(context.lineHits("moduleKey:file1.rs", 1)).isNull();
        assertThat(context.lineHits("moduleKey:file1.rs", 2)).isEqualTo(1);

        assertThat(context.conditions("moduleKey:file1.rs", 2)).isEqualTo(2);
        assertThat(context.coveredConditions("moduleKey:file1.rs", 2)).isEqualTo(2);

        assertThat(logTester.logs(LoggerLevel.DEBUG)).contains("Error while parsing LCOV report: can't save DA data for line 3 of coverage report file (java.lang.NumberFormatException: For input string: \"1.\").");
        String stringIndexOutOfBoundLogMessage = logTester.logs(LoggerLevel.DEBUG).get(1);
        assertThat(stringIndexOutOfBoundLogMessage).startsWith("Error while parsing LCOV report: can't save DA data for line 3 of coverage report file (java.lang.NumberFormatException:");
        assertThat(logTester.logs(LoggerLevel.DEBUG).get(logTester.logs(LoggerLevel.DEBUG).size() - 1)).startsWith("Error while parsing LCOV report: can't save BRDA data for line 6 of coverage report file (java.lang.ArrayIndexOutOfBoundsException: ");
        assertThat(logTester.logs(LoggerLevel.WARN)).contains("Found 3 inconsistencies in coverage report");
    }


    @Test
    public void sensor_descriptor() {
        DefaultSensorDescriptor descriptor = new DefaultSensorDescriptor();
        coverageSensor.describe(descriptor);
        assertThat(descriptor.name()).isEqualTo("LCOV Sensor for Rust coverage");
        assertThat(descriptor.languages()).containsOnly("rust");
    }

    @Test
    public void should_resolve_relative_path() throws Exception {
        settings.setProperty(CommunityRustPlugin.LCOV_REPORT_PATHS, "reports/report_relative_path.lcov");
        inputFile("deeply/nested/example/file1.rs", InputFile.Type.MAIN);
        inputFile("deeply/nested/example/file2.rs", InputFile.Type.MAIN);
        coverageSensor.execute(context);

        String file1Key = "moduleKey:deeply/nested/example/file1.rs";
        assertThat(context.lineHits(file1Key, 0)).isNull();
        assertThat(context.lineHits(file1Key, 1)).isEqualTo(2);
        assertThat(context.lineHits(file1Key, 2)).isEqualTo(2);

        assertThat(context.conditions(file1Key, 102)).isNull();
        assertThat(context.conditions(file1Key, 2)).isEqualTo(4);
        assertThat(context.coveredConditions(file1Key, 2)).isEqualTo(2);

        String file2Key = "moduleKey:deeply/nested/example/file2.rs";
        assertThat(context.lineHits(file2Key, 0)).isNull();
        assertThat(context.lineHits(file2Key, 1)).isEqualTo(5);
        assertThat(context.lineHits(file2Key, 2)).isEqualTo(5);
    }

    @Test
    public void should_resolve_absolute_path() throws Exception {
        File lcovFile = temp.newFile();
        String absolutePathFile1 = new File("src/test/resources/lcov/file1.rs").getAbsolutePath();
        String absolutePathFile2 = new File("src/test/resources/lcov/file2.rs").getAbsolutePath();

        Files.write(lcovFile.toPath(),
                ("SF:" + absolutePathFile1 + "\n" +
                        "DA:1,2\n" +
                        "DA:2,2\n" +
                        "DA:3,1\n" +
                        "FN:2,(anonymous_1)\n" +
                        "FNDA:2,(anonymous_1)\n" +
                        "BRDA:2,1,0,2\n" +
                        "BRDA:2,1,1,1\n" +
                        "BRDA:2,2,0,0\n" +
                        "BRDA:2,2,1,-\n" +
                        "end_of_record\n" +
                        "SF:" + absolutePathFile2 + "\n" +
                        "DA:1,5\n" +
                        "DA:2,5\n" +
                        "end_of_record\n").getBytes(StandardCharsets.UTF_8));
        settings.setProperty(CommunityRustPlugin.LCOV_REPORT_PATHS, lcovFile.getAbsolutePath());
        inputFile("file1.rs", InputFile.Type.MAIN);
        inputFile("file2.rs", InputFile.Type.MAIN);
        coverageSensor.execute(context);

        String file1Key = "moduleKey:file1.rs";
        assertThat(context.lineHits(file1Key, 0)).isNull();
        assertThat(context.lineHits(file1Key, 1)).isEqualTo(2);
        assertThat(context.lineHits(file1Key, 2)).isEqualTo(2);

        assertThat(context.conditions(file1Key, 102)).isNull();
        assertThat(context.conditions(file1Key, 2)).isEqualTo(4);
        assertThat(context.coveredConditions(file1Key, 2)).isEqualTo(2);

        String file2Key = "moduleKey:file2.rs";
        assertThat(context.lineHits(file2Key, 0)).isNull();
        assertThat(context.lineHits(file2Key, 1)).isEqualTo(5);
        assertThat(context.lineHits(file2Key, 2)).isEqualTo(5);
    }



}
