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
package org.elegoff.plugins.rust;

import org.elegoff.plugins.rust.language.RustLanguage;
import org.elegoff.rust.checks.CheckList;
import org.sonar.api.batch.fs.FilePredicates;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.rule.CheckFactory;
import org.sonar.api.batch.sensor.Sensor;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.SensorDescriptor;
import org.sonar.api.issue.NoSonarFilter;
import org.sonar.api.measures.FileLinesContextFactory;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Main sensor
 */
public class RustSensor implements Sensor {

    private final RustChecks checks;
    private final FileLinesContextFactory fileLinesContextFactory;
    private final NoSonarFilter noSonarFilter;

    public RustSensor(FileLinesContextFactory fileLinesContextFactory, CheckFactory checkFactory, NoSonarFilter noSonarFilter) {
        this.checks = new RustChecks(checkFactory)
                .addChecks(CheckList.REPOSITORY_KEY, CheckList.getChecks());
        this.fileLinesContextFactory = fileLinesContextFactory;
        this.noSonarFilter = noSonarFilter;
    }


    @Override
    public void describe(SensorDescriptor descriptor) {
        descriptor
                .onlyOnLanguage(RustLanguage.KEY)
                .name("Rust Sensor")
                .onlyOnFileType(InputFile.Type.MAIN);

    }

    @Override
    public void execute(SensorContext context) {
        List<InputFile> mainFiles = getInputFiles(InputFile.Type.MAIN, context);
        List<InputFile> testFiles = getInputFiles(InputFile.Type.TEST, context);
        RustScanner scanner = new RustScanner(context, checks, fileLinesContextFactory, noSonarFilter, mainFiles);
        scanner.execute(mainFiles, context);
        if (!testFiles.isEmpty()) {
            //new TestHighlightingScanner(context).execute(testFiles, context);
        }
    }

    private static List<InputFile> getInputFiles(InputFile.Type type, SensorContext context) {
        FilePredicates p = context.fileSystem().predicates();
        Iterable<InputFile> it = context.fileSystem().inputFiles(p.and(p.hasType(type), p.hasLanguage(RustLanguage.KEY)));
        List<InputFile> list = new ArrayList<>();
        it.forEach(list::add);
        return Collections.unmodifiableList(list);
    }


}