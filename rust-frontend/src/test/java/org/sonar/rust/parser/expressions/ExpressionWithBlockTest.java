package org.sonar.rust.parser.expressions;

import org.junit.Test;
import org.sonar.rust.RustGrammar;

import static org.sonar.sslr.tests.Assertions.assertThat;

public class ExpressionWithBlockTest {

    @Test
    public void testExpressionWithBlock() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.EXPRESSION_WITH_BLOCK))
                .matches("{}")
                .notMatches("== b")
                .matches("{deno_fetch::create_http_client(user_agent.clone(), ca_data.clone())}")
                .matches("{deno_fetch::create_http_client(user_agent.clone(), ca_data.clone()).unwrap()}")


        ;
    }
}
