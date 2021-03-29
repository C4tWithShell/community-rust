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
                .matches("callme()\n" +
                        ".now()")
                .matches("idf\n" +
                        ".fun()")
                .matches("d::mycall(a.clone(), b.clone()).unwrap()")
                .matches("node_fetch::create_http_client(user_agent.clone(), my_data.clone()).unwrap()")
                .matches("node_fetch::create_http_client(user_agent.clone(), my_data.clone())\n" +
                        "        .unwrap()")
                .matches("couple[0].to_lowercase()")
                .matches("self.0.iter()")
                .matches("(state.borrow().get_error_class_fn)(&error).as_bytes()")


                ;

    }
}
