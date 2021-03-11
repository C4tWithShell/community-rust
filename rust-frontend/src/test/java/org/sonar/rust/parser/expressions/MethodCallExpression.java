package org.sonar.rust.parser.expressions;

import org.junit.Test;
import org.sonar.rust.RustGrammar;

import static org.sonar.sslr.tests.Assertions.assertThat;

public class MethodCallExpression {

    @Test
    public void testMethodCallExpression() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.METHOD_CALL_EXPRESSION))
                .matches("\"Some string\".to_string()")
                .matches("\"123\".parse()")
                .matches("a.b()")
                .matches("a.b().c()")
                .matches("pi.unwrap_or(1.0).log(2.72)")
                ;

    }
}
