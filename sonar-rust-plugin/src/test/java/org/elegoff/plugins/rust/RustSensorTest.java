package org.elegoff.plugins.rust;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.sonar.api.batch.rule.CheckFactory;
import org.sonar.api.batch.rule.Checks;
import org.sonar.api.rule.RuleKey;
import org.sonar.api.rules.RuleAnnotationUtils;
import org.sonar.plugins.rust.api.RustCheck;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RustSensorTest {
    private static final CheckFactory checkFactory = mock(CheckFactory.class);
    private static final Checks<Object> checks = mock(Checks.class);

    static {
        when(checks.addAnnotatedChecks(any(Iterable.class))).thenReturn(checks);
        when(checkFactory.create(anyString())).thenReturn(checks);
    }

    @Rule
    public final TemporaryFolder tmp = new TemporaryFolder();

    @Test
    public void test_toString() {
        assertThat(new RustSensor(null, null, null, null).toString()).isEqualTo("RustSensor");
    }
}
