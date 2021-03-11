package org.sonar.rust.parser.expressions;

import org.junit.Test;
import org.sonar.rust.RustGrammar;

import static org.sonar.sslr.tests.Assertions.assertThat;

public class EnumExpressionTest {


    @Test
    public void testEnumExprField() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.ENUM_EXPR_FIELD))
                .matches("x:50")
        ;
    }

    @Test
    public void testEnumExprFields() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.ENUM_EXPR_FIELDS))
                .matches("x:50, y:200")
        ;
    }

    @Test
    public void testEnumExprStruct() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.ENUM_EXPR_STRUCT))
                .matches("Message::Move{x:50,y:200}")


        ;
    }

    @Test
    public void testEnumExprTuple() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.ENUM_EXPR_TUPLE))

                .matches("Message::WriteString(\"Some string\".to_string())")


        ;
    }

    @Test
    public void testEnumExprFieldLess() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.ENUM_EXPR_FIELDLESS))
                .matches("Message::Quit")


        ;
    }


    @Test
    public void testEnumVariantExpression() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.ENUMERATION_VARIANT_EXPRESSION))
                .matches("Message::Quit")
                .matches("Message::Move { x: 50, y: 200 }")
                .matches("Message::WriteString(\"Some string\".to_string())")


        ;
    }
}
