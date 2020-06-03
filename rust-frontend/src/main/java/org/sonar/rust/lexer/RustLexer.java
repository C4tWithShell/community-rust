package org.sonar.rust.lexer;

import com.sonar.sslr.impl.Lexer;

public class RustLexer {
    private RustLexer(){

    }

    public static Lexer create() {
        Lexer.Builder builder = Lexer.builder().withFailIfNoChannelToConsumeOneCharacter(true);
        addCommonChannels(builder);
        return builder.build();
    }

    private static void addCommonChannels(Lexer.Builder builder) {
        builder
                .withChannel(new NewLineChannel());
    }


}
