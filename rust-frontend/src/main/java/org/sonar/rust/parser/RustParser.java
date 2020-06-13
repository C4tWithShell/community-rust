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
package org.sonar.rust.parser;

import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.api.Grammar;
import com.sonar.sslr.api.Rule;
import com.sonar.sslr.api.Token;
import com.sonar.sslr.impl.Lexer;
import com.sonar.sslr.impl.Parser;
import com.sonar.sslr.impl.matcher.RuleDefinition;
import org.sonar.rust.api.RustGrammar;
import org.sonar.rust.lexer.RustLexer;

import java.util.ArrayList;
import java.util.List;

public final class RustParser  {
    private final Parser<Grammar> sslrParser;

    public static RustParser create() {
        return new RustParser();
    }

    private RustParser() {
        sslrParser = new SslrRustParser();
    }

    public AstNode parse(String source) {
        return sslrParser.parse(source);
    }

    public void setRootRule(Rule rule) {
        sslrParser.setRootRule(rule);
    }

    public Grammar getGrammar() {
        return sslrParser.getGrammar();
    }

    public RuleDefinition getRootRule() {
        return sslrParser.getRootRule();
    }


    private static class SslrRustParser extends Parser<Grammar> {

        private final Lexer lexer;

        private SslrRustParser() {
            super(RustGrammar.create());

            Rule rule = super.getGrammar().getRootRule();
            super.setRootRule(rule);

            this.lexer = RustLexer.create();
        }

        @Override
        public AstNode parse(String source) {
            lexer.lex(source);
            return super.parse(tokens());
        }

        private List<Token> tokens() {
            List<Token> tokens = lexer.getTokens();
            return tokens;
        }
    }
}
