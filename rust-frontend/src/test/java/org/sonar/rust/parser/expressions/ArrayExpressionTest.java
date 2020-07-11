package org.sonar.rust.parser.expressions;

import org.junit.Test;
import org.sonar.rust.RustGrammar;

import static org.sonar.sslr.tests.Assertions.assertThat;

public class ArrayExpressionTest {
    @Test
    public void testArrayExpression() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.ARRAY_EXPRESSION))
                .matches("[]")
                .matches("[42]")
                .matches("[42,43]")
                .matches("[ 42 , 43 ]")
                .matches("[\"forty_two\"]")
                .matches("[\"s1\",\"s2\",\"s3\"]")
                .matches("[BIT1, BIT2]")
        ;
    }
}
