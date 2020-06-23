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
