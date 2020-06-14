/**
 *
 * Sonar Rust Plugin (Community)
 * Copyright (C) 2020 Eric Le Goff
 * http://github.com/elegoff/sonar-rust
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.sonar.rust.lexer;

import com.sonar.sslr.impl.Lexer;
import com.sonar.sslr.impl.channel.BlackHoleChannel;
import com.sonar.sslr.impl.channel.IdentifierAndKeywordChannel;
import com.sonar.sslr.impl.channel.PunctuatorChannel;
import com.sonar.sslr.impl.channel.UnknownCharacterChannel;
import org.sonar.rust.api.RustKeyword;
import org.sonar.rust.api.RustPunctuator;

import static com.sonar.sslr.impl.channel.RegexpChannelBuilder.*;

public class RustLexer {
    /* https://doc.rust-lang.org/reference/identifiers.html */
    private static final String IDENTIFIER_OR_KEYWORD = or("[a-z A-Z] [a-z A-Z 0-9 _]*", "_ [a-z A-Z 0-9 _]+");
    private static final String RAW_IDENTIFIER = "#r" + IDENTIFIER_OR_KEYWORD;
    private static final String IDENTIFIER = or(IDENTIFIER_OR_KEYWORD, RAW_IDENTIFIER);

    private RustLexer() {

    }

    public static Lexer create() {
        Lexer.Builder builder = Lexer.builder().withFailIfNoChannelToConsumeOneCharacter(true);
        addChannels(builder);
        return builder.build();
    }

    private static void addChannels(Lexer.Builder builder) {
        builder
                .withChannel(commentRegexp("//[^\\n\\r]*+"))
                .withChannel(commentRegexp("/\\*", ANY_CHAR + "*?", "\\*/"))
                .withChannel(new BlackHoleChannel("\\s"))
                .withChannel(new IdentifierAndKeywordChannel(IDENTIFIER, true, RustKeyword.values()))
                .withChannel(new StringLiteralsChannel())
                .withChannel(new PunctuatorChannel(RustPunctuator.values()))

                .withChannel(new UnknownCharacterChannel());
        tuple(builder);
    }

    private static void tuple(Lexer.Builder builder){
        builder.withChannel(LexerDefintion.TUPLE_INDEX);

    }


}
