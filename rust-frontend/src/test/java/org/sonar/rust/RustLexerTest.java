package org.sonar.rust;

import com.google.common.base.Charsets;
import com.sonar.sslr.api.Token;
import org.junit.Test;

import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

public class RustLexerTest {
    @Test
    public void testSize() {
        assertThat(lex("")).hasSize(2);
        assertThat(lex("   ")).hasSize(2);
        assertThat(lex("foo")).hasSize(3);
    }

    private List<Token> lex(String source) {
        List<Token> li = RustLexer.create(RustParserConfiguration.builder()
                .setCharset(Charsets.UTF_8)
                .build())
                .parse(source)
                .getTokens();

        return li;
    }
}
