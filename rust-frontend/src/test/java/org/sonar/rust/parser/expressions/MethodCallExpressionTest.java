package org.sonar.rust.parser.expressions;

import org.junit.Test;
import org.sonar.rust.RustGrammar;

import static org.sonar.sslr.tests.Assertions.assertThat;

public class MethodCallExpressionTest {

    @Test
    public void testMethodCallExpression() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.METHOD_CALL_EXPRESSION))
                .matches("\"Some string\".to_string()")
                .matches("\"3.14\".parse()")
                .matches("a.b()")
                .matches("a.b().c()")
                .matches("pi.unwrap_or(1.0).log(2.72)")
                .matches("j.set(i.get())")
                .matches("j.set(1)")
                .matches("Some::<i32>.calc()")
                .matches("a.foo()")
                .matches("b.abc()")
                .matches("obj.add(1i32,2i32)")
                .matches("callme().now()")
                .matches("d::mycall(a.clone(), b.clone()).unwrap()")
                .matches("deno_fetch::create_http_client(user_agent.clone(), ca_data.clone()).unwrap()")


                ;

    }
}
