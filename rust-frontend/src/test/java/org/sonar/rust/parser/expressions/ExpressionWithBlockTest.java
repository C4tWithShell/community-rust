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
                .matches("{node_fetch::create_http_client(user_agent.clone(), my_data.clone()).unwrap()}")
                .matches("match path.parent() {\n" +
                        "             Some(ref parent) => self.ensure_dir_exists(parent),\n" +
                        "             None => Ok(()),\n" +
                        "         }")
        ;
    }
}
