/**
 * Community Rust Plugin
 * Copyright (C) 2021 Eric Le Goff
 * mailto:community-rust AT pm DOT me
 * http://github.com/elegoff/sonar-rust
 * <p>
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.elegoff.plugins.communityrust;


import com.sonar.sslr.api.GenericTokenType;
import com.sonar.sslr.api.Token;
import com.sonar.sslr.api.Trivia;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.highlighting.NewHighlighting;
import org.sonar.api.batch.sensor.highlighting.TypeOfText;
import org.sonar.rust.RustVisitorContext;
import org.sonar.rust.api.RustKeyword;
import org.sonar.rust.api.RustTokenType;
import org.sonar.sslr.parser.LexerlessGrammar;
import org.sonar.sslr.parser.ParserAdapter;
import org.sonarsource.analyzer.commons.TokenLocation;

import java.util.*;

public class RustTokensVisitor {


    private final Set<String> keywords = new HashSet<>(Arrays.asList(RustKeyword.keywordValues()));
    private final SensorContext context;
    private final ParserAdapter<LexerlessGrammar> lexer;

    public RustTokensVisitor(SensorContext context, ParserAdapter<LexerlessGrammar> lexer) {
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
        var highlighting = context.newHighlighting();
        highlighting.onFile(inputFile);

        var cpdTokens = context.newCpdTokens();
        cpdTokens.onFile(inputFile);

        List<Token> parsedTokens = lexer.parse(visitorContext.file().content()).getTokens();

        Set<String> testAttributes = new HashSet<String>(){{
            add("test");
            add("tokio::test");
        }};
        Set<Token> testTokens = identifyUnitTestTokens(parsedTokens,testAttributes );

        for (Token token : parsedTokens) {
            final String tokenImage = getTokenImage(token);
            final var tokenLocation = tokenLocation(token);

            if (token.getType().equals(RustTokenType.CHARACTER_LITERAL)
                    || token.getType().equals(RustTokenType.STRING_LITERAL)
                    || token.getType().equals(RustTokenType.RAW_STRING_LITERAL)
                    || token.getType().equals(RustTokenType.RAW_BYTE_STRING_LITERAL)

            ) {
                highlight(highlighting, tokenLocation, TypeOfText.STRING);

            } else if (keywords.contains(tokenImage)) {
                highlight(highlighting, tokenLocation, TypeOfText.KEYWORD);
            }

            if (token.getType().equals(RustTokenType.FLOAT_LITERAL)
                    || token.getType().equals(RustTokenType.BOOLEAN_LITERAL)
                    || token.getType().equals(RustTokenType.INTEGER_LITERAL)

            ) {
                highlight(highlighting, tokenLocation, TypeOfText.CONSTANT);

            }

            for (Trivia trivia : token.getTrivia()) {
                highlight(highlighting, tokenLocation(trivia.getToken()), TypeOfText.COMMENT);
            }

            if (testTokens.contains(token)) {
                highlight(highlighting, tokenLocation, TypeOfText.ANNOTATION
                );
            }

            if (!GenericTokenType.EOF.equals(token.getType())) {
                cpdTokens.addToken(tokenLocation.startLine(), tokenLocation.startLineOffset(), tokenLocation.endLine(), tokenLocation.endLineOffset(), tokenImage);
            }
        }

        highlighting.save();
        cpdTokens.save();
    }

    private Set<Token> identifyUnitTestTokens(List<Token> parsedTokens, Set<String> knownTestAttributes) {
        Set<Token> testTokens = new HashSet<>();
        for (int i = 0; i < parsedTokens.size(); i++) {
            if (("#".equals(getTokenImage(parsedTokens.get(i))))
                    && ("[".equals(getTokenImage(parsedTokens.get(i + 1))))
                    && (knownTestAttributes.contains(getTokenImage(parsedTokens.get(i + 2))))
                    && ("]".equals(getTokenImage(parsedTokens.get(i + 3))))
                    && ("fn".equals(getTokenImage(parsedTokens.get(i + 4))))) {
                int j = i + 5;
                //lookup for opening bracket
                while (!"{".equals(getTokenImage(parsedTokens.get(j)))) {
                    j++;
                }

                int cptOpeningBracket = 1;
                //lookup for outer closing bracket (end of test function position)
                while (cptOpeningBracket > 0) {
                    j++;
                    if ("{".equals(getTokenImage(parsedTokens.get(j)))) {
                        cptOpeningBracket++;
                    }
                    if ("}".equals(getTokenImage(parsedTokens.get(j)))) {
                        cptOpeningBracket--;
                    }
                }

                //all tokens constituting a test function are added to the set
                for (int k = i; k <= j; k++) {
                    testTokens.add(parsedTokens.get(k));
                }
            }
        }
        return testTokens;

    }

}