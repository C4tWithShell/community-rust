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


import org.elegoff.plugins.rust.api.IssueLocation;
import org.elegoff.plugins.rust.api.RustCheck;

import org.elegoff.rust.parser.RustParser;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.fs.TextRange;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.issue.NewIssue;
import org.sonar.api.batch.sensor.issue.NewIssueLocation;
import org.sonar.api.issue.NoSonarFilter;
import org.sonar.api.measures.FileLinesContextFactory;
import org.sonar.api.rule.RuleKey;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

import javax.annotation.CheckForNull;
import java.io.File;
import java.io.IOException;
import java.util.*;

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
        //this.cpdAnalyzer = new RustCpdAnalyzer(context);
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

    @CheckForNull
    private static InputFile component(String fileId, SensorContext sensorContext) {
        InputFile inputFile = sensorContext.fileSystem().inputFile(sensorContext.fileSystem().predicates().is(new File(fileId)));
        if (inputFile == null) {
            LOG.debug("Failed to find InputFile for {}", fileId);
        }
        return inputFile;
    }

    private void saveIssues(InputFile inputFile, List<RustCheck.PreciseIssue> issues) {
        for (RustCheck.PreciseIssue preciseIssue : issues) {
            RuleKey ruleKey = checks.ruleKey(preciseIssue.check());
            NewIssue newIssue = context
                    .newIssue()
                    .forRule(ruleKey);

            Integer cost = preciseIssue.cost();
            if (cost != null) {
                newIssue.gap(cost.doubleValue());
            }

            NewIssueLocation primaryLocation = newLocation(inputFile, newIssue, preciseIssue.primaryLocation());
            newIssue.at(primaryLocation);

            Deque<NewIssueLocation> secondaryLocationsFlow = new ArrayDeque<>();

            for (IssueLocation secondaryLocation : preciseIssue.secondaryLocations()) {
                String fileId = secondaryLocation.fileId();
                if (fileId != null) {
                    InputFile issueLocationFile = component(fileId, context);
                    if (issueLocationFile != null) {
                        secondaryLocationsFlow.addFirst(newLocation(issueLocationFile, newIssue, secondaryLocation));
                    }
                } else {
                    newIssue.addLocation(newLocation(inputFile, newIssue, secondaryLocation));
                }
            }

            // secondary locations on multiple files are only supported using flows
            if (!secondaryLocationsFlow.isEmpty()) {
                secondaryLocationsFlow.addFirst(primaryLocation);
                newIssue.addFlow(secondaryLocationsFlow);
            }
            newIssue.save();
        }
    }

    private static NewIssueLocation newLocation(InputFile inputFile, NewIssue issue, IssueLocation location) {
        NewIssueLocation newLocation = issue.newLocation()
                .on(inputFile);
        if (location.startLine() != IssueLocation.UNDEFINED_LINE) {
            TextRange range;
            if (location.startLineOffset() == IssueLocation.UNDEFINED_OFFSET) {
                range = inputFile.selectLine(location.startLine());
            } else {
                range = inputFile.newRange(location.startLine(), location.startLineOffset(), location.endLine(), location.endLineOffset());
            }
            newLocation.at(range);
        }

        String message = location.message();
        if (message != null) {
            newLocation.message(message);
        }
        return newLocation;
    }
}
