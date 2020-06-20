/**
 * Sonar Rust Plugin (Community)
 * Copyright (C) 2020 Eric Le Goff
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
package org.sonar.rust.ast.parser;

import com.sonar.sslr.api.GenericTokenType;
import org.sonar.api.internal.apachecommons.lang.ArrayUtils;
import org.sonar.rust.api.RustKeyword;
import org.sonar.rust.api.RustPunctuator;
import org.sonar.rust.api.RustTokenType;
import org.sonar.sslr.grammar.GrammarRuleKey;
import org.sonar.sslr.grammar.LexerlessGrammarBuilder;

import java.util.Arrays;

public enum RustLexer implements GrammarRuleKey {
    ABI,
    ANYTHING,
    ARITHMETIC_OR_LOGICAL_EXPRESSION,
    ARRAY_ELEMENTS,
    ARRAY_EXPRESSION,
    ARRAY_TYPE,
    ASCII,
    ASCII_ESCAPE,
    ASCII_FOR_CHAR,
    ASCII_FOR_STRING,
    ASSIGNMENT_EXPRESSION,
    ASYNC_BLOCK_EXPRESSION,
    ASYNC_CONST_QUALIFIERS,
    AS_CLAUSE,
    ATTR,
    ATTR_INPUT,
    AWAIT_EXPRESSION,
    BARE_FUNCTION_RETURN_TYPE,
    BARE_FUNCTION_TYPE,
    BIN_DIGIT,
    BIN_LITERAL,
    BLOCK_EXPRESSION,
    BOOLEAN_LITERAL,
    BORROW_EXPRESSION,
    BREAK_EXPRESSION,
    BYTE_ESCAPE,
    STRING_CONTINUE,
    BYTE_LITERAL,
    BYTE_STRING_LITERAL,
    CALL_EXPRESSION,
    CALL_PARAMS,
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
    ENUMERATION,
    ENUMERATION_VARIANT_EXPRESSION,
    ENUM_EXPR_FIELD,
    ENUM_EXPR_FIELDLESS,
    ENUM_EXPR_FIELDS,
    ENUM_EXPR_STRUCT,
    ENUM_EXPR_TUPLE,
    ENUM_ITEMS, ENUM_ITEM,
    ENUM_ITEM_DISCRIMINANT,
    ENUM_ITEM_STRUCT,
    ENUM_ITEM_TUPLE,
    EOF,
    ERROR_PROPAGATION_EXPRESSION,
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
    GENERiC_PARAMS,
    GROUPED_EXPRESSION,
    GROUPED_PATTERN,
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
    INFERRED_TYPE,
    INFINITE_LOOP_EXPRESSION,
    INHERENT_IMPL,
    INHERENT_IMPL_ITEM,
    INNER_ATTRIBUTE,
    INTEGER_LITERAL,
    INTEGER_SUFFIX,
    ITEM,
    ITERATOR_LOOP_EXPRESSION,
    KEYWORD,
    LAZY_BOOLEAN_EXPRESSION,
    LET_STATEMENT,
    LIFETIME,
    LIFETIMES,
    LIFETIME_BOUNDS,
    LIFETIME_OR_LABEL,
    LIFETIME_PARAM,
    LIFETIME_PARAMS,
    LIFETIME_TOKEN,
    LIFETIME_WHERE_CLAUSE_ITEM,
    LITERALS,
    LITERAL_PATTERN,
    LITTERAL_EXPRESSION,
    LOOP_EXPRESSION,
    LOOP_LABEL,
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
    MODULE,
    NAMED_FUNCTION_PARAM,
    NAMED_FUNCTION_PARAMETERS,
    NAMED_FUNCTION_PARAMETERS_WITH_VARIADICS,
    NEGATION_EXPRESSION,
    NEVER_TYPE,
    NON_KEYWORD_IDENTIFIER,
    NON_ZERO_DEC_DIGIT,
    NON_ZERO_DEC_DIGIT_DEC_DIGIT,
    OCT_DIGIT,
    OCT_LITERAL,
    OPERATOR_EXPRESSION,
    OUTER_ATTRIBUTE,
    PARENTHESIZED_TYPE,
    PATH_EXPRESSION,
    PATH_EXPR_SEGMENT,
    PATH_IDENT_SEGMENT,
    PATH_IN_EXPRESSION,
    PATH_PATTERN,
    PATTERN,
    PREDICATE_LOOP_EXPRESSION,
    PREDICATE_PATTERN_LOOP_EXPRESSION,
    PUNCTUATION,
    QUALIFIED_PATH_IN_EXPRESSION,
    QUALIFIED_PATH_IN_TYPE,
    QUALIFIED_PATH_TYPE,
    QUOTE_ESCAPE,
    RANGE_EXPR,
    RANGE_EXPRESSION,
    RANGE_FROM_EXPR,
    RANGE_FULL_EXPR,
    RANGE_INCLUSIVE_EXPR,
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
    RETURN_EXPRESSION,
    SELF_PARAM,
    SHORTHAND_SELF,
    SIMPLE_PATH,
    SIMPLE_PATH_SEGMENT,
    SLICE_PATTERN,
    SLICE_TYPE,
    SPACING,
    STATEMENT,
    STATEMENTS,
    STATIC_ITEM, TRAIT_FUNC,
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
    TOKEN,
    TOKEN_TREE,
    TRAIT,
    TRAIT_BOUND,
    TRAIT_CONST,
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
    UNSAFE_BLOCK_EXPRESSION,
    USE_DECLARATION,
    USE_TREE,
    VISIBILITY,
    VISIT_ITEM,
    WHERE_CLAUSE,
    WHERE_CLAUSE_ITEM,
    WHITESPACE,
    WILDCARD_PATTERN, LINE_COMMENT, BLOCK_COMMENT, BLOCK_COMMENT_OR_DOC, INNER_LINE_DOC, INNER_BLOCK_DOC, OUTER_LINE_DOC, OUTER_BLOCK_DOC;

    private static String IDF_REGEXP1 = "[a-zA-Z][a-z A-Z 0-9 _]*";
    private static String IDF_REGEXP2="_[a-z A-Z 0-9 _]+";


    public static LexerlessGrammarBuilder createGrammarBuilder() {
        LexerlessGrammarBuilder b = LexerlessGrammarBuilder.create();

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

        b.rule(COMPILATION_UNIT).is(b.oneOrMore(EXPRESSION),EOF);
        b.setRootRule(COMPILATION_UNIT);
        return b;
    }

    private static void literals(LexerlessGrammarBuilder b) {
        b.rule(SPACING).is(
                b.skippedTrivia(whitespace(b)),
                b.zeroOrMore(
                        b.commentTrivia(BLOCK_COMMENT_OR_DOC),
                        b.skippedTrivia(whitespace(b))));



        b.rule(EOF).is(b.token(GenericTokenType.EOF, b.endOfInput()));


        b.rule(CHAR_LITERAL).is(
                b.firstOf(b.regexp("^\\'[^\\\\n\\r\\t\\'].*\\'"),
                        b.sequence("'",UNICODE_ESCAPE,"'"),
                        b.sequence("'",QUOTE_ESCAPE,"'"),
                        b.sequence("'",ASCII_ESCAPE,"'")));

        b.rule(RustTokenType.STRING_LITERAL).is(stringLiteral(b), SPACING);


        b.rule(RustTokenType.IDENTIFIER).is(
                b.nextNot(KEYWORD),
                ASCII_FOR_STRING,
                SPACING);

        comments(b);
    }

    private static void comments(LexerlessGrammarBuilder b){
        b.rule(LINE_COMMENT).is(b.commentTrivia(
                b.regexp("////[^!/\\n]*|//[^!/\\n]*")
                ));
        b.rule(BLOCK_COMMENT).is(b.commentTrivia(
                b.firstOf(
                "/***/",
                "/**/",
                b.regexp("^\\/\\*\\*\\*.*\\*\\/")
        )));
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

    private static Object stringLiteral(LexerlessGrammarBuilder b) {
        return b.sequence(b.next("\""), b.regexp("\"([^\"\\\\]*+(\\\\[\\s\\S])?+)*+\""));
    }

    private static void punctuators(LexerlessGrammarBuilder b) {
        for (RustPunctuator tokenType : RustPunctuator.values()) {
            b.rule(tokenType).is(tokenType.getValue(), SPACING);
        }
        String[] punctuators = RustPunctuator.punctuatorValues();
        Arrays.sort(punctuators);
        ArrayUtils.reverse(punctuators);
        b.rule(PUNCTUATION).is(
                b.firstOf(
                        punctuators[0],
                        punctuators[1],
                        ArrayUtils.subarray(punctuators, 2, punctuators.length)));
    }

    private static void keywords(LexerlessGrammarBuilder b) {
        for (RustKeyword tokenType : RustKeyword.values()) {
            b.rule(tokenType).is(tokenType.getValue(), SPACING);
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
        return b.regexp("\\s*+");
    }

    private static Object inlineComment(LexerlessGrammarBuilder b) {
        return b.regexp("//[^\\n\\r]*+");
    }

    private static Object multilineComment(LexerlessGrammarBuilder b) {
        return b.regexp("/\\*[\\s\\S]*?\\*\\/");
    }

    /* recurring grammar pattern */
    private static Object seq(LexerlessGrammarBuilder b, GrammarRuleKey g, String sep) {
        return b.sequence(g, b.sequence(b.zeroOrMore(sep, g), b.optional(sep)));
    }

    private static void items(LexerlessGrammarBuilder b) {
        b.rule(ITEM).is(b.zeroOrMore(OUTER_ATTRIBUTE),
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
        use_item(b);
        alias_item(b);
        functions_item(b);
        structsItem(b);
        enumerations_item(b);
        unions_item(b);
        constants_item(b);
        static_item(b);
        traits_item(b);
        impl_item(b);
        extblocks_item(b);
        generic_item(b);
        assoc_item(b);
        visibility_item(b);
    }

    /* https://doc.rust-lang.org/reference/items/traits.html */
    private static void traits_item(LexerlessGrammarBuilder b) {
        b.rule(TRAIT).is(
                b.optional("unsafe"), "trait", IDENTIFIER, b.optional(GENERICS),
                b.optional(b.sequence(":", b.optional(TYPE_PARAM_BOUNDS))),
                b.optional(WHERE_CLAUSE), "{", b.zeroOrMore(TRAIT_ITEM), "}"
        );
        b.rule(TRAIT_ITEM).is(
                b.zeroOrMore(OUTER_ATTRIBUTE), b.optional(VISIBILITY),
                b.firstOf(TRAIT_FUNC, TRAIT_METHOD, TRAIT_CONST, TRAIT_TYPE)
        );
        b.rule(TRAIT_FUNC).is(
                TRAIT_FUNCTION_DECL, b.firstOf(";", BLOCK_EXPRESSION)
        );
        b.rule(TRAIT_METHOD).is(
                TRAIT_METHOD_DECL, b.firstOf(";", BLOCK_EXPRESSION)
        );
        b.rule(TRAIT_FUNCTION_DECL).is(
                FUNCTION_QUALIFIERS, "fn", IDENTIFIER, b.optional(GENERICS),
                "(", b.optional(TRAIT_FUNCTION_PARAMETERS), ")",
                b.optional(FUNCTION_RETURN_TYPE), b.optional(WHERE_CLAUSE)
        );
        b.rule(TRAIT_METHOD_DECL).is(
                FUNCTION_QUALIFIERS, "fn", IDENTIFIER, b.optional(GENERICS),
                "(", SELF_PARAM, b.zeroOrMore(b.sequence(",", TRAIT_FUNCTION_PARAM)), b.optional(","), ")",
                b.optional(FUNCTION_RETURN_TYPE), b.optional(WHERE_CLAUSE)
        );
        b.rule(TRAIT_FUNCTION_PARAMETERS).is(seq(b, TRAIT_FUNCTION_PARAM, ","));
        b.rule(TRAIT_FUNCTION_PARAM).is(
                b.zeroOrMore(OUTER_ATTRIBUTE), b.optional(b.sequence(PATTERN, ":")), TYPE
        );
        b.rule(TRAIT_CONST).is(
                "const", IDENTIFIER, ":", TYPE, b.optional(b.sequence("=", EXPRESSION)), ";"
        );
        b.rule(TRAIT_TYPE).is(
                "type", IDENTIFIER, b.optional(b.sequence(":", b.optional(TYPE_PARAM_BOUNDS))), ";"
        );
    }

    /* https://doc.rust-lang.org/reference/items/enumerations.html */
    private static void enumerations_item(LexerlessGrammarBuilder b) {
        b.rule(ENUMERATION).is("enum", IDENTIFIER,
                b.optional(GENERICS), b.optional(WHERE_CLAUSE), "{", ENUM_ITEMS, "}");
        b.rule(ENUM_ITEMS).is(seq(b, ENUM_ITEM, ","));
        b.rule(ENUM_ITEM).is(b.zeroOrMore(OUTER_ATTRIBUTE), b.optional(VISIBILITY),
                IDENTIFIER, b.optional(b.firstOf(ENUM_ITEM_TUPLE, ENUM_ITEM_STRUCT, ENUM_ITEM_DISCRIMINANT))
        );
        b.rule(ENUM_ITEM_TUPLE).is("(", b.optional(TUPLE_FIELDS), ")");
        b.rule(ENUM_ITEM_STRUCT).is("{", b.optional(STRUCT_FIELDS), "}");
        b.rule(ENUM_ITEM_DISCRIMINANT).is("=", EXPRESSION);
    }

    /* https://doc.rust-lang.org/reference/items/type-aliases.html */
    private static void alias_item(LexerlessGrammarBuilder b) {
        b.rule(TYPE_ALIAS).is(
                "type", IDENTIFIER, b.optional(GENERICS), b.optional(WHERE_CLAUSE),
                "=", TYPE, ";"
        );
    }

    private static void use_item(LexerlessGrammarBuilder b) {
        b.rule(USE_DECLARATION).is("use", USE_TREE, ";");
        b.rule(USE_TREE).is(b.firstOf(
                b.sequence(b.optional(b.sequence(b.optional(SIMPLE_PATH), "::")),
                        "*"),
                b.sequence(b.optional(b.sequence(b.optional(SIMPLE_PATH), "::")),
                        "{",
                        b.optional(USE_TREE, b.zeroOrMore(b.sequence(",", USE_TREE), b.optional(","))),
                        "}"
                ),
                b.sequence(SIMPLE_PATH, b.optional(b.sequence("as",
                        b.firstOf(IDENTIFIER, "_")
                )))
        ));

    }

    private static void functions_item(LexerlessGrammarBuilder b) {
        b.rule(FUNCTION).is(
                FUNCTION_QUALIFIERS, "fn", IDENTIFIER, b.optional(GENERICS),
                "(", FUNCTION_PARAMETERS, ")",
                b.optional(FUNCTION_RETURN_TYPE), b.optional(WHERE_CLAUSE),
                BLOCK_EXPRESSION
        );
        b.rule(FUNCTION_QUALIFIERS).is(
                b.optional(ASYNC_CONST_QUALIFIERS),
                b.optional("unsafe"),
                b.optional(b.sequence("extern", b.optional(ABI)))
        );
        b.rule(ASYNC_CONST_QUALIFIERS).is(b.firstOf("const", "async"));
        b.rule(ABI).is(b.firstOf(STRING_LITERAL, RAW_STRING_LITERAL));
        b.rule(FUNCTION_PARAMETERS).is(seq(b, FUNCTION_PARAM, ","));
        b.rule(FUNCTION_PARAM).is(
                b.zeroOrMore(OUTER_ATTRIBUTE), PATTERN, ":", TYPE
        );
        b.rule(FUNCTION_RETURN_TYPE).is("->", TYPE);

    }

    /* https://doc.rust-lang.org/reference/items/structs.html */
    private static void structsItem(LexerlessGrammarBuilder b) {
        b.rule(STRUCT).is(b.firstOf(STRUCT_STRUCT, TUPLE_STRUCT));
        b.rule(STRUCT_STRUCT).is(
                "struct", IDENTIFIER, b.optional(GENERICS), b.optional(WHERE_CLAUSE),
                b.firstOf(b.sequence("{", b.optional(STRUCT_FIELDS), "}"), ";"));
        b.rule(TUPLE_STRUCT).is(
                "struct", IDENTIFIER, b.optional(GENERICS), "(", b.optional(TUPLE_FIELDS), ")",
                b.optional(GENERICS), ";"
        );
        b.rule(STRUCT_FIELDS).is(seq(b, STRUCT_FIELD, ","));
        b.rule(STRUCT_FIELD).is(
                b.zeroOrMore(OUTER_ATTRIBUTE),
                b.optional(VISIBILITY),
                IDENTIFIER, ":", TYPE
        );
        b.rule(TUPLE_FIELDS).is(seq(b, TUPLE_FIELD, ","));
        b.rule(TUPLE_FIELD).is(
                b.zeroOrMore(OUTER_ATTRIBUTE),
                b.optional(VISIBILITY), TYPE
        );

    }

    /* https://doc.rust-lang.org/reference/items/unions.html */
    private static void unions_item(LexerlessGrammarBuilder b) {
        b.rule(UNION).is(
                "union", IDENTIFIER, b.optional(GENERICS), b.optional(WHERE_CLAUSE),
                "{", STRUCT_FIELDS, "}"
        );
    }

    /* https://doc.rust-lang.org/reference/items/constant-items.html */
    private static void constants_item(LexerlessGrammarBuilder b) {
        b.rule(CONSTANT_ITEM).is(
                "const", b.firstOf(IDENTIFIER, "_"),
                ":", TYPE, "=", EXPRESSION, ";"
        );

    }

    /* https://doc.rust-lang.org/reference/items/static-items.html */
    private static void static_item(LexerlessGrammarBuilder b) {
        b.rule(STATIC_ITEM).is(
                "static", b.optional("mut"), IDENTIFIER, ":", TYPE, "=", EXPRESSION, ";"
        );
    }

    /* https://doc.rust-lang.org/reference/items/implementations.html */
    private static void impl_item(LexerlessGrammarBuilder b) {
        b.rule(IMPLEMENTATION).is(b.firstOf(INHERENT_IMPL, TRAIT_IMPL));
        b.rule(INHERENT_IMPL).is(
                "impl", b.optional(GENERICS), TYPE, b.optional(WHERE_CLAUSE), "{",
                b.zeroOrMore(INNER_ATTRIBUTE),
                b.zeroOrMore(INHERENT_IMPL_ITEM)
        );
        b.rule(INHERENT_IMPL_ITEM).is(
                b.zeroOrMore(OUTER_ATTRIBUTE),
                b.firstOf(MACRO_INVOCATION_SEMI,
                        b.sequence("(", b.optional(VISIBILITY), b.firstOf(
                                CONSTANT_ITEM, FUNCTION, METHOD
                        ))));
        b.rule(TRAIT_IMPL).is(
                b.optional("unsafe"), "impl", b.optional(GENERICS),
                b.optional("!"), TYPE_PATH, "for", TYPE,
                b.optional(WHERE_CLAUSE), "{",
                b.zeroOrMore(INNER_ATTRIBUTE), b.zeroOrMore(TRAIT_IMPL_ITEM), "}"
        );

        b.rule(TRAIT_IMPL_ITEM).is(
                b.zeroOrMore(OUTER_ATTRIBUTE), "(",
                b.firstOf(MACRO_INVOCATION_SEMI,
                        b.sequence("(", b.optional(VISIBILITY), b.firstOf(TYPE_ALIAS, CONSTANT_ITEM, FUNCTION, METHOD), ")")
                )
        );

    }

    /* https://doc.rust-lang.org/reference/items/external-blocks.html */
    private static void extblocks_item(LexerlessGrammarBuilder b) {
        b.rule(EXTERN_BLOCK).is(
                "extern", b.optional(ABI), "{",
                b.zeroOrMore(INNER_ATTRIBUTE),
                b.zeroOrMore(EXTERNAL_ITEM), "}"
        );
        b.rule(EXTERNAL_ITEM).is(
                b.zeroOrMore(OUTER_ATTRIBUTE), "(",
                b.firstOf(MACRO_INVOCATION_SEMI,
                        b.sequence(
                                b.optional(VISIBILITY),
                                b.firstOf(EXTERNAL_STATIC_ITEM, EXTERNAL_FUNCTION_ITEM)
                        )));
        b.rule(EXTERNAL_STATIC_ITEM).is(
                "static", b.optional("mut"), IDENTIFIER, ":", TYPE, ";"
        );
        b.rule(EXTERNAL_FUNCTION_ITEM).is(
                "fn", IDENTIFIER, b.optional(GENERICS), "(",
                b.optional(b.firstOf(NAMED_FUNCTION_PARAMETERS, NAMED_FUNCTION_PARAMETERS_WITH_VARIADICS)),
                ")", b.optional(FUNCTION_RETURN_TYPE), b.optional(WHERE_CLAUSE), ";"
        );
        b.rule(NAMED_FUNCTION_PARAMETERS).is(seq(b, NAMED_FUNCTION_PARAM, ","));
        b.rule(NAMED_FUNCTION_PARAM).is(
                b.zeroOrMore(OUTER_ATTRIBUTE),
                b.firstOf(IDENTIFIER, "_"),
                ":", TYPE
        );
        b.rule(NAMED_FUNCTION_PARAMETERS_WITH_VARIADICS).is(
                b.zeroOrMore(b.sequence(NAMED_FUNCTION_PARAM, ",")),
                NAMED_FUNCTION_PARAM, ",", b.zeroOrMore(OUTER_ATTRIBUTE), "..."
        );
    }


    /* https://doc.rust-lang.org/reference/items/generics.html */
    private static void generic_item(LexerlessGrammarBuilder b) {
        b.rule(GENERICS).is("<", GENERiC_PARAMS, ">");
        b.rule(GENERiC_PARAMS).is(b.firstOf(
                LIFETIME_PARAMS,
                b.sequence(b.zeroOrMore(b.sequence(LIFETIME_PARAM, ",")), TYPE_PARAMS)));
        b.rule(LIFETIME_PARAMS).is(
                b.zeroOrMore(b.sequence(LIFETIME_PARAM, ",")), b.optional(LIFETIME_PARAM)
        );
        b.rule(LIFETIME_PARAM).is(
                b.optional(OUTER_ATTRIBUTE), LIFETIME_OR_LABEL, b.optional(b.sequence(":", LIFETIME_BOUNDS))
        );
        b.rule(TYPE_PARAMS).is(
                b.zeroOrMore(b.sequence(TYPE_PARAM, ",")), b.optional(TYPE_PARAM)
        );
        b.rule(TYPE_PARAM).is(
                b.optional(OUTER_ATTRIBUTE), IDENTIFIER,
                b.optional(b.sequence(":", b.optional(TYPE_PARAM_BOUNDS))),
                b.optional(b.sequence("=", TYPE))
        );
        b.rule(WHERE_CLAUSE).is(
                "where", b.zeroOrMore(b.sequence(WHERE_CLAUSE_ITEM, ",")), b.optional(WHERE_CLAUSE_ITEM)
        );
        b.rule(WHERE_CLAUSE_ITEM).is(b.firstOf(
                LIFETIME_WHERE_CLAUSE_ITEM
                , TYPE_BOUND_CLAUSE_ITEM));
        b.rule(LIFETIME_WHERE_CLAUSE_ITEM).is(LIFETIME, ":", LIFETIME_BOUNDS);
        b.rule(TYPE_BOUND_CLAUSE_ITEM).is(
                b.optional(FOR_LIFETIMES), TYPE, ":", b.optional(TYPE_PARAM_BOUNDS)
        );
        b.rule(FOR_LIFETIMES).is("for", "<", LIFETIME_PARAMS, ">");
    }

    private static void assoc_item(LexerlessGrammarBuilder b) {
        b.rule(METHOD).is(
                FUNCTION_QUALIFIERS, "fn", IDENTIFIER, b.optional(GENERICS),
                "(", SELF_PARAM, b.optional(b.sequence(",", FUNCTION_PARAM)),
                b.optional(","), ")",
                b.optional(FUNCTION_RETURN_TYPE), b.optional(WHERE_CLAUSE),
                BLOCK_EXPRESSION
        );
        b.rule(SELF_PARAM).is(b.zeroOrMore(OUTER_ATTRIBUTE), b.firstOf(
                SHORTHAND_SELF, TYPED_SELF
        ));
        b.rule(SHORTHAND_SELF).is(
                b.optional(b.firstOf("&", b.sequence("&", LIFETIME))),
                b.optional("mut"), "self"
        );
        b.rule(TYPED_SELF).is(b.optional("mut"), "self", ":", TYPE);

    }

    private static void visibility_item(LexerlessGrammarBuilder b) {
        b.rule(VISIBILITY).is(b.firstOf(
                "pub",
                b.sequence("pub", "(", "crate", ")"),
                b.sequence("pub", "(", "self", ")"),
                b.sequence("pub", "(", "super", ")"),
                b.sequence("pub", "(", "in", SIMPLE_PATH, ")")

        ));
    }


    private static void externcrates(LexerlessGrammarBuilder b) {
        b.rule(EXTERN_CRATE).is(
                "extern", "crate", CRATE_REF, b.optional(AS_CLAUSE)
        );
        b.rule(CRATE_REF).is(b.firstOf(IDENTIFIER, "self"));
        b.rule(AS_CLAUSE).is("as", b.firstOf(IDENTIFIER, "_"));

    }

    private static void modules(LexerlessGrammarBuilder b) {
        b.rule(MODULE).is(b.firstOf(
                b.sequence("mod", IDENTIFIER, ";"),
                b.sequence("mod", IDENTIFIER, "{",
                        b.zeroOrMore(INNER_ATTRIBUTE),
                        b.zeroOrMore(ITEM), "}"
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
                SIMPLE_PATH, "!", DELIM_TOKEN_TREE
        );
        b.rule(DELIM_TOKEN_TREE).is(b.firstOf(
                b.sequence("(", b.zeroOrMore(TOKEN_TREE), ")"),
                b.sequence("[", b.zeroOrMore(TOKEN_TREE), "]"),
                b.sequence("{", b.zeroOrMore(TOKEN_TREE), "}")));
        b.rule(TOKEN_TREE).is(b.firstOf(
                TOKEN, // except delimiters
                DELIM_TOKEN_TREE
        ));
        b.rule(MACRO_INVOCATION_SEMI).is(b.firstOf(
                b.sequence(SIMPLE_PATH, "!", "(", b.zeroOrMore(TOKEN_TREE), ");"),
                b.sequence(SIMPLE_PATH, "!", "[", b.zeroOrMore(TOKEN_TREE), "];"),
                b.sequence(SIMPLE_PATH, "!", "{", b.zeroOrMore(TOKEN_TREE), "}")
        ));
        macros_by_example(b);
        procedural_macros(b);
    }

    /* https://doc.rust-lang.org/reference/macros-by-example.html */
    private static void macros_by_example(LexerlessGrammarBuilder b) {
        b.rule(MACRO_RULES_DEFINITION).is(
                "macro_rule", "!", IDENTIFIER, MACRO_RULES_DEF
        );
        b.rule(MACRO_RULES_DEF).is(b.firstOf(
                b.sequence("(", MACRO_RULES, ")", ";"),
                b.sequence("[", MACRO_RULES, "]", ";"),
                b.sequence("{", MACRO_RULES, "}")
        ));
        b.rule(MACRO_RULES).is(
                MACRO_RULE, b.zeroOrMore(b.sequence(",", MACRO_RULE)), b.optional(",")
        );
        b.rule(MACRO_RULE).is(MACRO_MATCHER, "=>", MACRO_TRANSCRIBER);
        b.rule(MACRO_MATCHER).is(b.firstOf(
                b.sequence("(", MACRO_MATCH, ")"),
                b.sequence("[", MACRO_MATCH, "]"),
                b.sequence("{", MACRO_MATCH, "}")
        ));
        b.rule(MACRO_MATCH).is(b.firstOf(
                TOKEN, //except $ and delimiters
                MACRO_MATCHER,
                b.sequence("$", IDENTIFIER, ":", MACRO_FRAG_SPEC),
                b.sequence("$", "(", b.oneOrMore(MACRO_MATCH), ")"
                        , b.optional(MACRO_REP_SEP), MACRO_REP_OP)
        ));
        b.rule(MACRO_FRAG_SPEC).is(b.firstOf(
                "block", "expr", "ident", "item", "lifetime", "literal"
                , "meta", "pat", "path", "stmt", "tt", "ty", "vis"
        ));
        b.rule(MACRO_REP_SEP).is(TOKEN); //except $ and delimiters
        b.rule(MACRO_REP_OP).is(b.firstOf("*", "+", "?"));
        b.rule(MACRO_TRANSCRIBER).is(DELIM_TOKEN_TREE);


    }

    private static void procedural_macros(LexerlessGrammarBuilder b) {
    }

    private static void patterns(LexerlessGrammarBuilder b) {
        b.rule(PATTERN).is(b.firstOf(
                LITERAL_PATTERN,
                IDENTIFIER_PATTERN,
                WILDCARD_PATTERN,
                RANGE_PATTERN,
                REFERENCE_PATTERN,
                STRUCT_PATTERN,
                TUPLE_STRUCT_PATTERN,
                TUPLE_PATTERN,
                GROUPED_PATTERN,
                SLICE_PATTERN,
                PATH_PATTERN,
                MACRO_INVOCATION
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
                b.optional("ref"),
                b.optional("mut"),
                IDENTIFIER,
                b.optional(b.sequence("@", PATTERN))
        );
        b.rule(WILDCARD_PATTERN).is("_");
        b.rule(RANGE_PATTERN).is(b.firstOf(
                b.sequence(RANGE_PATTERN_BOUND, "..=", RANGE_PATTERN_BOUND),
                b.sequence(RANGE_PATTERN_BOUND, "...", RANGE_PATTERN_BOUND)
        ));
        b.rule(RANGE_PATTERN_BOUND).is(b.firstOf(
                CHAR_LITERAL, BYTE_LITERAL, b.sequence(b.optional("-"), INTEGER_LITERAL),
                b.sequence(b.optional("-"), FLOAT_LITERAL),
                PATH_IN_EXPRESSION, QUALIFIED_PATH_IN_EXPRESSION
        ));

        b.rule(REFERENCE_PATTERN).is(
                b.firstOf("&", "&&"),
                b.optional("mut"),
                PATTERN
        );
        b.rule(STRUCT_PATTERN).is(
                PATH_IN_EXPRESSION, "{", b.optional(STRUCT_PATTERN_ELEMENTS), "}"
        );
        b.rule(STRUCT_PATTERN_ELEMENTS).is(b.firstOf(
                b.sequence(STRUCT_PATTERN_FIELDS,
                        b.optional(b.firstOf(
                                "'",
                                b.sequence(",", STRUCT_PATTERN_ETCETERA)
                        ))), STRUCT_PATTERN_ETCETERA

        ));
        b.rule(STRUCT_PATTERN_FIELDS).is(
                STRUCT_PATTERN_FIELD, b.zeroOrMore(b.sequence(",", STRUCT_PATTERN_FIELD))
        );
        b.rule(STRUCT_PATTERN_FIELD).is(
                b.zeroOrMore(OUTER_ATTRIBUTE), "(",
                b.firstOf(
                        b.sequence(TUPLE_INDEX, ":", PATTERN),
                        b.sequence(IDENTIFIER, ":", PATTERN),
                        b.sequence(b.optional("ref"), b.optional("mut"), IDENTIFIER)
                ));
        b.rule(STRUCT_PATTERN_ETCETERA).is(b.zeroOrMore(OUTER_ATTRIBUTE), "..");

        b.rule(TUPLE_STRUCT_PATTERN).is(
                PATH_IN_EXPRESSION, "(", b.optional(TUPLE_STRUCT_ITEMS), ")"
        );
        b.rule(TUPLE_STRUCT_ITEMS).is(b.firstOf(
                PATTERN, b.zeroOrMore(b.sequence(",", PATTERN), b.optional("'")),
                b.sequence(b.zeroOrMore(b.sequence(PATTERN, ",")), "..",
                        b.optional(b.sequence(b.zeroOrMore(b.sequence(",", PATTERN)),
                                b.optional(",")
                        )))));
        b.rule(TUPLE_PATTERN).is("(", b.optional(TUPLE_PATTERN_ITEMS), ")");
        b.rule(TUPLE_PATTERN_ITEMS).is(b.firstOf(
                b.sequence(PATTERN, ","),
                b.sequence(PATTERN, b.oneOrMore(b.sequence(",", PATTERN)), b.optional(",")),
                b.sequence(b.zeroOrMore(b.sequence(",", PATTERN)), "..",
                        b.optional(b.sequence(
                                b.oneOrMore(b.sequence(",", PATTERN)),
                                b.optional(","))))));
        b.rule(GROUPED_PATTERN).is("(", PATTERN, ")");
        b.rule(SLICE_PATTERN).is("[", PATTERN,
                b.zeroOrMore(b.sequence(",", PATTERN)), b.optional(","), "]"
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
                b.optional(FOR_LIFETIMES), FUNCTION_QUALIFIERS, "fn",
                "(", b.optional(FUNCTION_PARAMETERS_MAYBE_NAMED_VARIADIC), ")",
                b.optional(BARE_FUNCTION_RETURN_TYPE)
        );
        b.rule(BARE_FUNCTION_RETURN_TYPE).is("->", TYPE_NO_BOUNDS);
        b.rule(FUNCTION_PARAMETERS_MAYBE_NAMED_VARIADIC).is(b.firstOf(
                MAYBE_NAMED_FUNCTION_PARAMETERS, MAYBE_NAMED_FUNCTION_PARAMETERS_VARIADIC
        ));
        b.rule(MAYBE_NAMED_FUNCTION_PARAMETERS).is(seq(b, MAYBE_NAMED_PARAM, ","));
        b.rule(MAYBE_NAMED_PARAM).is(
                b.zeroOrMore(OUTER_ATTRIBUTE),
                b.optional(b.sequence(
                        b.firstOf(IDENTIFIER, "_"), ":"
                )), TYPE
        );
        b.rule(MAYBE_NAMED_FUNCTION_PARAMETERS_VARIADIC).is(
                b.zeroOrMore(b.sequence(MAYBE_NAMED_PARAM, ",")),
                MAYBE_NAMED_PARAM, ",", b.zeroOrMore(OUTER_ATTRIBUTE), "..."
        );


    }

    public static void statement(LexerlessGrammarBuilder b) {
        b.rule(STATEMENT).is(b.firstOf(
                ";",
                ITEM,
                LET_STATEMENT,
                EXPRESSION_STATEMENT,
                MACRO_INVOCATION_SEMI
        ));
        b.rule(LET_STATEMENT).is(
                b.zeroOrMore(OUTER_ATTRIBUTE),
                "let", PATTERN,
                b.optional(b.sequence(":", TYPE)),
                b.optional(b.sequence("=", EXPRESSION)),
                ";");
        b.rule(EXPRESSION_STATEMENT).is(b.firstOf(
                b.sequence(EXPRESSION_WITHOUT_BLOCK, ";"),
                b.sequence(EXPRESSION_WITH_BLOCK, b.optional(";"))
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
        if_expr(b);
        match(b);
        return_expr(b);
        await(b);
        b.rule(EXPRESSION).is(b.firstOf(EXPRESSION_WITHOUT_BLOCK, EXPRESSION_WITH_BLOCK));
        b.rule(EXPRESSION_WITHOUT_BLOCK).is(b.zeroOrMore(OUTER_ATTRIBUTE),
                b.firstOf(LITTERAL_EXPRESSION,
                        PATH_EXPRESSION,
                        OPERATOR_EXPRESSION,
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
        b.rule(AWAIT_EXPRESSION).is(EXPRESSION, ".", "await");
    }

    private static void return_expr(LexerlessGrammarBuilder b) {
        b.rule(RETURN_EXPRESSION).is("return", b.optional(EXPRESSION));
    }

    //https://doc.rust-lang.org/reference/expressions/match-expr.html
    private static void match(LexerlessGrammarBuilder b) {
        b.rule(MATCH_EXPRESSION).is(
                "match", EXPRESSION, //except struct expressions !!
                "{",
                b.zeroOrMore(INNER_ATTRIBUTE),
                b.optional(MATCH_ARMS),
                "}"
        );
        b.rule(MATCH_ARMS).is(

                b.zeroOrMore(b.sequence(
                        MATCH_ARM, "=>",
                        b.firstOf(
                                b.sequence(EXPRESSION_WITHOUT_BLOCK, ","),
                                b.sequence(EXPRESSION_WITH_BLOCK, b.optional(","))
                        )
                )),

                b.sequence(MATCH_ARM, "=>", EXPRESSION, b.optional(",")));
        b.rule(MATCH_ARM).is(
                b.zeroOrMore(OUTER_ATTRIBUTE),
                MATCH_ARM_PATTERNS,
                b.optional(MATCH_ARM_GUARD)
        );
        b.rule(MATCH_ARM_PATTERNS).is(
                b.optional("|"),
                PATTERN,
                b.zeroOrMore(b.sequence("|", PATTERN))
        );
        b.rule(MATCH_ARM_GUARD).is("if", EXPRESSION);

    }

    private static void if_expr(LexerlessGrammarBuilder b) {
        b.rule(IF_EXPRESSION).is(
                "if", EXPRESSION, //except struct expressions !!
                b.optional(b.sequence(
                        //else
                        "else", b.firstOf(BLOCK_EXPRESSION, IF_EXPRESSION, IF_LET_EXPRESSION)
                ))
        );
        b.rule(IF_LET_EXPRESSION).is(
                "if", "let", MATCH_ARM_PATTERNS, "=", EXPRESSION, //except struct or lazy boolean operator expression
                BLOCK_EXPRESSION,
                b.optional(b.sequence(
                        //else
                        "else", b.firstOf(BLOCK_EXPRESSION, IF_EXPRESSION, IF_LET_EXPRESSION)
                ))
        );
    }

    private static void range(LexerlessGrammarBuilder b) {

        b.rule(RANGE_EXPRESSION).is(b.firstOf(
                RANGE_EXPR,
                RANGE_FROM_EXPR,
                RANGE_TO_EXPR,
                RANGE_FULL_EXPR,
                RANGE_INCLUSIVE_EXPR,
                RANGE_TO_INCLUSIVE_EXPR));


        b.rule(RANGE_EXPR).is(EXPRESSION, "..", EXPRESSION);


        b.rule(RANGE_FROM_EXPR).is(EXPRESSION, "..");
        b.rule(RANGE_TO_EXPR).is("..", EXPRESSION);
        b.rule(RANGE_FULL_EXPR).is("..");
        b.rule(RANGE_INCLUSIVE_EXPR).is(EXPRESSION, "..=", EXPRESSION);
        b.rule(RANGE_TO_INCLUSIVE_EXPR).is("..=", EXPRESSION);
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
                "loop", BLOCK_EXPRESSION);
        b.rule(PREDICATE_LOOP_EXPRESSION).is(
                "while", EXPRESSION, //except struct expression
                BLOCK_EXPRESSION
        );
        b.rule(PREDICATE_PATTERN_LOOP_EXPRESSION).is(
                "while", "let", MATCH_ARM_PATTERNS, "=", EXPRESSION, //except struct expression
                BLOCK_EXPRESSION
        );
        b.rule(ITERATOR_LOOP_EXPRESSION).is(
                "for", PATTERN, "IN", EXPRESSION, //except struct expression
                BLOCK_EXPRESSION
        );
        b.rule(LOOP_LABEL).is(LIFETIME_OR_LABEL, ":");
        b.rule(BREAK_EXPRESSION).is("break", b.optional(LIFETIME_OR_LABEL), b.optional(EXPRESSION));
        b.rule(CONTINUE_EXPRESSION).is(
                "continue", b.optional(LIFETIME_OR_LABEL));


    }

    private static void field(LexerlessGrammarBuilder b) {
        b.rule(FIELD_EXPRESSION).is(EXPRESSION, ".", IDENTIFIER);
    }

    private static void methodcall(LexerlessGrammarBuilder b) {

        b.rule(METHOD_CALL_EXPRESSION).is(
                EXPRESSION, ".", PATH_EXPR_SEGMENT,
                "(", b.optional(CALL_PARAMS), ")"
        );


    }

    private static void call(LexerlessGrammarBuilder b) {
        b.rule(CALL_EXPRESSION).is(EXPRESSION, "(", b.optional(CALL_PARAMS), ")");
        b.rule(CALL_PARAMS).is(EXPRESSION, b.zeroOrMore(b.sequence("(", EXPRESSION, ")")), b.optional(","));

    }

    /* https://doc.rust-lang.org/reference/expressions/enum-variant-expr.html */
    private static void enums(LexerlessGrammarBuilder b) {
        b.rule(ENUMERATION_VARIANT_EXPRESSION).is(b.firstOf(
                ENUM_EXPR_STRUCT,
                ENUM_EXPR_TUPLE,
                ENUM_EXPR_FIELDLESS
        ));
        b.rule(ENUM_EXPR_STRUCT).is(PATH_IN_EXPRESSION, "{",
                b.optional(ENUM_EXPR_FIELDS), "}"
        );
        b.rule(ENUM_EXPR_FIELDS).is(
                ENUM_EXPR_FIELD,
                b.zeroOrMore(b.sequence(",", ENUM_EXPR_FIELDS), b.optional("'"))
        );
        b.rule(ENUM_EXPR_FIELD).is(b.firstOf(IDENTIFIER,
                b.sequence(b.firstOf(IDENTIFIER, TUPLE_INDEX), ":", EXPRESSION)
        ));
        b.rule(ENUM_EXPR_TUPLE).is(
                PATH_IN_EXPRESSION, "(",
                b.optional(b.sequence(
                        EXPRESSION,
                        b.zeroOrMore(b.sequence(",", EXPRESSION)),
                        b.optional("'")
                )), ")"
        );
        b.rule(ENUM_EXPR_FIELDLESS).is(PATH_IN_EXPRESSION);
    }

    private static void tuple(LexerlessGrammarBuilder b) {
        b.rule(TUPLE_EXPRESSION).is("(", b.zeroOrMore(INNER_ATTRIBUTE),
                b.optional(TUPLE_ELEMENT),
                ")");
        b.rule(TUPLE_ELEMENT).is(b.oneOrMore(b.sequence(EXPRESSION, ",")), b.optional(EXPRESSION));
        b.rule(TUPLE_INDEXING_EXPRESSION).is(EXPRESSION, ".", TUPLE_INDEX);


    }

    private static void array(LexerlessGrammarBuilder b) {
        b.rule(ARRAY_EXPRESSION).is("[", b.zeroOrMore(INNER_ATTRIBUTE),
                b.optional(ARRAY_ELEMENTS),
                "]");

        b.rule(ARRAY_ELEMENTS).is(b.firstOf(
                b.sequence(EXPRESSION, b.zeroOrMore(b.sequence(",", EXPRESSION)), b.optional(",")),
                b.sequence(EXPRESSION, ";", EXPRESSION)
        ));


        b.rule(INDEX_EXPRESSION).is(EXPRESSION, "[", EXPRESSION, "]");
    }

    private static void grouped(LexerlessGrammarBuilder b) {
        b.rule(GROUPED_EXPRESSION).is("(", b.zeroOrMore(INNER_ATTRIBUTE), EXPRESSION);
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
                b.sequence(b.firstOf("&", "&&"), EXPRESSION),
                b.sequence(b.firstOf("&", "&&"), "mut", EXPRESSION)
        ));
        b.rule(DEREFERENCE_EXPRESSION).is("*", EXPRESSION);
        b.rule(ERROR_PROPAGATION_EXPRESSION).is(EXPRESSION, "?");
        b.rule(NEGATION_EXPRESSION).is(b.firstOf(
                b.sequence("-", EXPRESSION), b.sequence("!", EXPRESSION)
        ));

        b.rule(ARITHMETIC_OR_LOGICAL_EXPRESSION).is(b.firstOf(
                b.sequence(EXPRESSION, "+", EXPRESSION),
                b.sequence(EXPRESSION, "-", EXPRESSION),
                b.sequence(EXPRESSION, "*", EXPRESSION),
                b.sequence(EXPRESSION, "/", EXPRESSION),
                b.sequence(EXPRESSION, "%", EXPRESSION),
                b.sequence(EXPRESSION, "&", EXPRESSION),
                b.sequence(EXPRESSION, "|", EXPRESSION),
                b.sequence(EXPRESSION, "^", EXPRESSION),
                b.sequence(EXPRESSION, "<<", EXPRESSION),
                b.sequence(EXPRESSION, ">>", EXPRESSION)
        ));
        b.rule(COMPARISON_EXPRESSION).is(b.firstOf(
                b.sequence(EXPRESSION, "==", EXPRESSION),
                b.sequence(EXPRESSION, "!=", EXPRESSION),
                b.sequence(EXPRESSION, ">", EXPRESSION),
                b.sequence(EXPRESSION, "<", EXPRESSION),
                b.sequence(EXPRESSION, ">=", EXPRESSION),
                b.sequence(EXPRESSION, "<=", EXPRESSION)
        ));
        b.rule(LAZY_BOOLEAN_EXPRESSION).is(b.firstOf(
                b.sequence(EXPRESSION, "||", EXPRESSION),
                b.sequence(EXPRESSION, "&&", EXPRESSION)
        ));
        b.rule(TYPE_CAST_EXPRESSION).is(EXPRESSION, "as", TYPE_NO_BOUNDS);
        b.rule(ASSIGNMENT_EXPRESSION).is(EXPRESSION, "=", EXPRESSION);
        b.rule(COMPOUND_ASSIGNMENT_EXPRESSION).is(b.firstOf(
                b.sequence(EXPRESSION, "+=", EXPRESSION),
                b.sequence(EXPRESSION, "-=", EXPRESSION),
                b.sequence(EXPRESSION, "*=", EXPRESSION),
                b.sequence(EXPRESSION, "/=", EXPRESSION),
                b.sequence(EXPRESSION, "%=", EXPRESSION),
                b.sequence(EXPRESSION, "&=", EXPRESSION),
                b.sequence(EXPRESSION, "|=", EXPRESSION),
                b.sequence(EXPRESSION, "^=", EXPRESSION),
                b.sequence(EXPRESSION, "<<=", EXPRESSION),
                b.sequence(EXPRESSION, ">>=", EXPRESSION)
        ));


    }

    private static void block(LexerlessGrammarBuilder b) {
        b.rule(BLOCK_EXPRESSION).is("{", b.zeroOrMore(INNER_ATTRIBUTE),
                b.optional(STATEMENTS), "}"
        );
        b.rule(STATEMENTS).is(b.firstOf(
                b.oneOrMore(STATEMENT),
                b.sequence(b.oneOrMore(STATEMENT), EXPRESSION_WITHOUT_BLOCK),
                EXPRESSION_WITHOUT_BLOCK
        ));
        b.rule(ASYNC_BLOCK_EXPRESSION).is("async", b.optional("move"), BLOCK_EXPRESSION);
        b.rule(UNSAFE_BLOCK_EXPRESSION).is("unsafe", BLOCK_EXPRESSION);

    }

    private static void path(LexerlessGrammarBuilder b) {
        b.rule(PATH_EXPRESSION).is(b.firstOf(PATH_IN_EXPRESSION, QUALIFIED_PATH_IN_EXPRESSION));
    }

    private static void literal(LexerlessGrammarBuilder b) {
        b.rule(LITTERAL_EXPRESSION).is(b.firstOf(
                CHAR_LITERAL,
                STRING_LITERAL
                , RAW_STRING_LITERAL
                , BYTE_LITERAL
                , BYTE_STRING_LITERAL
                , RAW_BYTE_STRING_LITERAL
                , INTEGER_LITERAL
                , FLOAT_LITERAL
                , BOOLEAN_LITERAL
        ));

    }


    /* https://doc.rust-lang.org/reference/expressions/struct-expr.html */
    public static void struct(LexerlessGrammarBuilder b) {
        b.rule(STRUCT_EXPRESSION).is(b.firstOf(
                STRUCT_EXPR_STRUCT,
                STRUCT_EXPR_TUPLE,
                STRUCT_EXPR_UNIT));
        b.rule(STRUCT_EXPR_STRUCT).is(PATH_IN_EXPRESSION, "{", b.zeroOrMore(INNER_ATTRIBUTE),
                b.optional(b.firstOf(STRUCT_EXPR_FIELDS, STRUCT_BASE)));
        b.rule(STRUCT_EXPR_FIELDS).is(STRUCT_EXPR_FIELD,
                b.zeroOrMore(b.sequence(",", STRUCT_EXPR_FIELD)),
                b.firstOf(
                        b.sequence(",", STRUCT_BASE),
                        b.optional(","))
        );
        b.rule(STRUCT_EXPR_FIELD).is(b.firstOf(EXPRESSION,
                b.sequence(b.firstOf(IDENTIFIER, TUPLE_INDEX), ":", EXPRESSION)
        ));
        b.rule(STRUCT_BASE).is("..", EXPRESSION);
        b.rule(STRUCT_EXPR_TUPLE).is(
                PATH_IN_EXPRESSION, "(",
                b.zeroOrMore(INNER_ATTRIBUTE),
                b.optional(
                        b.sequence(
                                EXPRESSION,
                                b.zeroOrMore(b.sequence(",", EXPRESSION)),
                                b.optional("'")
                        )
                ),
                ")"
        );
        b.rule(STRUCT_EXPR_UNIT).is(PATH_IN_EXPRESSION);
    }

    /* https://doc.rust-lang.org/reference/attributes.html*/
    public static void attributes(LexerlessGrammarBuilder b) {
        b.rule(INNER_ATTRIBUTE).is("#", "!", ATTR);
        b.rule(OUTER_ATTRIBUTE).is("#", ATTR);
        b.rule(ATTR).is(SIMPLE_PATH, b.optional(ATTR_INPUT));
        b.rule(ATTR_INPUT).is(b.firstOf(DELIM_TOKEN_TREE, b.sequence("=", LITTERAL_EXPRESSION)));
    }

    /* https://doc.rust-lang.org/reference/expressions/closure-expr.html*/
    public static void closure(LexerlessGrammarBuilder b) {
        b.rule(CLOSURE_EXPRESSION).is(b.sequence
                (b.optional("move"),
                        b.firstOf("||", b.sequence("|", b.optional(CLOSURE_PARAMETERS), "|"))));
        b.rule(CLOSURE_PARAMETERS).is(CLOSURE_PARAM,
                b.zeroOrMore(b.sequence(",", CLOSURE_PARAM)),
                b.optional("'")
        );
        b.rule(CLOSURE_PARAM).is(b.zeroOrMore(OUTER_ATTRIBUTE),
                PATTERN,
                b.optional(b.sequence(":", TYPE))
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
        b.rule(TRAIT_OBJECT_TYPE).is(b.optional("dyn"), TYPE_PARAM_BOUNDS);
        b.rule(TRAIT_OBJECT_TYPE_ONE_BOUND).is(b.optional("dyn"), TRAIT_BOUND);
        b.rule(RAW_POINTER_TYPE).is("*", b.firstOf("mut", "const"), TYPE_NO_BOUNDS);
        b.rule(INFERRED_TYPE).is("_");
        b.rule(SLICE_TYPE).is("[", TYPE, "]");
        b.rule(ARRAY_TYPE).is("[", TYPE, ";", EXPRESSION, "]");
        b.rule(IMPL_TRAIT_TYPE).is("impl", TYPE_PARAM_BOUNDS);
        b.rule(IMPL_TRAIT_TYPE_ONE_BOUND).is("impl", TRAIT_BOUND);
        b.rule(NEVER_TYPE).is("!");

    }

    /* https://doc.rust-lang.org/reference/trait-bounds.html */
    public static void trait(LexerlessGrammarBuilder b) {
        b.rule(TYPE_PARAM_BOUNDS).is(TYPE_PARAM_BOUND,
                b.zeroOrMore(b.sequence("+", TYPE_PARAM_BOUND)),
                b.optional("+")
        );
        b.rule(TYPE_PARAM_BOUND).is(b.firstOf(LIFETIME, TRAIT_BOUND));
        b.rule(TRAIT_BOUND).is(b.firstOf(
                b.sequence(b.optional("?"), b.optional(FOR_LIFETIMES), TYPE_PATH),
                b.sequence("(", b.optional("?"), b.optional(FOR_LIFETIMES), TYPE_PATH, ")")
        ));
        b.rule(LIFETIME_BOUNDS).is(
                b.zeroOrMore(b.sequence(LIFETIME, "+")),
                b.optional(LIFETIME)
        );
        b.rule(LIFETIME).is(b.firstOf(LIFETIME_OR_LABEL, "'static", "'_"));
    }

    public static void tupletype(LexerlessGrammarBuilder b) {

        b.rule(TUPLE_TYPE).is(b.firstOf(
                b.sequence("(", ")"),
                b.sequence("(", b.oneOrMore(b.sequence(TYPE, ",")), b.optional(TYPE), ")")
        ));


    }

    /* https://doc.rust-lang.org/reference/types/pointer.html */
    public static void pointer(LexerlessGrammarBuilder b) {
        b.rule(REFERENCE_TYPE).is("&", b.optional(LIFETIME),
                b.optional("mut"), TYPE_PARAM_BOUNDS);
    }

    /* https://doc.rust-lang.org/reference/paths.html */
    public static void lexicalpath(LexerlessGrammarBuilder b) {
        b.rule(SIMPLE_PATH).is(
                b.optional("::"),
                SIMPLE_PATH_SEGMENT,
                b.zeroOrMore(b.sequence("::", SIMPLE_PATH_SEGMENT))
        );
        b.rule(SIMPLE_PATH_SEGMENT).is(b.firstOf(
                IDENTIFIER, "super", "self", "crate", "$crate"
        ));

        b.rule(PATH_IN_EXPRESSION).is(
                b.optional("::"),
                PATH_EXPR_SEGMENT,
                b.zeroOrMore(b.sequence("::", PATH_EXPR_SEGMENT))
        );

        b.rule(PATH_EXPR_SEGMENT).is(
                PATH_IDENT_SEGMENT, b.optional(b.sequence("::", GENERIC_ARGS))
        );
        b.rule(PATH_IDENT_SEGMENT).is(b.firstOf(IDENTIFIER,
                "super", "self", "Self", "crate", "$crate"
        ));
        b.rule(GENERIC_ARGS).is(b.firstOf(
                b.sequence("<", ">"),
                b.sequence("<", GENERIC_ARGS_LIFETIMES, b.optional(","), ">"),
                b.sequence("<", GENERIC_ARGS_BINDINGS, b.optional(","), ">"),
                b.sequence("<", GENERIC_ARGS_TYPES, b.optional(","), ">"),
                b.sequence("<", GENERIC_ARGS_TYPES, GENERIC_ARGS_BINDINGS, b.optional(","), ">"),
                b.sequence("<", GENERIC_ARGS_LIFETIMES, GENERIC_ARGS_TYPES, b.optional(","), ">"),
                b.sequence("<", GENERIC_ARGS_LIFETIMES, GENERIC_ARGS_BINDINGS, b.optional(","), ">"),
                b.sequence("<", GENERIC_ARGS_LIFETIMES, GENERIC_ARGS_TYPES, GENERIC_ARGS_BINDINGS, b.optional(","), ">")
        ));
        b.rule(GENERIC_ARGS_LIFETIMES).is(
                LIFETIME, b.zeroOrMore(b.sequence(",", LIFETIME))
        );
        b.rule(GENERIC_ARGS_TYPES).is(
                TYPE, b.zeroOrMore(b.sequence(",", TYPE))
        );
        b.rule(GENERIC_ARGS_BINDINGS).is(
                GENERIC_ARGS_BINDING, b.zeroOrMore(b.sequence(",", GENERIC_ARGS_BINDING))
        );
        b.rule(GENERIC_ARGS_BINDING).is(
                IDENTIFIER, "=", TYPE
        );

        b.rule(QUALIFIED_PATH_IN_EXPRESSION).is(
                QUALIFIED_PATH_TYPE, b.oneOrMore(b.sequence("::", PATH_EXPR_SEGMENT)));

        b.rule(QUALIFIED_PATH_TYPE).is(
                "<", TYPE, b.optional(b.sequence("as", TYPE_PATH)), ">"
        );

        b.rule(QUALIFIED_PATH_IN_TYPE).is(QUALIFIED_PATH_TYPE, b.oneOrMore(
                b.sequence("::", TYPE_PATH_SEGMENT)

        ));
        b.rule(TYPE_PATH).is(
                b.optional("::"), TYPE_PATH_SEGMENT, b.zeroOrMore(b.sequence("::", TYPE_PATH_SEGMENT))
        );
        b.rule(TYPE_PATH_SEGMENT).is(
                PATH_IDENT_SEGMENT,
                b.optional("::"),
                b.optional(b.firstOf(GENERIC_ARGS, TYPE_PATH_FN))
        );
        b.rule(TYPE_PATH_FN).is(
                "(",
                b.optional(TYPE_PATH_FN_INPUTS),
                ")",
                b.optional(b.sequence("->", TYPE))
        );
        b.rule(TYPE_PATH_FN_INPUTS).is(
                TYPE,
                b.zeroOrMore(b.sequence(",", TYPE)),
                b.optional(",")
        );


    }

    private static String toRegexp(String[] arr) {
        StringBuilder sb = new StringBuilder("[");
        //assume non empty array
        sb.append("\"");
        sb.append(arr[0]);
        sb.append("\"");

        for (int i = 1; i < arr.length; i++) {
            sb.append("|");
            sb.append("\"");
            sb.append(arr[i]);
            sb.append("\"");
        }
        sb.append("]");
        return sb.toString();
    }

    public static void lexicaltoken(LexerlessGrammarBuilder b) {


        b.rule(TOKEN).is(b.firstOf(IDENTIFIER_OR_KEYWORD, LITERALS, LIFETIMES, PUNCTUATION, DELIMITERS));

        b.rule(LIFETIME_OR_LABEL).is("'", NON_KEYWORD_IDENTIFIER);

        identifiers(b);

        b.rule(LIFETIME_TOKEN).is(b.firstOf(
                b.sequence("'", IDENTIFIER_OR_KEYWORD),
                b.sequence("'", "_")
        ));
        b.rule(LIFETIMES).is(b.firstOf(LIFETIME_TOKEN, LIFETIME_OR_LABEL)); //not explicit in reference
        //LITERALS are  not explicitly listed like below
        b.rule(LITERALS).is(b.firstOf(CHAR_LITERAL, STRING_LITERAL,
                RAW_STRING_LITERAL, BYTE_LITERAL, BYTE_STRING_LITERAL,RAW_BYTE_STRING_LITERAL, INTEGER_LITERAL,
                FLOAT_LITERAL, BOOLEAN_LITERAL, LIFETIME_TOKEN, LIFETIME_OR_LABEL,PUNCTUATION,DELIMITERS
        ));


        b.rule(DELIMITERS).is(b.firstOf("{","}", "[","]","(",")"));

        characters(b);
        bytes(b);
        integerliteral(b);
        floatliteral(b);
        b.rule(BOOLEAN_LITERAL).is(b.firstOf("true","false"));

    }

    private static void floatliteral(LexerlessGrammarBuilder b) {
        b.rule(FLOAT_LITERAL).is(b.firstOf(
                b.sequence(DEC_LITERAL,"."),//(not immediately followed by ., _ or an identifier)
                b.sequence(DEC_LITERAL, FLOAT_EXPONENT),
                b.sequence(DEC_LITERAL, ".", DEC_LITERAL, b.optional(FLOAT_EXPONENT)),
                b.sequence(DEC_LITERAL,b.optional(b.sequence(".", DEC_LITERAL)), b.optional(FLOAT_EXPONENT), FLOAT_SUFFIX)
        ), SPACING);
        b.rule(FLOAT_EXPONENT).is(b.sequence(
                b.firstOf("e","E"), b.optional(b.firstOf("+","-")),
                b.zeroOrMore(b.firstOf(DEC_DIGIT,"_")), DEC_DIGIT,b.zeroOrMore(b.firstOf(DEC_DIGIT,"_"))
        ));
        b.rule(FLOAT_SUFFIX).is(b.firstOf("f32","f64"));
    }

    private static void bytes(LexerlessGrammarBuilder b) {

        b.rule(BYTE_LITERAL).is(b.firstOf(
                b.regexp("^b\\'"+"[^\\'\\n\\r\\t\\\\].*"+"\\'"),
                b.sequence("b'",BYTE_ESCAPE,"'")
                ));

        b.rule(ASCII_FOR_CHAR).is(b.regexp("[^\\'\\n\\r\\t\\\\].*"));
        b.rule(ASCII_FOR_STRING).is(b.regexp("[^\"\\r\\\\].*"));// except ", \ and IsolatedCR (lookahead? (?![m-o])[a-z])

        b.rule(BYTE_STRING_LITERAL).is(b.firstOf(
                b.regexp("^b\""+"[^\"\\r\\\\].*"+"\""),
                b.sequence("b\"",BYTE_ESCAPE,"\"")
        ));


        b.rule(BYTE_ESCAPE).is(b.firstOf(b.sequence("\\x", HEX_DIGIT, HEX_DIGIT), "\\n", "\\r", "\\t", "\\", "\\0"));

        b.rule(RAW_BYTE_STRING_LITERAL).is("br",RAW_BYTE_STRING_CONTENT);
        b.rule(RAW_BYTE_STRING_CONTENT).is(b.firstOf(
                b.sequence("\"", b.zeroOrMore(ASCII), "\""),
                b.sequence("#",RAW_STRING_CONTENT,"#")
        ));
        b.rule(ASCII).is(b.regexp("[\\x00-\\x7F]"));


    }



    private static void identifiers(LexerlessGrammarBuilder b) {
        b.rule(IDENTIFIER_OR_KEYWORD).is(b.firstOf(b.regexp("^"+IDF_REGEXP1), b.regexp("^"+IDF_REGEXP2)));
        b.rule(RAW_IDENTIFIER).is(b.firstOf(b.regexp("^r#"+IDF_REGEXP1+"(?<!r#(crate|self|super|Self))"), b.regexp("^r#"+IDF_REGEXP2)));;//except Except crate, self, super, Self
        b.rule(NON_KEYWORD_IDENTIFIER).is(b.regexp("^"+IDF_REGEXP1 +exceptKeywords()));//Except a strict or reserved keyword
        b.rule(IDENTIFIER).is(b.firstOf(RAW_IDENTIFIER,NON_KEYWORD_IDENTIFIER));
    }

    private static String exceptKeywords() {
        StringBuilder sb= new StringBuilder("(?<!(");
        String[] values = RustKeyword.keywordValues();
        sb.append(values[0]);
        for(String kw : values){
            sb.append("|");
            sb.append(kw);
        }
        sb.append("))");

        return sb.toString();

    }

    private static void  characters(LexerlessGrammarBuilder b) {

        b.rule(QUOTE_ESCAPE).is(b.firstOf("\\'", "\\\""));
        b.rule(ASCII_ESCAPE).is(b.firstOf(b.sequence("\\x", OCT_DIGIT, HEX_DIGIT),
                "\\n" , "\\r", "\\t" , "\\","\0"));
        b.rule(UNICODE_ESCAPE).is("\\u{", b.oneOrMore(b.sequence(HEX_DIGIT,b.zeroOrMore("_"))), "}");
        b.rule(STRING_LITERAL).is("\"", b.zeroOrMore(b.firstOf(
                b.regexp("[^\" \\ \\r \\n].*"),QUOTE_ESCAPE
                , ASCII_ESCAPE
                , UNICODE_ESCAPE
                , STRING_CONTINUE)));
        b.rule(STRING_CONTINUE).is("\\\n");
        b.rule(RAW_STRING_LITERAL).is("r", RAW_STRING_CONTENT);
        b.rule(RAW_STRING_CONTENT).is(b.firstOf(
                b.regexp("^\"[^\\r\\n].*\""),
                b.sequence("#",RAW_STRING_CONTENT, "#")));


    }

    /* https://doc.rust-lang.org/reference/tokens.html#integer-literals */
    private static void integerliteral(LexerlessGrammarBuilder b) {
        b.rule(INTEGER_LITERAL).is(b.firstOf(DEC_LITERAL, BIN_LITERAL, OCT_LITERAL, HEX_LITERAL), b.optional(INTEGER_SUFFIX),SPACING);
        b.rule(DEC_LITERAL).is(DEC_DIGIT, b.optional(b.firstOf(DEC_DIGIT, "_")));
        b.rule(TUPLE_INDEX).is(b.firstOf("0", b.sequence(b.zeroOrMore(NON_ZERO_DEC_DIGIT),DEC_DIGIT)));

        b.rule(BIN_LITERAL).is("0b", b.zeroOrMore(b.firstOf(BIN_DIGIT, "_")), BIN_DIGIT,
                b.zeroOrMore(b.firstOf(BIN_DIGIT, "_")));
        b.rule(OCT_LITERAL).is("0o", b.zeroOrMore(b.firstOf(OCT_DIGIT, "_")), OCT_DIGIT,
                b.zeroOrMore(b.firstOf(OCT_DIGIT, "_")));
        b.rule(HEX_LITERAL).is("0x", b.zeroOrMore(b.firstOf(HEX_DIGIT, "_")), HEX_DIGIT,
                b.zeroOrMore(b.firstOf(HEX_DIGIT, "_")));
        b.rule(BIN_DIGIT).is(b.regexp("[0-1]"));
        b.rule(OCT_DIGIT).is(b.regexp("[0-7]"));
        b.rule(DEC_DIGIT).is(b.regexp("[0-9]"));
        b.rule(NON_ZERO_DEC_DIGIT).is(b.regexp("[1-9]"));
        b.rule(HEX_DIGIT).is(b.regexp("[0-9a-fA-F]"));
        b.rule(INTEGER_SUFFIX).is(b.firstOf("u8", "u16", "u32", "u64", "u128", "usize"
                , "i8", "i16", "i32", "i64", "i128", "isize"));

    }


}