/**
 * Community Rust Plugin
 * Copyright (C) 2021-2024 Vladimir Shelkovnikov
 * https://github.com/C4tWithShell/community-rust
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
package org.elegoff.plugins.communityrust.xunit;

import java.io.File;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.sonar.api.batch.fs.InputComponent;
import org.sonar.api.batch.sensor.internal.SensorContextTester;
import org.sonar.api.config.internal.MapSettings;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.Metric;
import org.sonar.api.testfixtures.log.LogTesterJUnit5;


import static org.fest.assertions.Assertions.assertThat;

class XUnitSensorTest {
  private final File moduleBaseDir = new File("src/test/resources/org/elegoff/plugins/communityrust/xunit").getAbsoluteFile();
  @RegisterExtension
  public LogTesterJUnit5 logTester = new LogTesterJUnit5();
  private XUnitSensor unitSensor;
  private SensorContextTester context;
  private MapSettings settings;

  @BeforeEach
  void init() {

    unitSensor = new XUnitSensor();
    settings = new MapSettings();
    settings.setProperty(XUnitSensor.REPORT_PATH_KEY, "basic.xml");
    context = SensorContextTester.create(moduleBaseDir);
    context.setSettings(settings);
  }

  @Test
  void test_basic_report() {
    unitSensor.execute(context);
    assertThat(moduleMeasure(CoreMetrics.TESTS)).isEqualTo(1);
    assertThat(moduleMeasure(CoreMetrics.SKIPPED_TESTS)).isEqualTo(0);
    assertThat(moduleMeasure(CoreMetrics.TEST_ERRORS)).isEqualTo(0);
    assertThat(moduleMeasure(CoreMetrics.TEST_FAILURES)).isEqualTo(0);
  }

  @Test
  void test_report_with_failure() {

    settings.setProperty(XUnitSensor.REPORT_PATH_KEY, "report_with_failure.xml");
    context = SensorContextTester.create(moduleBaseDir);
    context.setSettings(settings);

    unitSensor.execute(context);
    assertThat(moduleMeasure(CoreMetrics.TESTS)).isEqualTo(13);
    assertThat(moduleMeasure(CoreMetrics.SKIPPED_TESTS)).isEqualTo(0);
    assertThat(moduleMeasure(CoreMetrics.TEST_ERRORS)).isEqualTo(0);
    assertThat(moduleMeasure(CoreMetrics.TEST_FAILURES)).isEqualTo(1);
  }

  @Test
  void shouldReportNothingWhenNoReportFound() {
    settings.setProperty(XUnitSensor.REPORT_PATH_KEY, "notexistingpath");
    unitSensor.execute(context);
    assertThat(context.measures(context.module().key())).isEmpty();
  }

  @Test
  void test_report_with_failure_no_message() {

    settings.setProperty(XUnitSensor.REPORT_PATH_KEY, "cargo-nextest.xml");
    context = SensorContextTester.create(moduleBaseDir);
    context.setSettings(settings);

    unitSensor.execute(context);
    assertThat(moduleMeasure(CoreMetrics.TESTS)).isEqualTo(6);
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
