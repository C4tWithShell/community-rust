package org.sonar.rust.ast.api;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.sonar.rust.api.RustKeyword;

import static org.assertj.core.api.Assertions.assertThat;

public class RustKeywordTest {
    @Test
    public void test() {
        Assertions.assertThat(RustKeyword.values()).hasSize(50);
        assertThat(RustKeyword.keywordValues()).hasSize(RustKeyword.values().length);

        for (RustKeyword keyword : RustKeyword.values()) {
            assertThat(keyword.getName()).isEqualTo(keyword.name());
            assertThat(keyword.getValue()).isNotNull();
        }
    }
}
