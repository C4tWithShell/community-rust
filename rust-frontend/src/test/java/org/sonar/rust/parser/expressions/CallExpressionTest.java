package org.sonar.rust.parser.expressions;

import org.junit.Test;
import org.sonar.rust.RustGrammar;

import static org.sonar.sslr.tests.Assertions.assertThat;

public class CallExpressionTest {

    @Test
    public void testCallExpression() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.CALL_EXPRESSION))
                //FIXME.matches("foo()")


        ;
    }
}
