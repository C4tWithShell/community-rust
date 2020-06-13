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


import com.google.common.collect.ImmutableList;
import org.elegoff.plugins.rust.language.RustLanguage;
import org.elegoff.rust.checks.CheckList;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.Sensor;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.SensorDescriptor;
import org.sonar.api.config.Configuration;
import org.sonar.api.issue.NoSonarFilter;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.plugins.rust.api.RustCheck;
import org.sonar.rust.Measurer;
import org.sonar.rust.RustScanner;
import org.sonar.rust.SonarComponents;

import java.util.List;

public class RustSensor implements Sensor {
    private static final Logger LOG = Loggers.get(RustSensor.class);

    private final SonarComponents sonarComponents;
    private final FileSystem fs;

    private final Configuration settings;
    private final NoSonarFilter noSonarFilter;

    public RustSensor(SonarComponents sonarComponents, FileSystem fs,
                             Configuration settings, NoSonarFilter noSonarFilter) {
        this.noSonarFilter = noSonarFilter;
        this.sonarComponents = sonarComponents;
        this.fs = fs;
        this.settings = settings;
    }

    @Override
    public void describe(SensorDescriptor descriptor) {
        descriptor.onlyOnLanguage(RustLanguage.KEY).name("RustSensor");
    }

    @Override
    public void execute(SensorContext context) {
        sonarComponents.setSensorContext(context);

        List<Class<? extends RustCheck>> checks = ImmutableList.<Class<? extends RustCheck>>builder()
                .addAll(CheckList.getRustChecks())

                .build();
        sonarComponents.registerCheckClasses(CheckList.REPOSITORY_KEY, checks);


        Measurer measurer = new Measurer(context, noSonarFilter);

        RustScanner scanner = new RustScanner( sonarComponents, measurer);
        scanner.scan(getSourceFiles());
        sonarComponents.saveAnalysisErrors();
    }

    private Iterable<InputFile> getSourceFiles() {
        return javaFiles(InputFile.Type.MAIN);
    }



    private Iterable<InputFile> javaFiles(InputFile.Type type) {
        return fs.inputFiles(fs.predicates().and(fs.predicates().hasLanguage(RustLanguage.KEY), fs.predicates().hasType(type)));
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

}