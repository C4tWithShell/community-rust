package org.sonar.rust.parser.expressions;

import org.junit.Test;
import org.sonar.rust.RustGrammar;

import static org.sonar.sslr.tests.Assertions.assertThat;

public class OperatorExpression {
    @Test
    public void testOperatorExpression() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.OPERATOR_EXPRESSION))
                .matches("1<<0")
                .matches("1+1")
                .matches("1+1+1+1")
                .matches("3*2")
                .matches("3*2*8")
                .matches("22/7")
                .matches("2^4")
                .matches("1-3")
                .matches("1-3-2")
                .matches("22%7")
        ;
    }
}
