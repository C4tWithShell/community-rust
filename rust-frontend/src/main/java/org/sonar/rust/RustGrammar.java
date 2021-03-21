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
    ABI,
    ADDITION_EXPRESSION,
    ADDITION_EXPRESSION_TERM,
    ANDEQ_EXPRESSION,
    ANY_TOKEN,
    ARITHMETIC_OR_LOGICAL_EXPRESSION,
    ARRAY_ELEMENTS,
    ARRAY_EXPRESSION,
    ARRAY_TYPE,
    ASCII,
    ASCII_ESCAPE,
    ASCII_FOR_CHAR,
    ASCII_FOR_STRING,
    ASSIGNMENT_EXPRESSION,
    ASSIGNMENT_EXPRESSION_TERM,
    ASYNC_BLOCK_EXPRESSION,
    ASYNC_CONST_QUALIFIERS,
    AS_CLAUSE,
    ATTR,
    ATTR_INPUT,
    AWAIT_EXPRESSION,
    AWAIT_EXPRESSION_TERM,
    BARE_FUNCTION_RETURN_TYPE,
    BARE_FUNCTION_TYPE,
    BIN_DIGIT,
    BIN_LITERAL,
    BITAND_EXPRESSION,
    BITAND_EXPRESSION_TERM,
    BITOR_EXPRESSION,
    BITOR_EXPRESSION_TERM,
    BITXOR_EXPRESSION,
    BITXOR_EXPRESSION_TERM,
    BLOCK_COMMENT,
    BLOCK_COMMENT_OR_DOC,
    BLOCK_EXPRESSION,
    BOOLEAN_LITERAL,
    BORROW_EXPRESSION,
    BREAK_EXPRESSION,
    BYTE_ESCAPE,
    BYTE_LITERAL,
    BYTE_STRING_LITERAL,
    CALL_EXPRESSION,
    CALL_EXPRESSION_TERM,
    CALL_PARAMS,
    CARETEQ_EXPRESSION,
    CHAR_LITERAL,
    CLOSURE_EXPRESSION,
    CLOSURE_PARAM,
    CLOSURE_PARAMETERS,
    COMPARISON_EXPRESSION,
    COMPILATION_UNIT,
    COMPOUND_ASSIGNMENT_EXPRESSION,
    CONSTANT_ITEM,
    CONTINUE_EXPRESSION,
    CRATE_REF,
    DEC_DIGIT,
    DEC_LITERAL,
    DELIMITERS,
    DELIM_TOKEN_TREE,
    DEREFERENCE_EXPRESSION,
    DIVISION_EXPRESSION,
    DIVISION_EXPRESSION_TERM,
    ENUMERATION,
    ENUMERATION_VARIANT_EXPRESSION,
    ENUM_EXPR_FIELD,
    ENUM_EXPR_FIELDLESS,
    ENUM_EXPR_FIELDS,
    ENUM_EXPR_STRUCT,
    ENUM_EXPR_TUPLE,
    ENUM_ITEMS,
    ENUM_ITEM,
    ENUM_ITEM_DISCRIMINANT,
    ENUM_ITEM_STRUCT,
    ENUM_ITEM_TUPLE,
    EOF,
    EQ_EXPRESSION,
    ERROR_PROPAGATION_EXPRESSION,
    ERROR_PROPAGATION_EXPRESSION_TERM,
    EXPRESSION,
    EXPRESSION_STATEMENT,
    EXPRESSION_WITHOUT_BLOCK,
    EXPRESSION_WITH_BLOCK,
    EXTERNAL_FUNCTION_ITEM,
    EXTERNAL_ITEM,
    EXTERNAL_STATIC_ITEM,
    EXTERN_BLOCK,
    EXTERN_CRATE,
    FIELD_EXPRESSION,
    FIELD_EXPRESSION_TERM,
    FLOAT_EXPONENT,
    FLOAT_LITERAL,
    FLOAT_SUFFIX,
    FOR_LIFETIMES,
    FUNCTION,
    FUNCTION_PARAM,
    FUNCTION_PARAMETERS,
    FUNCTION_PARAMETERS_MAYBE_NAMED_VARIADIC,
    FUNCTION_QUALIFIERS,
    FUNCTION_RETURN_TYPE,
    GENERICS,
    GENERIC_ARGS,
    GENERIC_ARGS_BINDING,
    GENERIC_ARGS_BINDINGS,
    GENERIC_ARGS_LIFETIMES,
    GENERIC_ARGS_TYPES,
    GENERIC_PARAMS,
    GROUPED_EXPRESSION,
    GROUPED_PATTERN,
    GT_EXPRESSION,
    HEX_DIGIT,
    HEX_LITERAL,
    IDENTIFIER,
    IDENTIFIER_OR_KEYWORD,
    IDENTIFIER_PATTERN,
    IF_EXPRESSION,
    IF_LET_EXPRESSION,
    IMPLEMENTATION,
    IMPL_TRAIT_TYPE,
    IMPL_TRAIT_TYPE_ONE_BOUND,
    INDEX_EXPRESSION,
    INDEX_EXPRESSION_TERM,
    INFERRED_TYPE,
    INFINITE_LOOP_EXPRESSION,
    INHERENT_IMPL,
    INHERENT_IMPL_ITEM,
    INNER_ATTRIBUTE,
    INNER_BLOCK_DOC,
    INNER_LINE_DOC,
    INTEGER_LITERAL,
    INTEGER_SUFFIX,
    ITEM,
    ITERATOR_LOOP_EXPRESSION,
    KEYWORD,
    LAZY_AND,
    LAZY_BOOLEAN_EXPRESSION,
    LAZY_OR,
    LET_STATEMENT,
    LE_EXPRESSION,
    LIFETIME,
    LIFETIMES,
    LIFETIME_BOUNDS,
    LIFETIME_OR_LABEL,
    LIFETIME_PARAM,
    LIFETIME_PARAMS,
    LIFETIME_TOKEN,
    LIFETIME_WHERE_CLAUSE_ITEM,
    LINE_COMMENT,
    LITERALS,
    LITERAL_PATTERN,
    LITERAL_EXPRESSION,
    LOOP_EXPRESSION,
    LOOP_LABEL,
    LT_EXPRESSION, GE_EXPRESSION,
    MACRO_FRAG_SPEC,
    MACRO_INVOCATION,
    MACRO_INVOCATION_SEMI,
    MACRO_ITEM,
    MACRO_MATCH,
    MACRO_MATCHER,
    MACRO_REP_OP,
    MACRO_REP_SEP,
    MACRO_RULE,
    MACRO_RULES,
    MACRO_RULES_DEF,
    MACRO_RULES_DEFINITION,
    MACRO_TRANSCRIBER,
    MATCH_ARM,
    MATCH_ARMS,
    MATCH_ARM_GUARD,
    MATCH_ARM_PATTERNS,
    MATCH_EXPRESSION,
    MAYBE_NAMED_FUNCTION_PARAMETERS,
    MAYBE_NAMED_FUNCTION_PARAMETERS_VARIADIC,
    MAYBE_NAMED_PARAM, STRUCT_FIELDS,
    METHOD,
    METHOD_CALL_EXPRESSION,
    METHOD_CALL_EXPRESSION_TERM,
    MINUSEQ_EXPRESSION,
    MODULE,
    MULTIPLICATION_EXPRESSION,
    MULTIPLICATION_EXPRESSION_TERM,
    NAMED_FUNCTION_PARAM,
    NAMED_FUNCTION_PARAMETERS,
    NAMED_FUNCTION_PARAMETERS_WITH_VARIADICS,
    NEGATION_EXPRESSION,
    NEQ_EXPRESSION,
    NEVER_TYPE,
    NON_KEYWORD_IDENTIFIER,
    NON_ZERO_DEC_DIGIT,
    OBSOLETE_RANGE_PATTERN,
    OCT_DIGIT,
    OCT_LITERAL,
    OPERATOR_EXPRESSION,
    OREQ_EXPRESSION,
    OUTER_ATTRIBUTE,
    OUTER_BLOCK_DOC,
    OUTER_LINE_DOC,
    PARENTHESIZED_TYPE,
    PATH_EXPRESSION,
    PATH_EXPR_SEGMENT,
    PATH_IDENT_SEGMENT,
    PATH_IN_EXPRESSION,
    PATH_PATTERN,
    PATTERN,
    PERCENTEQ_EXPRESSION,
    PLUSEQ_EXPRESSION,
    PREDICATE_LOOP_EXPRESSION,
    PREDICATE_PATTERN_LOOP_EXPRESSION,
    PUNCTUATION,
    PUNCTUATION_EXCEPT_DOLLAR,
    PUNCTUATION_EXCEPT_SEMI,
    QUALIFIED_PATH_IN_EXPRESSION,
    QUALIFIED_PATH_IN_TYPE,
    QUALIFIED_PATH_TYPE,
    QUOTE_ESCAPE,
    RANGE_EXPR,
    RANGE_EXPRESSION,
    RANGE_EXPR_TERM,
    RANGE_FROM_EXPR,
    RANGE_FROM_EXPR_TERM,
    RANGE_FULL_EXPR,
    RANGE_INCLUSIVE_EXPR,
    RANGE_INCLUSIVE_EXPR_TERM,
    RANGE_PATTERN,
    RANGE_PATTERN_BOUND,
    RANGE_TO_EXPR,
    RANGE_TO_INCLUSIVE_EXPR,
    RAW_BYTE_STRING_CONTENT,
    RAW_BYTE_STRING_LITERAL,
    RAW_IDENTIFIER,
    RAW_POINTER_TYPE,
    RAW_STRING_CONTENT,
    RAW_STRING_LITERAL,
    REFERENCE_PATTERN,
    REFERENCE_TYPE,
    REMAINDER_EXPRESSION,
    REMAINDER_EXPRESSION_TERM,
    REST_PATTERN,
    RETURN_EXPRESSION,
    SELF_PARAM,
    SHLEQ_EXPRESSION,
    SHL_EXPRESSION,
    SHL_EXPRESSION_TERM,
    SHORTHAND_SELF,
    SHREQ_EXPRESSION,
    SHR_EXPRESSION,
    SHR_EXPRESSION_TERM,
    SIMPLE_PATH,
    SIMPLE_PATH_SEGMENT,
    SLASHEQ_EXPRESSION,
    SLICE_PATTERN,
    SLICE_TYPE,
    SPC,
    STAREQ_EXPRESSION,
    STATEMENT,
    STATEMENTS,
    STATIC_ITEM,
    STRING_CONTENT,
    STRING_CONTINUE,
    STRING_LITERAL,
    STRUCT,
    STRUCT_BASE,
    STRUCT_EXPRESSION,
    STRUCT_EXPR_FIELD,
    STRUCT_EXPR_FIELDS,
    STRUCT_EXPR_STRUCT,
    STRUCT_EXPR_TUPLE,
    STRUCT_EXPR_UNIT,
    STRUCT_FIELD,
    STRUCT_PATTERN,
    STRUCT_PATTERN_ELEMENTS,
    STRUCT_PATTERN_ETCETERA,
    STRUCT_PATTERN_FIELD,
    STRUCT_PATTERN_FIELDS,
    STRUCT_STRUCT,
    SUBTRACTION_EXPRESSION,
    SUBTRACTION_EXPRESSION_TERM,
    TOKEN,
    TOKEN_EXCEPT_DELIMITERS,
    TOKEN_MACRO,
    TOKEN_TREE,
    TRAIT,
    TRAIT_BOUND,
    TRAIT_CONST,
    TRAIT_FUNC,
    TRAIT_FUNCTION_DECL,
    TRAIT_FUNCTION_PARAM,
    TRAIT_FUNCTION_PARAMETERS,
    TRAIT_IMPL,
    TRAIT_IMPL_ITEM,
    TRAIT_ITEM,
    TRAIT_METHOD,
    TRAIT_METHOD_DECL,
    TRAIT_OBJECT_TYPE,
    TRAIT_OBJECT_TYPE_ONE_BOUND,
    TRAIT_TYPE,
    TUPLE_ELEMENT,
    TUPLE_EXPRESSION,
    TUPLE_FIELD,
    TUPLE_FIELDS,
    TUPLE_INDEX,
    TUPLE_INDEXING_EXPRESSION,
    TUPLE_INDEXING_EXPRESSION_TERM,
    TUPLE_PATTERN,
    TUPLE_PATTERN_ITEMS,
    TUPLE_STRUCT,
    TUPLE_STRUCT_ITEMS,
    TUPLE_STRUCT_PATTERN,
    TUPLE_TYPE,
    TYPE,
    TYPED_SELF,
    TYPE_ALIAS,
    TYPE_BOUND_CLAUSE_ITEM,
    TYPE_CAST_EXPRESSION,
    TYPE_CAST_EXPRESSION_TERM,
    TYPE_NO_BOUNDS,
    TYPE_PARAM,
    TYPE_PARAMS,
    TYPE_PARAM_BOUND,
    TYPE_PARAM_BOUNDS,
    TYPE_PATH,
    TYPE_PATH_FN,
    TYPE_PATH_FN_INPUTS,
    TYPE_PATH_SEGMENT,
    UNICODE_ESCAPE,
    UNION,
    UNKNOWN_CHAR,
    UNSAFE_BLOCK_EXPRESSION,
    USE_DECLARATION,
    USE_TREE,
    VISIBILITY,
    VISIT_ITEM,
    WHERE_CLAUSE,
    WHERE_CLAUSE_ITEM,
    WILDCARD_PATTERN;

    private static final String IDFREGEXP1 = "[a-zA-Z][a-zA-Z0-9_]*";
    private static final String IDFREGEXP2 = "_[a-zA-Z0-9_]+";
    private static final String UNSAFE = "unsafe";
    private static final String CONST = "const";
    private static final String EXTERN = "extern";


    public static LexerlessGrammarBuilder create() {
        LexerlessGrammarBuilder b = LexerlessGrammarBuilder.create();




        b.rule(COMPILATION_UNIT).is(SPC, b.zeroOrMore(STATEMENT,SPC), EOF);

        punctuators(b);
        keywords(b);
        literals(b);
        lexical(b);
        types(b);
        attributes(b);
        expressions(b);
        items(b);
        macros(b);
        patterns(b);
        statement(b);

        b.setRootRule(COMPILATION_UNIT);

        return b;
    }

    private static Object inlineComment(LexerlessGrammarBuilder b) {
        return b.regexp("//[^\\n\\r]*+");
    }

    private static Object multilineComment(LexerlessGrammarBuilder b) {
        return b.regexp("/\\*[\\s\\S]*?\\*\\/");
    }

    private static void literals(LexerlessGrammarBuilder b) {
        b.rule(SPC).is(
                b.skippedTrivia(whitespace(b)),
                b.zeroOrMore(
                        b.commentTrivia(b.firstOf(inlineComment(b), multilineComment(b))),
                        b.skippedTrivia(whitespace(b))));


        b.rule(EOF).is(b.token(GenericTokenType.EOF, b.endOfInput())).skip();

        b.rule(UNKNOWN_CHAR).is(
                b.token(GenericTokenType.UNKNOWN_CHAR, b.regexp("(?s).")),
                SPC).skip();


        b.rule(CHAR_LITERAL).is(b.token(RustTokenType.CHARACTER_LITERAL,
                b.firstOf(b.regexp("^\\'[^\\\r\\n\\t\\'].*\\'"),
                        b.sequence("'", UNICODE_ESCAPE, "'"),
                        b.sequence("'", QUOTE_ESCAPE, "'"),
                        b.sequence("'", ASCII_ESCAPE, "'")))).skip();



        b.rule(STRING_CONTENT).is(b.regexp("(\\\\.|[^\\\\\"])++"));

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




        comments(b);
    }

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
        String[] punctuatorsExceptSemi = RustGrammar.getPunctuatorsExcept(punctuators, ";");

        Arrays.sort(punctuators);
        ArrayUtils.reverse(punctuators);
        b.rule(PUNCTUATION).is(
                b.firstOf(
                        punctuators[0],
                        punctuators[1],
                        ArrayUtils.subarray(punctuators, 2, punctuators.length)));
        Arrays.sort(punctuatorsExceptDollar);
        ArrayUtils.reverse(punctuatorsExceptDollar);
        Arrays.sort(punctuatorsExceptSemi);
        ArrayUtils.reverse(punctuatorsExceptSemi);
        b.rule(PUNCTUATION_EXCEPT_DOLLAR).is(
                b.firstOf(
                        punctuatorsExceptDollar[0],
                        punctuatorsExceptDollar[1],
                        ArrayUtils.subarray(punctuatorsExceptDollar, 2, punctuatorsExceptDollar.length)));
        b.rule(PUNCTUATION_EXCEPT_SEMI).is(
                b.firstOf(
                        punctuatorsExceptSemi[0],
                        punctuatorsExceptSemi[1],
                        ArrayUtils.subarray(punctuatorsExceptSemi, 2, punctuatorsExceptSemi.length)));
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


    /* recurring grammar pattern */
    private static Object seq(LexerlessGrammarBuilder b, GrammarRuleKey g, RustPunctuator sep) {
        return b.sequence(g, b.sequence(b.zeroOrMore(SPC, sep, SPC, g),
                b.optional(SPC, sep, SPC)));
    }

    private static void items(LexerlessGrammarBuilder b) {
        b.rule(ITEM).is(b.zeroOrMore(OUTER_ATTRIBUTE, SPC),
                b.firstOf(VISIT_ITEM, MACRO_ITEM));
        b.rule(VISIT_ITEM).is(b.optional(VISIBILITY), b.firstOf(
                MODULE,
                EXTERN_CRATE,
                USE_DECLARATION,
                FUNCTION,
                TYPE_ALIAS,
                STRUCT,
                ENUMERATION,
                UNION,
                CONSTANT_ITEM,
                STATIC_ITEM,
                TRAIT,
                IMPLEMENTATION,
                EXTERN_BLOCK
        ));
        b.rule(MACRO_ITEM).is(b.firstOf(MACRO_INVOCATION_SEMI, MACRO_RULES_DEFINITION));
        modules(b);
        externcrates(b);
        useItem(b);
        aliasItem(b);
        functionsItem(b);
        structsItem(b);
        enumerationsItem(b);
        unionsItem(b);
        constantsItem(b);
        staticItem(b);
        traitsItem(b);
        implItem(b);
        extblocksItem(b);
        genericItem(b);
        assocItem(b);
        visibilityItem(b);
    }

    /* https://doc.rust-lang.org/reference/items/traits.html */
    private static void traitsItem(LexerlessGrammarBuilder b) {
        b.rule(TRAIT).is(
                b.optional(UNSAFE, SPC),
                RustKeyword.KW_TRAIT, SPC, IDENTIFIER, SPC,
                b.optional(GENERICS, SPC),
                b.optional(RustPunctuator.COLON, b.optional(TYPE_PARAM_BOUNDS)),
                b.optional(WHERE_CLAUSE), "{", b.zeroOrMore(TRAIT_ITEM, SPC), "}"
        );
        b.rule(TRAIT_ITEM).is(
                b.zeroOrMore(OUTER_ATTRIBUTE, SPC), b.optional(VISIBILITY, SPC),
                b.firstOf(TRAIT_FUNC, TRAIT_METHOD, TRAIT_CONST, TRAIT_TYPE)
        );
        b.rule(TRAIT_FUNC).is(
                TRAIT_FUNCTION_DECL, b.firstOf(";", BLOCK_EXPRESSION)
        );
        b.rule(TRAIT_METHOD).is(
                TRAIT_METHOD_DECL, b.firstOf(";", BLOCK_EXPRESSION)
        );
        b.rule(TRAIT_FUNCTION_DECL).is(
                FUNCTION_QUALIFIERS, SPC, RustKeyword.KW_FN, SPC, IDENTIFIER, SPC, b.optional(GENERICS, SPC),
                "(", b.optional(SPC, TRAIT_FUNCTION_PARAMETERS, SPC), ")", SPC,
                b.optional(FUNCTION_RETURN_TYPE, SPC), b.optional(WHERE_CLAUSE, SPC)
        );
        b.rule(TRAIT_METHOD_DECL).is(
                FUNCTION_QUALIFIERS, SPC, RustKeyword.KW_FN, SPC, IDENTIFIER, SPC, b.optional(GENERICS, SPC),
                "(", SPC, SELF_PARAM, SPC, b.zeroOrMore(b.sequence(RustPunctuator.COMMA, SPC, TRAIT_FUNCTION_PARAM, SPC)),
                b.optional(RustPunctuator.COMMA, SPC), ")",
                b.optional(FUNCTION_RETURN_TYPE, SPC), b.optional(WHERE_CLAUSE, SPC)
        );
        b.rule(TRAIT_FUNCTION_PARAMETERS).is(seq(b, TRAIT_FUNCTION_PARAM, RustPunctuator.COMMA));
        b.rule(TRAIT_FUNCTION_PARAM).is(
                b.zeroOrMore(OUTER_ATTRIBUTE, SPC), b.optional(PATTERN, SPC, RustPunctuator.COLON), TYPE
        );
        b.rule(TRAIT_CONST).is(
                RustKeyword.KW_CONST, SPC, IDENTIFIER, SPC, RustPunctuator.COLON, SPC, TYPE, SPC,
                b.optional(b.sequence(RustPunctuator.EQ, SPC, EXPRESSION)), RustPunctuator.SEMI
        );
        b.rule(TRAIT_TYPE).is(
                RustKeyword.KW_TYPE, SPC, IDENTIFIER, b.optional(SPC, RustPunctuator.COLON, SPC, b.optional(TYPE_PARAM_BOUNDS)), RustPunctuator.SEMI
        );
    }

    /* https://doc.rust-lang.org/reference/items/enumerations.html */
    private static void enumerationsItem(LexerlessGrammarBuilder b) {
        b.rule(ENUMERATION).is(RustKeyword.KW_ENUM, SPC, IDENTIFIER, SPC,
                b.optional(GENERICS, SPC), b.optional(WHERE_CLAUSE, SPC), "{", SPC, ENUM_ITEMS, SPC, "}");
        b.rule(ENUM_ITEMS).is(seq(b, ENUM_ITEM, RustPunctuator.COMMA));
        b.rule(ENUM_ITEM).is(SPC, b.zeroOrMore(OUTER_ATTRIBUTE, SPC),  b.optional(VISIBILITY, SPC),
                IDENTIFIER,SPC, b.optional(b.firstOf(ENUM_ITEM_TUPLE, ENUM_ITEM_STRUCT, ENUM_ITEM_DISCRIMINANT))
        );
        b.rule(ENUM_ITEM_TUPLE).is("(", b.optional(SPC, TUPLE_FIELDS), ")");
        b.rule(ENUM_ITEM_STRUCT).is("{", b.optional(SPC, STRUCT_FIELDS), "}");
        b.rule(ENUM_ITEM_DISCRIMINANT).is(SPC, RustPunctuator.EQ, SPC, EXPRESSION);
    }

    /* https://doc.rust-lang.org/reference/items/type-aliases.html */
    private static void aliasItem(LexerlessGrammarBuilder b) {
        b.rule(TYPE_ALIAS).is(
                RustKeyword.KW_TYPE, SPC, IDENTIFIER, SPC, b.optional(GENERICS), b.optional(WHERE_CLAUSE),
                RustPunctuator.EQ, SPC, TYPE, ";"
        );
    }

    private static void useItem(LexerlessGrammarBuilder b) {
        b.rule(USE_DECLARATION).is("use", SPC, USE_TREE, ";");
        /*
        b.rule(USE_TREE).is(b.firstOf(
                b.sequence(b.optional(b.sequence(b.optional(SIMPLE_PATH), RustPunctuator.PATHSEP)),
                        RustPunctuator.STAR),
                b.sequence(b.optional(b.sequence(b.optional(SIMPLE_PATH), RustPunctuator.PATHSEP)),
                        "{",
                        b.optional(USE_TREE, b.zeroOrMore(b.sequence(RustPunctuator.COMMA, SPC, USE_TREE), b.optional(RustPunctuator.COMMA))),
                        "}"
                ),
                b.sequence(SIMPLE_PATH, b.optional(b.sequence(SPC, RustKeyword.KW_AS, SPC,
                        b.firstOf(IDENTIFIER, RustPunctuator.UNDERSCORE)
                )))
        ));

         */
        b.rule(USE_TREE).is(b.firstOf(
            b.sequence(b.optional(b.optional(SIMPLE_PATH), RustPunctuator.PATHSEP), RustPunctuator.STAR),
                b.sequence(b.optional(b.optional(SIMPLE_PATH), RustPunctuator.PATHSEP),
                "{", SPC, b.optional(seq(b, USE_TREE, RustPunctuator.COMMA )),SPC,  "}"
                        )  ,
                b.sequence(SIMPLE_PATH, b.optional(
                        RustKeyword.KW_AS, b.firstOf(IDENTIFIER, RustPunctuator.UNDERSCORE)
                ))
        ));

    }

    private static void functionsItem(LexerlessGrammarBuilder b) {
        b.rule(FUNCTION).is(
                FUNCTION_QUALIFIERS, SPC, RustKeyword.KW_FN, SPC, IDENTIFIER,
                b.optional(GENERICS, SPC), SPC,
                "(", SPC, b.optional(FUNCTION_PARAMETERS, SPC), SPC, ")",
                b.optional(SPC, FUNCTION_RETURN_TYPE, SPC), b.optional(WHERE_CLAUSE, SPC), SPC,
                BLOCK_EXPRESSION
        );
        b.rule(FUNCTION_QUALIFIERS).is(
                b.optional(ASYNC_CONST_QUALIFIERS),
                SPC,
                b.optional(UNSAFE),
                SPC,
                b.optional(EXTERN, SPC, b.optional(ABI))
        );
        b.rule(ASYNC_CONST_QUALIFIERS).is(b.firstOf(CONST, "async"));
        b.rule(ABI).is(b.firstOf(STRING_LITERAL, RAW_STRING_LITERAL));
        b.rule(FUNCTION_PARAMETERS).is(FUNCTION_PARAM,
                b.zeroOrMore(SPC, RustPunctuator.COMMA, SPC, FUNCTION_PARAM),
                b.optional(RustPunctuator.COMMA, SPC));


        b.rule(FUNCTION_PARAM).is(
                b.zeroOrMore(OUTER_ATTRIBUTE), PATTERN, SPC, RustPunctuator.COLON, SPC, TYPE
        );
        b.rule(FUNCTION_RETURN_TYPE).is(RustPunctuator.RARROW, SPC, TYPE);

    }

    /* https://doc.rust-lang.org/reference/items/structs.html */
    private static void structsItem(LexerlessGrammarBuilder b) {
        b.rule(STRUCT).is(b.firstOf(STRUCT_STRUCT, TUPLE_STRUCT));
        b.rule(STRUCT_STRUCT).is(
                RustKeyword.KW_STRUCT, SPC, IDENTIFIER, SPC, b.optional(GENERICS, SPC), b.optional(WHERE_CLAUSE, SPC),
                b.firstOf(b.sequence("{", SPC, b.optional(STRUCT_FIELDS, SPC), "}"), RustPunctuator.SEMI));
        b.rule(TUPLE_STRUCT).is(
                RustKeyword.KW_STRUCT, SPC, IDENTIFIER, b.optional(GENERICS), "(",
                b.optional(TUPLE_FIELDS), ")",
                b.optional(GENERICS), RustPunctuator.SEMI
        );
        b.rule(STRUCT_FIELDS).is(seq(b, STRUCT_FIELD, RustPunctuator.COMMA));
        b.rule(STRUCT_FIELD).is(
                b.zeroOrMore(OUTER_ATTRIBUTE),
                b.optional(VISIBILITY),
                IDENTIFIER, SPC, RustPunctuator.COLON, SPC, TYPE
        );
        b.rule(TUPLE_FIELDS).is(seq(b, TUPLE_FIELD, RustPunctuator.COMMA));
        b.rule(TUPLE_FIELD).is(
                b.zeroOrMore(OUTER_ATTRIBUTE),
                b.optional(VISIBILITY), TYPE
        );

    }

    /* https://doc.rust-lang.org/reference/items/unions.html */
    private static void unionsItem(LexerlessGrammarBuilder b) {
        b.rule(UNION).is(
                RustKeyword.KW_UNION, SPC, IDENTIFIER, SPC, b.optional(GENERICS, SPC),
                b.optional(WHERE_CLAUSE, SPC),
                "{", SPC, STRUCT_FIELDS, SPC, "}"
        );
    }

    /* https://doc.rust-lang.org/reference/items/constant-items.html */
    private static void constantsItem(LexerlessGrammarBuilder b) {
        b.rule(CONSTANT_ITEM).is(
                RustKeyword.KW_CONST, SPC, b.firstOf(IDENTIFIER, RustPunctuator.UNDERSCORE),
                RustPunctuator.COLON, SPC, TYPE,
                SPC, RustPunctuator.EQ, SPC, EXPRESSION, RustPunctuator.SEMI
        );

    }

    /* https://doc.rust-lang.org/reference/items/static-items.html */
    private static void staticItem(LexerlessGrammarBuilder b) {
        b.rule(STATIC_ITEM).is(
                RustKeyword.KW_STATIC, SPC, b.optional(RustKeyword.KW_MUT, SPC), IDENTIFIER,
                SPC, RustPunctuator.COLON, SPC, TYPE, SPC, RustPunctuator.EQ, SPC, EXPRESSION, ";"
        );
    }

    /* https://doc.rust-lang.org/reference/items/implementations.html */
    private static void implItem(LexerlessGrammarBuilder b) {
        b.rule(IMPLEMENTATION).is(b.firstOf(INHERENT_IMPL, TRAIT_IMPL));
        b.rule(INHERENT_IMPL).is(
                RustKeyword.KW_IMPL, SPC, b.optional(GENERICS, SPC), TYPE, SPC,
                b.optional(WHERE_CLAUSE, SPC), "{", SPC,
                b.zeroOrMore(INNER_ATTRIBUTE),
                b.zeroOrMore(INHERENT_IMPL_ITEM),SPC,  "}"
        );
        b.rule(INHERENT_IMPL_ITEM).is(
                SPC,
                b.zeroOrMore(OUTER_ATTRIBUTE, SPC),
                b.firstOf(MACRO_INVOCATION_SEMI,
                        b.sequence(b.optional(VISIBILITY, SPC), b.firstOf(
                                CONSTANT_ITEM, FUNCTION, METHOD
                        ))), SPC);


        b.rule(TRAIT_IMPL).is(
                b.optional(UNSAFE, SPC), RustKeyword.KW_IMPL, SPC, b.optional(GENERICS, SPC),
                b.optional(RustPunctuator.NOT), TYPE_PATH, SPC, RustKeyword.KW_FOR, SPC, TYPE,
                b.optional(WHERE_CLAUSE, SPC), "{",SPC,
                b.zeroOrMore(INNER_ATTRIBUTE, SPC), b.zeroOrMore(TRAIT_IMPL_ITEM, SPC), SPC, "}"
        );


        b.rule(TRAIT_IMPL_ITEM).is(
                b.zeroOrMore(OUTER_ATTRIBUTE, SPC),
                b.firstOf(MACRO_INVOCATION_SEMI,
                        b.sequence(b.optional(VISIBILITY), b.firstOf(TYPE_ALIAS, CONSTANT_ITEM, FUNCTION, METHOD))
                )
        );

    }

    /* https://doc.rust-lang.org/reference/items/external-blocks.html */
    private static void extblocksItem(LexerlessGrammarBuilder b) {
        b.rule(EXTERN_BLOCK).is(
                EXTERN, SPC, b.optional(ABI, SPC), "{", SPC,
                b.zeroOrMore(INNER_ATTRIBUTE, SPC),
                b.zeroOrMore(EXTERNAL_ITEM, SPC), "}"
        );
        b.rule(EXTERNAL_ITEM).is(
                b.zeroOrMore(OUTER_ATTRIBUTE, SPC), SPC,
                b.firstOf(MACRO_INVOCATION_SEMI,
                        b.sequence(
                                b.optional(VISIBILITY, SPC),
                                b.firstOf(EXTERNAL_STATIC_ITEM, EXTERNAL_FUNCTION_ITEM)
                        )));
        b.rule(EXTERNAL_STATIC_ITEM).is(
                RustKeyword.KW_STATIC, SPC, b.optional(RustKeyword.KW_MUT, SPC),
                IDENTIFIER, SPC, RustPunctuator.COLON, SPC, TYPE, RustPunctuator.SEMI
        );
        b.rule(EXTERNAL_FUNCTION_ITEM).is(
                RustKeyword.KW_FN, SPC, IDENTIFIER, SPC, b.optional(GENERICS, SPC), "(", SPC,
                b.optional(b.firstOf(NAMED_FUNCTION_PARAMETERS_WITH_VARIADICS, NAMED_FUNCTION_PARAMETERS)),
                ")", SPC, b.optional(FUNCTION_RETURN_TYPE, SPC), b.optional(WHERE_CLAUSE, SPC), SPC, RustPunctuator.SEMI
        );
        b.rule(NAMED_FUNCTION_PARAMETERS).is(seq(b, NAMED_FUNCTION_PARAM, RustPunctuator.COMMA));
        b.rule(NAMED_FUNCTION_PARAM).is(
                b.zeroOrMore(OUTER_ATTRIBUTE, SPC),
                b.firstOf(IDENTIFIER, RustPunctuator.UNDERSCORE), SPC,
                RustPunctuator.COLON, SPC, TYPE
        );

        b.rule(NAMED_FUNCTION_PARAMETERS_WITH_VARIADICS).is(
                b.oneOrMore(NAMED_FUNCTION_PARAM, SPC, RustPunctuator.COMMA, SPC),
                b.zeroOrMore(OUTER_ATTRIBUTE, SPC),
                RustPunctuator.DOTDOTDOT
        );
    }


    /* https://doc.rust-lang.org/reference/items/generics.html */
    private static void genericItem(LexerlessGrammarBuilder b) {
        b.rule(GENERICS).is("<", GENERIC_PARAMS, ">");


        b.rule(GENERIC_PARAMS).is(b.firstOf(
                b.sequence(b.zeroOrMore(LIFETIME_PARAM, SPC, RustPunctuator.COMMA, SPC), LIFETIME_PARAM, SPC),
                b.sequence(b.zeroOrMore(LIFETIME_PARAM, SPC, RustPunctuator.COMMA,SPC), TYPE_PARAMS)

        ));


        b.rule(LIFETIME_PARAMS).is(
                b.zeroOrMore(LIFETIME_PARAM, RustPunctuator.COMMA, SPC), b.optional(LIFETIME_PARAM, SPC)
        );
        b.rule(LIFETIME_PARAM).is(
                b.optional(OUTER_ATTRIBUTE, SPC), LIFETIME_OR_LABEL, SPC, b.optional(RustPunctuator.COLON, LIFETIME_BOUNDS, SPC)
        );
        b.rule(TYPE_PARAMS).is(
                b.zeroOrMore(TYPE_PARAM, RustPunctuator.COMMA, SPC), b.optional(TYPE_PARAM, SPC)
        );
        b.rule(TYPE_PARAM).is(
                b.optional(OUTER_ATTRIBUTE, SPC), IDENTIFIER, SPC,
                b.optional(RustPunctuator.COLON, b.optional(TYPE_PARAM_BOUNDS),
                        b.optional(RustPunctuator.EQ, TYPE))
        );
        b.rule(WHERE_CLAUSE).is(
                RustKeyword.KW_WHERE, b.zeroOrMore(b.sequence(WHERE_CLAUSE_ITEM, RustPunctuator.COMMA)), b.optional(WHERE_CLAUSE_ITEM)
        );
        b.rule(WHERE_CLAUSE_ITEM).is(b.firstOf(
                LIFETIME_WHERE_CLAUSE_ITEM
                , TYPE_BOUND_CLAUSE_ITEM));
        b.rule(LIFETIME_WHERE_CLAUSE_ITEM).is(LIFETIME, SPC, RustPunctuator.COLON, SPC, LIFETIME_BOUNDS);
        b.rule(TYPE_BOUND_CLAUSE_ITEM).is(
                b.optional(FOR_LIFETIMES), TYPE, RustPunctuator.COLON, b.optional(TYPE_PARAM_BOUNDS)
        );

        b.rule(FOR_LIFETIMES).is(RustKeyword.KW_FOR, SPC, RustPunctuator.LT, LIFETIME_PARAMS, RustPunctuator.GT);


    }

    private static void assocItem(LexerlessGrammarBuilder b) {
        b.rule(METHOD).is(
                FUNCTION_QUALIFIERS, SPC, RustKeyword.KW_FN, SPC, IDENTIFIER,
                b.optional(GENERICS, SPC),
                "(", SELF_PARAM, SPC, b.optional(b.sequence(SPC, RustPunctuator.COMMA, SPC, FUNCTION_PARAM)),
                b.optional(SPC, RustPunctuator.COMMA), ")", SPC,
                b.optional(FUNCTION_RETURN_TYPE, SPC), b.optional(WHERE_CLAUSE, SPC),SPC,
                BLOCK_EXPRESSION
        );

        b.rule(SELF_PARAM).is(b.zeroOrMore(OUTER_ATTRIBUTE, SPC), b.firstOf(
                TYPED_SELF, SHORTHAND_SELF
        ));


        b.rule(SHORTHAND_SELF).is(
                b.optional(b.firstOf(b.sequence(RustPunctuator.AND, LIFETIME, SPC), RustPunctuator.AND)),
                b.optional(RustKeyword.KW_MUT, SPC), RustKeyword.KW_SELFVALUE
        );


        b.rule(TYPED_SELF).is(b.optional(RustKeyword.KW_MUT, SPC), RustKeyword.KW_SELFVALUE, SPC, RustPunctuator.COLON, SPC, TYPE);

    }

    private static void visibilityItem(LexerlessGrammarBuilder b) {
        b.rule(VISIBILITY).is(b.firstOf(
                b.sequence(RustKeyword.KW_PUB, SPC, "(", SPC, RustKeyword.KW_CRATE, SPC, ")"),
                b.sequence(RustKeyword.KW_PUB, SPC, "(", SPC, RustKeyword.KW_SELFVALUE, SPC, ")"),
                b.sequence(RustKeyword.KW_PUB, SPC, "(", SPC, RustKeyword.KW_SUPER, SPC, ")"),
                b.sequence(RustKeyword.KW_PUB, SPC, "(", SPC, RustKeyword.KW_IN, SIMPLE_PATH, SPC, ")"),
                RustKeyword.KW_PUB

        ));
    }


    private static void externcrates(LexerlessGrammarBuilder b) {
        b.rule(EXTERN_CRATE).is(
                EXTERN, SPC, "crate", SPC, CRATE_REF, b.optional(SPC, AS_CLAUSE), RustPunctuator.SEMI
        );
        b.rule(CRATE_REF).is(b.firstOf(RustKeyword.KW_SELFVALUE, IDENTIFIER));
        b.rule(AS_CLAUSE).is(RustKeyword.KW_AS, SPC, b.firstOf(RustPunctuator.UNDERSCORE, IDENTIFIER));

    }

    private static void modules(LexerlessGrammarBuilder b) {
        b.rule(MODULE).is(b.firstOf(
                b.sequence("mod", SPC, IDENTIFIER, SPC, RustPunctuator.SEMI),
                b.sequence("mod", SPC, IDENTIFIER, SPC, "{", SPC,
                        b.zeroOrMore(INNER_ATTRIBUTE, SPC),
                        b.zeroOrMore(ITEM, SPC), "}"
                )));

    }

    private static void lexical(LexerlessGrammarBuilder b) {
        //not explicit in reference


        lexicalpath(b);
        lexicaltoken(b);
    }

    /* https://doc.rust-lang.org/reference/macros.html */
    private static void macros(LexerlessGrammarBuilder b) {
        b.rule(MACRO_INVOCATION).is(
                SIMPLE_PATH, RustPunctuator.NOT, DELIM_TOKEN_TREE
        );

        b.rule(DELIM_TOKEN_TREE).is(b.firstOf(
                b.sequence("(", SPC, b.zeroOrMore(TOKEN_TREE,SPC), SPC, ")"),
                b.sequence("[", SPC, b.zeroOrMore(TOKEN_TREE,SPC), SPC, "]"),
                b.sequence("{", SPC, b.zeroOrMore(TOKEN_TREE,SPC), SPC, "}")));

        b.rule(TOKEN_EXCEPT_DELIMITERS).is(b.firstOf(
                IDENTIFIER_OR_KEYWORD, LITERALS, LIFETIMES, PUNCTUATION
        ));
        b.rule(TOKEN_TREE).is(
                b.firstOf(
                        TOKEN_EXCEPT_DELIMITERS,
                        DELIM_TOKEN_TREE
                ));
        b.rule(MACRO_INVOCATION_SEMI).is(b.firstOf(
                b.sequence(SIMPLE_PATH, RustPunctuator.NOT, "(", b.zeroOrMore(SPC, TOKEN_TREE,SPC), ");"),
                b.sequence(SIMPLE_PATH, RustPunctuator.NOT, "[", b.zeroOrMore(SPC, TOKEN_TREE,SPC), "];"),
                b.sequence(SIMPLE_PATH, RustPunctuator.NOT, "{", b.zeroOrMore(SPC, TOKEN_TREE,SPC), "}")
        ));
        macrosByExample(b);
    }

    /* https://doc.rust-lang.org/reference/macros-by-example.html */
    private static void macrosByExample(LexerlessGrammarBuilder b) {
        b.rule(MACRO_RULES_DEFINITION).is(
                "macro_rules!", SPC, IDENTIFIER, SPC, MACRO_RULES_DEF
        );
        b.rule(MACRO_RULES_DEF).is(b.firstOf(
                b.sequence("(", SPC, MACRO_RULES, SPC, ")", RustPunctuator.SEMI),
                b.sequence("[", SPC, MACRO_RULES, SPC, "]", RustPunctuator.SEMI),
                b.sequence("{", SPC, MACRO_RULES, SPC, "}")
        ));
        b.rule(MACRO_RULES).is(
                MACRO_RULE, b.zeroOrMore(b.sequence(RustPunctuator.COMMA, MACRO_RULE)), b.optional(RustPunctuator.COMMA)
        );
        b.rule(MACRO_RULE).is(MACRO_MATCHER, SPC, "=>", SPC, MACRO_TRANSCRIBER);
        b.rule(MACRO_MATCHER).is(b.firstOf(
                b.sequence("(", SPC, MACRO_MATCH, SPC, ")"),
                b.sequence("[", SPC, MACRO_MATCH, SPC, "]"),
                b.sequence("{", SPC, MACRO_MATCH, SPC, "}")
        ));


        b.rule(MACRO_MATCH).is(b.firstOf(

                b.sequence("$", IDENTIFIER, SPC, RustPunctuator.COLON, SPC, MACRO_FRAG_SPEC),

                b.sequence("$(", b.oneOrMore(MACRO_MATCH, SPC), b.firstOf(")+", ")*", ")?")),
                TOKEN_MACRO,
                MACRO_MATCHER
        ));

        b.rule(TOKEN_MACRO).is(b.firstOf(LITERALS, IDENTIFIER_OR_KEYWORD,
                LIFETIMES, PUNCTUATION_EXCEPT_DOLLAR));
        b.rule(MACRO_FRAG_SPEC).is(b.firstOf(
                "block", "expr", "ident", "item", "lifetime", "literal"
                , "meta", "path", "pat", "stmt", "tt", "ty", "vis"
        ));
        b.rule(MACRO_REP_SEP).is(TOKEN); //except $ and delimiters
        b.rule(MACRO_REP_OP).is(b.firstOf(RustPunctuator.STAR, RustPunctuator.PLUS, RustPunctuator.QUESTION));
        b.rule(MACRO_TRANSCRIBER).is(DELIM_TOKEN_TREE);


    }

    private static void patterns(LexerlessGrammarBuilder b) {
        b.rule(PATTERN).is(b.firstOf(
                RANGE_PATTERN,
                TUPLE_STRUCT_PATTERN,
                STRUCT_PATTERN,
                MACRO_INVOCATION,
                IDENTIFIER_PATTERN,
                WILDCARD_PATTERN,
                REST_PATTERN,
                OBSOLETE_RANGE_PATTERN,
                REFERENCE_PATTERN,
                TUPLE_PATTERN,
                GROUPED_PATTERN,
                SLICE_PATTERN,
                PATH_PATTERN,
                LITERAL_PATTERN
        ));
        b.rule(LITERAL_PATTERN).is(b.firstOf(
                BOOLEAN_LITERAL,
                CHAR_LITERAL,
                BYTE_LITERAL,
                STRING_LITERAL,
                RAW_STRING_LITERAL,
                BYTE_STRING_LITERAL,
                RAW_BYTE_STRING_LITERAL,
                b.sequence(b.optional("-"), INTEGER_LITERAL),
                b.sequence(b.optional("-"), FLOAT_LITERAL)
                )
        );
        b.rule(IDENTIFIER_PATTERN).is(
                b.optional("ref",SPC),
                b.optional(RustKeyword.KW_MUT,SPC),
                IDENTIFIER,SPC,
                b.optional(b.sequence("@",SPC, PATTERN))
        );
        b.rule(WILDCARD_PATTERN).is(RustPunctuator.UNDERSCORE);
        b.rule(REST_PATTERN).is(RustPunctuator.DOTDOT);

        b.rule(OBSOLETE_RANGE_PATTERN).is(b.sequence(RANGE_PATTERN_BOUND, RustPunctuator.DOTDOTDOT, RANGE_PATTERN_BOUND));
        b.rule(RANGE_PATTERN).is(b.sequence(RANGE_PATTERN_BOUND, RustPunctuator.DOTDOTEQ, RANGE_PATTERN_BOUND));
        b.rule(RANGE_PATTERN_BOUND).is(b.firstOf(
                CHAR_LITERAL, BYTE_LITERAL, b.sequence(b.optional("-"), INTEGER_LITERAL),
                b.sequence(b.optional("-"), FLOAT_LITERAL),
                PATH_IN_EXPRESSION, QUALIFIED_PATH_IN_EXPRESSION
        ));

        b.rule(REFERENCE_PATTERN).is(
                b.firstOf(RustPunctuator.AND, RustPunctuator.ANDAND),
                b.optional(RustKeyword.KW_MUT),
                PATTERN
        );
        b.rule(STRUCT_PATTERN).is(
                PATH_IN_EXPRESSION, "{", SPC, b.optional(STRUCT_PATTERN_ELEMENTS),SPC,  "}"
        );
        b.rule(STRUCT_PATTERN_ELEMENTS).is(b.firstOf(
                b.sequence(STRUCT_PATTERN_FIELDS,
                        b.optional(b.firstOf(
                                "'",
                                b.sequence(RustPunctuator.COMMA, STRUCT_PATTERN_ETCETERA)
                        ))), STRUCT_PATTERN_ETCETERA

        ));
        b.rule(STRUCT_PATTERN_FIELDS).is(
                STRUCT_PATTERN_FIELD, b.zeroOrMore(b.sequence(RustPunctuator.COMMA, STRUCT_PATTERN_FIELD))
        );
        b.rule(STRUCT_PATTERN_FIELD).is(
                b.zeroOrMore(OUTER_ATTRIBUTE), "(",
                b.firstOf(
                        b.sequence(TUPLE_INDEX, RustPunctuator.COLON, PATTERN),
                        b.sequence(IDENTIFIER, RustPunctuator.COLON, PATTERN),
                        b.sequence(b.optional(RustKeyword.KW_REF), SPC, b.optional(RustKeyword.KW_MUT),SPC,  IDENTIFIER)
                ), ")");
        b.rule(STRUCT_PATTERN_ETCETERA).is(b.zeroOrMore(OUTER_ATTRIBUTE), "..");

        b.rule(TUPLE_STRUCT_PATTERN).is(
                PATH_IN_EXPRESSION, "(", b.optional(TUPLE_STRUCT_ITEMS), ")"
        );
        b.rule(TUPLE_STRUCT_ITEMS).is(seq(b, PATTERN, RustPunctuator.COMMA));

        b.rule(TUPLE_PATTERN).is("(", b.optional(TUPLE_PATTERN_ITEMS), ")");


        b.rule(TUPLE_PATTERN_ITEMS).is(b.firstOf(
                seq(b, PATTERN, RustPunctuator.COMMA),
                b.sequence(PATTERN, SPC, RustPunctuator.COMMA, SPC),
                REST_PATTERN

        ));

        b.rule(GROUPED_PATTERN).is("(", SPC,PATTERN,SPC,  ")");
        b.rule(SLICE_PATTERN).is("[", SPC,PATTERN,SPC,
                b.zeroOrMore(b.sequence(RustPunctuator.COMMA, SPC, PATTERN)), b.optional(RustPunctuator.COMMA), SPC, "]"
        );
        b.rule(PATH_PATTERN).is(b.firstOf(PATH_IN_EXPRESSION, QUALIFIED_PATH_IN_EXPRESSION));
    }

    private static void types(LexerlessGrammarBuilder b) {
        //1
        type(b);
        //5
        tupletype(b);
        //13
        pointer(b);
        //14
        functionpointer(b);
        //15
        trait(b);


    }

    /* https://doc.rust-lang.org/reference/types/function-pointer.html */
    private static void functionpointer(LexerlessGrammarBuilder b) {
        b.rule(BARE_FUNCTION_TYPE).is(
                b.optional(FOR_LIFETIMES), FUNCTION_QUALIFIERS, RustKeyword.KW_FN,
                "(", b.optional(FUNCTION_PARAMETERS_MAYBE_NAMED_VARIADIC), ")",
                b.optional(BARE_FUNCTION_RETURN_TYPE)
        );
        b.rule(BARE_FUNCTION_RETURN_TYPE).is("->", TYPE_NO_BOUNDS);
        b.rule(FUNCTION_PARAMETERS_MAYBE_NAMED_VARIADIC).is(b.firstOf(
                MAYBE_NAMED_FUNCTION_PARAMETERS, MAYBE_NAMED_FUNCTION_PARAMETERS_VARIADIC
        ));
        b.rule(MAYBE_NAMED_FUNCTION_PARAMETERS).is(seq(b, MAYBE_NAMED_PARAM, RustPunctuator.COMMA));
        b.rule(MAYBE_NAMED_PARAM).is(
                b.zeroOrMore(OUTER_ATTRIBUTE),
                b.optional(b.sequence(
                        b.firstOf(IDENTIFIER, RustPunctuator.UNDERSCORE), RustPunctuator.COLON
                )), TYPE
        );
        b.rule(MAYBE_NAMED_FUNCTION_PARAMETERS_VARIADIC).is(
                b.zeroOrMore(b.sequence(MAYBE_NAMED_PARAM, RustPunctuator.COMMA)),
                MAYBE_NAMED_PARAM, RustPunctuator.COMMA, b.zeroOrMore(OUTER_ATTRIBUTE), "..."
        );


    }

    public static void statement(LexerlessGrammarBuilder b) {
        b.rule(STATEMENT).is(b.firstOf(
                RustPunctuator.SEMI,
                ITEM,
                LET_STATEMENT,
                EXPRESSION_STATEMENT,
                MACRO_INVOCATION_SEMI
        ));
        b.rule(LET_STATEMENT).is(
                b.zeroOrMore(OUTER_ATTRIBUTE, SPC),
                RustKeyword.KW_LET, SPC, PATTERN, SPC,
                b.optional(RustPunctuator.COLON, SPC, TYPE, SPC),
                b.optional(RustPunctuator.EQ, SPC, EXPRESSION, SPC),
                RustPunctuator.SEMI);

        b.rule(EXPRESSION_STATEMENT).is(b.firstOf(
                b.sequence(EXPRESSION_WITHOUT_BLOCK, SPC, RustPunctuator.SEMI),
                b.sequence(EXPRESSION_WITH_BLOCK, b.optional(SPC, RustPunctuator.SEMI))

        ));


        b.rule(ANY_TOKEN).is(
                b.firstOf(
                        DELIMITERS,
                        LITERAL_EXPRESSION,
                        IDENTIFIER,
                        PUNCTUATION_EXCEPT_SEMI,
                        LIFETIME_TOKEN
                ));
    }

    /* https://doc.rust-lang.org/reference/expressions.html */
    public static void expressions(LexerlessGrammarBuilder b) {
        literal(b);
        path(b);
        block(b);
        operator(b);
        grouped(b);
        array(b);
        tuple(b);
        struct(b);
        enums(b);
        call(b);
        methodcall(b);
        field(b);
        closure(b);
        loops(b);
        range(b);
        ifExpr(b);
        match(b);
        returnExpr(b);
        await(b);
        b.rule(EXPRESSION).is(b.firstOf(EXPRESSION_WITHOUT_BLOCK, EXPRESSION_WITH_BLOCK));
        b.rule(EXPRESSION_WITHOUT_BLOCK).is(b.zeroOrMore(OUTER_ATTRIBUTE),
                b.firstOf(
                        RANGE_EXPRESSION,
                        OPERATOR_EXPRESSION,
                        METHOD_CALL_EXPRESSION,
                        CALL_EXPRESSION,
                        MACRO_INVOCATION,
                        FIELD_EXPRESSION,
                        LITERAL_EXPRESSION,
                        STRUCT_EXPRESSION,
                        PATH_EXPRESSION,
                        GROUPED_EXPRESSION,
                        ARRAY_EXPRESSION,
                        AWAIT_EXPRESSION,
                        INDEX_EXPRESSION,
                        TUPLE_EXPRESSION,
                        TUPLE_INDEXING_EXPRESSION,
                        ENUMERATION_VARIANT_EXPRESSION,
                        CLOSURE_EXPRESSION,
                        CONTINUE_EXPRESSION,
                        BREAK_EXPRESSION,
                        RETURN_EXPRESSION
                ));
        b.rule(EXPRESSION_WITH_BLOCK).is(b.zeroOrMore(OUTER_ATTRIBUTE),
                b.firstOf(
                        BLOCK_EXPRESSION,
                        ASYNC_BLOCK_EXPRESSION,
                        UNSAFE_BLOCK_EXPRESSION,
                        LOOP_EXPRESSION,
                        IF_EXPRESSION,
                        IF_LET_EXPRESSION,
                        MATCH_EXPRESSION
                ));
    }

    private static void await(LexerlessGrammarBuilder b) {
        b.rule(AWAIT_EXPRESSION).is(b.firstOf(CALL_EXPRESSION, IDENTIFIER ), AWAIT_EXPRESSION_TERM);
        b.rule(AWAIT_EXPRESSION_TERM).is(RustPunctuator.DOT, "await", b.zeroOrMore(AWAIT_EXPRESSION_TERM));
    }

    private static void returnExpr(LexerlessGrammarBuilder b) {
        b.rule(RETURN_EXPRESSION).is("return", SPC, b.optional(EXPRESSION));
    }

    //https://doc.rust-lang.org/reference/expressions/match-expr.html
    private static void match(LexerlessGrammarBuilder b) {
        b.rule(MATCH_EXPRESSION).is(
                RustKeyword.KW_MATCH, SPC,EXPRESSION, //except struct expressions !!
                SPC,"{",SPC,
                b.zeroOrMore(INNER_ATTRIBUTE,SPC),
                b.optional(MATCH_ARMS,SPC),
                "}"
        );


        b.rule(MATCH_ARMS).is(
                b.oneOrMore(MATCH_ARM,SPC, RustPunctuator.FATARROW,SPC,
                        EXPRESSION,SPC, b.optional(RustPunctuator.COMMA,SPC )))
        ;


        b.rule(MATCH_ARM).is(
                b.zeroOrMore(OUTER_ATTRIBUTE,SPC),
                MATCH_ARM_PATTERNS,
                b.optional(MATCH_ARM_GUARD)
        );
        b.rule(MATCH_ARM_PATTERNS).is(
                b.optional(RustPunctuator.OR, SPC),
                PATTERN,SPC,
                b.zeroOrMore(b.sequence(RustPunctuator.OR, SPC, PATTERN, SPC))
        );
        b.rule(MATCH_ARM_GUARD).is(RustKeyword.KW_IF, SPC, EXPRESSION);

    }

    private static void ifExpr(LexerlessGrammarBuilder b) {
        b.rule(IF_EXPRESSION).is(
                RustKeyword.KW_IF, SPC, EXPRESSION,SPC, BLOCK_EXPRESSION,SPC,
                b.optional(

                        RustKeyword.KW_ELSE, SPC, b.firstOf(BLOCK_EXPRESSION, IF_EXPRESSION, IF_LET_EXPRESSION)
                )
        );
        b.rule(IF_LET_EXPRESSION).is(
                RustKeyword.KW_IF,SPC, RustKeyword.KW_LET, SPC, MATCH_ARM_PATTERNS, SPC , RustPunctuator.EQ, SPC, EXPRESSION, //except struct or lazy boolean operator expression
                SPC, BLOCK_EXPRESSION, SPC,
                b.optional(RustKeyword.KW_ELSE, SPC, b.firstOf(BLOCK_EXPRESSION, IF_EXPRESSION, IF_LET_EXPRESSION)
                )
        );
    }

    private static void range(LexerlessGrammarBuilder b) {

        b.rule(RANGE_EXPRESSION).is(b.firstOf(
                RANGE_INCLUSIVE_EXPR,
                RANGE_TO_INCLUSIVE_EXPR,
                RANGE_EXPR,
                RANGE_FROM_EXPR,
                RANGE_TO_EXPR,
                RANGE_FULL_EXPR

        ));

        b.rule(RANGE_EXPR).is(b.firstOf(DEC_LITERAL, IDENTIFIER),RANGE_EXPR_TERM);
        b.rule(RANGE_EXPR_TERM).is(b.sequence(RustPunctuator.DOTDOT, EXPRESSION, b.zeroOrMore(RANGE_EXPR_TERM)));
        b.rule(RANGE_FROM_EXPR).is(b.firstOf(DEC_LITERAL, IDENTIFIER), RANGE_FROM_EXPR_TERM);
        b.rule(RANGE_FROM_EXPR_TERM).is(RustPunctuator.DOTDOT, b.zeroOrMore(RANGE_FROM_EXPR_TERM));


        b.rule(RANGE_TO_EXPR).is(RustPunctuator.DOTDOT, EXPRESSION);
        b.rule(RANGE_FULL_EXPR).is(RustPunctuator.DOTDOT);

        b.rule(RANGE_INCLUSIVE_EXPR).is(b.firstOf(DEC_LITERAL, IDENTIFIER), RANGE_INCLUSIVE_EXPR_TERM);
        b.rule(RANGE_INCLUSIVE_EXPR_TERM).is(RustPunctuator.DOTDOTEQ, EXPRESSION, b.zeroOrMore(RANGE_INCLUSIVE_EXPR_TERM));


        b.rule(RANGE_TO_INCLUSIVE_EXPR).is(RustPunctuator.DOTDOTEQ, EXPRESSION);
    }

    private static void loops(LexerlessGrammarBuilder b) {
        b.rule(LOOP_EXPRESSION).is(b.optional(LOOP_LABEL),
                b.firstOf(
                        INFINITE_LOOP_EXPRESSION,
                        PREDICATE_LOOP_EXPRESSION,
                        PREDICATE_PATTERN_LOOP_EXPRESSION,
                        ITERATOR_LOOP_EXPRESSION)
        );
        b.rule(INFINITE_LOOP_EXPRESSION).is(
                RustKeyword.KW_LOOP, SPC, BLOCK_EXPRESSION);
        b.rule(PREDICATE_LOOP_EXPRESSION).is(
                RustKeyword.KW_WHILE, SPC, EXPRESSION, //except struct expression
                BLOCK_EXPRESSION
        );
        b.rule(PREDICATE_PATTERN_LOOP_EXPRESSION).is(
                RustKeyword.KW_WHILE, SPC, RustKeyword.KW_LET, SPC, MATCH_ARM_PATTERNS, SPC, RustPunctuator.EQ,
                SPC, EXPRESSION, //except struct expression
                SPC, BLOCK_EXPRESSION
        );
        b.rule(ITERATOR_LOOP_EXPRESSION).is(
                RustKeyword.KW_FOR, SPC, PATTERN,SPC,  RustKeyword.KW_IN, SPC, EXPRESSION, //except struct expression
                SPC, BLOCK_EXPRESSION
        );
        b.rule(LOOP_LABEL).is(LIFETIME_OR_LABEL, SPC, RustPunctuator.COLON);
        b.rule(BREAK_EXPRESSION).is(RustKeyword.KW_BREAK, SPC, b.optional(LIFETIME_OR_LABEL, SPC), b.optional(EXPRESSION, SPC));
        b.rule(CONTINUE_EXPRESSION).is(
                RustKeyword.KW_CONTINUE, b.optional(SPC, LIFETIME_OR_LABEL));


    }

    private static void field(LexerlessGrammarBuilder b) {

        b.rule(FIELD_EXPRESSION).is(
                b.firstOf(IDENTIFIER,LITERALS,EXPRESSION_WITH_BLOCK
                        //other expressions without block

                ), FIELD_EXPRESSION_TERM
        );



        b.rule(FIELD_EXPRESSION_TERM).is(
                b.sequence(RustPunctuator.DOT, IDENTIFIER,SPC
                        , b.zeroOrMore(FIELD_EXPRESSION_TERM))


        );


    }

    private static void methodcall(LexerlessGrammarBuilder b) {

        b.rule(METHOD_CALL_EXPRESSION).is(
                b.firstOf(LITERALS,  IDENTIFIER), METHOD_CALL_EXPRESSION_TERM
        );
        b.rule(METHOD_CALL_EXPRESSION_TERM).is(
                RustPunctuator.DOT,SPC, PATH_EXPR_SEGMENT,SPC,
                        "(", SPC,b.optional(CALL_PARAMS,SPC), ")", b.zeroOrMore(METHOD_CALL_EXPRESSION_TERM))
        ;


    }

    private static void call(LexerlessGrammarBuilder b) {

        b.rule(CALL_EXPRESSION).is(b.firstOf(EXPRESSION_WITH_BLOCK, FIELD_EXPRESSION, PATH_EXPRESSION, IDENTIFIER), CALL_EXPRESSION_TERM);

        b.rule(CALL_EXPRESSION_TERM).is(
                "(", SPC,b.optional(CALL_PARAMS), SPC, ")", b.zeroOrMore(SPC, CALL_EXPRESSION_TERM)
        );


        b.rule(CALL_PARAMS).is(seq(b, EXPRESSION, RustPunctuator.COMMA));


    }

    /* https://doc.rust-lang.org/reference/expressions/enum-variant-expr.html */
    private static void enums(LexerlessGrammarBuilder b) {
        b.rule(ENUMERATION_VARIANT_EXPRESSION).is(b.firstOf(
                ENUM_EXPR_STRUCT,
                ENUM_EXPR_TUPLE,
                ENUM_EXPR_FIELDLESS
        ));
        b.rule(ENUM_EXPR_STRUCT).is(PATH_IN_EXPRESSION, SPC,"{",SPC,
                b.optional(ENUM_EXPR_FIELDS,SPC), "}"
        );
        b.rule(ENUM_EXPR_FIELDS).is(
                ENUM_EXPR_FIELD,SPC,
                b.zeroOrMore(b.sequence(RustPunctuator.COMMA, SPC, ENUM_EXPR_FIELDS), b.optional(RustPunctuator.COMMA, SPC))
        );
        b.rule(ENUM_EXPR_FIELD).is(b.firstOf(
                b.sequence(b.firstOf(IDENTIFIER, TUPLE_INDEX), SPC,
                        RustPunctuator.COLON, SPC, EXPRESSION),
                IDENTIFIER
        ));
        b.rule(ENUM_EXPR_TUPLE).is(
                PATH_IN_EXPRESSION, SPC, "(",SPC,
                b.optional(b.sequence(
                        EXPRESSION,SPC,
                        b.zeroOrMore(b.sequence(RustPunctuator.COMMA,SPC, EXPRESSION)),
                        b.optional(RustPunctuator.COMMA,SPC)
                )), ")"
        );
        b.rule(ENUM_EXPR_FIELDLESS).is(PATH_IN_EXPRESSION);
    }

    private static void tuple(LexerlessGrammarBuilder b) {
        b.rule(TUPLE_EXPRESSION).is("(", SPC,b.zeroOrMore(INNER_ATTRIBUTE,SPC),
                b.optional(TUPLE_ELEMENT),SPC,
                ")");

        b.rule(TUPLE_ELEMENT).is(b.oneOrMore(b.sequence(EXPRESSION, SPC, RustPunctuator.COMMA, SPC)), b.optional(EXPRESSION,SPC));

        b.rule(TUPLE_INDEXING_EXPRESSION).is(b.firstOf(LITERALS, TUPLE_INDEXING_EXPRESSION_TERM));
        b.rule(TUPLE_INDEXING_EXPRESSION_TERM).is(RustPunctuator.DOT, TUPLE_INDEX, TUPLE_INDEXING_EXPRESSION_TERM);


    }

    private static void array(LexerlessGrammarBuilder b) {
        b.rule(ARRAY_EXPRESSION).is("[", b.zeroOrMore(INNER_ATTRIBUTE),
                b.optional(ARRAY_ELEMENTS),
                "]");

        b.rule(ARRAY_ELEMENTS).is(b.firstOf(
                b.sequence(SPC, EXPRESSION, RustPunctuator.SEMI, SPC, EXPRESSION),
                b.sequence(SPC, EXPRESSION, SPC, b.zeroOrMore(RustPunctuator.COMMA, SPC, EXPRESSION), b.optional(RustPunctuator.COMMA, SPC))


        ));


        b.rule(INDEX_EXPRESSION).is(b.firstOf(LITERALS, INDEX_EXPRESSION_TERM));
        b.rule(INDEX_EXPRESSION_TERM).is("[", EXPRESSION, "]", INDEX_EXPRESSION_TERM);
    }

    private static void grouped(LexerlessGrammarBuilder b) {
        b.rule(GROUPED_EXPRESSION).is("(", SPC,b.zeroOrMore(INNER_ATTRIBUTE, SPC), EXPRESSION, SPC, ")");
    }

    private static void operator(LexerlessGrammarBuilder b) {
        // https://doc.rust-lang.org/reference/expressions/operator-expr.html
        b.rule(OPERATOR_EXPRESSION).is(b.firstOf(
                BORROW_EXPRESSION,
                DEREFERENCE_EXPRESSION,
                ERROR_PROPAGATION_EXPRESSION,
                NEGATION_EXPRESSION,
                ARITHMETIC_OR_LOGICAL_EXPRESSION,
                COMPARISON_EXPRESSION,
                LAZY_BOOLEAN_EXPRESSION,
                TYPE_CAST_EXPRESSION,
                ASSIGNMENT_EXPRESSION,
                COMPOUND_ASSIGNMENT_EXPRESSION
        ));


        b.rule(BORROW_EXPRESSION).is(b.firstOf(
                b.sequence(b.firstOf(RustPunctuator.AND, RustPunctuator.ANDAND), SPC, RustKeyword.KW_MUT, SPC, EXPRESSION),
                b.sequence(b.firstOf(RustPunctuator.AND, RustPunctuator.ANDAND), SPC, EXPRESSION)
        ));
        b.rule(DEREFERENCE_EXPRESSION).is(RustPunctuator.STAR, EXPRESSION);
        b.rule(ERROR_PROPAGATION_EXPRESSION).is(b.firstOf(EXPRESSION_WITH_BLOCK,
                CALL_EXPRESSION,
                LITERAL_EXPRESSION,
                PATH_EXPRESSION,
                GROUPED_EXPRESSION,
                ARRAY_EXPRESSION,
                AWAIT_EXPRESSION,
                INDEX_EXPRESSION,
                TUPLE_EXPRESSION,
                TUPLE_INDEXING_EXPRESSION,
                STRUCT_EXPRESSION,
                ENUMERATION_VARIANT_EXPRESSION,
                METHOD_CALL_EXPRESSION,
                FIELD_EXPRESSION,
                CLOSURE_EXPRESSION,
                CONTINUE_EXPRESSION,
                BREAK_EXPRESSION,
                RANGE_EXPRESSION,
                RETURN_EXPRESSION,
                MACRO_INVOCATION), ERROR_PROPAGATION_EXPRESSION_TERM);

        b.rule(ERROR_PROPAGATION_EXPRESSION_TERM).is(SPC, RustPunctuator.QUESTION, b.optional(ERROR_PROPAGATION_EXPRESSION_TERM));

        b.rule(NEGATION_EXPRESSION).is(b.firstOf(
                b.sequence("-", EXPRESSION), b.sequence(RustPunctuator.NOT, EXPRESSION)
        ));

        b.rule(ARITHMETIC_OR_LOGICAL_EXPRESSION).is(b.firstOf(
                 SHL_EXPRESSION,
                 ADDITION_EXPRESSION,
                 SUBTRACTION_EXPRESSION,
                 MULTIPLICATION_EXPRESSION,
                 DIVISION_EXPRESSION,
                 REMAINDER_EXPRESSION,
                 BITAND_EXPRESSION,
                 BITOR_EXPRESSION,
                 BITXOR_EXPRESSION,
                 SHR_EXPRESSION));


        b.rule(ADDITION_EXPRESSION).is(b.firstOf(METHOD_CALL_EXPRESSION, CALL_EXPRESSION, IDENTIFIER, LITERALS), SPC, ADDITION_EXPRESSION_TERM);
        b.rule(ADDITION_EXPRESSION_TERM).is(
                RustPunctuator.PLUS, SPC, EXPRESSION, SPC, b.zeroOrMore(ADDITION_EXPRESSION_TERM, SPC));


        b.rule(SUBTRACTION_EXPRESSION).is(b.firstOf(METHOD_CALL_EXPRESSION,CALL_EXPRESSION, IDENTIFIER, LITERALS), SPC, SUBTRACTION_EXPRESSION_TERM);
        b.rule(SUBTRACTION_EXPRESSION_TERM).is(
                RustPunctuator.MINUS, SPC, EXPRESSION, SPC, b.zeroOrMore(SUBTRACTION_EXPRESSION_TERM, SPC));



        b.rule(MULTIPLICATION_EXPRESSION).is(b.firstOf(METHOD_CALL_EXPRESSION,CALL_EXPRESSION, IDENTIFIER, LITERALS), SPC,MULTIPLICATION_EXPRESSION_TERM);
        b.rule(MULTIPLICATION_EXPRESSION_TERM).is(RustPunctuator.STAR, SPC, EXPRESSION, SPC, b.zeroOrMore(MULTIPLICATION_EXPRESSION_TERM, SPC));



        b.rule(DIVISION_EXPRESSION).is(b.firstOf(METHOD_CALL_EXPRESSION,CALL_EXPRESSION, IDENTIFIER, LITERALS), SPC,DIVISION_EXPRESSION_TERM);
        b.rule(DIVISION_EXPRESSION_TERM).is(RustPunctuator.SLASH,SPC, EXPRESSION, SPC, b.zeroOrMore(DIVISION_EXPRESSION_TERM, SPC));

        b.rule(REMAINDER_EXPRESSION).is(b.firstOf(METHOD_CALL_EXPRESSION,CALL_EXPRESSION, IDENTIFIER, LITERALS), SPC,REMAINDER_EXPRESSION_TERM);
        b.rule(REMAINDER_EXPRESSION_TERM).is(RustPunctuator.PERCENT,SPC, EXPRESSION, SPC, b.zeroOrMore(REMAINDER_EXPRESSION_TERM, SPC));

        b.rule(BITAND_EXPRESSION).is(b.firstOf(METHOD_CALL_EXPRESSION,CALL_EXPRESSION, IDENTIFIER, LITERALS), SPC,BITAND_EXPRESSION_TERM);
        b.rule(BITAND_EXPRESSION_TERM).is(RustPunctuator.AND,SPC, EXPRESSION, SPC, b.zeroOrMore(BITAND_EXPRESSION_TERM, SPC));

        b.rule(BITOR_EXPRESSION).is(b.firstOf(METHOD_CALL_EXPRESSION,CALL_EXPRESSION, IDENTIFIER, LITERALS), SPC,BITOR_EXPRESSION_TERM);
        b.rule(BITOR_EXPRESSION_TERM).is(RustPunctuator.OR,SPC, EXPRESSION, SPC, b.zeroOrMore(BITOR_EXPRESSION_TERM, SPC));


        b.rule(BITXOR_EXPRESSION).is(b.firstOf(CALL_EXPRESSION, IDENTIFIER, LITERALS), SPC,BITXOR_EXPRESSION_TERM);
        b.rule(BITXOR_EXPRESSION_TERM).is(RustPunctuator.CARET,SPC, EXPRESSION, SPC, b.zeroOrMore(BITXOR_EXPRESSION_TERM, SPC));

        b.rule(SHL_EXPRESSION).is(b.firstOf(CALL_EXPRESSION, IDENTIFIER, LITERALS), SPC,SHL_EXPRESSION_TERM);
        b.rule(SHL_EXPRESSION_TERM).is(RustPunctuator.SHL,SPC, EXPRESSION, SPC, b.zeroOrMore(SHL_EXPRESSION_TERM, SPC));

        b.rule(SHR_EXPRESSION).is(b.firstOf(CALL_EXPRESSION, IDENTIFIER, LITERALS), SPC,SHR_EXPRESSION_TERM);
        b.rule(SHR_EXPRESSION_TERM).is(RustPunctuator.SHR,SPC, EXPRESSION, SPC, b.zeroOrMore(SHR_EXPRESSION_TERM, SPC));


        b.rule(COMPARISON_EXPRESSION).is(b.firstOf(
                b.sequence(b.firstOf(IDENTIFIER, LITERALS), EQ_EXPRESSION),
                b.sequence(b.firstOf(IDENTIFIER, LITERALS), NEQ_EXPRESSION),
                b.sequence(b.firstOf(IDENTIFIER, LITERALS), GT_EXPRESSION),
                b.sequence(b.firstOf(IDENTIFIER, LITERALS), LT_EXPRESSION),
                b.sequence(b.firstOf(IDENTIFIER, LITERALS), GE_EXPRESSION),
                b.sequence(b.firstOf(IDENTIFIER, LITERALS), LE_EXPRESSION)
        ));
        b.rule(EQ_EXPRESSION).is(b.firstOf(LITERALS,
                b.sequence(SPC, RustPunctuator.EQEQ, SPC, EXPRESSION, SPC, b.optional(EQ_EXPRESSION))));
        b.rule(NEQ_EXPRESSION).is(b.firstOf(LITERALS,
                b.sequence(SPC, RustPunctuator.NE, SPC, EXPRESSION, SPC, b.optional(NEQ_EXPRESSION))));
        b.rule(GT_EXPRESSION).is(b.firstOf(LITERALS,
                b.sequence(SPC, RustPunctuator.GT, SPC, EXPRESSION, SPC, b.optional(GT_EXPRESSION))));
        b.rule(LT_EXPRESSION).is(b.firstOf(LITERALS,
                b.sequence(SPC, RustPunctuator.LT, SPC, EXPRESSION, SPC, b.optional(LT_EXPRESSION))));
        b.rule(GE_EXPRESSION).is(b.firstOf(LITERALS,
                b.sequence(SPC, RustPunctuator.GE, SPC, EXPRESSION, SPC, b.optional(GE_EXPRESSION))));
        b.rule(LE_EXPRESSION).is(b.firstOf(LITERALS,
                b.sequence(SPC, RustPunctuator.LE, SPC, EXPRESSION, SPC, b.optional(LE_EXPRESSION))));


        b.rule(LAZY_BOOLEAN_EXPRESSION).is(b.firstOf(
                b.sequence(b.firstOf(IDENTIFIER, LITERALS), LAZY_OR),
                b.sequence(b.firstOf(IDENTIFIER, LITERALS), LAZY_AND)
        ));
        b.rule(LAZY_OR).is(b.firstOf(LITERALS,
                b.sequence(SPC, RustPunctuator.OROR, SPC, EXPRESSION, SPC, b.optional(LAZY_OR))));
        b.rule(LAZY_AND).is(b.firstOf(LITERALS,
                b.sequence(SPC, RustPunctuator.ANDAND, SPC, EXPRESSION, SPC, b.optional(LAZY_AND))));


        b.rule(TYPE_CAST_EXPRESSION).is(b.firstOf(EXPRESSION_WITH_BLOCK,

                LITERAL_EXPRESSION,
                PATH_EXPRESSION,
                //other operator expressions ?
                GROUPED_EXPRESSION,
                ARRAY_EXPRESSION,
                AWAIT_EXPRESSION,
                INDEX_EXPRESSION,
                TUPLE_EXPRESSION,
                TUPLE_INDEXING_EXPRESSION,
                STRUCT_EXPRESSION,
                ENUMERATION_VARIANT_EXPRESSION,
                CALL_EXPRESSION,
                METHOD_CALL_EXPRESSION,
                FIELD_EXPRESSION,
                CLOSURE_EXPRESSION,
                CONTINUE_EXPRESSION,
                BREAK_EXPRESSION,
                RANGE_EXPRESSION,
                RETURN_EXPRESSION,
                MACRO_INVOCATION
        ), TYPE_CAST_EXPRESSION_TERM);

        b.rule(TYPE_CAST_EXPRESSION_TERM).is(SPC, RustKeyword.KW_AS, SPC, TYPE_NO_BOUNDS,
                SPC, b.optional(TYPE_CAST_EXPRESSION_TERM));

        b.rule(ASSIGNMENT_EXPRESSION).is(b.firstOf(IDENTIFIER, LITERALS), ASSIGNMENT_EXPRESSION_TERM);
        b.rule(ASSIGNMENT_EXPRESSION_TERM).is(b.firstOf(LITERALS,
                b.sequence(SPC,RustPunctuator.EQ, SPC,EXPRESSION, SPC, b.optional(ASSIGNMENT_EXPRESSION_TERM))));


        b.rule(COMPOUND_ASSIGNMENT_EXPRESSION).is(b.firstOf(
                b.sequence(b.firstOf(IDENTIFIER, LITERALS), PLUSEQ_EXPRESSION),
                b.sequence(b.firstOf(IDENTIFIER, LITERALS), MINUSEQ_EXPRESSION),
                b.sequence(b.firstOf(IDENTIFIER, LITERALS), STAREQ_EXPRESSION),
                b.sequence(b.firstOf(IDENTIFIER, LITERALS), SLASHEQ_EXPRESSION),
                b.sequence(b.firstOf(IDENTIFIER, LITERALS), PERCENTEQ_EXPRESSION),
                b.sequence(b.firstOf(IDENTIFIER, LITERALS), ANDEQ_EXPRESSION),
                b.sequence(b.firstOf(IDENTIFIER, LITERALS), OREQ_EXPRESSION),
                b.sequence(b.firstOf(IDENTIFIER, LITERALS), CARETEQ_EXPRESSION),
                b.sequence(b.firstOf(IDENTIFIER, LITERALS), SHLEQ_EXPRESSION),
                b.sequence(b.firstOf(IDENTIFIER, LITERALS), SHREQ_EXPRESSION)

        ));
        b.rule(PLUSEQ_EXPRESSION).is(SPC,RustPunctuator.PLUSEQ, SPC,EXPRESSION, SPC,b.optional(PLUSEQ_EXPRESSION));
        b.rule(MINUSEQ_EXPRESSION).is(SPC,RustPunctuator.MINUSEQ, SPC,EXPRESSION,SPC, b.optional(MINUSEQ_EXPRESSION));
        b.rule(STAREQ_EXPRESSION).is(SPC,RustPunctuator.STAREQ, SPC,EXPRESSION,SPC, b.optional(STAREQ_EXPRESSION));
        b.rule(SLASHEQ_EXPRESSION).is(SPC,RustPunctuator.SLASHEQ, SPC,EXPRESSION, SPC,b.optional(SLASHEQ_EXPRESSION));
        b.rule(PERCENTEQ_EXPRESSION).is(SPC,RustPunctuator.PERCENTEQ, SPC,EXPRESSION,SPC, b.optional(PERCENTEQ_EXPRESSION));
        b.rule(ANDEQ_EXPRESSION).is(SPC,RustPunctuator.ANDEQ, SPC,EXPRESSION, SPC,b.optional(ANDEQ_EXPRESSION));
        b.rule(OREQ_EXPRESSION).is(SPC,RustPunctuator.OREQ, SPC,EXPRESSION, SPC,b.optional(OREQ_EXPRESSION));
        b.rule(CARETEQ_EXPRESSION).is(SPC,RustPunctuator.CARETEQ, SPC,EXPRESSION,SPC, b.optional(CARETEQ_EXPRESSION));
        b.rule(SHLEQ_EXPRESSION).is(SPC,RustPunctuator.SHLEQ, SPC,EXPRESSION,SPC, b.optional(SHLEQ_EXPRESSION));
        b.rule(SHREQ_EXPRESSION).is(SPC,RustPunctuator.SHREQ,SPC, EXPRESSION, SPC,b.optional(SHREQ_EXPRESSION));


    }

    private static void block(LexerlessGrammarBuilder b) {
        b.rule(BLOCK_EXPRESSION).is("{", SPC, b.zeroOrMore(INNER_ATTRIBUTE),
                SPC, b.optional(STATEMENTS), SPC, "}"
        );
        b.rule(STATEMENTS).is(b.firstOf(
                b.sequence(b.oneOrMore(STATEMENT, SPC), EXPRESSION_WITHOUT_BLOCK),
                b.oneOrMore(STATEMENT, SPC),
                EXPRESSION_WITHOUT_BLOCK
        ));

        b.rule(ASYNC_BLOCK_EXPRESSION).is("async", b.optional("move"), BLOCK_EXPRESSION);
        b.rule(UNSAFE_BLOCK_EXPRESSION).is(UNSAFE, BLOCK_EXPRESSION);

    }

    private static void path(LexerlessGrammarBuilder b) {
        b.rule(PATH_EXPRESSION).is(b.firstOf(PATH_IN_EXPRESSION, QUALIFIED_PATH_IN_EXPRESSION));
    }

    private static void literal(LexerlessGrammarBuilder b) {
        b.rule(LITERAL_EXPRESSION).is(b.firstOf(
                CHAR_LITERAL,
                STRING_LITERAL
                , RAW_STRING_LITERAL
                , BYTE_LITERAL
                , BYTE_STRING_LITERAL
                , RAW_BYTE_STRING_LITERAL
                , FLOAT_LITERAL
                , INTEGER_LITERAL
                , BOOLEAN_LITERAL
        ));

    }


    /* https://doc.rust-lang.org/reference/expressions/struct-expr.html */
    public static void struct(LexerlessGrammarBuilder b) {
        b.rule(STRUCT_EXPRESSION).is(b.firstOf(
                STRUCT_EXPR_STRUCT,
                STRUCT_EXPR_TUPLE,
                STRUCT_EXPR_UNIT));
        b.rule(STRUCT_EXPR_STRUCT).is(PATH_IN_EXPRESSION, SPC,"{",SPC,
                b.zeroOrMore(INNER_ATTRIBUTE,SPC),
                b.optional(b.firstOf(STRUCT_EXPR_FIELDS,  STRUCT_BASE)),SPC,
                "}"

        );
        b.rule(STRUCT_EXPR_FIELDS).is(STRUCT_EXPR_FIELD,
                b.zeroOrMore(b.sequence(RustPunctuator.COMMA,SPC, STRUCT_EXPR_FIELD)),
                b.firstOf(
                        b.sequence(SPC, RustPunctuator.COMMA, SPC, STRUCT_BASE),
                        b.optional(SPC, RustPunctuator.COMMA,SPC))
        );


        b.rule(STRUCT_EXPR_FIELD).is(b.firstOf(

                b.sequence(b.firstOf(IDENTIFIER, TUPLE_INDEX), SPC,RustPunctuator.COLON,SPC, EXPRESSION),
                IDENTIFIER

        ));


        b.rule(STRUCT_BASE).is("..",SPC,  EXPRESSION);
        b.rule(STRUCT_EXPR_TUPLE).is(
                PATH_IN_EXPRESSION, "(",SPC,
                b.zeroOrMore(INNER_ATTRIBUTE,SPC),
                b.optional(
                        b.sequence(
                                EXPRESSION,SPC,
                                b.zeroOrMore(b.sequence(RustPunctuator.COMMA, EXPRESSION,SPC)),
                                b.optional(RustPunctuator.COMMA,SPC)
                        )
                ),
                ")"
        );
        b.rule(STRUCT_EXPR_UNIT).is(PATH_IN_EXPRESSION);
    }

    /* https://doc.rust-lang.org/reference/attributes.html*/
    public static void attributes(LexerlessGrammarBuilder b) {
        b.rule(INNER_ATTRIBUTE).is("#![", ATTR, "]");
        b.rule(OUTER_ATTRIBUTE).is("#[", ATTR, "]");
        b.rule(ATTR).is(SIMPLE_PATH, SPC, b.optional(ATTR_INPUT, SPC));
        b.rule(ATTR_INPUT).is(b.firstOf(DELIM_TOKEN_TREE,
                b.sequence(RustPunctuator.EQ, SPC,
                        LITERAL_EXPRESSION)));
    }

    /* https://doc.rust-lang.org/reference/expressions/closure-expr.html*/
    public static void closure(LexerlessGrammarBuilder b) {
        b.rule(CLOSURE_EXPRESSION).is(
                b.optional(RustKeyword.KW_MOVE),
                b.firstOf("||", b.sequence("|", b.optional(CLOSURE_PARAMETERS), "|",SPC)),
                b.firstOf(EXPRESSION,  b.sequence(SPC, RustPunctuator.RARROW, SPC, TYPE_NO_BOUNDS, SPC,BLOCK_EXPRESSION))
        );
        b.rule(CLOSURE_PARAMETERS).is(CLOSURE_PARAM,
                b.zeroOrMore(b.sequence(RustPunctuator.COMMA, SPC, CLOSURE_PARAM)),
                b.optional(RustPunctuator.COMMA)
        );
        b.rule(CLOSURE_PARAM).is(b.zeroOrMore(OUTER_ATTRIBUTE,SPC),
                PATTERN,SPC,
                b.optional(RustPunctuator.COLON, SPC, TYPE)
        );
    }

    /* https://doc.rust-lang.org/reference/types.html#type-expressions */
    public static void type(LexerlessGrammarBuilder b) {
        b.rule(TYPE).is(b.firstOf(TYPE_NO_BOUNDS, IMPL_TRAIT_TYPE, TRAIT_OBJECT_TYPE));
        b.rule(TYPE_NO_BOUNDS).is(b.firstOf(
                PARENTHESIZED_TYPE,
                IMPL_TRAIT_TYPE_ONE_BOUND,
                TRAIT_OBJECT_TYPE_ONE_BOUND,
                TYPE_PATH,
                TUPLE_TYPE,
                NEVER_TYPE,
                RAW_POINTER_TYPE,
                REFERENCE_TYPE,
                ARRAY_TYPE,
                SLICE_TYPE,
                INFERRED_TYPE,
                QUALIFIED_PATH_IN_TYPE,
                BARE_FUNCTION_TYPE,
                MACRO_INVOCATION
        ));
        b.rule(PARENTHESIZED_TYPE).is("(", TYPE, ")");
        b.rule(TRAIT_OBJECT_TYPE).is(b.optional(RustKeyword.KW_DYN), TYPE_PARAM_BOUNDS);
        b.rule(TRAIT_OBJECT_TYPE_ONE_BOUND).is(b.optional(RustKeyword.KW_DYN), TRAIT_BOUND);
        b.rule(RAW_POINTER_TYPE).is(RustPunctuator.STAR, b.firstOf(RustKeyword.KW_MUT, CONST), TYPE_NO_BOUNDS);
        b.rule(INFERRED_TYPE).is(RustPunctuator.UNDERSCORE);
        b.rule(SLICE_TYPE).is("[", TYPE, "]");
        b.rule(ARRAY_TYPE).is("[", SPC, TYPE, SPC, RustPunctuator.SEMI, SPC, EXPRESSION, SPC, "]");
        b.rule(IMPL_TRAIT_TYPE).is(RustKeyword.KW_IMPL, TYPE_PARAM_BOUNDS);
        b.rule(IMPL_TRAIT_TYPE_ONE_BOUND).is(RustKeyword.KW_IMPL, TRAIT_BOUND);
        b.rule(NEVER_TYPE).is(RustPunctuator.NOT);

    }

    /* https://doc.rust-lang.org/reference/trait-bounds.html */
    public static void trait(LexerlessGrammarBuilder b) {
        b.rule(TYPE_PARAM_BOUNDS).is(SPC, TYPE_PARAM_BOUND, SPC,
                b.zeroOrMore(b.sequence(RustPunctuator.PLUS, SPC, TYPE_PARAM_BOUND, SPC)),
                b.optional(SPC, RustPunctuator.PLUS, SPC)
        );
        b.rule(TYPE_PARAM_BOUND).is(b.firstOf(LIFETIME, TRAIT_BOUND));
        b.rule(TRAIT_BOUND).is(b.firstOf(
                b.sequence(b.optional(RustPunctuator.QUESTION, SPC), b.optional(FOR_LIFETIMES, SPC), TYPE_PATH, SPC),
                b.sequence("(", SPC, b.optional(RustPunctuator.QUESTION, SPC),
                        b.optional(FOR_LIFETIMES, SPC), TYPE_PATH, SPC, ")")
        ));
        b.rule(LIFETIME_BOUNDS).is(
                b.zeroOrMore(LIFETIME, SPC, RustPunctuator.PLUS, SPC),
                b.optional(LIFETIME, SPC)
        );
        b.rule(LIFETIME).is(b.firstOf("'static", "'_", LIFETIME_OR_LABEL));
    }

    public static void tupletype(LexerlessGrammarBuilder b) {

        b.rule(TUPLE_TYPE).is(b.firstOf(
                b.sequence("(", ")"),
                b.sequence("(", SPC, b.oneOrMore(b.sequence(TYPE, RustPunctuator.COMMA, SPC)), b.optional(TYPE), ")")
        ));


    }

    /* https://doc.rust-lang.org/reference/types/pointer.html */
    public static void pointer(LexerlessGrammarBuilder b) {
        b.rule(REFERENCE_TYPE).is(RustPunctuator.AND, b.optional(LIFETIME),
                b.optional(SPC, RustKeyword.KW_MUT, SPC), TYPE_PARAM_BOUNDS);
    }

    /* https://doc.rust-lang.org/reference/paths.html */
    public static void lexicalpath(LexerlessGrammarBuilder b) {
        b.rule(SIMPLE_PATH).is(
                b.optional(RustPunctuator.PATHSEP),
                SIMPLE_PATH_SEGMENT,
                b.zeroOrMore(b.sequence(RustPunctuator.PATHSEP, SIMPLE_PATH_SEGMENT))
        );
        b.rule(SIMPLE_PATH_SEGMENT).is(b.firstOf(
                RustKeyword.KW_SUPER, RustKeyword.KW_SELFVALUE, b.regexp("^crate$"), b.regexp("^\\$crate$"), IDENTIFIER
        ));

        b.rule(PATH_IN_EXPRESSION).is(
                b.optional(RustPunctuator.PATHSEP),
                PATH_EXPR_SEGMENT,
                b.zeroOrMore(b.sequence(RustPunctuator.PATHSEP, PATH_EXPR_SEGMENT))
        );

        b.rule(PATH_EXPR_SEGMENT).is(
                PATH_IDENT_SEGMENT, b.optional(b.sequence(RustPunctuator.PATHSEP, GENERIC_ARGS))
        );
        b.rule(PATH_IDENT_SEGMENT).is(b.firstOf(
                RustKeyword.KW_SUPER, b.regexp("^[sS]elf$"), RustKeyword.KW_CRATE, b.regexp("^\\$crate$"), IDENTIFIER
        ));
        b.rule(GENERIC_ARGS).is(b.firstOf(
                b.sequence(RustPunctuator.LT, GENERIC_ARGS_TYPES, GENERIC_ARGS_BINDINGS, b.optional(RustPunctuator.COMMA), SPC, RustPunctuator.GT),
                b.sequence(RustPunctuator.LT, RustPunctuator.GT),
                b.sequence(RustPunctuator.LT, GENERIC_ARGS_LIFETIMES, GENERIC_ARGS_TYPES, GENERIC_ARGS_BINDINGS, b.optional(RustPunctuator.COMMA), RustPunctuator.GT),



                b.sequence(RustPunctuator.LT, GENERIC_ARGS_LIFETIMES, GENERIC_ARGS_TYPES, b.optional(RustPunctuator.COMMA), RustPunctuator.GT),
                b.sequence(RustPunctuator.LT, GENERIC_ARGS_LIFETIMES, GENERIC_ARGS_BINDINGS, b.optional(RustPunctuator.COMMA), RustPunctuator.GT),

                b.sequence(RustPunctuator.LT, GENERIC_ARGS_LIFETIMES, b.optional(RustPunctuator.COMMA), RustPunctuator.GT),
                b.sequence(RustPunctuator.LT, GENERIC_ARGS_BINDINGS, b.optional(RustPunctuator.COMMA), RustPunctuator.GT),
                b.sequence(RustPunctuator.LT, GENERIC_ARGS_TYPES, b.optional(RustPunctuator.COMMA), RustPunctuator.GT)
        ));
        b.rule(GENERIC_ARGS_LIFETIMES).is(
                LIFETIME, b.zeroOrMore(b.sequence(RustPunctuator.COMMA, LIFETIME))
        );
        b.rule(GENERIC_ARGS_TYPES).is(
                TYPE, b.zeroOrMore(b.sequence(RustPunctuator.COMMA, TYPE))
        );
        b.rule(GENERIC_ARGS_BINDINGS).is(
                GENERIC_ARGS_BINDING, b.zeroOrMore(b.sequence(SPC, RustPunctuator.COMMA, SPC, GENERIC_ARGS_BINDING))
        );
        b.rule(GENERIC_ARGS_BINDING).is(
                IDENTIFIER, SPC, RustPunctuator.EQ, SPC, TYPE
        );

        b.rule(QUALIFIED_PATH_IN_EXPRESSION).is(
                QUALIFIED_PATH_TYPE, b.oneOrMore(b.sequence(RustPunctuator.PATHSEP, PATH_EXPR_SEGMENT)));

        b.rule(QUALIFIED_PATH_TYPE).is(
                RustPunctuator.LT, TYPE, b.optional(RustKeyword.KW_AS, SPC, TYPE_PATH), RustPunctuator.GT
        );

        b.rule(QUALIFIED_PATH_IN_TYPE).is(QUALIFIED_PATH_TYPE, b.oneOrMore(
                b.sequence(RustPunctuator.PATHSEP, TYPE_PATH_SEGMENT)

        ));

        b.rule(TYPE_PATH_SEGMENT).is(
                PATH_IDENT_SEGMENT,
                b.optional(b.firstOf(
                        GENERIC_ARGS,
                        TYPE_PATH_FN,
                        b.sequence(RustPunctuator.PATHSEP, GENERIC_ARGS),
                        b.sequence(RustPunctuator.PATHSEP,TYPE_PATH_FN)))
        );
        b.rule(TYPE_PATH_FN).is(
                "(",
                b.optional(TYPE_PATH_FN_INPUTS),
                ")",
                b.optional(b.sequence(SPC, "->", SPC, TYPE))
        );
        b.rule(TYPE_PATH_FN_INPUTS).is(
                TYPE,
                b.zeroOrMore(b.sequence(RustPunctuator.COMMA, TYPE)),
                b.optional(RustPunctuator.COMMA)
        );
        b.rule(TYPE_PATH).is(
                //::? TypePathSegment (:: TypePathSegment)*
                //b.optional(RustPunctuator.PATHSEP), TYPE_PATH_SEGMENT, b.zeroOrMore(b.sequence(RustPunctuator.PATHSEP, TYPE_PATH_SEGMENT))
                b.optional(RustPunctuator.PATHSEP),
                TYPE_PATH_SEGMENT,
                b.zeroOrMore(RustPunctuator.PATHSEP, TYPE_PATH_SEGMENT)

        );


    }

    public static void lexicaltoken(LexerlessGrammarBuilder b) {


        b.rule(TOKEN).is(b.firstOf(LITERALS, IDENTIFIER_OR_KEYWORD,
                LIFETIMES, PUNCTUATION, DELIMITERS));

        b.rule(LIFETIME_OR_LABEL).is("'", NON_KEYWORD_IDENTIFIER);

        identifiers(b);

        b.rule(LIFETIME_TOKEN).is(b.firstOf(
                b.sequence("'", IDENTIFIER_OR_KEYWORD),
                b.sequence("'", RustPunctuator.UNDERSCORE)
        ));
        b.rule(LIFETIMES).is(b.firstOf(LIFETIME_TOKEN, LIFETIME_OR_LABEL)); //not explicit in reference
        //LITERALS are  not explicitly listed like below
        b.rule(LITERALS).is(b.firstOf(CHAR_LITERAL, STRING_LITERAL,
                RAW_STRING_LITERAL, BYTE_LITERAL, BYTE_STRING_LITERAL, RAW_BYTE_STRING_LITERAL, FLOAT_LITERAL,
                INTEGER_LITERAL, BOOLEAN_LITERAL, LIFETIMES
        ));


        b.rule(DELIMITERS).is(b.firstOf("{", "}", "[", "]", "(", ")"));

        characters(b);
        bytes(b);
        integerliteral(b);
        floatliteral(b);
        b.rule(BOOLEAN_LITERAL).is(b.token(RustTokenType.BOOLEAN_LITERAL, b.firstOf("true", "false")));

    }

    private static void floatliteral(LexerlessGrammarBuilder b) {

        b.rule(FLOAT_LITERAL).is(b.token(RustTokenType.FLOAT_LITERAL,
                b.firstOf(
                        b.sequence(DEC_LITERAL, b.optional(b.sequence(".", DEC_LITERAL)), b.optional(FLOAT_EXPONENT), FLOAT_SUFFIX),
                        b.sequence(DEC_LITERAL, ".", DEC_LITERAL, b.optional(FLOAT_EXPONENT)),
                        b.sequence(DEC_LITERAL, FLOAT_EXPONENT),
                        b.sequence(DEC_LITERAL, ".")//(not immediately followed by ., _ or an identifier)
                )));
        b.rule(FLOAT_EXPONENT).is(b.regexp("[eE]+[+-]?[0-9][0-9_]*"));

        b.rule(FLOAT_SUFFIX).is(b.firstOf("f64", "f32"));
    }

    private static void bytes(LexerlessGrammarBuilder b) {

        b.rule(BYTE_LITERAL).is(b.token(RustTokenType.BYTE_LITERAL,
                b.firstOf(
                        b.regexp("^b\\'" + "[^\\'\\n\\r\\t\\\\].*" + "\\'"),
                        b.sequence("b'", BYTE_ESCAPE, "'")
                )));

        b.rule(ASCII_FOR_CHAR).is(b.regexp("[^\\'\\n\\r\\t\\\\].*"));
        b.rule(ASCII_FOR_STRING).is(b.regexp("[^\"\\r\\\\].*"));// except ", \ and IsolatedCR (lookahead? (?![m-o])[a-z])

        b.rule(BYTE_STRING_LITERAL).is(b.token(RustTokenType.BYTE_STRING_LITERAL,
                b.firstOf(
                        b.regexp("^b\"" + "[^\"\\r\\\\].*" + "\""),
                        b.sequence("b\"", BYTE_ESCAPE, "\"")
                )));


        b.rule(BYTE_ESCAPE).is(b.firstOf(b.sequence("\\x", HEX_DIGIT, HEX_DIGIT), "\\n", "\\r", "\\t", "\\", "\\0"));

        b.rule(RAW_BYTE_STRING_LITERAL).is(b.token(RustTokenType.RAW_BYTE_STRING_LITERAL, b.sequence("br", RAW_BYTE_STRING_CONTENT)));
        b.rule(RAW_BYTE_STRING_CONTENT).is(b.firstOf(
                b.regexp("^\"[\\x00-\\x7F]*\""),
                b.sequence(RustPunctuator.POUND, RAW_STRING_CONTENT, RustPunctuator.POUND)
        ));
        b.rule(ASCII).is(b.regexp("[\\x00-\\x7F]"));


    }


    private static void identifiers(LexerlessGrammarBuilder b) {
        b.rule(IDENTIFIER_OR_KEYWORD).is(b.firstOf(b.regexp("^" + IDFREGEXP1), b.regexp("^" + IDFREGEXP2)));


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

    private static void characters(LexerlessGrammarBuilder b) {

        b.rule(QUOTE_ESCAPE).is(b.firstOf("\\'", "\\\""));
        b.rule(ASCII_ESCAPE).is(b.firstOf(b.sequence("\\x", OCT_DIGIT, HEX_DIGIT),
                "\\n", "\\r", "\\t", "\\", "\0"));
        b.rule(UNICODE_ESCAPE).is("\\u{", b.oneOrMore(b.sequence(HEX_DIGIT, b.zeroOrMore(RustPunctuator.UNDERSCORE))), "}");
        b.rule(STRING_CONTINUE).is("\\\n");
        b.rule(RAW_STRING_LITERAL).is(b.token(RustTokenType.RAW_STRING_LITERAL,
                b.sequence("r", RAW_STRING_CONTENT)));
        b.rule(RAW_STRING_CONTENT).is(b.firstOf(
                b.regexp("^\"[^\\r\\n].*\""),
                b.sequence(RustPunctuator.POUND, RAW_STRING_CONTENT, RustPunctuator.POUND)));


    }

    /* https://doc.rust-lang.org/reference/tokens.html#integer-literals */
    private static void integerliteral(LexerlessGrammarBuilder b) {
        b.rule(INTEGER_LITERAL).is(b.token(RustTokenType.INTEGER_LITERAL,
                b.sequence(
                        b.firstOf(HEX_LITERAL, OCT_LITERAL, BIN_LITERAL, DEC_LITERAL),
                        b.optional(INTEGER_SUFFIX), SPC)));
        b.rule(DEC_LITERAL).is(DEC_DIGIT, b.zeroOrMore(b.firstOf(DEC_DIGIT, RustPunctuator.UNDERSCORE)));
        b.rule(TUPLE_INDEX).is(b.firstOf("0", b.sequence(b.oneOrMore(NON_ZERO_DEC_DIGIT), b.optional(DEC_DIGIT))));

        b.rule(BIN_LITERAL).is("0b", b.zeroOrMore(b.firstOf(BIN_DIGIT, RustPunctuator.UNDERSCORE)));
        b.rule(OCT_LITERAL).is("0o", b.zeroOrMore(b.firstOf(OCT_DIGIT, RustPunctuator.UNDERSCORE)));
        b.rule(HEX_LITERAL).is("0x", b.zeroOrMore(b.firstOf(HEX_DIGIT, RustPunctuator.UNDERSCORE)));

        b.rule(BIN_DIGIT).is(b.regexp("[0-1]"));
        b.rule(OCT_DIGIT).is(b.regexp("[0-7]"));
        b.rule(DEC_DIGIT).is(b.regexp("[0-9]"));
        b.rule(NON_ZERO_DEC_DIGIT).is(b.regexp("[1-9]"));
        b.rule(HEX_DIGIT).is(b.regexp("[0-9a-fA-F]"));
        b.rule(INTEGER_SUFFIX).is(b.firstOf("u8", "u16", "u32", "u64", "u128", "usize"
                , "i8", "i16", "i32", "i64", "i128", "isize"));

    }


}