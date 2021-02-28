/**
 * Sonar Rust Plugin (Community)
 * Copyright (C) 2021 Eric Le Goff
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

import com.sonar.sslr.api.GenericTokenType;
import org.apache.commons.lang.ArrayUtils;
import org.sonar.rust.api.RustKeyword;
import org.sonar.rust.api.RustPunctuator;
import org.sonar.rust.api.RustTokenType;
import org.sonar.sslr.grammar.GrammarRuleKey;
import org.sonar.sslr.grammar.LexerlessGrammarBuilder;

import java.util.Arrays;

public enum RustGrammar implements GrammarRuleKey {
    ANY_TOKEN,
    ASCII_ESCAPE,
    BIN_DIGIT,
    BIN_LITERAL,
    BLOCK_COMMENT,
    BLOCK_COMMENT_OR_DOC,
    BOOLEAN_LITERAL,
    BYTE_ESCAPE,
    BYTE_LITERAL,
    BYTE_STRING_LITERAL,
    CHAR_LITERAL,
    COMPILATION_UNIT,
    DEC_DIGIT,
    DEC_LITERAL,
    DELIMITERS,
    EOF,
    FLOAT_EXPONENT,
    FLOAT_LITERAL,
    FLOAT_SUFFIX,
    HEX_DIGIT,
    HEX_LITERAL,
    IDENTIFIER,
    INNER_BLOCK_DOC,
    INNER_LINE_DOC,
    INTEGER_LITERAL,
    INTEGER_SUFFIX,
    KEYWORD,
    LINE_COMMENT,
    NON_KEYWORD_IDENTIFIER,
    OCT_DIGIT,
    OCT_LITERAL,
    OUTER_BLOCK_DOC,
    OUTER_LINE_DOC,
    PUNCTUATION,
    PUNCTUATION_EXCEPT_DOLLAR,
    QUOTE_ESCAPE,
    RAW_BYTE_STRING_CONTENT,
    RAW_BYTE_STRING_LITERAL,
    RAW_IDENTIFIER,
    RAW_STRING_CONTENT,
    RAW_STRING_LITERAL,
    SPC,
    STATEMENT,
    STRING_CONTENT,
    STRING_CONTINUE,
    STRING_LITERAL,
    UNICODE_ESCAPE,
    UNKNOWN_CHAR
    ;

    private static final String IDFREGEXP1 = "[a-zA-Z][a-zA-Z0-9_]*";
    private static final String IDFREGEXP2 = "_[a-zA-Z0-9_]+";



    public static LexerlessGrammarBuilder create() {
        LexerlessGrammarBuilder b = LexerlessGrammarBuilder.create();

        b.rule(COMPILATION_UNIT).is(SPC, b.zeroOrMore(STATEMENT), EOF);
        minimal(b);
        punctuators(b);

        b.setRootRule(COMPILATION_UNIT);

        return b;
    }

    private static Object inlineComment(LexerlessGrammarBuilder b) {
        return b.regexp("//[^\\n\\r]*+");
    }

    private static Object multilineComment(LexerlessGrammarBuilder b) {
        return b.regexp("/\\*[\\s\\S]*?\\*\\/");
    }

    private static void minimal(LexerlessGrammarBuilder b) {
        b.rule(SPC).is(
                b.skippedTrivia(whitespace(b)),
                b.zeroOrMore(
                        b.commentTrivia(b.firstOf(inlineComment(b), multilineComment(b))),
                        b.skippedTrivia(whitespace(b))));


        b.rule(EOF).is(b.token(GenericTokenType.EOF, b.endOfInput())).skip();
        comments(b);
        b.rule(STATEMENT).is(b.oneOrMore(b.sequence(ANY_TOKEN,SPC )));
        keywords(b);
        identifiers(b);
        b.rule(ANY_TOKEN).is(
                b.firstOf(
                        DELIMITERS,
                        CHAR_LITERAL,
                        BYTE_LITERAL,
                        BYTE_STRING_LITERAL,
                        INTEGER_LITERAL,
                        FLOAT_LITERAL,
                        BOOLEAN_LITERAL,
                        STRING_LITERAL,
                        RAW_STRING_LITERAL,
                        HEX_LITERAL,
                        OCT_LITERAL,
                        RAW_BYTE_STRING_LITERAL,
                        IDENTIFIER,
                        KEYWORD,
                        RustPunctuator.SEMI,
                        UNKNOWN_CHAR

                ));
        b.rule(CHAR_LITERAL).is(b.token(RustTokenType.CHARACTER_LITERAL,
                b.firstOf(b.regexp("^\\'[^\\\\n\\r\\t\\'].*\\'"),
                        b.sequence("'", UNICODE_ESCAPE, "'"),
                        b.sequence("'", QUOTE_ESCAPE, "'"),
                        b.sequence("'", ASCII_ESCAPE, "'")))).skip();
        b.rule(QUOTE_ESCAPE).is(b.firstOf("\\'", "\\\""));
        b.rule(BYTE_STRING_LITERAL).is(b.token(RustTokenType.BYTE_STRING_LITERAL,
                b.firstOf(
                        b.regexp("^b\"" + "[^\"\\r\\\\].*" + "\""),
                        b.sequence("b\"", BYTE_ESCAPE, "\"")
                )));


        b.rule(BYTE_ESCAPE).is(b.firstOf(b.sequence("\\x", HEX_DIGIT, HEX_DIGIT), "\\n", "\\r", "\\t", "\\", "\\0"));
        b.rule(UNICODE_ESCAPE).is("\\u{", b.oneOrMore(b.sequence(HEX_DIGIT, b.zeroOrMore(RustPunctuator.UNDERSCORE))), "}");
        b.rule(HEX_DIGIT).is(b.regexp("[0-9a-fA-F]"));
        b.rule(FLOAT_LITERAL).is(b.token(RustTokenType.FLOAT_LITERAL,
                b.firstOf(
                        b.sequence(DEC_LITERAL, b.optional(b.sequence(".", DEC_LITERAL)), b.optional(FLOAT_EXPONENT), FLOAT_SUFFIX),
                        b.sequence(DEC_LITERAL, ".", DEC_LITERAL, b.optional(FLOAT_EXPONENT)),
                        b.sequence(DEC_LITERAL, FLOAT_EXPONENT),
                        b.sequence(DEC_LITERAL, ".")//(not immediately followed by ., _ or an identifier)
                )));
        b.rule(FLOAT_EXPONENT).is(b.regexp("[eE]+[+-]?[0-9][0-9_]*"));

        b.rule(FLOAT_SUFFIX).is(b.firstOf("f64", "f32"));
        b.rule(HEX_LITERAL).is("0x", b.zeroOrMore(b.firstOf(HEX_DIGIT, RustPunctuator.UNDERSCORE)));
        b.rule(RAW_STRING_LITERAL).is(b.token(RustTokenType.RAW_STRING_LITERAL,
                b.sequence("r", RAW_STRING_CONTENT)));
        b.rule(RAW_STRING_CONTENT).is(b.firstOf(
                b.regexp("^\"[^\\r\\n].*\""),
                b.sequence(RustPunctuator.POUND, RAW_STRING_CONTENT, RustPunctuator.POUND)));
        b.rule(OCT_LITERAL).is("0o", b.zeroOrMore(b.firstOf(OCT_DIGIT, RustPunctuator.UNDERSCORE)));
        b.rule(OCT_DIGIT).is(b.regexp("[0-7]"));
        b.rule(DEC_LITERAL).is(DEC_DIGIT, b.zeroOrMore(b.firstOf(DEC_DIGIT, RustPunctuator.UNDERSCORE)));
        b.rule(DEC_DIGIT).is(b.regexp("[0-9]"));
        b.rule(BOOLEAN_LITERAL).is(b.token(RustTokenType.BOOLEAN_LITERAL, b.firstOf("true", "false")));
        b.rule(UNKNOWN_CHAR).is(
                b.token(GenericTokenType.UNKNOWN_CHAR, b.regexp("(?s).")),
                SPC).skip();
        b.rule(BYTE_LITERAL).is(b.token(RustTokenType.BYTE_LITERAL,
                b.firstOf(
                        b.regexp("^b\\'" + "[^\\'\\n\\r\\t\\\\].*" + "\\'"),
                        b.sequence("b'", BYTE_ESCAPE, "'")
                )));
        b.rule(RAW_BYTE_STRING_LITERAL).is(b.token(RustTokenType.RAW_BYTE_STRING_LITERAL, b.sequence("br", RAW_BYTE_STRING_CONTENT)));
        b.rule(RAW_BYTE_STRING_CONTENT).is(b.firstOf(
                b.regexp("^\"[\\x00-\\x7F]*\""),
                b.sequence(RustPunctuator.POUND, RAW_STRING_CONTENT, RustPunctuator.POUND)
        ));
        b.rule(INTEGER_LITERAL).is(b.token(RustTokenType.INTEGER_LITERAL,
                b.sequence(
                        b.firstOf(HEX_LITERAL, OCT_LITERAL, BIN_LITERAL, DEC_LITERAL),
                        b.optional(INTEGER_SUFFIX), SPC)));
        b.rule(INTEGER_SUFFIX).is(b.firstOf("u8", "u16", "u32", "u64", "u128", "usize"
                , "i8", "i16", "i32", "i64", "i128", "isize"));
        b.rule(BIN_LITERAL).is("0b", b.zeroOrMore(b.firstOf(BIN_DIGIT, RustPunctuator.UNDERSCORE)));


        b.rule(BIN_DIGIT).is(b.regexp("[0-1]"));
        b.rule(ASCII_ESCAPE).is(b.firstOf(b.sequence("\\x", OCT_DIGIT, HEX_DIGIT),
                "\\n", "\\r", "\\t", "\\", "\0"));
        b.rule(DELIMITERS).is(b.firstOf("{", "}", "[", "]", "(", ")"));

        b.rule(STRING_LITERAL).is(b.token(RustTokenType.STRING_LITERAL,
                b.sequence(
                        "\"", b.zeroOrMore(b.firstOf(

                                QUOTE_ESCAPE
                                , ASCII_ESCAPE
                                , UNICODE_ESCAPE
                                , STRING_CONTINUE
                                , STRING_CONTENT
                        ), SPC),
                        "\""
                )));
        b.rule(STRING_CONTINUE).is("\\\n");
        b.rule(STRING_CONTENT).is(b.regexp("(\\r\\n)+|[^\"\\r]+"));


    } //minimal




    private static void comments(LexerlessGrammarBuilder b) {
        b.rule(LINE_COMMENT).is(b.commentTrivia(
                b.regexp("////[^!/\\n]*|//[^!/\\n]*")
        ));


        b.rule(BLOCK_COMMENT).is(b.commentTrivia(
                b.firstOf(
                        "/***/",
                        "/**/",
                        b.regexp("^\\/\\*.*\\*\\/")
                )
        ));


        b.rule(INNER_LINE_DOC).is(b.commentTrivia(b.regexp("(?!\\n\\r)//!.*")));
        b.rule(INNER_BLOCK_DOC).is(
                b.regexp("^\\/\\*!.*\\*\\/")
        );
        b.rule(OUTER_LINE_DOC).is(b.commentTrivia(b.regexp("///[^\\r\\n\\/]*")));
        b.rule(OUTER_BLOCK_DOC).is(b.regexp("^\\/\\*\\*[^\\r\\n\\*].*\\*\\/")
        );
        b.rule(BLOCK_COMMENT_OR_DOC).is(b.commentTrivia(
                b.firstOf(BLOCK_COMMENT, OUTER_BLOCK_DOC, INNER_BLOCK_DOC)
        ));
    }

    private static String[] getPunctuatorsExcept(String[] arr, String toRemove) {
        int newLength = arr.length;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i].contains(toRemove)) {
                newLength--;
            }
        }
        String[] result = new String[newLength];
        int count = 0;
        for (int i = 0; i < arr.length; i++) {
            if (!arr[i].contains(toRemove)) {
                result[count] = arr[i];
                count++;
            }
        }
        return result;
    }

    private static void punctuators(LexerlessGrammarBuilder b) {
        for (RustPunctuator tokenType : RustPunctuator.values()) {
            b.rule(tokenType).is(tokenType.getValue());
        }
        String[] punctuators = RustPunctuator.punctuatorValues();

        String[] punctuatorsExceptDollar = RustGrammar.getPunctuatorsExcept(punctuators, "$");

        Arrays.sort(punctuators);
        ArrayUtils.reverse(punctuators);
        b.rule(PUNCTUATION).is(
                b.firstOf(
                        punctuators[0],
                        punctuators[1],
                        ArrayUtils.subarray(punctuators, 2, punctuators.length)));
        Arrays.sort(punctuatorsExceptDollar);
        ArrayUtils.reverse(punctuatorsExceptDollar);
        b.rule(PUNCTUATION_EXCEPT_DOLLAR).is(
                b.firstOf(
                        punctuatorsExceptDollar[0],
                        punctuatorsExceptDollar[1],
                        ArrayUtils.subarray(punctuators, 2, punctuatorsExceptDollar.length)));
    }

    private static void keywords(LexerlessGrammarBuilder b) {
        for (RustKeyword tokenType : RustKeyword.values()) {
            b.rule(tokenType).is(tokenType.getValue(), SPC);
        }
        String[] keywords = RustKeyword.keywordValues();
        Arrays.sort(keywords);
        ArrayUtils.reverse(keywords);
        b.rule(KEYWORD).is(
                b.firstOf(
                        keywords[0],
                        keywords[1],
                        ArrayUtils.subarray(keywords, 2, keywords.length)));
    }

    private static Object whitespace(LexerlessGrammarBuilder b) {
        return b.skippedTrivia(b.regexp("[ \t\n\r]*+"));
    }




    private static void identifiers(LexerlessGrammarBuilder b) {



        b.rule(RAW_IDENTIFIER).is(b.firstOf(b.regexp("^r#" + IDFREGEXP1 + "(?<!r#(crate|self|super|Self))"), b.regexp("^r#" + IDFREGEXP2)));
        b.rule(NON_KEYWORD_IDENTIFIER).is(b.regexp("^" + IDFREGEXP1 + exceptKeywords()));//Except a strict or reserved keyword
        b.rule(IDENTIFIER).is(b.token(RustTokenType.IDENTIFIER,
                b.firstOf(RAW_IDENTIFIER, NON_KEYWORD_IDENTIFIER))).skip();
    }

    private static String exceptKeywords() {
        StringBuilder sb = new StringBuilder("(?<!(");
        String[] values = RustKeyword.keywordValues();
        sb.append("^").append(values[0]).append("$");
        for (String kw : values) {
            sb.append("|^");
            sb.append(kw).append("$");
        }
        sb.append("))");

        return sb.toString();

    }



}