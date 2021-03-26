package org.sonar.rust.parser.expressions;

import org.junit.Test;
import org.sonar.rust.RustGrammar;

import static org.sonar.sslr.tests.Assertions.assertThat;

public class ClosureExpressionTest {

    @Test
    public void testClosureExpression() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.CLOSURE_EXPRESSION))
                .matches("|k:i32|->(){println!(\"hello,{}\",k)}")
                .matches("|j: i32| -> () { println!(\"hello, {}\", j); }")
                .matches("move |j| println!(\"{}, {}\", word, j)")
        ;
    }
}
