package org.sonar.rust.ast.parser.grammar.lexical;

import org.junit.Test;
import org.sonar.rust.ast.parser.RustLexer;
import org.sonar.rust.ast.parser.grammar.GrammarTest;
import org.sonar.sslr.tests.Assertions;

public class LitteralsTest extends GrammarTest {

    @Test
    public void testUnicode(){
        Assertions.assertThat(g.rule(RustLexer.UNICODE_ESCAPE))
                .matches("\\u{0027}");
    }
    @Test
    public void testQuote(){
        Assertions.assertThat(g.rule(RustLexer.QUOTE_ESCAPE))
                .matches("\\'")
                .matches("\\\"")
        ;
    }

    @Test
    public void testAscii(){
        Assertions.assertThat(g.rule(RustLexer.ASCII_ESCAPE))
                .matches("\\x7f")
                .matches("\\r")
                .matches("\\t")
                .matches("\\")

                ;
    }


    @Test
    public void testChars(){
        Assertions.assertThat(g.rule(RustLexer.CHAR_LITERAL))
                .matches("'a'")
                .matches("'5'")
                .matches("'\\u{0027}'")
                ;
    }

    @Test
    public void testStrings(){
        Assertions.assertThat(g.rule(RustLexer.STRING_LITERAL))
                .matches("\"a\"")
                .matches("\"5\"")
            .matches("\"some text\"")
                .matches("\"some text with \\\" quote escape \"")
                .matches("\"\\'\"")
                .matches("\"\\\"\"")
                .matches("\"\\x7f\"")
                .matches("\"\\r\"")
                .matches("\"\\t\"")
                .matches("\"\\\"")

        ;
    }

}
