package org.sonar.rust;

import com.google.common.base.Charsets;
import com.sonar.sslr.api.Token;
import org.junit.Test;

import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

public class RustLexerTest {
    @Test
    public void test() {
        //FIXME
        //assertThat(lex("")).hasSize(1);
        //assertThat(lex("   ")).hasSize(1);
        //assertThat(lex("foo")).hasSize(2);
        //assertThat(lex("foo?")).hasSize(3);
    }

    private List<Token> lex(String source) {
        return RustLexer.create(RustParserConfiguration.builder()
                .setCharset(Charsets.UTF_8)
                .build())
                .parse(source)
                .getTokens();
    }
}
