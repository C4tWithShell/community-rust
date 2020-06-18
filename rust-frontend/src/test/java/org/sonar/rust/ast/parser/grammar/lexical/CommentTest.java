package org.sonar.rust.ast.parser.grammar.lexical;

import org.junit.Test;
import org.sonar.rust.ast.parser.RustLexer;
import org.sonar.sslr.parser.LexerlessGrammar;
import org.sonar.sslr.tests.Assertions;

public class CommentTest {
    private final LexerlessGrammar g = RustLexer.createGrammarBuilder().build();

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
   
}
