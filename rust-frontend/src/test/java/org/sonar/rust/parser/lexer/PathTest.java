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
package org.sonar.rust.parser.lexer;

import org.junit.Test;
import org.sonar.rust.RustGrammar;

import static org.sonar.sslr.tests.Assertions.assertThat;

public class PathTest {
    @Test
    public void testSimplePathSegment() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.SIMPLE_PATH_SEGMENT))
                .matches("super")
                .matches("self")
                .matches("crate")
                .matches("$crate")
                .matches("abc")
                .matches("r#a")
                .matches("U213")
                .matches("crate_type")
        ;
    }

    @Test
    public void testSimplePath() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.SIMPLE_PATH))
                .matches("std::io::Write")
                .matches("std::io::super")
                .matches("Write")
                .matches("crate_type")
        ;
    }

    @Test
    public void testPathInExpression() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.PATH_IN_EXPRESSION))
                .matches("Vec::<u8>::with_capacity")
                .matches("collect::<Vec<_>>")
        ;
    }

    @Test
    public void testQualifiedPathType() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.QUALIFIED_PATH_TYPE))
                .matches("<T1>")
                .matches("<T1 as T>")

        ;
    }

    @Test
    public void testQualifiedPathInExpression() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.QUALIFIED_PATH_IN_EXPRESSION))
                .matches("<S as T1>::f")

        ;
    }


    @Test
    public void testPathIdentSegment() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.PATH_IDENT_SEGMENT))
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
        assertThat(RustGrammar.create().build().rule(RustGrammar.TYPE_PATH_FN_INPUTS))
                .matches("isize")

        ;
    }

    @Test
    public void testTypePathFn() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.TYPE_PATH_FN))
                .matches("(isize) -> isize")

        ;
    }

    @Test
    public void testTypePathSegment() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.TYPE_PATH_SEGMENT))
                .matches("super")
                .matches("abc")
                .matches("r#a")
                .matches("U213")
                .matches("abc::(isize) -> isize")
                .matches("abc::<>")
                .matches("abc::")
                .matches("abc::(isize) -> isize")
                .notMatches("abc::abc for")
        ;
    }

    @Test
    public void testTypePath() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.TYPE_PATH))
                .matches("abc::(isize) -> isize")
                .matches("abc::")
                .notMatches("abc::abc for")
                .matches("T")

        ;
    }
}
