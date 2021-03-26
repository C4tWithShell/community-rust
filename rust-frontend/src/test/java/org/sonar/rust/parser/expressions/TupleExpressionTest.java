package org.sonar.rust.parser.expressions;

import org.junit.Test;
import org.sonar.rust.RustGrammar;

import static org.sonar.sslr.tests.Assertions.assertThat;

public class TupleExpressionTest {



    @Test
    public void testTupleElement() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.TUPLE_ELEMENT))
                .matches("42,")
                .matches("0.0,")


        ;
    }

    @Test
    public void testTupleIndexingExpression() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.TUPLE_INDEXING_EXPRESSION))
                .matches("point.1")
                .matches("self.0")
        ;
    }

    @Test
    public void testTupleExpression() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.TUPLE_EXPRESSION))
                .matches("(0.0,4.5)")
                .matches("(0.0, 4.5)")
                .matches("(\"a\", 4usize, true)")
                .matches("()")

        ;
    }
}
