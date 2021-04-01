package org.sonar.rust.parser.expressions;

import org.junit.Test;
import org.sonar.rust.RustGrammar;

import static org.sonar.sslr.tests.Assertions.assertThat;

public class GroupedExpressionTest {

    @Test
    public void testGroupedExpression() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.GROUPED_EXPRESSION))
                .matches("(1+1)")
                .matches("( 1 + 1 )")
                .matches("(foo)")
                .matches("(1 +(2+3))")
                .matches("( #![crate_type = \"lib\"] 40+2 )")
                .matches("(state.get_error_class_fn)")
                .matches("(state.borrow().get_error_class_fn)")
                .matches("(disk_byte as char)")
                .matches("(js_error.start_column.unwrap() - sc)")

        ;
    }
}
