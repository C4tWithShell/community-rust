/**
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.elegoff.plugins.rust.externalreport.clippy;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import javax.annotation.Nullable;

import org.elegoff.plugins.rust.clippy.ClippySensor;
import org.junit.Rule;
import org.junit.Test;
import org.sonar.api.SonarEdition;
import org.sonar.api.SonarQubeSide;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.fs.TextRange;
import org.sonar.api.batch.fs.internal.TestInputFileBuilder;
import org.sonar.api.batch.rule.Severity;
import org.sonar.api.batch.sensor.internal.DefaultSensorDescriptor;
import org.sonar.api.batch.sensor.internal.SensorContextTester;
import org.sonar.api.batch.sensor.issue.ExternalIssue;
import org.sonar.api.batch.sensor.issue.IssueLocation;
import org.sonar.api.internal.SonarRuntimeImpl;
import org.sonar.api.rules.RuleType;
import org.sonar.api.utils.Version;
import org.sonar.api.utils.log.LogTester;
import org.sonar.api.utils.log.LoggerLevel;

import static org.assertj.core.api.Assertions.assertThat;
import static java.nio.charset.StandardCharsets.UTF_8;

public class ClippySensorTest{
    private static final String CLIPPY_FILE = "rust-project:clippy/main.rs";
    private static final String CLIPPY_AEC="external_clippy:clippy::absurd_extreme_comparisons";
    private static final String CLIPPY_REPORT_TXT = "myreport.txt";
    private static final String UNKNOWN_KEY_REPORT = "synreport.txt";

    private static final Path PROJECT_DIR = Paths.get("src", "test", "resources", "org", "sonar", "plugins", "rust", "clippy");

    private static ClippySensor clippySensor = new ClippySensor();

    @Rule
    public LogTester logTester = new LogTester();

    @Test
    public void testDescriptor() {
        DefaultSensorDescriptor sensorDescriptor = new DefaultSensorDescriptor();
        clippySensor.describe(sensorDescriptor);
        assertThat(sensorDescriptor.name()).isEqualTo("Import of Clippy issues");
        assertThat(sensorDescriptor.languages()).containsOnly("rust");
        assertThat(sensorDescriptor.configurationPredicate()).isNotNull();
        assertNoErrorWarnDebugLogs(logTester);
    }

    @Test
    public void issuesDetection() throws IOException {
        List<ExternalIssue> externalIssues = executeSensorImporting(7, 9, CLIPPY_REPORT_TXT);
        assertThat(externalIssues).hasSize(2);

        ExternalIssue first = externalIssues.get(0);
        assertThat(first.ruleKey().toString()).isEqualTo(CLIPPY_AEC);
        assertThat(first.type()).isEqualTo(RuleType.CODE_SMELL);
        assertThat(first.severity()).isEqualTo(Severity.MAJOR);
        IssueLocation firstPrimaryLoc = first.primaryLocation();
        assertThat(firstPrimaryLoc.inputComponent().key()).isEqualTo(CLIPPY_FILE);
        assertThat(firstPrimaryLoc.message())
                .isEqualTo("this comparison involving the minimum or maximum element for this type contains a case that is always true or always false\n" +
                        "because `0` is the minimum value for this type, the case where the two sides are not equal never occurs, consider using `vec.len() == 0` instead");
        TextRange firstTextRange = firstPrimaryLoc.textRange();
        assertThat(firstTextRange).isNotNull();
        assertThat(firstTextRange.start().line()).isEqualTo(9);
        assertThat(firstTextRange.end().line()).isEqualTo(9);

        ExternalIssue second = externalIssues.get(1);
        assertThat(second.ruleKey().toString()).isEqualTo(CLIPPY_AEC);
        assertThat(second.type()).isEqualTo(RuleType.CODE_SMELL);
        assertThat(second.severity()).isEqualTo(Severity.MAJOR);
        IssueLocation secondPrimaryLoc = second.primaryLocation();
        assertThat(secondPrimaryLoc.inputComponent().key()).isEqualTo(CLIPPY_FILE);
        assertThat(secondPrimaryLoc.message()).isEqualTo("this comparison involving the minimum or maximum element for this type contains a case that is always true or always false\n" +
                "because `std::i32::MAX` is the maximum value for this type, this comparison is always false");
        TextRange secondTextRange = secondPrimaryLoc.textRange();
        assertThat(secondTextRange).isNotNull();
        assertThat(secondTextRange.start().line()).isEqualTo(10);
        assertThat(secondTextRange.end().line()).isEqualTo(10);
        assertNoErrorWarnDebugLogs(logTester);
    }

    @Test
    public void noIssuesWithoutReportPathsProperty() throws IOException {
        List<ExternalIssue> externalIssues = executeSensorImporting(7, 9, null);
        assertThat(externalIssues).isEmpty();
        assertNoErrorWarnDebugLogs(logTester);
    }

    @Test
    public void noIssuesWithInvalidReportPath() throws IOException {
        List<ExternalIssue> externalIssues = executeSensorImporting(7, 9, "invalid-path.txt");
        assertThat(externalIssues).isEmpty();
        assertThat(onlyOneLogElement(logTester.logs(LoggerLevel.ERROR)))
                .startsWith("No issues information will be saved as the report file '")
                .contains("invalid-path.txt' can't be read.");
    }

    @Test
    public void noIssuesWithEmptyClippyReport() throws IOException {
        List<ExternalIssue> externalIssues = executeSensorImporting(7, 9, "empty-report.txt");
        assertThat(externalIssues).isEmpty();
        assertNoErrorWarnDebugLogs(logTester);
    }

    @Test
    public void clippyReportWithUnknownRuleKey() throws IOException {
        List<ExternalIssue> externalIssues = executeSensorImporting(7, 9, UNKNOWN_KEY_REPORT);
        assertThat(externalIssues).hasSize(4);

        ExternalIssue first = externalIssues.get(0);
        IssueLocation firstPrimaryLoc = first.primaryLocation();
        assertThat(firstPrimaryLoc.message())
                .isEqualTo("`if _ { .. } else { .. }` is an expression\n" +
                        "it is more idiomatic to write\n" +
                        "let <mut> default = if is_maybe_const { ..; Some(Type::Verbatim(verbatim::between(begin_bound, input))) } else { if eq_token.is_some() {\n" +
                        "                Some(input.parse::<Type>()?)\n" +
                        "            } else {\n" +
                        "                None\n" +
                        "            } };");

    }


    public static void assertNoErrorWarnDebugLogs(LogTester logTester) {
        org.assertj.core.api.Assertions.assertThat(logTester.logs(LoggerLevel.ERROR)).isEmpty();
        org.assertj.core.api.Assertions.assertThat(logTester.logs(LoggerLevel.WARN)).isEmpty();
        org.assertj.core.api.Assertions.assertThat(logTester.logs(LoggerLevel.DEBUG)).isEmpty();
    }

    private static List<ExternalIssue> executeSensorImporting(int majorVersion, int minorVersion, @Nullable String fileName) throws IOException {
        Path baseDir = PROJECT_DIR.getParent();
        SensorContextTester context = SensorContextTester.create(baseDir);
        try (Stream<Path> fileStream = Files.list(PROJECT_DIR)) {
            fileStream.forEach(file -> addFileToContext(context, baseDir, file));
            context.setRuntime(SonarRuntimeImpl.forSonarQube(Version.create(majorVersion, minorVersion), SonarQubeSide.SERVER, SonarEdition.DEVELOPER));
            if (fileName != null) {
                String path = PROJECT_DIR.resolve(fileName).toAbsolutePath().toString();
                context.settings().setProperty("sonar.rust.clippy.reportPaths", path);
            }
            clippySensor.execute(context);
            return new ArrayList<>(context.allExternalIssues());
        }
    }

    private static void addFileToContext(SensorContextTester context, Path projectDir, Path file) {
        try {
            String projectId = projectDir.getFileName().toString() + "-project";
            context.fileSystem().add(TestInputFileBuilder.create(projectId, projectDir.toFile(), file.toFile())
                    .setCharset(UTF_8)
                    .setLanguage(language(file))
                    .setContents(new String(Files.readAllBytes(file), UTF_8))
                    .setType(InputFile.Type.MAIN)
                    .build());
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private static String language(Path file) {
        String path = file.toString();
        return path.substring(path.lastIndexOf('.') + 1);
    }

    public static String onlyOneLogElement(List<String> elements) {
        assertThat(elements).hasSize(1);
        return elements.get(0);
    }

}