package org.sonar.rust;

import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.issue.NoSonarFilter;

public class Measurer extends SubscriptionVisitor{

    private final SensorContext sensorContext;
    private final NoSonarFilter noSonarFilter;

    public Measurer(SensorContext context, NoSonarFilter noSonarFilter) {
        this.sensorContext = context;
        this.noSonarFilter = noSonarFilter;
    }
}
