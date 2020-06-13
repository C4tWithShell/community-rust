package org.sonar.rust.api;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class RustKeywordTest {
    @Test
    public void test() {
        assertThat(RustKeyword.values()).hasSize(49);
        assertThat(RustKeyword.keywordValues()).hasSize(RustKeyword.values().length);
    }
}
