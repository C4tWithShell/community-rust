package org.sonar.rust.parser.expressions;

import org.junit.Test;
import org.sonar.rust.RustGrammar;

import static org.sonar.sslr.tests.Assertions.assertThat;

public class ExpressionTest {

    @Test
    public void testExpression() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.EXPRESSION))
                .matches("{let y=42;}")
                .matches("{;}")
                .matches("0")
                .matches("3+2")
                .matches("1 << 1")
                .matches("foo")

        ;
    }
}
