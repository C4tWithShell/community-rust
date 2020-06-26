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
package org.sonar.rust;

import com.google.common.annotations.VisibleForTesting;
import com.sonar.sslr.api.RecognitionException;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.rule.CheckFactory;
import org.sonar.api.batch.rule.Checks;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.measures.FileLinesContextFactory;
import org.sonar.plugins.rust.api.RustCheck;

import java.util.ArrayList;
import java.util.List;

public class SonarComponents {
    private final FileLinesContextFactory fileLinesContextFactory;

    private final CheckFactory checkFactory;

    private final FileSystem fs;

    private final List<Checks<RustCheck>> checks;
    private static final int ERROR_SERIALIZATION_LIMIT = 100_000;
    @VisibleForTesting
    public List<AnalysisError> analysisErrors;
    private SensorContext context;
    private int errorsSize = 0;

    public SonarComponents(FileLinesContextFactory fileLinesContextFactory, FileSystem fs,
             CheckFactory checkFactory) {
        this.fileLinesContextFactory = fileLinesContextFactory;
        this.fs = fs;

        this.checkFactory = checkFactory;

        this.checks = new ArrayList<>();

        this.analysisErrors = new ArrayList<>();

    }

    public void setSensorContext(SensorContext context) {
        //TODO
    }

    public void registerCheckClasses(String repositoryKey, List<Class<? extends RustCheck>> checks) {
        //TODO
    }

    public void saveAnalysisErrors() {
        //TODO
    }

    public RustCheck[] checkClasses() {
        return checks.stream().flatMap(ce -> ce.all().stream()).toArray(RustCheck[]::new);
    }

    public boolean analysisCancelled() {
        //TODO
        return false;
    }

    public boolean shouldFailAnalysisOnException() {
        //TODO
        return false;
    }

    public void addAnalysisError(AnalysisError analysisError) {
        if (errorsSize < ERROR_SERIALIZATION_LIMIT) {
            errorsSize += analysisError.serializedSize();
            analysisErrors.add(analysisError);
        }
    }

    public Object reportAnalysisError(RecognitionException any, InputFile any1) {
        //TODO
        return null;
    }
}
