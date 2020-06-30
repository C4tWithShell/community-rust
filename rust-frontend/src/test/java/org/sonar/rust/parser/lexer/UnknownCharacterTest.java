package org.sonar.rust.parser.lexer;

import org.junit.Test;
import org.sonar.rust.RustGrammar;

import static org.sonar.sslr.tests.Assertions.assertThat;

public class UnknownCharacterTest {

    @Test
    public void reallife() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.UNKNOWN_CHAR))
                .matches("?");
    }
}
