package org.sonar.rust.parser.expressions;

import org.junit.Test;
import org.sonar.rust.RustGrammar;

import static org.sonar.sslr.tests.Assertions.assertThat;

public class AwaitExpressionTest {

    @Test
    public void testAwaitExpression() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.AWAIT_EXPRESSION))
                .matches("a.await")
                .matches("m().await")
                .matches("check_source_files(config, paths).await")

        ;
    }
}
