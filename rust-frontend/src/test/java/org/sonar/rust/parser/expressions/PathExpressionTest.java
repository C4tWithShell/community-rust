package org.sonar.rust.parser.expressions;

import org.junit.Test;
import org.sonar.rust.RustGrammar;

import static org.sonar.sslr.tests.Assertions.assertThat;

public class PathExpressionTest {

    @Test
    public void testPathExpression() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.PATH_EXPRESSION))
                .matches("local_var")
                .matches("globals::STATIC_VAR")
                .matches("Some::<i32>")
                .matches("Vec::<i32>::push")
                .matches("<[i32]>::reverse")
                .matches("Identifier::Numeric")
                .matches("Vec::new")
                .matches("StepPosition::JumpEnd")

        ;
    }
}
