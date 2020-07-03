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
package org.sonar.rust;

import com.google.common.collect.ImmutableList;
import com.sonar.sslr.api.Grammar;
import org.sonar.sslr.grammar.GrammarRuleKey;
import org.sonar.sslr.grammar.LexerlessGrammarBuilder;
import org.sonar.sslr.parser.ParserAdapter;

import java.nio.charset.Charset;

public enum RustLexer implements GrammarRuleKey {

    TOKENS;

    private static LexerlessGrammarBuilder create() {
        LexerlessGrammarBuilder b =RustGrammar.create();

        b.rule(TOKENS).is(RustGrammar.SPACING, b.optional(RustGrammar.ANY_TOKEN), RustGrammar.EOF);

        b.setRootRule(TOKENS);

        return b;
    }


    private static LexerlessGrammarBuilder create(GrammarRuleKey root) {
        LexerlessGrammarBuilder b =RustGrammar.create();

        b.rule(TOKENS).is(RustGrammar.SPACING, b.zeroOrMore(RustGrammar.ANY_TOKEN), RustGrammar.EOF);

        b.setRootRule(root);

        return b;
    }

    public static ParserAdapter create(RustParserConfiguration conf) {
        return new ParserAdapter(conf.getCharset(), create().build());
    }


    public static ParserAdapter create(RustParserConfiguration conf, GrammarRuleKey root) {
        return new ParserAdapter(conf.getCharset(), create(root).build());
    }

}
