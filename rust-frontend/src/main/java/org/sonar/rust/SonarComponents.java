package org.sonar.rust;

import com.google.common.annotations.VisibleForTesting;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.rule.CheckFactory;
import org.sonar.api.batch.rule.Checks;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.error.AnalysisError;
import org.sonar.api.measures.FileLinesContextFactory;
import org.sonar.plugins.rust.api.RustCheck;

import java.util.ArrayList;
import java.util.List;

public class SonarComponents {
    private final FileLinesContextFactory fileLinesContextFactory;

    private final CheckFactory checkFactory;

    private final FileSystem fs;

    private final List<Checks<RustCheck>> checks;
    @VisibleForTesting
    public List<AnalysisError> analysisErrors;
    private SensorContext context;
    private final int errorsSize = 0;

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
}
