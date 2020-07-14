package org.sonar.rust;

import com.google.common.base.Charsets;
import com.sonar.sslr.api.Token;
import org.junit.Test;
import org.sonar.sslr.tests.Assertions;

import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

public class RustLexerTest {
    @Test
    public void testSize() {
        assertThat(lex("")).hasSize(1);
        assertThat(lex("   ")).hasSize(1);
        assertThat(lex("foo")).hasSize(2);
    }

    private List<Token> lex(String source) {
        List<Token> li = RustLexer.create(RustParserConfiguration.builder()
                .setCharset(Charsets.UTF_8)
                .build())
                .parse(source)
                .getTokens();

        return li;
    }

    @Test
    public void testTokens() {
        Assertions.assertThat(RustLexer.create().build().rule(RustLexer.TOKENS))
                .matches("")
                .matches("fn")
                .matches("main()")
                .matches("fn main() {\n" +
                        "    println!(\"Hello, world!\");\n" +
                        "}")


        ;
    }
}
