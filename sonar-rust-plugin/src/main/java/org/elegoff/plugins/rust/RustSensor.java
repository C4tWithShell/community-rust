/**
 * Sonar Rust Plugin (Community)
 * Copyright (C) 2020 Eric Le Goff
 * http://github.com/elegoff/sonar-rust
 * <p>
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.elegoff.plugins.rust;


import com.google.common.collect.ImmutableList;
import org.elegoff.plugins.rust.language.RustLanguage;
import org.elegoff.rust.checks.CheckList;
import org.sonar.api.batch.fs.FilePredicate;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.rule.CheckFactory;
import org.sonar.api.batch.rule.Checks;
import org.sonar.api.batch.sensor.Sensor;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.SensorDescriptor;
import org.sonar.api.measures.FileLinesContextFactory;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.plugins.rust.api.RustCheck;
import org.sonar.plugins.rust.api.RustFile;
import org.sonarsource.analyzer.commons.ProgressReport;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class RustSensor implements Sensor {

    private static final Logger LOG = Loggers.get(RustSensor.class);

    private final Checks<Object> checks;
    private final FileSystem fileSystem;
    private final FilePredicate mainFilesPredicate;
    private final FileLinesContextFactory fileLinesContextFactory;


    public RustSensor(FileSystem fileSystem, CheckFactory checkFactory, FileLinesContextFactory fileLinesContextFactory) {
        this.fileLinesContextFactory = fileLinesContextFactory;
        List<Class<? extends RustCheck>> checks = ImmutableList.<Class<? extends RustCheck>>builder()
                .addAll(CheckList.getRustChecks())
                .build();
        this.checks = checkFactory.create(CheckList.REPOSITORY_KEY).addAnnotatedChecks((Iterable<?>) checks);
        this.fileSystem = fileSystem;
        this.mainFilesPredicate = fileSystem.predicates().and(
                fileSystem.predicates().hasType(InputFile.Type.MAIN),
                fileSystem.predicates().hasLanguage(RustLanguage.KEY));
    }

    @Override
    public void describe(SensorDescriptor descriptor) {
        descriptor.onlyOnLanguage(RustLanguage.KEY).name("RustSensor");
    }

    @Override
    public void execute(SensorContext context) {
        List<InputFile> inputFiles = new ArrayList<>();
        fileSystem.inputFiles(mainFilesPredicate).forEach(inputFiles::add);

        if (inputFiles.isEmpty()) {
            return;
        }


        ProgressReport progressReport = new ProgressReport("Report about progress of Rust analyzer", TimeUnit.SECONDS.toMillis(10));
        progressReport.start(inputFiles.stream().map(InputFile::toString).collect(Collectors.toList()));

        boolean cancelled = false;
        try {
            for (InputFile inputFile : inputFiles) {
                if (context.isCancelled()) {
                    cancelled = true;
                    break;
                }
                scanFile(context, inputFile);
                progressReport.nextFile();
            }
        } finally {
            if (!cancelled) {
                progressReport.stop();
            } else {
                progressReport.cancel();
            }
        }
    }

    private void scanFile(SensorContext context, InputFile inputFile) {
        try {
            RustFile rustFile = RustFile.create(inputFile);
            LineCounter.analyse(context, fileLinesContextFactory, rustFile);


        } catch (Exception e) {
            processParseException(e, context, inputFile);
        }
    }

    private Iterable<InputFile> getSourceFiles() {
        return rustFiles(InputFile.Type.MAIN);
    }


    private Iterable<InputFile> rustFiles(InputFile.Type type) {
        return fileSystem.inputFiles(fileSystem.predicates().and(fileSystem.predicates().hasLanguage(RustLanguage.KEY), fileSystem.predicates().hasType(type)));
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }


    private void processParseException(Exception e, SensorContext context, InputFile inputFile) {
        reportAnalysisError(e, context, inputFile);

        LOG.warn(String.format("Unable to analyse file %s;", inputFile.uri()));
        LOG.debug("Cause: {}", e.getMessage());


    }

    private static void reportAnalysisError(Exception e, SensorContext context, InputFile inputFile) {
        context.newAnalysisError()
                .onFile(inputFile)
                .message(e.getMessage())
                .save();
    }

}