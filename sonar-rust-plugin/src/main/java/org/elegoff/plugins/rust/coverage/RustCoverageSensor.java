package org.elegoff.plugins.rust.coverage;

import org.sonar.api.batch.sensor.Sensor;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.SensorDescriptor;

public class RustCoverageSensor  implements Sensor {
    public static final String REPORT_PATHS_KEY = "sonar.rust.tarpaulin.reportPaths";
    public static final String DEFAULT_REPORT_PATH = "cobertura.xml";

    @Override
    public void describe(SensorDescriptor sensorDescriptor) {

    }

    @Override
    public void execute(SensorContext sensorContext) {

    }
}
