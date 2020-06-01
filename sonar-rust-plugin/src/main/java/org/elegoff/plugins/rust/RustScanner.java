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

import org.elegoff.plugins.rust.api.rust.parser.RustParser;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.issue.NoSonarFilter;
import org.sonar.api.measures.FileLinesContextFactory;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RustScanner extends Scanner{

    private static final Logger LOG = Loggers.get(RustScanner.class);

    private final RustParser parser;
    private final Map<InputFile, String> packageNames = new HashMap<>();
    private final RustChecks checks;
    private final FileLinesContextFactory fileLinesContextFactory;
    private final NoSonarFilter noSonarFilter;
    //private final RustCpdAnalyzer cpdAnalyzer;
    //private final Map<String, Set<Symbol>> globalSymbolsByModuleName = SymbolUtils.externalModulesSymbols();

    public RustScanner(
            SensorContext context, RustChecks checks,
            FileLinesContextFactory fileLinesContextFactory, NoSonarFilter noSonarFilter, List<InputFile> files
    ) {
        super(context);
        this.checks = checks;
        this.fileLinesContextFactory = fileLinesContextFactory;
        this.noSonarFilter = noSonarFilter;
        //this.cpdAnalyzer = new PythonRustCpdAnalyzer(context);
        this.parser = RustParser.create();

        // computes "globalSymbolsByModuleName"
        //GlobalSymbolsScanner globalSymbolsStep = new GlobalSymbolsScanner(context);
        //globalSymbolsStep.execute(files, context);
    }

    @Override
    String name() {
        return null;
    }

    @Override
    void scanFile(InputFile file) throws IOException {

    }

    @Override
    void processException(Exception e, InputFile file) {

    }
}
