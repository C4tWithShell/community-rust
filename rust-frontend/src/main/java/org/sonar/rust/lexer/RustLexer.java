package org.sonar.rust.lexer;

import com.sonar.sslr.impl.Lexer;

public class RustLexer {
    private RustLexer(){

    }

    public static Lexer create(LexerState lexerState) {
        Lexer.Builder builder = Lexer.builder().withFailIfNoChannelToConsumeOneCharacter(true);
        return builder.build();
    }
}
