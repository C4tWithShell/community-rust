package org.sonar.rust.parser.lexer;

import com.google.common.base.Charsets;
import com.sonar.sslr.api.Token;
import org.junit.Test;
import org.sonar.rust.RustGrammar;
import org.sonar.rust.RustParser;
import org.sonar.rust.RustParserConfiguration;
import org.sonar.sslr.tests.Assertions;

import java.util.List;

import static com.sonar.sslr.test.lexer.LexerMatchers.hasComment;
import static org.junit.Assert.assertThat;

public class CommentTest {
    @Test
    public void reallife() {
        //FIXME assertThat(lex("/*foo*/"), hasComment("/*foo*/"));
        //FIXME assertThat(lex("/*foo \n bar*/"), hasComment("/*foo \n bar*/"));
    }

    private List<Token> lex(String source) {
        return RustParser.create(RustParserConfiguration.builder()
                .setCharset(Charsets.UTF_8)
                .build())
                .parse(source)
                .getTokens();
    }

    @Test
    public void lineComment() {
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
    public void testInnerLineDoc() {
        Assertions.assertThat(RustGrammar.create().build().rule(RustGrammar.INNER_LINE_DOC))
                .matches("//!")
                .matches("//! - some documentation")
        ;
    }

    @Test
    public void testOuterLineDoc() {
        Assertions.assertThat(RustGrammar.create().build().rule(RustGrammar.OUTER_LINE_DOC))
                .matches("///")
                .matches("///- some documentation")
                .notMatches("////")
        ;
    }

    @Test
    public void testInnerBlockDoc() {
        Assertions.assertThat(RustGrammar.create().build().rule(RustGrammar.INNER_BLOCK_DOC))
                .matches("/*!  - Inner block doc */")
        ;
    }


    @Test
    public void testOuterBlockDoc() {
        Assertions.assertThat(RustGrammar.create().build().rule(RustGrammar.OUTER_BLOCK_DOC))
                .matches("/**  - Outer block doc (exactly) 2 asterisks */")
                .notMatches("/*** 3 asteriks */")
                .notMatches("/* 1 asteriks */")
        ;
    }

    @Test
    public void testBlockComment() {
        Assertions.assertThat(RustGrammar.create().build().rule(RustGrammar.BLOCK_COMMENT))
                .matches("/**/")
                .matches("/***/")
                .matches("/*  Only a comment */")
                .matches("/*** comment */")
        ;
    }

    @Test
    public void testBlockCommentOrDoc() {
        Assertions.assertThat(RustGrammar.create().build().rule(RustGrammar.BLOCK_COMMENT_OR_DOC))
                .matches("/**/")
                .matches("/***/")
                .matches("/*** comment */")
                .matches("/**  - Outer block doc (exactly) 2 asterisks */")
                .matches("/*!  - Inner block doc */")
        ;
    }

}
