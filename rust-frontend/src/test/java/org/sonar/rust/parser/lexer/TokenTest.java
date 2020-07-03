package org.sonar.rust.parser.lexer;

import org.junit.Test;
import org.sonar.rust.RustGrammar;

import static org.sonar.sslr.tests.Assertions.assertThat;

public class TokenTest {

    @Test
    public void test() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.TOKEN))
                .matches("a") //identifiers
                .matches("abc")
                .matches("A")
                .matches("AbCD")
                .matches("U123")
                .matches("as") //keywords
                .matches("break")
                .matches("const")
                .matches("continue")
                ;
    }
}
