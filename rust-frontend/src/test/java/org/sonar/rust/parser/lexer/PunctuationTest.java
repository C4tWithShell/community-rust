package org.sonar.rust.parser.lexer;

import org.junit.Test;
import org.sonar.rust.RustGrammar;
import static org.sonar.sslr.tests.Assertions.assertThat;

public class PunctuationTest {

    @Test
    public void reallife() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.PUNCTUATION))

                .matches("=").matches("+").matches("-").matches("*").matches("/").matches("%")
                .matches("^").matches("!").matches("=").matches("=").matches("=").matches("=")
                .matches("&").matches("||").matches("&&").matches("||").matches("<<").matches(">>")
                .matches("+=").matches("-=").matches("*=").matches("/=").matches("%=")
                .matches("^=").matches("&=").matches("|=").matches("<<=").matches(">>=")
                .matches("=").matches("==").matches("!=").matches(">").matches("<")
                .matches(">=").matches("<=").matches("@").matches("_").matches(".")
                .matches("..").matches("...").matches("..=").matches(",").matches(";")
                .matches(":").matches("::").matches("->").matches("=>").matches("#")
                .matches("$").matches("?")
                ;
    }
}

