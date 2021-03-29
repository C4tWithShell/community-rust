package org.sonar.rust.parser.expressions;

import org.junit.Test;
import org.sonar.rust.RustGrammar;

import static org.sonar.sslr.tests.Assertions.assertThat;

public class FieldExpressionTest {
    @Test
    public void testFieldExpression() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.FIELD_EXPRESSION))
                .matches("mystruct.myfield")
                .matches("other.major")
                .matches("foo().x")
                //FIXME.matches("(Struct {a: 10, b: 20}).a")
                .matches("t.get_error_class")
                .matches("state.borrow().get_error_class_fn")





                ;
    }
}