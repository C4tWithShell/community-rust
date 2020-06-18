package org.sonar.rust.ast.api;

import org.junit.Test;
import org.sonar.rust.api.RustTokenType;

import static org.assertj.core.api.Assertions.assertThat;

public class RustTokenTypeTest {
    @Test
    public void test() {
        assertThat(RustTokenType.values()).hasSize(7);

        for (RustTokenType tokenType : RustTokenType.values()) {
            assertThat(tokenType.getName()).isEqualTo(tokenType.name());
            assertThat(tokenType.getValue()).isNotNull();
        }
    }
}
