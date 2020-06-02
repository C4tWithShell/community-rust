/**
 *
 * Sonar Rust Plugin (Community)
 * Copyright (C) 2020 Eric Le Goff
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

import org.assertj.core.api.SoftAssertions;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.fs.internal.DefaultInputFile;
import org.sonar.api.batch.rule.ActiveRules;
import org.sonar.api.batch.rule.CheckFactory;
import org.sonar.api.batch.sensor.internal.SensorContextTester;
import org.sonar.api.issue.NoSonarFilter;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.FileLinesContext;
import org.sonar.api.measures.FileLinesContextFactory;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RustFileLinesContextTest {
    private FileLinesContextForTesting fileLinesContext;

    private class FileLinesContextForTesting implements FileLinesContext {

        public final Set<Integer> executableLines = new HashSet<>();
        public final Set<Integer> linesOfCode = new HashSet<>();

        @Override
        public void setIntValue(String metricKey, int line, int value) {
            Assert.assertEquals(1, value);

            switch (metricKey) {
                case CoreMetrics.NCLOC_DATA_KEY:
                    linesOfCode.add(line);
                    break;
                case CoreMetrics.EXECUTABLE_LINES_DATA_KEY:
                    executableLines.add(line);
                    break;
                default:
                    Assert.fail("Unsupported metric key " + metricKey);
            }
        }

        @Override
        public void setStringValue(String metricKey, int line, String value) {
            Assert.fail("unexpected method called: setStringValue()");
        }

        @Override
        public void save() {
        }
    }

    @Before
    public void setUp() throws IOException {
        ActiveRules rules = mock(ActiveRules.class);
        var checkFactory = new CheckFactory(rules);
        FileLinesContextFactory fileLinesContextFactory = mock(FileLinesContextFactory.class);
        fileLinesContext = new FileLinesContextForTesting();
        when(fileLinesContextFactory.createFor(Mockito.any(InputFile.class))).thenReturn(fileLinesContext);

        File baseDir = Utils.loadResource("/org/elegoff/plugins/rust");
        SensorContextTester context = SensorContextTester.create(baseDir);
        DefaultInputFile inputFile = Utils.buildInputFile(baseDir, "main.rs");
        context.fileSystem().add(inputFile);

        RustSensor sensor = new RustSensor(fileLinesContextFactory, checkFactory, new NoSonarFilter());
        sensor.execute(context);
    }

    @Test
    public void TestLinesOfCode() throws UnsupportedEncodingException, IOException {
        Set<Integer> linesOfCode = Stream.of(10)
                .collect(Collectors.toCollection(HashSet::new));

        var softly = new SoftAssertions();
        assertThat(fileLinesContext.linesOfCode).containsExactlyInAnyOrderElementsOf(linesOfCode);
        softly.assertAll();
    }
}
