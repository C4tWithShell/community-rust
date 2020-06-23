package org.sonar.rust.ast.parser.grammar.lexical;

import org.junit.Test;
import org.sonar.rust.ast.parser.RustLexer;
import org.sonar.rust.ast.parser.grammar.LexerTesting;

import org.sonar.sslr.tests.Assertions;


public class IdentifierTest extends LexerTesting {



    @Test
    public void checkIDENTIFIER_OR_KEYWORD() {
        Assertions.assertThat(g.rule(RustLexer.IDENTIFIER_OR_KEYWORD))
                .matches("a")
                .matches("abc")
                .matches("A")
                .matches("AbCD")
                .matches("U123")
                .matches("_a")
                .matches("_56")
                .matches("_AbC")
                .notMatches("_")
                .notMatches("42")
                ;
   }

   @Test
   public void checkRawIdentifier() {
       Assertions.assertThat(g.rule(RustLexer.RAW_IDENTIFIER))
               .notMatches("r#")
                .matches("r#a")
                .matches("r#_52")
                .matches("r#V123")
                .notMatches("s#52")
               //corner cases
                .notMatches("r#crate")
               .notMatches("r#self")
               .notMatches("r#super")
               .notMatches("r#Self");
   }

   @Test
    public void testNonKeywords(){
       Assertions.assertThat(g.rule(RustLexer.NON_KEYWORD_IDENTIFIER))
                .matches("a")
                .matches("bc")
                .matches("Abc")
                .notMatches("as");
   }

   @Test
   public void testIdentifier(){
       Assertions.assertThat(g.rule(RustLexer.IDENTIFIER))
         .matches("a")
               .matches("bc")
               .matches("Abc")
               .notMatches("as")
               .notMatches("r#")
               .matches("r#a")
               .matches("r#_52")
               .matches("r#V123")
               .notMatches("s#52");

   }


}
