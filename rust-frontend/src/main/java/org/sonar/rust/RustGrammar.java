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
    //ARITHMETIC_OR_LOGICAL_EXPRESSION,
    //ARRAY_ELEMENTS,
    //ARRAY_EXPRESSION,
    //ARRAY_TYPE,
    //ASCII,
    ASCII_ESCAPE,
    //ASCII_FOR_CHAR,
    //ASCII_FOR_STRING,
    //ASSIGNMENT_EXPRESSION,
    //ASSIGNMENT_EXPRESSION_TERM,
    //ASYNC_BLOCK_EXPRESSION,
    //ASYNC_CONST_QUALIFIERS,
    //AS_CLAUSE,
    //ATTR,
    //ATTR_INPUT,
    //AWAIT_EXPRESSION,
    //AWAIT_EXPRESSION_TERM,
    //BARE_FUNCTION_RETURN_TYPE,
    //BARE_FUNCTION_TYPE,
    BIN_DIGIT,
    BIN_LITERAL,
    //BITAND_EXPRESSION,
    //BITOR_EXPRESSION,
    //BITXOR_EXPRESSION,
    BLOCK_COMMENT,
    BLOCK_COMMENT_OR_DOC,
    //BLOCK_EXPRESSION,
    BOOLEAN_LITERAL,
    //BORROW_EXPRESSION,
    //BREAK_EXPRESSION,
    BYTE_ESCAPE,
    BYTE_LITERAL,
    BYTE_STRING_LITERAL,
    //CALL_EXPRESSION,
    //CALL_EXPRESSION_TERM,
    //CALL_PARAMS,
    //CALL_PARAMS_TERM,
    //CARETEQ_EXPRESSION,
    CHAR_LITERAL,
    //CLOSURE_EXPRESSION,
    //CLOSURE_PARAM,
    //CLOSURE_PARAMETERS,
    //COMPARISON_EXPRESSION,
    COMPILATION_UNIT,
    //COMPOUND_ASSIGNMENT_EXPRESSION,
    //CONSTANT_ITEM,
    //CONTINUE_EXPRESSION,
    //CRATE_REF,
    DEC_DIGIT,
    DEC_LITERAL,
    DELIMITERS,
    //DELIM_TOKEN_TREE,
    //DEREFERENCE_EXPRESSION,
    //DIVISION_EXPRESSION,
    //ENUMERATION,
    //ENUMERATION_VARIANT_EXPRESSION,
    //ENUM_EXPR_FIELD,
    //ENUM_EXPR_FIELDLESS,
    //ENUM_EXPR_FIELDS,
    //ENUM_EXPR_STRUCT,
    //ENUM_EXPR_TUPLE,
    //ENUM_ITEMS, ENUM_ITEM,
    //ENUM_ITEM_DISCRIMINANT,
    //ENUM_ITEM_STRUCT,
    //ENUM_ITEM_TUPLE,
    EOF,
    //EQ_EXPRESSION,
    //ERROR_PROPAGATION_EXPRESSION,
    //ERROR_PROPAGATION_EXPRESSION_TERM,
    //EXPRESSION_STATEMENT,
    //EXPRESSION_WITHOUT_BLOCK,
    //EXPRESSION_WITHOUT_BLOCK_ES,
    //EXPRESSION_WITHOUT_BLOCK_STS,
    //EXPRESSION_WITH_BLOCK,
    //EXTERNAL_FUNCTION_ITEM,
    //EXTERNAL_ITEM,
    //EXTERNAL_STATIC_ITEM,
    //EXTERN_BLOCK,
    //EXTERN_CRATE,
    //FIELD_EXPRESSION,
    //FIELD_EXPRESSION_TERM,
    FLOAT_EXPONENT,
    FLOAT_LITERAL,
    FLOAT_SUFFIX,
    //FOR_LIFETIMES,
    //FUNCTION,
    //FUNCTION_PARAM,
    //FUNCTION_PARAMETERS,
    //FUNCTION_PARAMETERS_MAYBE_NAMED_VARIADIC,
    //FUNCTION_QUALIFIERS,
    //FUNCTION_RETURN_TYPE,
    //GENERICS,
    //GENERIC_ARGS,
    //GENERIC_ARGS_BINDING,
    //GENERIC_ARGS_BINDINGS,
    //GENERIC_ARGS_LIFETIMES,
    //GENERIC_ARGS_TYPES,
    //GENERIC_PARAMS,
    //GROUPED_EXPRESSION,
    //GROUPED_PATTERN,
    //GT_EXPRESSION,
    HEX_DIGIT,
    HEX_LITERAL,
    IDENTIFIER,
    //IDENTIFIER_OR_KEYWORD,
    //IDENTIFIER_PATTERN,
    //IF_EXPRESSION,
    //IF_LET_EXPRESSION,
    //IMPLEMENTATION,
    //IMPL_TRAIT_TYPE,
    //IMPL_TRAIT_TYPE_ONE_BOUND,
    //INDEX_EXPRESSION,
    //INDEX_EXPRESSION_TERM,
    //INFERRED_TYPE,
    //INFINITE_LOOP_EXPRESSION,
    //INHERENT_IMPL,
    //INHERENT_IMPL_ITEM,
    //INNER_ATTRIBUTE,
    INNER_BLOCK_DOC,
    INNER_LINE_DOC,
    INTEGER_LITERAL,
    INTEGER_SUFFIX,
    //ITEM,
    //ITERATOR_LOOP_EXPRESSION,
    KEYWORD,
    //LAZY_BOOLEAN_EXPRESSION,
    //LET_STATEMENT,
    //LE_EXPRESSION,
    //LIFETIME,
    //LIFETIMES,
    //LIFETIME_BOUNDS,
    //LIFETIME_OR_LABEL,
    //LIFETIME_PARAM,
    //LIFETIME_PARAMS,
    //LIFETIME_TOKEN,
    //LIFETIME_WHERE_CLAUSE_ITEM,
    LINE_COMMENT,
    //LITERALS,
    //LITERAL_PATTERN,
    //LITTERAL_EXPRESSION,
    //LOOP_EXPRESSION,
    //LOOP_LABEL,
    //LT_EXPRESSION, GE_EXPRESSION,
    //MACRO_FRAG_SPEC,
    //MACRO_INVOCATION,
    //MACRO_INVOCATION_SEMI,
    //MACRO_ITEM,
    //MACRO_MATCH,
    //MACRO_MATCHER,
    //MACRO_REP_OP,
    //MACRO_REP_SEP,
    //MACRO_RULE,
    //MACRO_RULES,
    //MACRO_RULES_DEF,
    //MACRO_RULES_DEFINITION,
    //MACRO_TRANSCRIBER,
    //MATCH_ARM,
    //MATCH_ARMS,
    //MATCH_ARM_GUARD,
    //MATCH_ARM_PATTERNS,
    //MATCH_EXPRESSION,
    //MAYBE_NAMED_FUNCTION_PARAMETERS,
    //MAYBE_NAMED_FUNCTION_PARAMETERS_VARIADIC,
    //MAYBE_NAMED_PARAM, STRUCT_FIELDS,
    //METHOD,
    //METHOD_CALL_EXPRESSION,
    //METHOD_CALL_EXPRESSION_TERM,
    //MINUSEQ_EXPRESSION,
    //MODULE,
    //MULTIPLICATION_EXPRESSION,
    //NAMED_FUNCTION_PARAM,
    //NAMED_FUNCTION_PARAMETERS,
    //NAMED_FUNCTION_PARAMETERS_WITH_VARIADICS,
    //NEGATION_EXPRESSION,
    //NEQ_EXPRESSION,
    //NEVER_TYPE,
    NON_KEYWORD_IDENTIFIER,
    //NON_ZERO_DEC_DIGIT,
    OCT_DIGIT,
    OCT_LITERAL,
    //OPERATOR_EXPRESSION,
    //OREQ_EXPRESSION,
    //OUTER_ATTRIBUTE,
    OUTER_BLOCK_DOC,
    OUTER_LINE_DOC,
    //PARENTHESIZED_TYPE,
    //PATH_EXPRESSION,
    //PATH_EXPR_SEGMENT,
    //PATH_IDENT_SEGMENT,
    //PATH_IN_EXPRESSION,
    //PATH_PATTERN,
    //PATTERN,
    //PERCENTEQ_EXPRESSION,
    //PLUSEQ_EXPRESSION,
    //PREDICATE_LOOP_EXPRESSION,
    //PREDICATE_PATTERN_LOOP_EXPRESSION,
    PUNCTUATION,
    PUNCTUATION_EXCEPT_DOLLAR,
    //QUALIFIED_PATH_IN_EXPRESSION,
    //QUALIFIED_PATH_IN_TYPE,
    //QUALIFIED_PATH_TYPE,
    QUOTE_ESCAPE,
    //RANGE_EXPR,
    //RANGE_EXPRESSION,
    //RANGE_EXPR_TERM,
    //RANGE_FROM_EXPR,
    //RANGE_FROM_EXPR_TERM,
    //RANGE_FULL_EXPR,
    //RANGE_INCLUSIVE_EXPR,
    //RANGE_INCLUSIVE_EXPR_TERM,
    //RANGE_PATTERN,
    //RANGE_PATTERN_BOUND,
    //RANGE_TO_EXPR,
    //RANGE_TO_INCLUSIVE_EXPR,
    RAW_BYTE_STRING_CONTENT,
    RAW_BYTE_STRING_LITERAL,
    RAW_IDENTIFIER,
    //RAW_POINTER_TYPE,
    RAW_STRING_CONTENT,
    RAW_STRING_LITERAL,
    //REFERENCE_PATTERN,
    //REFERENCE_TYPE,
    //REMAINDER_EXPRESSION,
    //RETURN_EXPRESSION,
    //SELF_PARAM,
    //SHLEQ_EXPRESSION,
    //SHL_EXPRESSION,
    //SHORTHAND_SELF,
    //SHREQ_EXPRESSION,
    //SHR_EXPRESSION,
    //SIMPLE_PATH,
    //SIMPLE_PATH_SEGMENT,
    //SLASHEQ_EXPRESSION,
    //SLICE_PATTERN,
    //SLICE_TYPE,
    SPC,
    //STAREQ_EXPRESSION,
    STATEMENT,
    //STATEMENTS,
    //STATIC_ITEM,
    STRING_CONTENT,
    STRING_CONTINUE,
    STRING_LITERAL,
    //STRUCT,
    //STRUCT_BASE,
    //STRUCT_EXPRESSION,
    //STRUCT_EXPR_FIELD,
    //STRUCT_EXPR_FIELDS,
    //STRUCT_EXPR_FIELD_TERM,
    //STRUCT_EXPR_STRUCT,
    //STRUCT_EXPR_TUPLE,
    //STRUCT_EXPR_UNIT,
    //STRUCT_FIELD,
    //STRUCT_PATTERN,
    //STRUCT_PATTERN_ELEMENTS,
    //STRUCT_PATTERN_ETCETERA,
    //STRUCT_PATTERN_FIELD,
    //STRUCT_PATTERN_FIELDS,
    //STRUCT_STRUCT,
    //SUBTRACTION_EXPRESSION,
    //TOKEN,
    //TOKEN_EXCEPT_DELIMITERS,
    //TOKEN_MACRO,
    //TOKEN_TREE,
    //TRAIT,
    //TRAIT_BOUND,
    //TRAIT_CONST,
    //TRAIT_FUNC,
    //TRAIT_FUNCTION_DECL,
    //TRAIT_FUNCTION_PARAM,
    //TRAIT_FUNCTION_PARAMETERS,
    //TRAIT_IMPL,
    //TRAIT_IMPL_ITEM,
    //TRAIT_ITEM,
    //TRAIT_METHOD,
    //TRAIT_METHOD_DECL,
    //TRAIT_OBJECT_TYPE,
    //TRAIT_OBJECT_TYPE_ONE_BOUND,
    //TRAIT_TYPE,
    //TUPLE_ELEMENT,
    //TUPLE_ELEMENT_TERM,
    //TUPLE_EXPRESSION,
    //TUPLE_FIELD,
    //TUPLE_FIELDS,
    //TUPLE_INDEX,
    //TUPLE_INDEXING_EXPRESSION,
    //TUPLE_INDEXING_EXPRESSION_TERM,
    //TUPLE_PATTERN,
    //TUPLE_PATTERN_ITEMS,
    //TUPLE_STRUCT,
    //TUPLE_STRUCT_ITEMS,
    //TUPLE_STRUCT_PATTERN,
    //TUPLE_TYPE,
    //TYPE,
    //TYPED_SELF,
    //TYPE_ALIAS,
    //TYPE_BOUND_CLAUSE_ITEM,
    //TYPE_CAST_EXPRESSION,
    //TYPE_CAST_EXPRESSION_TERM,
    //TYPE_NO_BOUNDS,
    //TYPE_PARAM,
    //TYPE_PARAMS,
    //TYPE_PARAM_BOUND,
    //TYPE_PARAM_BOUNDS,
    //TYPE_PATH,
    //TYPE_PATH_FN,
    //TYPE_PATH_FN_INPUTS,
    //TYPE_PATH_SEGMENT,
    UNICODE_ESCAPE,
    //UNION,
    UNKNOWN_CHAR
    //UNSAFE_BLOCK_EXPRESSION,
    //USE_DECLARATION,
    //USE_TREE,
    //VISIBILITY,
    //VISIT_ITEM,
    //WHERE_CLAUSE,
    //WHERE_CLAUSE_ITEM,
    //WILDCARD_PATTERN
    ;

    private static final String IDFREGEXP1 = "[a-zA-Z][a-zA-Z0-9_]*";
    private static final String IDFREGEXP2 = "_[a-zA-Z0-9_]+";
    private static final String UNSAFE = "unsafe";
    private static final String CONST = "const";
    private static final String EXTERN = "extern";


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
        //b.rule(IDENTIFIER_OR_KEYWORD).is(b.firstOf(b.regexp("^" + IDFREGEXP1), b.regexp("^" + IDFREGEXP2)));
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