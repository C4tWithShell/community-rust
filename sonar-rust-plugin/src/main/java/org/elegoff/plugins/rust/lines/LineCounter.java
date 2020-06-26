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
package org.elegoff.plugins.rust.lines;

import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.FileLinesContext;
import org.sonar.api.measures.FileLinesContextFactory;
import org.sonar.api.measures.Metric;
import org.sonar.api.utils.Version;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.plugins.rust.api.RustFile;

import java.io.Serializable;

public class LineCounter {

    private static final Logger LOG = Loggers.get(LineCounter.class);

    private LineCounter() {
    }

    private static <T extends Serializable> void saveMeasure(SensorContext context, InputFile inputFile, Metric<T> metric, T value) {
        context.<T>newMeasure()
                .withValue(value)
                .forMetric(metric)
                .on(inputFile)
                .save();
    }

    public static void analyse(SensorContext context, FileLinesContextFactory fileLinesContextFactory, RustFile rustFile) {
        InputFile inputFile = rustFile.getInputFile();
        LOG.debug("Count lines in {}", inputFile.uri());
        saveMeasures(
                inputFile,
                new LineCountParser(rustFile.contents()).getLineCountData(),
                fileLinesContextFactory.createFor(inputFile), context);

    }

    private static void saveMeasures(InputFile rustFile, LineCountData data, FileLinesContext fileLinesContext, SensorContext context) {
        for (int line = 1; line <= data.linesNumber(); line++) {
            fileLinesContext.setIntValue(CoreMetrics.NCLOC_DATA_KEY, line, data.linesOfCodeLines().contains(line) ? 1 : 0);
        }
        fileLinesContext.save();

        saveMeasure(context, rustFile, CoreMetrics.COMMENT_LINES, data.effectiveCommentLines().size());
        saveMeasure(context, rustFile, CoreMetrics.NCLOC, data.linesOfCodeLines().size());
    }








}