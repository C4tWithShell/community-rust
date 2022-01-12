package org.elegoff.plugins.communityrust.xunit;


import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.sonar.api.batch.fs.InputComponent;
import org.sonar.api.batch.sensor.internal.SensorContextTester;
import org.sonar.api.config.internal.MapSettings;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.Metric;
import org.sonar.api.utils.log.LogTester;

import java.io.File;

import static org.fest.assertions.Assertions.assertThat;

public class XUnitSensorTest {
    private XUnitSensor unitSensor;
    private SensorContextTester context;
    private MapSettings settings;

    private File moduleBaseDir = new File("src/test/resources/org/elegoff/plugins/communityrust/xunit").getAbsoluteFile();

    @Rule
    public LogTester logTester = new LogTester();

    @Rule
    public TemporaryFolder tmpDir = new TemporaryFolder();

    @Before
    public void init() {

        unitSensor = new XUnitSensor();
        settings = new MapSettings();
        settings.setProperty(XUnitSensor.REPORT_PATH_KEY, "basic.xml");
        context = SensorContextTester.create(moduleBaseDir);
        context.setSettings(settings);
    }

    @Test
    public void test_basic_report() {
        unitSensor.execute(context);
        assertThat(moduleMeasure(CoreMetrics.TESTS)).isEqualTo(1);
        assertThat(moduleMeasure(CoreMetrics.SKIPPED_TESTS)).isEqualTo(0);
        assertThat(moduleMeasure(CoreMetrics.TEST_ERRORS)).isEqualTo(0);
        assertThat(moduleMeasure(CoreMetrics.TEST_FAILURES)).isEqualTo(0);
    }

    @Test
    public void test_report_with_failure() {

        settings.setProperty(XUnitSensor.REPORT_PATH_KEY, "report_with_failure.xml");
        context = SensorContextTester.create(moduleBaseDir);
        context.setSettings(settings);

        unitSensor.execute(context);
        assertThat(moduleMeasure(CoreMetrics.TESTS)).isEqualTo(13);
        assertThat(moduleMeasure(CoreMetrics.SKIPPED_TESTS)).isEqualTo(0);
        assertThat(moduleMeasure(CoreMetrics.TEST_ERRORS)).isEqualTo(0);
        assertThat(moduleMeasure(CoreMetrics.TEST_FAILURES)).isEqualTo(1);
    }

    private Integer moduleMeasure(Metric<Integer> metric) {
        return measure(context.module(), metric);
    }

    private Integer measure(InputComponent component, Metric<Integer> metric) {
        return context.measure(component.key(), metric).value();
    }


}
