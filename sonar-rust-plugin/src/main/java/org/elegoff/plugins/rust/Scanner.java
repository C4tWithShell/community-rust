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

import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonarsource.analyzer.commons.ProgressReport;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

abstract class Scanner {
    private static final Logger LOG = Loggers.get(Scanner.class);
    private static final String FAIL_FAST_PROPERTY_NAME = "sonar.internal.analysis.failFast";
    protected final SensorContext context;

    Scanner(SensorContext context) {
        this.context = context;
    }

    void execute(List<InputFile> files, SensorContext context) {
        ProgressReport progressReport = new ProgressReport(this.name() + " progress", TimeUnit.SECONDS.toMillis(10));
        LOG.info("Starting " + this.name());
        List<String> filenames = files.stream().map(InputFile::toString).collect(Collectors.toList());
        progressReport.start(filenames);
        for (InputFile file : files) {
            if (context.isCancelled()) {
                progressReport.cancel();
                return;
            }
            try {
                this.scanFile(file);
            } catch (Exception e) {
                this.processException(e, file);
                if (context.config().getBoolean(FAIL_FAST_PROPERTY_NAME).orElse(false)) {
                    throw new IllegalStateException("Exception when analyzing " + file, e);
                }
            } finally {
                progressReport.nextFile();
            }
        }

        progressReport.stop();
    }

    abstract String name();

    abstract void scanFile(InputFile file) throws IOException;

    abstract void processException(Exception e, InputFile file);
}
