/**
 * Sonar Rust Plugin (Community)
 * Copyright (C) 2021 Eric Le Goff
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
package org.elegoff.plugins.rust;

import com.google.common.collect.ImmutableList;
import org.assertj.core.api.Condition;
import org.elegoff.plugins.rust.language.RustLanguage;
import org.elegoff.rust.checks.CheckList;
import org.fest.assertions.Assertions;
import org.junit.Before;
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
import org.sonar.api.batch.rule.internal.DefaultActiveRules;
import org.sonar.api.batch.sensor.highlighting.TypeOfText;
import org.sonar.api.batch.sensor.internal.DefaultSensorDescriptor;
import org.sonar.api.batch.sensor.internal.SensorContextTester;
import org.sonar.api.batch.sensor.issue.Issue;
import org.sonar.api.config.internal.MapSettings;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

public class RustSensorTest {

    private FileLinesContext fileLinesContext;
    private SensorContextTester tester;
    private RustSensor sensor;
    private File dir = new File("src/test/resources/");
    private static String FILE1 = "sensor/main.rs";
    private static String FILE2 = "sensor/main2.rs";
    private static String FILE3 = "sensor/parser.rs";

    @org.junit.Rule
    public LogTester logTester = new LogTester();

    @Before
    public void init() {
        tester = SensorContextTester.create(dir);

        MapSettings settings = RustPluginConfigurationTest.getDefaultSettings();
        tester.setSettings(settings);

        FileLinesContextFactory fileLinesContextFactory = mock(FileLinesContextFactory.class);
        fileLinesContext = mock(FileLinesContext.class);
        when(fileLinesContextFactory.createFor(any(InputFile.class))).thenReturn(fileLinesContext);
        ActiveRulesBuilder activeRuleBuilder = new ActiveRulesBuilder();
        CheckFactory checkFactory = new CheckFactory(activeRuleBuilder.build());
        sensor = new RustSensor(checkFactory, fileLinesContextFactory);
    }

    @Test
    public void analyse() throws IOException {
        DefaultInputFile inputFile = executeSensorOnSingleFile(FILE1);

        assertEquals((Integer) 715, tester.measure(inputFile.key(), CoreMetrics.NCLOC).value());
        assertEquals((Integer) 164, tester.measure(inputFile.key(), CoreMetrics.STATEMENTS).value());
        assertEquals((Integer) 164, tester.measure(inputFile.key(), CoreMetrics.COMPLEXITY).value());
        assertEquals((Integer) 53, tester.measure(inputFile.key(), CoreMetrics.COMMENT_LINES).value());
        //assertEquals((Integer) 1, tester.measure(inputFile.key(), CoreMetrics.FUNCTIONS).value());
        assertEquals(715, tester.cpdTokens(inputFile.key()).size());
        assertEquals(Collections.singletonList(TypeOfText.COMMENT), tester.highlightingTypeAt(inputFile.key(), 1, 1));
        assertEquals(Collections.singletonList(TypeOfText.KEYWORD), tester.highlightingTypeAt(inputFile.key(), 14, 1));
        assertEquals(Collections.singletonList(TypeOfText.STRING), tester.highlightingTypeAt(inputFile.key(), 25, 17));

        assertEquals(0, tester.allIssues().size());

        verify(fileLinesContext).setIntValue(CoreMetrics.NCLOC_DATA_KEY, 14, 1);
        verify(fileLinesContext).setIntValue(CoreMetrics.NCLOC_DATA_KEY, 15, 1);
        verify(fileLinesContext).setIntValue(CoreMetrics.NCLOC_DATA_KEY, 17, 1);


        verify(fileLinesContext).save();

        Assertions.assertThat(tester.allAnalysisErrors()).isEmpty();

    }

    @Test
    public void analyse2() throws IOException {
        DefaultInputFile inputFile = executeSensorOnSingleFile(FILE2);

        assertEquals((Integer) 6, tester.measure(inputFile.key(), CoreMetrics.NCLOC).value());
        assertEquals((Integer) 4, tester.measure(inputFile.key(), CoreMetrics.STATEMENTS).value());
        assertEquals((Integer) 4, tester.measure(inputFile.key(), CoreMetrics.COMPLEXITY).value());
        assertEquals((Integer) 1, tester.measure(inputFile.key(), CoreMetrics.COMMENT_LINES).value());
        //assertEquals((Integer) 1, tester.measure(inputFile.key(), CoreMetrics.FUNCTIONS).value());
        assertEquals(6, tester.cpdTokens(inputFile.key()).size());
        assertEquals(Collections.singletonList(TypeOfText.KEYWORD), tester.highlightingTypeAt(inputFile.key(), 1, 1));
        assertEquals(Collections.singletonList(TypeOfText.COMMENT), tester.highlightingTypeAt(inputFile.key(), 6, 1));
        assertEquals(Collections.singletonList(TypeOfText.STRING), tester.highlightingTypeAt(inputFile.key(), 7, 13));

        assertEquals(0, tester.allIssues().size());

        verify(fileLinesContext).setIntValue(CoreMetrics.NCLOC_DATA_KEY, 1, 1);
        verify(fileLinesContext).setIntValue(CoreMetrics.NCLOC_DATA_KEY, 2, 1);
        verify(fileLinesContext).setIntValue(CoreMetrics.NCLOC_DATA_KEY, 4, 1);


        verify(fileLinesContext).save();

        Assertions.assertThat(tester.allAnalysisErrors()).isEmpty();

    }


    @Test
    public void canParse(){
        List<String> filesToParse = new ArrayList<>();
        filesToParse.add(FILE1);
        filesToParse.add(FILE2);
        filesToParse.add(FILE3);

        for (String fileName : filesToParse){

            try {
                DefaultInputFile f = addInputFile(fileName);
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
        sensor.execute(tester);
        Assertions.assertThat(tester.allAnalysisErrors()).isEmpty();


    }


    private DefaultInputFile executeSensorOnSingleFile(String fileName) throws IOException {
        DefaultInputFile inputFile = addInputFile(fileName);
        sensor.execute(tester);
        return inputFile;
    }

    @Test
    public void two_files_without_cancellation() throws Exception {
        DefaultInputFile file1 = addInputFile(FILE1);
        DefaultInputFile file2 = addInputFile(FILE2);
        sensor.execute(tester);
        Assertions.assertThat(tester.measure(file1.key(), CoreMetrics.NCLOC)).isNotNull();
        Assertions.assertThat(tester.measure(file2.key(), CoreMetrics.NCLOC)).isNotNull();
    }

    @Test
    public void two_files_with_cancellation() throws Exception {
        DefaultInputFile file1 = addInputFile(FILE1);
        DefaultInputFile file2 = addInputFile(FILE2);
        tester.setCancelled(true);
        sensor.execute(tester);
        Assertions.assertThat(tester.measure(file2.key(), CoreMetrics.NCLOC)).isNotNull();
        Assertions.assertThat(tester.measure(file1.key(), CoreMetrics.NCLOC)).isNull();
    }

    private DefaultInputFile addInputFile(String fileName) throws IOException {
        String content = new String(Files.readAllBytes(new File(dir, fileName).toPath()));
        Assertions.assertThat(content).isNotEmpty();
        DefaultInputFile inputFile = new TestInputFileBuilder(tester.module().key(), fileName)
                .setModuleBaseDir(tester.fileSystem().baseDirPath())
                .setType(InputFile.Type.MAIN)
                .setLanguage(RustLanguage.KEY)
                .setCharset(StandardCharsets.UTF_8)
                .initMetadata(content)
                .build();
        tester.fileSystem().add(inputFile);
        return inputFile;
    }

}
