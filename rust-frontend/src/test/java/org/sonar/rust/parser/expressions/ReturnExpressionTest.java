package org.sonar.rust.parser.expressions;

import org.junit.Test;
import org.sonar.rust.RustGrammar;

import static org.sonar.sslr.tests.Assertions.assertThat;

public class ReturnExpressionTest {
    @Test
    public void testReturnExpression() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.RETURN_EXPRESSION))
                .matches("return")
                .matches("return result")
                .matches("return calc()")

        ;
    }
}
