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
package org.elegoff.plugins.rust;


import com.sonar.sslr.api.GenericTokenType;
import com.sonar.sslr.api.Token;
import com.sonar.sslr.api.Trivia;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.cpd.NewCpdTokens;
import org.sonar.api.batch.sensor.highlighting.NewHighlighting;
import org.sonar.api.batch.sensor.highlighting.TypeOfText;
import org.sonar.rust.RustVisitorContext;
import org.sonar.rust.api.RustKeyword;
import org.sonar.rust.api.RustTokenType;
import org.sonar.sslr.parser.ParserAdapter;
import org.sonarsource.analyzer.commons.TokenLocation;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class RustTokensVisitor{


        private final Set<String> keywords = new HashSet<>(Arrays.asList(RustKeyword.keywordValues()));
        private final SensorContext context;
        private final ParserAdapter lexer;

        public RustTokensVisitor(SensorContext context, ParserAdapter lexer) {
            this.context = context;
            this.lexer = lexer;
        }

        private static String getTokenImage(Token token) {
            if (token.getType().equals(RustTokenType.CHARACTER_LITERAL)) {
                return RustTokenType.CHARACTER_LITERAL.getValue();
            }
            return token.getValue().toLowerCase(Locale.ENGLISH);
        }

        private static void highlight(NewHighlighting highlighting, TokenLocation tokenLocation, TypeOfText typeOfText) {
            highlighting.highlight(tokenLocation.startLine(), tokenLocation.startLineOffset(), tokenLocation.endLine(), tokenLocation.endLineOffset(), typeOfText);
        }

        private static TokenLocation tokenLocation(Token token) {
            return new TokenLocation(token.getLine(), token.getColumn(), token.getOriginalValue());
        }

        public void scanFile(InputFile inputFile, RustVisitorContext visitorContext) {
            NewHighlighting highlighting = context.newHighlighting();
            highlighting.onFile(inputFile);

            NewCpdTokens cpdTokens = context.newCpdTokens();
            cpdTokens.onFile(inputFile);

            for (Token token : lexer.parse(visitorContext.file().content()).getTokens()) {
                final String tokenImage = getTokenImage(token);
                final TokenLocation tokenLocation = tokenLocation(token);

                if (token.getType().equals(RustTokenType.CHARACTER_LITERAL)||token.getType().equals(RustTokenType.STRING_LITERAL)) {
                    highlight(highlighting, tokenLocation, TypeOfText.STRING);

                } else if (keywords.contains(tokenImage)) {
                    highlight(highlighting, tokenLocation, TypeOfText.KEYWORD);
                }

                for (Trivia trivia : token.getTrivia()) {
                    highlight(highlighting, tokenLocation(trivia.getToken()), TypeOfText.COMMENT);
                }

                if (!GenericTokenType.EOF.equals(token.getType())) {
                    cpdTokens.addToken(tokenLocation.startLine(), tokenLocation.startLineOffset(), tokenLocation.endLine(), tokenLocation.endLineOffset(), tokenImage);
                }
            }

            highlighting.save();
            cpdTokens.save();
        }

}