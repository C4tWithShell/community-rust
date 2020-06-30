package org.sonar.rust.parser.lexer;

import org.junit.Test;
import org.sonar.rust.RustGrammar;

import static org.sonar.sslr.tests.Assertions.assertThat;

public class KeywordTest {
    @Test
    public void checkList() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.KEYWORD))
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
