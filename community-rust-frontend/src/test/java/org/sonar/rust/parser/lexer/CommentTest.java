/**
 * Community Rust Plugin
 * Copyright (C) 2021-2024 Vladimir Shelkovnikov
 * https://github.com/C4tWithShell/community-rust
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

import com.google.common.base.Charsets;
import com.sonar.sslr.api.Token;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.sonar.rust.RustGrammar;
import org.sonar.rust.RustParser;
import org.sonar.rust.RustParserConfiguration;
import org.sonar.sslr.tests.Assertions;


import static com.sonar.sslr.test.lexer.LexerMatchers.hasComment;
import static org.junit.Assert.assertThat;

class CommentTest {
  @Test
  void reallife() {
    assertThat(lex("/*my comment*/"), hasComment("/*my comment*/"));
    assertThat(lex("/*foo \n bar*/"), hasComment("/*foo \n bar*/"));
  }

  private List<Token> lex(String source) {
    return RustParser.create(RustParserConfiguration.builder()
        .setCharset(Charsets.UTF_8)
        .build())
      .parse(source)
      .getTokens();
  }

  @Test
  void lineComment() {
    Assertions.assertThat(RustGrammar.create().build().rule(RustGrammar.LINE_COMMENT))
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
  void testInnerLineDoc() {
    Assertions.assertThat(RustGrammar.create().build().rule(RustGrammar.INNER_LINE_DOC))
      .matches("//!")
      .matches("//! - some documentation")
    ;
  }

  @Test
  void testOuterLineDoc() {
    Assertions.assertThat(RustGrammar.create().build().rule(RustGrammar.OUTER_LINE_DOC))
      .matches("///")
      .matches("///- some documentation")
      .notMatches("////")
    ;
  }

  @Test
  void testInnerBlockDoc() {
    Assertions.assertThat(RustGrammar.create().build().rule(RustGrammar.INNER_BLOCK_DOC))
      .matches("/*!  - Inner block doc */")
    ;
  }

  @Test
  void testOuterBlockDoc() {
    Assertions.assertThat(RustGrammar.create().build().rule(RustGrammar.OUTER_BLOCK_DOC))
      .matches("/**  - Outer block doc (exactly) 2 asterisks */")
      .notMatches("/*** 3 asteriks */")
      .notMatches("/* 1 asteriks */")
    ;
  }

  @Test
  void testBlockComment() {
    Assertions.assertThat(RustGrammar.create().build().rule(RustGrammar.BLOCK_COMMENT))
      .matches("/**/")
      .matches("/***/")
      .matches("/*  Only a comment */")
      .matches("/*** comment */")
    ;
  }

  @Test
  void testBlockCommentOrDoc() {
    Assertions.assertThat(RustGrammar.create().build().rule(RustGrammar.BLOCK_COMMENT_OR_DOC))
      .matches("/**/")
      .matches("/***/")
      .matches("/*** comment */")
      .matches("/**  - Outer block doc (exactly) 2 asterisks */")
      .matches("/*!  - Inner block doc */")
    ;
  }

}
