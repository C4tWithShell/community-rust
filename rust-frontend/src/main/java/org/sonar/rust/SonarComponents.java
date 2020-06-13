package org.sonar.rust;

import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.plugins.rust.api.RustCheck;

import java.util.List;

public class SonarComponents {
    public void setSensorContext(SensorContext context) {
    }

    public void registerCheckClasses(String repositoryKey, List<Class<? extends RustCheck>> checks) {
    }

    public void saveAnalysisErrors() {
    }
}
