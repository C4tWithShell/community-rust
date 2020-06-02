package org.elegoff.rust.lexer;

import com.sonar.sslr.api.Preprocessor;
import com.sonar.sslr.impl.Lexer;
import com.sonar.sslr.impl.channel.BlackHoleChannel;
import com.sonar.sslr.impl.channel.IdentifierAndKeywordChannel;
import com.sonar.sslr.impl.channel.PunctuatorChannel;
import com.sonar.sslr.impl.channel.UnknownCharacterChannel;
import org.elegoff.rust.RustSquidConfiguration;
import org.elegoff.rust.api.RustKeyword;
import org.elegoff.rust.api.RustPunctuator;
import org.elegoff.rust.api.RustTokenType;
import org.elegoff.rust.channels.CharacterLiteralsChannel;
import org.elegoff.rust.channels.StringLiteralsChannel;

import static com.sonar.sslr.impl.channel.RegexpChannelBuilder.*;
import static com.sonar.sslr.impl.channel.RegexpChannelBuilder.o2n;

public class RustLexer {

    private static final String HEX_PREFIX = "0[xX]";
    private static final String BIN_PREFIX = "0[bB]";
    private static final String EXPONENT = "[Ee][+-]?+[0-9_]([']?+[0-9_]++)*+";
    private static final String BINARY_EXPONENT = "[pP][+-]?+[0-9]([']?+[0-9]++)*+";
    private static final String UD_SUFFIX = "[_a-zA-Z][_a-zA-Z0-9]*+";
    private static final String DECDIGIT_SEQUENCE = "[0-9]([']?+[0-9]++)*+";
    private static final String HEXDIGIT_SEQUENCE = "[0-9a-fA-F]([']?+[0-9a-fA-F]++)*+";
    private static final String BINDIGIT_SEQUENCE = "[01]([']?+[01]++)*+";
    private static final String POINT = "\\.";

    private RustLexer(){

    }

    public static Lexer create(RustSquidConfiguration squidConfig) {

        //
        // changes here must be always aligned: RustLexer.java <=> CppLexer.java
        //
        Lexer.Builder builder = Lexer.builder()
                .withCharset(squidConfig.getCharset())
                .withFailIfNoChannelToConsumeOneCharacter(true)
                .withChannel(new BlackHoleChannel("\\s"))
                // C++ Standard, Section 2.8 "Comments"
                .withChannel(commentRegexp("//[^\\n\\r]*+"))
                .withChannel(commentRegexp("/\\*", ANY_CHAR + "*?", "\\*/"))
                // backslash at the end of the line: just throw away
                .withChannel(new BackslashChannel())
                .withChannel(new CharacterLiteralsChannel())
                // C++ Standard, Section 2.14.5 "String literals"
                .withChannel(new StringLiteralsChannel())
                // C++ Standard, Section 2.14.2 "Integer literals"
                // C++ Standard, Section 2.14.4 "Floating literals"
                .withChannel(
                        regexp(RustTokenType.NUMBER,
                                and(
                                        or(
                                                g(POINT, DECDIGIT_SEQUENCE, opt(g(EXPONENT))),
                                                g(HEX_PREFIX, opt(g(HEXDIGIT_SEQUENCE)), opt(POINT), opt(g(HEXDIGIT_SEQUENCE)), opt(
                                                        g(BINARY_EXPONENT))),
                                                g(BIN_PREFIX, BINDIGIT_SEQUENCE),
                                                g(DECDIGIT_SEQUENCE, opt(POINT), opt(g(DECDIGIT_SEQUENCE)), opt(g(EXPONENT)))
                                        ),
                                        opt(g(UD_SUFFIX))
                                )
                        )
                )
                // C++ Standard, Section 2.14.7 "Pointer literals"
                .withChannel(regexp(RustTokenType.NUMBER, RustKeyword.NULLPTR.getValue() + "\\b"))
                // C++ Standard, Section 2.12 "Keywords"
                // C++ Standard, Section 2.11 "Identifiers"
                .withChannel(new IdentifierAndKeywordChannel(and("[a-zA-Z_]", o2n("\\w")), true, RustKeyword.values()))
                // C++ Standard, Section 2.13 "Operators and punctuators"
                .withChannel(new PunctuatorChannel(RustPunctuator.values()))
                .withChannel(new UnknownCharacterChannel());



        return builder.build();
    }

}
