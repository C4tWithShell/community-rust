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
package org.sonar.rust.ast.parser.grammar.lexical;

import org.junit.Test;
import org.sonar.rust.ast.parser.RustLexer;
import org.sonar.rust.ast.parser.grammar.LexerTesting;
import org.sonar.sslr.tests.Assertions;

public class PathTest extends LexerTesting {

    @Test
    public void testSimplePathSegment() {
        Assertions.assertThat(g.rule(RustLexer.SIMPLE_PATH_SEGMENT))
                .matches("super")
                .matches("self")
                .matches("crate")
                .matches("$crate")
                .matches("abc")
                .matches("r#a")
                .matches("U213")
        ;
    }

    @Test
    public void testSimplePath() {
        Assertions.assertThat(g.rule(RustLexer.SIMPLE_PATH))
                .matches("std::io::Write")
                .matches("std::io::super")
        ;
    }

    @Test
    public void testPathInExpression() {
        Assertions.assertThat(g.rule(RustLexer.PATH_IN_EXPRESSION))
                .matches("Vec::<u8>::with_capacity")
                .matches("collect::<Vec<_>>")
        ;
    }

    @Test
    public void testQualifiedPathInExpression() {
        Assertions.assertThat(g.rule(RustLexer.QUALIFIED_PATH_IN_EXPRESSION))
                .matches("<S as T1>::f")

        ;
    }


    @Test
    public void testPathIdentSegment() {
        Assertions.assertThat(g.rule(RustLexer.PATH_IDENT_SEGMENT))
                .matches("super")
                .matches("self")
                .matches("crate")
                .matches("$crate")
                .matches("abc")
                .matches("r#a")
                .matches("U213")
        ;


    }

    @Test
    public void testTypePathFnInputs() {
        Assertions.assertThat(g.rule(RustLexer.TYPE_PATH_FN_INPUTS))
                .matches("isize")

        ;
    }

    @Test
    public void testTypePathFn() {
        Assertions.assertThat(g.rule(RustLexer.TYPE_PATH_FN))
                .matches("(isize) -> isize")

        ;
    }

    @Test
    public void testTypePathSegment() {
        Assertions.assertThat(g.rule(RustLexer.TYPE_PATH_SEGMENT))
                .matches("super")
                .matches("abc")
                .matches("r#a")
                .matches("U213")
                .matches("abc::(isize) -> isize")
                .matches("abc::<>")
        ;
    }

    @Test
    public void testTypePath() {
        Assertions.assertThat(g.rule(RustLexer.TYPE_PATH))
                .matches("std::boxed::Box<dyn std::ops::FnOnce(isize) -> isize>")
                .matches("abc::abc")

        ;
    }


}
