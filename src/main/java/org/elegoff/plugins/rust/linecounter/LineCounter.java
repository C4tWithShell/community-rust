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
package org.elegoff.plugins.rust.linecounter;

import org.elegoff.plugins.rust.languages.RustSourceCode;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.FileLinesContext;
import org.sonar.api.measures.FileLinesContextFactory;
import org.sonar.api.measures.Metric;
import org.sonar.api.utils.Version;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

import java.io.IOException;
import java.io.Serializable;

/**
 * Class used to count the code and comment lines of a RUST file and save these "facts" into SonarQube as measures
 */
public class LineCounter {
    private static final Logger LOGGER = Loggers.get(LineCounter.class);

    /**
     * Hide constructor
     */
    private LineCounter() {
    }


    /**
     * Analyzes a file and saves its line measures. This method is called by dependency injection by the Sonar runner.
     *
     * @param context                 the {@code SensorContext}
     * @param fileLinesContextFactory {@code FileLinesContextFactory} used to save line measures
     * @param sourceCode              the source code to be analyzed
     */
    public static void analyse(SensorContext context, FileLinesContextFactory fileLinesContextFactory, RustSourceCode sourceCode) {
        InputFile inputFile = sourceCode.getRustFile();
        LOGGER.debug("Count lines in {}", inputFile.filename());

        try {
            saveMeasures(
                    inputFile,
                    new LineCountParser(sourceCode.getContent()).getLineCountData(),
                    fileLinesContextFactory.createFor(inputFile), context);
        } catch (IOException e) {
            LOGGER.warn("Unable to count lines for file " + inputFile.filename() + ", ignoring measures", e);
        }
    }

    /**
     * Saves the measures of the passed RUST file: lines of code and comments
     *
     * @param rustFile         the RUST file being analyzed
     * @param data             the {@code LineCountData} describing this RUST file
     * @param fileLinesContext {@code FileLinesContext} used to save the code lines and comments of the RUST file
     * @param context          the {@code SensorContext}
     */
    private static void saveMeasures(InputFile rustFile, LineCountData data, FileLinesContext fileLinesContext, SensorContext context) {
        for (int line = 1; line <= data.linesNumber(); line++) {
            fileLinesContext.setIntValue(CoreMetrics.NCLOC_DATA_KEY, line, data.linesOfCodeLines().contains(line) ? 1 : 0);
            if (Version.create(7, 3).isGreaterThanOrEqual(context.getSonarQubeVersion())) {
                fileLinesContext.setIntValue(CoreMetrics.COMMENT_LINES_DATA_KEY, line, data.effectiveCommentLines().contains(line) ? 1 : 0);
            }
        }
        fileLinesContext.save();

        saveMeasure(context, rustFile, CoreMetrics.COMMENT_LINES, data.effectiveCommentLines().size());
        saveMeasure(context, rustFile, CoreMetrics.NCLOC, data.linesOfCodeLines().size());
    }

    /**
     * Saves the passed measure in SonarQube's database
     *
     * @param context   the {@code SensorContext}, used to save the measure
     * @param inputFile the file being analyzed
     * @param metric    a metric to be saved
     * @param value     the metric's value
     */
    private static <T extends Serializable> void saveMeasure(SensorContext context, InputFile inputFile, Metric<T> metric, T value) {
        context.<T>newMeasure()
                .withValue(value)
                .forMetric(metric)
                .on(inputFile)
                .save();
    }
}