package org.sonar.rust.ast.parser.grammar.lexical;

import org.junit.Test;
import org.sonar.rust.ast.parser.RustLexer;
import org.sonar.rust.ast.parser.grammar.LexerTesting;
import org.sonar.sslr.tests.Assertions;



public class KeywordTest extends LexerTesting {


    @Test
    public void checkList() {
        Assertions.assertThat(g.rule(RustLexer.KEYWORD))
                .matches("as")
                .matches("break")
                .matches("const")
                .matches("continue")
                .matches("crate")
                .matches("else")
                .matches("enum")
                .matches("extern")
                .matches("false")
                .matches("fn")
                .matches("for")
                .matches("if")
                .matches("impl")
                .matches("in")
                .matches("let")
                .matches("loop")
                .matches("match")
                .matches("mod")
                .matches("move")
                .matches("pub")
                .matches("ref")
                .matches("return")
                .matches("self")
                .matches("Self")
                .matches("static")
                .matches("struct")
                .matches("super")
                .matches("trait")
                .matches("true")
                .matches("type")
                .matches("unsafe")
                .matches("use")
                .matches("where")
                .matches("while")
                .matches("async")
                .matches("await")
                .matches("dyn");
    }
}