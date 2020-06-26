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

public class CommentTest extends LexerTesting {


    @Test
    public void testLineComment(){
        Assertions.assertThat(g.rule(RustLexer.LINE_COMMENT))
                .matches("//")
                .matches("//comment")
                .matches("// comment")
                .notMatches("//!comment")
                .matches("////")
                .notMatches("///")
                .notMatches("//!")
                .notMatches("//comment\ncomment2")

        ;
    }

    @Test
    public void testInnerLineDoc(){
        Assertions.assertThat(g.rule(RustLexer.INNER_LINE_DOC))
                .matches("//!")
                .matches("//! - some documentation")
                ;
    }

    @Test
    public void testOuterLineDoc(){
        Assertions.assertThat(g.rule(RustLexer.OUTER_LINE_DOC))
                .matches("///")
                .matches("///- some documentation")
                .notMatches("////")
        ;
    }

    @Test
    public void testInnerBlockDoc() {
        Assertions.assertThat(g.rule(RustLexer.INNER_BLOCK_DOC))
                .matches("/*!  - Inner block doc */")
        ;
    }


    @Test
    public void testOuterBlockDoc() {
        Assertions.assertThat(g.rule(RustLexer.OUTER_BLOCK_DOC))
                .matches("/**  - Outer block doc (exactly) 2 asterisks */")
                .notMatches("/*** 3 asteriks */")
                .notMatches("/* 1 asteriks */")
        ;
    }

    @Test
    public void testBlockComment(){
        Assertions.assertThat(g.rule(RustLexer.BLOCK_COMMENT))
                .matches("/**/")
                .matches("/***/")
                .matches("/*** comment */")
                ;
    }

    @Test
    public void testBlockCommentOrDoc(){
        Assertions.assertThat(g.rule(RustLexer.BLOCK_COMMENT_OR_DOC))
                .matches("/**/")
                .matches("/***/")
                .matches("/*** comment */")
                .matches("/**  - Outer block doc (exactly) 2 asterisks */")
                .matches("/*!  - Inner block doc */")
        ;
    }


}
