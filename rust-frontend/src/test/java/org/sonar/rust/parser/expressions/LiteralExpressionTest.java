package org.sonar.rust.parser.expressions;

import org.junit.Test;
import org.sonar.rust.RustGrammar;

import static org.sonar.sslr.tests.Assertions.assertThat;

public class LiteralExpressionTest {

    @Test
    public void testLiteralExpression() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.LITERAL_EXPRESSION))
                .matches("'foo'")
                .matches("'foo''bar'")
                .matches("\"b\"")
                .matches("\"52\"")
                .matches("r\"foo\"")
                .matches("b'5'")
                .notMatches("+")
                .notMatches("{")
                .notMatches("\"hello\")")
                .matches("\"hello,world!\"")
                .notMatches("== b")
                .matches("42")
                .notMatches("42..")
                .matches("0.0")
        ;
    }
}
