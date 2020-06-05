package org.sonar.rust.lexer;

import com.sonar.sslr.impl.Lexer;
import com.sonar.sslr.impl.channel.BlackHoleChannel;
import com.sonar.sslr.impl.channel.IdentifierAndKeywordChannel;
import com.sonar.sslr.impl.channel.PunctuatorChannel;
import com.sonar.sslr.impl.channel.UnknownCharacterChannel;
import org.sonar.rust.api.RustKeyword;
import org.sonar.rust.api.RustPunctuator;

import static com.sonar.sslr.impl.channel.RegexpChannelBuilder.or;

public class RustLexer {
    /* https://doc.rust-lang.org/reference/identifiers.html */
    private static final String IDENTIFIER_OR_KEYWORD = or("[a-z A-Z] [a-z A-Z 0-9 _]*", "_ [a-z A-Z 0-9 _]+");
    private static final String RAW_IDENTIFIER = "#r" + IDENTIFIER_OR_KEYWORD;
    private static final String IDENTIFIER = or(IDENTIFIER_OR_KEYWORD, RAW_IDENTIFIER);

    private RustLexer() {

    }

    public static Lexer create() {
        Lexer.Builder builder = Lexer.builder().withFailIfNoChannelToConsumeOneCharacter(true);
        addCommonChannels(builder);
        return builder.build();
    }

    private static void addCommonChannels(Lexer.Builder builder) {
        builder
                .withChannel(new NewLineChannel())
                .withChannel(new BlackHoleChannel("\\s"))
                .withChannel(new IdentifierAndKeywordChannel(IDENTIFIER, true, RustKeyword.values()))

                .withChannel(new PunctuatorChannel(RustPunctuator.values()))

                .withChannel(new UnknownCharacterChannel());
    }


}
