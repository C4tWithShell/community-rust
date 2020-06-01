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
package org.elegoff.plugins.rust.rules;

import org.elegoff.plugins.rust.language.RustLanguage;
import org.elegoff.plugins.rust.language.RustSourceCode;
import org.elegoff.plugins.rust.parsing.LineCounter;
import org.elegoff.plugins.rust.settings.RustLanguageSettings;
import org.sonar.api.batch.fs.FilePredicate;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.rule.CheckFactory;
import org.sonar.api.batch.sensor.Sensor;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.SensorDescriptor;
import org.sonar.api.measures.FileLinesContextFactory;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;


import java.io.IOException;


/**
 * Main sensor
 */
public class RustSensor implements Sensor {
    private static final Logger LOGGER = Loggers.get(RustSensor.class);


    private final FileSystem fileSystem;
    private final FilePredicate mainFilesPredicate;
    private final FileLinesContextFactory fileLinesContextFactory;


    /**
     * Constructor
     *
     * @param fileSystem the file system on which the sensor will find the files to be analyzed
     * @param checkFactory check factory used to get the checks to execute against the files
     * @param fileLinesContextFactory factory used to report measures
     */
    public RustSensor(FileSystem fileSystem, CheckFactory checkFactory, FileLinesContextFactory fileLinesContextFactory) {
        this.fileLinesContextFactory = fileLinesContextFactory;
        this.fileSystem = fileSystem;
        this.mainFilesPredicate = fileSystem.predicates().and(
                fileSystem.predicates().hasType(InputFile.Type.MAIN),
                fileSystem.predicates().hasLanguage(RustLanguage.KEY));
    }


    @Override
    public void describe(SensorDescriptor descriptor) {
        descriptor.onlyOnLanguage(RustLanguage.KEY);
        descriptor.name("RUST Sensor");
    }

    @Override
    public void execute(SensorContext context) {
        LOGGER.debug("RUST sensor executed with context: " + context);

        for (InputFile inputFile : fileSystem.inputFiles(mainFilesPredicate)) {
            LOGGER.debug("Analyzing file: " + inputFile.filename());
            try {
                RustSourceCode sourceCode = new RustSourceCode(inputFile, context.config().getBoolean(RustLanguageSettings.FILTER_UTF8_LB_KEY));
                computeLinesMeasures(context, sourceCode);

            } catch (IOException e) {
                LOGGER.warn("Error reading source file " + inputFile.filename(), e);
            }
        }
    }


    /**
     * Calculates and feeds line measures (comments, actual number of code lines)
     *
     * @param context the sensor context
     * @param sourceCode the RUST source code to be analyzed
     */
    private void computeLinesMeasures(SensorContext context, RustSourceCode sourceCode) {
        LineCounter.analyse(context, fileLinesContextFactory, sourceCode);
    }

}