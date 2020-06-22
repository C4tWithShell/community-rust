package org.sonar.rust.ast.api;

import org.junit.Test;
import org.sonar.rust.api.RustPunctuator;

import static org.assertj.core.api.Assertions.assertThat;

public class RustPunctuatorTest {
    @Test
    public void test() {
        assertThat(RustPunctuator.values()).hasSize(45);

        for (RustPunctuator punctuator : RustPunctuator.values()) {
            assertThat(punctuator.getName()).isEqualTo(punctuator.name());
            assertThat(punctuator.getValue()).isNotNull();
        }
    }
}
