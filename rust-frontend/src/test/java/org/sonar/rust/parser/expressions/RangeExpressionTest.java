package org.sonar.rust.parser.expressions;

import org.junit.Test;
import org.sonar.rust.RustGrammar;

import static org.sonar.sslr.tests.Assertions.assertThat;

public class RangeExpressionTest {

    @Test
    public void testRangeExpr() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.RANGE_EXPR))
                .matches("1..2")
                .matches("start..end")

        ;
    }

    @Test
    public void testRangeFrom() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.RANGE_FROM_EXPR))
                .matches("1..")

        ;
    }

    @Test
    public void testRangeTo() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.RANGE_TO_EXPR))
                .matches("..42")

        ;
    }

    @Test
    public void testRangeFull() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.RANGE_FULL_EXPR))
                .matches("..")

        ;
    }

    @Test
    public void testRangeInclusive() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.RANGE_INCLUSIVE_EXPR))
                .matches("40..=42")

        ;
    }

    @Test
    public void testRangeToInclusive() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.RANGE_TO_INCLUSIVE_EXPR))
                .matches("..=7")

        ;
    }


    @Test
    public void testRangeExpression() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.RANGE_EXPRESSION))
                .matches("1..2")// std::ops::Range
                .matches("3..")// std::ops::RangeFrom
                .matches("..4")// std::ops::RangeTo
                .matches("..")// std::ops::RangeFull
                .matches("5..=6")// std::ops::RangeInclusive
                .matches("..=7")// std::ops::RangeToInclusive
        ;
    }
}
