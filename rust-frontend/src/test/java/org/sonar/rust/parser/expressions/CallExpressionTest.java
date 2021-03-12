/**
 *
 * Sonar Rust Plugin (Community)
 * Copyright (C) 2020 Eric Le Goff
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

public class CallExpressionTest {

    @Test
    public void testCallExpressionTerm() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.CALL_PARAMS))
                //.matches("")
                .matches("1i32")
                .matches("{let y=42;}")
                .matches("{;}")
                .matches("0")
                .matches("3+2")
                .matches("1 << 1")
                .matches("foo")



        ;
    }

    @Test
    public void testCallExpression() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.CALL_EXPRESSION))
                .matches("foo()")
                .matches("abc()")
                .matches("add(1i32,2i32)")
                .matches("add(1i32, 2i32)")




        ;
    }

    @Test
    public void testMethodCallExpression() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.METHOD_CALL_EXPRESSION))
                .matches("a.foo()")
                .matches("b.abc()")
                .matches("obj.add(1i32,2i32)")
                .matches("\"3.14\".parse()")


        ;
    }
}
