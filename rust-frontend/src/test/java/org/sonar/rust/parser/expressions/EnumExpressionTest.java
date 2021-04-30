/**
 * Sonar Rust Plugin (Community)
 * Copyright (C) 2021 Eric Le Goff
 * http://github.com/elegoff/sonar-rust
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
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