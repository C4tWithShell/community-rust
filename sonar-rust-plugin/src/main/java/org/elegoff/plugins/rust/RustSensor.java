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
import org.sonar.api.scanner.sensor.ProjectSensor;

import java.io.File;
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

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(SensorContext context) {
        List<InputFile> mainFiles = getInputFiles(InputFile.Type.MAIN, context);
        List<InputFile> testFiles = getInputFiles(InputFile.Type.TEST, context);
        RustScanner scanner = new RustScanner(context, checks, fileLinesContextFactory, noSonarFilter, mainFiles);
        scanner.execute(mainFiles, context);
    }

    private static List<InputFile> getInputFiles(InputFile.Type type, SensorContext context) {
        FilePredicates p = context.fileSystem().predicates();
        Iterable<InputFile> it = context.fileSystem().inputFiles(p.and(p.hasType(type), p.hasLanguage(RustLanguage.KEY)));
        List<InputFile> list = new ArrayList<>();
        it.forEach(list::add);
        return Collections.unmodifiableList(list);
    }

}