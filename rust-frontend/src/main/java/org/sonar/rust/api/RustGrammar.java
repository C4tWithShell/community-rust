package org.sonar.rust.api;

import com.sonar.sslr.api.Grammar;
import org.sonar.sslr.grammar.GrammarRuleKey;
import org.sonar.sslr.grammar.LexerfulGrammarBuilder;
import org.sonar.sslr.internal.vm.PatternExpression;

import static com.sonar.sslr.api.GenericTokenType.EOF;
import static com.sonar.sslr.api.GenericTokenType.IDENTIFIER;
import static org.sonar.rust.api.RustTokenType.NEWLINE;


public enum RustGrammar implements GrammarRuleKey {
    ARITHMETIC_OR_LOGICAL_EXPRESSION,
    ARRAY_ELEMENTS,
    ARRAY_EXPRESSION,
    ARRAY_TYPE,
    ASCII_ESCAPE,
    ASSIGNMENT_EXPRESSION,
    ASYNC_BLOCK_EXPRESSION,
    ATTR,
    ATTR_INPUT,
    AWAIT_EXPRESSION,
    BARE_FUNCTION_TYPE,
    BLOCK_EXPRESSION,
    BOOLEAN_LITERAL,
    BORROW_EXPRESSION,
    BREAK_EXPRESSION,
    BYTE_LITERAL,
    BYTE_STRING_LITERAL,
    CALL_EXPRESSION,
    CALL_PARAMS,
    CHAR_LITERAL,
    CLOSURE_EXPRESSION,
    CLOSURE_PARAM,
    CLOSURE_PARAMETERS,
    COMPARISON_EXPRESSION,
    COMPOUND_ASSIGNMENT_EXPRESSION,
    CONTINUE_EXPRESSION,
    DEC_DIGIT,
    DELIM_TOKEN_TREE,
    DEREFERENCE_EXPRESSION,
    ENUMERATION_VARIANT_EXPRESSION,
    ENUM_EXPR_FIELD,
    ENUM_EXPR_FIELDLESS,
    ENUM_EXPR_FIELDS,
    ENUM_EXPR_STRUCT,
    ENUM_EXPR_TUPLE,
    ERROR_PROPAGATION_EXPRESSION,
    EXPRESSION,
    EXPRESSION_STATEMENT,
    EXPRESSION_WITHOUT_BLOCK,
    EXPRESSION_WITH_BLOCK,
    FIELD_EXPRESSION,
    FILE_INPUT,
    FLOAT_LITERAL,
    FOR_LIFE_TIMES,
    GENERIC_ARGS,
    GENERIC_ARGS_BINDING,
    GENERIC_ARGS_BINDINGS,
    GENERIC_ARGS_LIFETIMES,
    GENERIC_ARGS_TYPES,
    GROUPED_EXPRESSION,
    GROUPED_PATTERN,
    IDENTIFIER_OR_KEYWORD,
    IDENTIFIER_PATTERN,
    IF_EXPRESSION,
    IF_LET_EXPRESSION,
    IMPL_TRAIT_TYPE,
    IMPL_TRAIT_TYPE_ONE_BOUND,
    INDEX_EXPRESSION,
    INFERRED_TYPE,
    INFINITE_LOOP_EXPRESSION,
    INNER_ATTRIBUTE,
    INTEGER_LITERAL,
    ISOLATED_CR,
    ITEM,
    ITERATOR_LOOP_EXPRESSION,
    LAZY_BOOLEAN_EXPRESSION,
    LET_STATEMENT,
    LIFETIME,
    LIFETIME_BOUNDS,
    LIFETIME_OR_LABEL,
    LIFE_TIME_TOKEN,
    LITERAL_PATTERN,
    LITTERAL_EXPRESSION,
    LOOP_EXPRESSION,
    LOOP_LABEL,
    MACRO_INVOCATION,
    MACRO_INVOCATION_SEMI,
    MACRO_INVOCATIONSEMI,
    MATCH_ARM,
    MATCH_ARMS,
    MATCH_ARM_GUARD,
    MATCH_ARM_PATTERNS,
    MATCH_EXPRESSION,
    METHOD_CALL_EXPRESSION,
    NEGATION_EXPRESSION,
    NEVER_TYPE,
    NON_KEYWORD_IDENTIFIER,
    NON_ZERO_DEC_DIGIT,
    OPERATOR_EXPRESSION,
    OUTER_ATTRIBUTE,
    PARENTHESIZED_TYPE,
    PATH_EXPRESSION,
    PATH_EXPRESSION_SEGMENT,
    PATH_EXPR_SEGMENT,
    PATH_IDENT_SEGMENT,
    PATH_IN_EXPRESSION,
    PATH_PATTERN,
    PATTERN,
    PREDICATE_LOOP_EXPRESSION,
    PREDICATE_PATTERN_LOOP_EXPRESSION,
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
    RAW_BYTE_STRING_LITERAL,
    RAW_POINTER_TYPE,
    RAW_STRING_LITERAL,
    REFERENCE_PATTERN,
    REFERENCE_TYPE,
    RETURN_EXPRESSION,
    SIMPLE_PATH,
    SIMPLE_PATH_SEGMENT,
    SLICE_PATTERN,
    SLICE_TYPE,
    STATEMENT,
    STATEMENTS,
    STRING_CONTINUE,
    STRING_LITERAL,
    STRUCT_BASE,
    STRUCT_EXPRESSION,
    STRUCT_EXPR_FIELD,
    STRUCT_EXPR_FIELDS,
    STRUCT_EXPR_STRUCT,
    STRUCT_EXPR_TUPLE,
    STRUCT_EXPR_UNIT,
    STRUCT_PATTERN,
    STRUCT_PATTERN_ELEMENTS,
    STRUCT_PATTERN_ETCETERA,
    STRUCT_PATTERN_FIELDS,
    STRUCT_PATTERN_FIELD,
    TOKEN,
    TOKEN_TREE,
    TRAIT_BOUND,
    TRAIT_OBJECT_TYPE,
    TRAIT_OBJECT_TYPE_ONE_BOUND,
    TUPLE_ELEMENT,
    TUPLE_EXPRESSION,
    TUPLE_INDEX,
    TUPLE_INDEXING_EXPRESSION,
    TUPLE_PATTERN,
    TUPLE_PATTERN_ITEMS,
    TUPLE_STRUCT_ITEMS,
    TUPLE_STRUCT_PATTERN,
    TUPLE_TYPE,
    TYPE,
    TYPE_CAST_EXPRESSION,
    TYPE_NO_BOUNDS,
    TYPE_PARAM_BOUND,
    TYPE_PARAM_BOUNDS,
    TYPE_PATH,
    TYPE_PATH_FN,
    TYPE_PATH_FN_INPUTS,
    TYPE_PATH_SEGMENT,
    UNICODE_ESCAPE,
    UNSAFE_BLOCK_EXPRESSION,
    WILDCARD_PATTERN;

    public static Grammar create() {
        LexerfulGrammarBuilder b = LexerfulGrammarBuilder.create();
        b.rule(FILE_INPUT).is(b.zeroOrMore(b.firstOf(NEWLINE, EXPRESSION)), EOF);
        lexical(b);
        macros(b);
        attributes(b);
        statement(b);
        expressions(b);
        patterns(b);
        types(b);

        b.setRootRule(FILE_INPUT);
        return b.buildWithMemoizationOfMatchesForAllRules();
    }

    /* https://doc.rust-lang.org/reference/macros.html */
    private static void macros(LexerfulGrammarBuilder b) {
        b.rule(MACRO_INVOCATION).is(b.sequence(
                SIMPLE_PATH,"!",DELIM_TOKEN_TREE
        ));
        b.rule(DELIM_TOKEN_TREE).is(b.firstOf(
                b.sequence("(", b.zeroOrMore(TOKEN_TREE), ")"),
                b.sequence("[", b.zeroOrMore(TOKEN_TREE), "]"),
                b.sequence("{", b.zeroOrMore(TOKEN_TREE), "}")
        ));
        b.rule(TOKEN_TREE).is(b.firstOf(
                TOKEN, // except delimiters
                DELIM_TOKEN_TREE
        ));
        b.rule(MACRO_INVOCATION_SEMI).is(b.firstOf(
                b.sequence(SIMPLE_PATH, "!", "(", b.zeroOrMore(TOKEN_TREE), ");"),
                b.sequence(SIMPLE_PATH, "!", "[", b.zeroOrMore(TOKEN_TREE), "];"),
                b.sequence(SIMPLE_PATH, "!", "{", b.zeroOrMore(TOKEN_TREE), "}")
        ));
    }

    private static void patterns(LexerfulGrammarBuilder b) {
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

        ));
        b.rule(IDENTIFIER_PATTERN).is(b.sequence(
                b.optional("ref"),
                b.optional("mut"),
                IDENTIFIER,
                b.optional(b.sequence("@",PATTERN))
        ));
        b.rule(WILDCARD_PATTERN).is("_");
        b.rule(RANGE_PATTERN).is(b.firstOf(
                b.sequence(RANGE_PATTERN_BOUND, "..=", RANGE_PATTERN_BOUND),
                b.sequence(RANGE_PATTERN_BOUND, "...", RANGE_PATTERN_BOUND)
        ));
        b.rule(REFERENCE_PATTERN).is(b.sequence(
                b.firstOf("&","&&"),
                b.optional("mut"),
                PATTERN
        ));
        b.rule(STRUCT_PATTERN).is(b.sequence(
                PATH_IN_EXPRESSION, "{",b.optional(STRUCT_PATTERN_ELEMENTS), "}"
        ));
        b.rule(STRUCT_PATTERN_ELEMENTS).is(b.firstOf(
                b.sequence(STRUCT_PATTERN_FIELDS,
                        b.optional(b.firstOf(
                                "'",
                                b.sequence(",", STRUCT_PATTERN_ETCETERA)
                        ))),STRUCT_PATTERN_ETCETERA

        ));
        b.rule(STRUCT_PATTERN_FIELDS).is(b.sequence(
                STRUCT_PATTERN_FIELD, b.zeroOrMore(b.sequence(",", STRUCT_PATTERN_FIELD))
        ));
        b.rule(STRUCT_PATTERN_FIELD).is(b.sequence(
                b.zeroOrMore(OUTER_ATTRIBUTE),"(",
                b.firstOf(
                        b.sequence(TUPLE_INDEX, ":", PATTERN),
                        b.sequence(IDENTIFIER, ":", PATTERN),
                        b.sequence(b.optional("ref"), b.optional("mut"), IDENTIFIER)
                )));
        b.rule(STRUCT_PATTERN_ETCETERA).is(b.sequence( b.zeroOrMore(OUTER_ATTRIBUTE), ".."));

        b.rule(TUPLE_STRUCT_PATTERN).is(b.sequence(
                PATH_IN_EXPRESSION,"(", b.optional(TUPLE_STRUCT_ITEMS),")"
        ));
        b.rule(TUPLE_STRUCT_ITEMS).is(b.firstOf(
                PATTERN, b.zeroOrMore(b.sequence(",", PATTERN), b.optional("'")),
                b.sequence(b.zeroOrMore(b.sequence(PATTERN,",")), "..",
                        b.optional(b.sequence(b.zeroOrMore(b.sequence(",",PATTERN)),
                                b.optional(",")
                                )))));
        b.rule(TUPLE_PATTERN).is(b.sequence("(", b.optional(TUPLE_PATTERN_ITEMS), ")"));
        b.rule(TUPLE_PATTERN_ITEMS).is(b.firstOf(
                b.sequence(PATTERN, ","),
                b.sequence(PATTERN, b.oneOrMore(b.sequence(",",PATTERN)), b.optional(",")),
                b.sequence(b.zeroOrMore(b.sequence(",",PATTERN)),"..",
                        b.optional(b.sequence(
                                b.oneOrMore(b.sequence(",",PATTERN)),
                                b.optional(","))))));
        b.rule(GROUPED_PATTERN).is(b.sequence("(", PATTERN, ")"));
        b.rule(SLICE_PATTERN).is(b.sequence("[", PATTERN,
                b.zeroOrMore(b.sequence(",",PATTERN)), b.optional(","), "]"
                ));
        b.rule(PATH_PATTERN).is(b.firstOf(PATH_IN_EXPRESSION, QUALIFIED_PATH_IN_EXPRESSION));
    }

    private static void types(LexerfulGrammarBuilder b) {
        //1
        type(b);
        //5
        tupletype(b);
        //13
        pointer(b);
        //15
        trait(b);


    }

    public static void statement(LexerfulGrammarBuilder b) {
        b.rule(STATEMENT).is(b.firstOf(
                ";",
                ITEM,
                LET_STATEMENT,
                EXPRESSION_STATEMENT,
                MACRO_INVOCATIONSEMI
        ));
    }

    /* https://doc.rust-lang.org/reference/expressions.html */
    public static void expressions(LexerfulGrammarBuilder b) {
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

    private static void await(LexerfulGrammarBuilder b) {
        b.rule(AWAIT_EXPRESSION).is(b.sequence(EXPRESSION, ".", "await"));
    }

    private static void return_expr(LexerfulGrammarBuilder b) {
        b.rule(RETURN_EXPRESSION).is(b.sequence("return", b.optional(EXPRESSION)));
    }

    private static void match(LexerfulGrammarBuilder b) {
        //https://doc.rust-lang.org/reference/expressions/match-expr.html
        b.rule(MATCH_EXPRESSION).is(b.sequence(
                "match", EXPRESSION, //except struct expressions !!
                "{",
                b.zeroOrMore(INNER_ATTRIBUTE),
                b.optional(MATCH_ARMS),
                "}"
        ));
        b.rule(MATCH_ARMS).is(b.sequence(

                b.zeroOrMore(b.sequence(
                        MATCH_ARM, "=>",
                        b.firstOf(
                                b.sequence(EXPRESSION_WITHOUT_BLOCK, ","),
                                b.sequence(EXPRESSION_WITH_BLOCK, b.optional(","))
                        )
                )),

                b.sequence(MATCH_ARM, "=>", EXPRESSION, b.optional(","))));
        b.rule(MATCH_ARM).is(b.sequence(
                b.zeroOrMore(OUTER_ATTRIBUTE),
                MATCH_ARM_PATTERNS,
                b.optional(MATCH_ARM_GUARD)
        ));
        b.rule(MATCH_ARM_PATTERNS).is(b.sequence(
                b.optional("|"),
                PATTERN,
                b.zeroOrMore(b.sequence("|", PATTERN))
        ));
        b.rule(MATCH_ARM_GUARD).is(b.sequence("if", EXPRESSION));

    }

    private static void if_expr(LexerfulGrammarBuilder b) {
        b.rule(IF_EXPRESSION).is(b.sequence(
                "if", EXPRESSION, //except struct expressions !!
                b.optional(b.sequence(
                        //else
                        "else", b.firstOf(BLOCK_EXPRESSION, IF_EXPRESSION, IF_LET_EXPRESSION)
                ))
        ));
        b.rule(IF_LET_EXPRESSION).is(b.sequence(
                "if", "let", MATCH_ARM_PATTERNS, "=", EXPRESSION, //except struct or lazy boolean operator expression
                BLOCK_EXPRESSION,
                b.optional(b.sequence(
                        //else
                        "else", b.firstOf(BLOCK_EXPRESSION, IF_EXPRESSION, IF_LET_EXPRESSION)
                ))
        ));
    }

    private static void range(LexerfulGrammarBuilder b) {
        b.rule(RANGE_EXPRESSION).is(b.firstOf(
                RANGE_EXPR,
                RANGE_FROM_EXPR,
                RANGE_TO_EXPR,
                RANGE_FULL_EXPR,
                RANGE_INCLUSIVE_EXPR,
                RANGE_TO_INCLUSIVE_EXPR));
        b.rule(RANGE_EXPR).is(b.sequence(EXPRESSION, "..", EXPRESSION));
        b.rule(RANGE_FROM_EXPR).is(b.sequence(EXPRESSION, ".."));
        b.rule(RANGE_TO_EXPR).is(b.sequence("..", EXPRESSION));
        b.rule(RANGE_FULL_EXPR).is("..");
        b.rule(RANGE_INCLUSIVE_EXPR).is(b.sequence(EXPRESSION, "..=", EXPRESSION));
        b.rule(RANGE_TO_INCLUSIVE_EXPR).is(b.sequence("..=", EXPRESSION));
    }

    private static void loops(LexerfulGrammarBuilder b) {
        b.rule(LOOP_EXPRESSION).is(b.sequence(b.optional(LOOP_LABEL),
                b.firstOf(
                        INFINITE_LOOP_EXPRESSION,
                        PREDICATE_LOOP_EXPRESSION,
                        PREDICATE_PATTERN_LOOP_EXPRESSION,
                        ITERATOR_LOOP_EXPRESSION)
        ));
        b.rule(INFINITE_LOOP_EXPRESSION).is(b.sequence(
                "loop", BLOCK_EXPRESSION));
        b.rule(PREDICATE_LOOP_EXPRESSION).is(b.sequence(
                "while", EXPRESSION, //except struct expression
                BLOCK_EXPRESSION
        ));
        b.rule(PREDICATE_PATTERN_LOOP_EXPRESSION).is(b.sequence(
                "while", "let", MATCH_ARM_PATTERNS, "=", EXPRESSION, //except struct expression
                BLOCK_EXPRESSION
        ));
        b.rule(ITERATOR_LOOP_EXPRESSION).is(b.sequence(
                "for", PATTERN, "IN", EXPRESSION, //except struct expression
                BLOCK_EXPRESSION
        ));
        b.rule(LOOP_LABEL).is(b.sequence(LIFETIME_OR_LABEL, ":"));
        b.rule(BREAK_EXPRESSION).is(b.sequence("break", b.optional(LIFETIME_OR_LABEL), b.optional(EXPRESSION)));
        b.rule(CONTINUE_EXPRESSION).is(b.sequence(
                "continue", b.optional(LIFETIME_OR_LABEL)));


    }

    private static void field(LexerfulGrammarBuilder b) {
        b.rule(FIELD_EXPRESSION).is(b.sequence(EXPRESSION, ".", IDENTIFIER));
    }

    private static void methodcall(LexerfulGrammarBuilder b) {
        b.rule(METHOD_CALL_EXPRESSION).is(b.sequence(
                EXPRESSION, ".", PATH_EXPRESSION_SEGMENT,
                "(", b.optional(CALL_PARAMS), ")"
        ));
    }

    private static void call(LexerfulGrammarBuilder b) {
        b.rule(CALL_EXPRESSION).is(b.sequence(EXPRESSION, "(", b.optional(CALL_PARAMS), ")"));
        b.rule(CALL_PARAMS).is(b.sequence(EXPRESSION,
                b.zeroOrMore(b.sequence("(", EXPRESSION, ")")), b.optional(",")));

    }

    /* https://doc.rust-lang.org/reference/expressions/enum-variant-expr.html */
    private static void enums(LexerfulGrammarBuilder b) {
        b.rule(ENUMERATION_VARIANT_EXPRESSION).is(b.firstOf(
                ENUM_EXPR_STRUCT,
                ENUM_EXPR_TUPLE,
                ENUM_EXPR_FIELDLESS
        ));
        b.rule(ENUM_EXPR_STRUCT).is(b.sequence(PATH_IN_EXPRESSION, "{",
                b.optional(ENUM_EXPR_FIELDS), "}"
        ));
        b.rule(ENUM_EXPR_FIELDS).is(b.sequence(
                ENUM_EXPR_FIELD,
                b.zeroOrMore(b.sequence(",", ENUM_EXPR_FIELDS), b.optional("'"))
        ));
        b.rule(ENUM_EXPR_FIELD).is(b.firstOf(IDENTIFIER,
                b.sequence(b.firstOf(IDENTIFIER, TUPLE_INDEX), ":", EXPRESSION)
        ));
        b.rule(ENUM_EXPR_TUPLE).is(b.sequence(
                PATH_IN_EXPRESSION, "(",
                b.optional(b.sequence(
                        EXPRESSION,
                        b.zeroOrMore(b.sequence(",", EXPRESSION)),
                        b.optional("'")
                )), ")"
        ));
        b.rule(ENUM_EXPR_FIELDLESS).is(PATH_IN_EXPRESSION);
    }

    private static void tuple(LexerfulGrammarBuilder b) {
        b.rule(TUPLE_EXPRESSION).is(b.sequence("(", b.zeroOrMore(INNER_ATTRIBUTE), b.optional(TUPLE_ELEMENT), ")"));
        b.rule(TUPLE_ELEMENT).is(b.oneOrMore(b.sequence(EXPRESSION, ",")), b.optional(EXPRESSION));
        b.rule(TUPLE_INDEXING_EXPRESSION).is(b.sequence(EXPRESSION, ".", TUPLE_INDEX));


    }

    private static void array(LexerfulGrammarBuilder b) {
        b.rule(ARRAY_EXPRESSION).is(b.sequence("[", b.zeroOrMore(INNER_ATTRIBUTE), b.optional(ARRAY_ELEMENTS)));
        b.rule(ARRAY_ELEMENTS).is(b.firstOf(
                b.sequence(EXPRESSION, b.zeroOrMore(b.sequence(",", EXPRESSION)), b.optional(",")),
                b.sequence(EXPRESSION, ";", EXPRESSION)
        ));
        b.rule(INDEX_EXPRESSION).is(b.sequence(EXPRESSION, "[", EXPRESSION, "]"));
    }

    private static void grouped(LexerfulGrammarBuilder b) {
        b.rule(GROUPED_EXPRESSION).is(b.sequence("(", b.zeroOrMore(INNER_ATTRIBUTE), EXPRESSION));
    }

    private static void operator(LexerfulGrammarBuilder b) {
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
        b.rule(DEREFERENCE_EXPRESSION).is(b.sequence("*", EXPRESSION));
        b.rule(ERROR_PROPAGATION_EXPRESSION).is(b.sequence(EXPRESSION, "?"));
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
        b.rule(TYPE_CAST_EXPRESSION).is(b.sequence(EXPRESSION, "as", TYPE_NO_BOUNDS));
        b.rule(ASSIGNMENT_EXPRESSION).is(b.sequence(EXPRESSION, "=", EXPRESSION));
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

    private static void block(LexerfulGrammarBuilder b) {
        b.rule(BLOCK_EXPRESSION).is(b.sequence("{", b.zeroOrMore(INNER_ATTRIBUTE),
                b.optional(STATEMENTS), "}"
        ));
        b.rule(STATEMENTS).is(b.firstOf(
                b.oneOrMore(STATEMENT),
                b.sequence(b.oneOrMore(STATEMENT), EXPRESSION_WITHOUT_BLOCK),
                EXPRESSION_WITHOUT_BLOCK
        ));
        b.rule(ASYNC_BLOCK_EXPRESSION).is(b.sequence("async", b.optional("move"), BLOCK_EXPRESSION));
        b.rule(UNSAFE_BLOCK_EXPRESSION).is(b.sequence("unsafe", BLOCK_EXPRESSION));

    }

    private static void path(LexerfulGrammarBuilder b) {
        b.rule(PATH_EXPRESSION).is(b.firstOf(PATH_IN_EXPRESSION, QUALIFIED_PATH_IN_EXPRESSION));
    }

    private static void literal(LexerfulGrammarBuilder b) {
        b.rule(LITTERAL_EXPRESSION).is(b.firstOf(
                CHAR_LITERAL,
                STRING_LITERAL,
                RAW_STRING_LITERAL,
                BYTE_LITERAL,
                BYTE_STRING_LITERAL,
                RAW_BYTE_STRING_LITERAL,
                INTEGER_LITERAL,
                FLOAT_LITERAL,
                BOOLEAN_LITERAL
        ));
        b.rule(BOOLEAN_LITERAL).is(b.firstOf("true", "false"));
    }


    /* https://doc.rust-lang.org/reference/expressions/struct-expr.html */
    public static void struct(LexerfulGrammarBuilder b) {
        b.rule(STRUCT_EXPRESSION).is(b.firstOf(
                STRUCT_EXPR_STRUCT,
                STRUCT_EXPR_TUPLE,
                STRUCT_EXPR_UNIT));
        b.rule(STRUCT_EXPR_STRUCT).is(b.sequence(PATH_IN_EXPRESSION, "{", b.zeroOrMore(INNER_ATTRIBUTE),
                b.optional(b.firstOf(STRUCT_EXPR_FIELDS, STRUCT_BASE))));
        b.rule(STRUCT_EXPR_FIELDS).is(b.sequence(STRUCT_EXPR_FIELD,
                b.zeroOrMore(b.sequence(",", STRUCT_EXPR_FIELD)),
                b.firstOf(
                        b.sequence(",", STRUCT_BASE),
                        b.optional(","))
        ));
        b.rule(STRUCT_EXPR_FIELD).is(b.firstOf(EXPRESSION,
                b.sequence(b.firstOf(IDENTIFIER, TUPLE_INDEX), ":", EXPRESSION)
        ));
        b.rule(STRUCT_BASE).is(b.sequence("..", EXPRESSION));
        b.rule(STRUCT_EXPR_TUPLE).is(b.sequence(
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
        ));
        b.rule(STRUCT_EXPR_UNIT).is(PATH_IN_EXPRESSION);
    }

    /* https://doc.rust-lang.org/reference/attributes.html*/
    public static void attributes(LexerfulGrammarBuilder b) {
        b.rule(INNER_ATTRIBUTE).is(b.sequence("#", "!", ATTR));
        b.rule(OUTER_ATTRIBUTE).is(b.sequence("#", ATTR));
        b.rule(ATTR).is(b.sequence(SIMPLE_PATH, b.optional(ATTR_INPUT)));
        b.rule(ATTR_INPUT).is(b.firstOf(DELIM_TOKEN_TREE, b.sequence("=", LITTERAL_EXPRESSION)));
    }

    /* https://doc.rust-lang.org/reference/expressions/closure-expr.html*/
    public static void closure(LexerfulGrammarBuilder b) {
        b.rule(CLOSURE_EXPRESSION).is(b.sequence
                (b.optional("move"),
                        b.firstOf("||", b.sequence("|", b.optional(CLOSURE_PARAMETERS), "|"))));
        b.rule(CLOSURE_PARAMETERS).is(b.sequence(CLOSURE_PARAM,
                b.zeroOrMore(b.sequence(",", CLOSURE_PARAM)),
                b.optional("'")
        ));
        b.rule(CLOSURE_PARAM).is(b.sequence(b.zeroOrMore(OUTER_ATTRIBUTE),
                PATTERN,
                b.optional(b.sequence(":", TYPE))
        ));
    }

    /* https://doc.rust-lang.org/reference/types.html#type-expressions */
    public static void type(LexerfulGrammarBuilder b) {
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
        b.rule(PARENTHESIZED_TYPE).is(b.sequence("(", TYPE, ")"));
        b.rule(TRAIT_OBJECT_TYPE).is(b.sequence(b.optional("dyn"), TYPE_PARAM_BOUNDS));
        b.rule(TRAIT_OBJECT_TYPE_ONE_BOUND).is(b.sequence(b.optional("dyn"), TRAIT_BOUND));
        b.rule(RAW_POINTER_TYPE).is(b.sequence("*", b.firstOf("mut", "const"), TYPE_NO_BOUNDS));
        b.rule(INFERRED_TYPE).is("_");
        b.rule(SLICE_TYPE).is(b.sequence("[", TYPE, "]"));
        b.rule(ARRAY_TYPE).is(b.sequence("[", TYPE, ";", EXPRESSION, "]"));
        b.rule(IMPL_TRAIT_TYPE).is(b.sequence("impl", TYPE_PARAM_BOUNDS));
        b.rule(IMPL_TRAIT_TYPE_ONE_BOUND).is(b.sequence("impl", TRAIT_BOUND));
        b.rule(NEVER_TYPE).is("!");
        b.rule(TUPLE_TYPE).is(b.firstOf(
                b.sequence("(", ")"),
                b.sequence("(", b.oneOrMore(b.sequence(TYPE, ",")), b.optional(TYPE), ")")
        ));
    }

    /* https://doc.rust-lang.org/reference/trait-bounds.html */
    public static void trait(LexerfulGrammarBuilder b) {
        b.rule(TYPE_PARAM_BOUNDS).is(b.sequence(TYPE_PARAM_BOUND,
                b.zeroOrMore(b.sequence("+", TYPE_PARAM_BOUND)),
                b.optional("+")
        ));
        b.rule(TYPE_PARAM_BOUND).is(b.firstOf(LIFETIME, TRAIT_BOUND));
        b.rule(TRAIT_BOUND).is(b.firstOf(
                b.sequence(b.optional("?"), b.optional(FOR_LIFE_TIMES), TYPE_PATH),
                b.sequence("(", b.optional("?"), b.optional(FOR_LIFE_TIMES), TYPE_PATH, ")")
        ));
        b.rule(LIFETIME_BOUNDS).is(b.sequence(
                b.zeroOrMore(b.sequence(LIFETIME, "+")),
                b.optional(LIFETIME)
        ));
        b.rule(LIFETIME).is(b.firstOf(LIFETIME_OR_LABEL, "'static", "'_"));
    }

    public static void tupletype(LexerfulGrammarBuilder b) {
        b.rule(TUPLE_INDEX).is(b.firstOf("0", b.sequence(
                NON_ZERO_DEC_DIGIT, b.zeroOrMore(DEC_DIGIT)
        )));
        b.rule(NON_ZERO_DEC_DIGIT).is(regexp("[1-9]"));
        b.rule(DEC_DIGIT).is(regexp("[0-9]"));
    }

    private static PatternExpression regexp(String pattern) {
        return new PatternExpression((pattern));
    }

    /* https://doc.rust-lang.org/reference/types/pointer.html */
    public static void pointer(LexerfulGrammarBuilder b) {
        b.rule(REFERENCE_TYPE).is(b.sequence("&", b.optional(LIFETIME),
                b.optional("mut"), TYPE_PARAM_BOUNDS));
    }

    /* https://doc.rust-lang.org/reference/paths.html */
    public static void lexicalpath(LexerfulGrammarBuilder b) {
        b.rule(SIMPLE_PATH).is(b.sequence(
                b.optional("::"),
                SIMPLE_PATH_SEGMENT,
                b.zeroOrMore(b.sequence("::", SIMPLE_PATH_SEGMENT))
        ));
        b.rule(SIMPLE_PATH_SEGMENT).is(b.firstOf(
                IDENTIFIER , "super" , "self" , "crate" , "$crate"
        ));

/*
        b.rule(PATH_EXPRESSION2).is(b.sequence(
                b.optional("::"),
                PATH_EXPR_SEGMENT,
                b.zeroOrMore(b.sequence("::", PATH_EXPR_SEGMENT))
        ));
*/

        b.rule(PATH_EXPR_SEGMENT).is(b.sequence(
                PATH_IDENT_SEGMENT, b.optional(b.sequence("::", GENERIC_ARGS))
        ));
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
        b.rule(GENERIC_ARGS_LIFETIMES).is(b.sequence(
                LIFETIME, b.zeroOrMore(b.sequence(",", LIFETIME))
        ));
        b.rule(GENERIC_ARGS_TYPES).is(b.sequence(
                TYPE, b.zeroOrMore(b.sequence(",", TYPE))
        ));
        b.rule(GENERIC_ARGS_BINDINGS).is(b.sequence(
                GENERIC_ARGS_BINDING, b.zeroOrMore(b.sequence(",", GENERIC_ARGS_BINDING))
        ));
        b.rule(GENERIC_ARGS_BINDING).is(b.sequence(
                IDENTIFIER, "=", TYPE
        ));


        b.rule(QUALIFIED_PATH_IN_TYPE).is(b.sequence(QUALIFIED_PATH_TYPE, b.oneOrMore(
                b.sequence("::", TYPE_PATH_SEGMENT)

        )));
        b.rule(TYPE_PATH_SEGMENT).is(b.sequence(
                PATH_IDENT_SEGMENT,
                b.optional("::"),
                b.optional(b.firstOf(GENERIC_ARGS, TYPE_PATH_FN))
        ));
        b.rule(TYPE_PATH_FN).is(b.sequence(
                "(",
                b.optional(TYPE_PATH_FN_INPUTS),
                ")",
                b.optional(b.sequence("->", TYPE))
        ));
        b.rule(TYPE_PATH_FN_INPUTS).is(b.sequence(
                TYPE,
                b.zeroOrMore(b.sequence(",", TYPE)),
                b.optional(",")
        ));
    }

    public static void lexicaltoken(LexerfulGrammarBuilder b) {
        b.rule(LIFE_TIME_TOKEN).is(b.firstOf(
                b.sequence("'", IDENTIFIER_OR_KEYWORD),
                b.sequence("'", "_")
        ));
        b.rule(LIFETIME_OR_LABEL).is(b.sequence("'", NON_KEYWORD_IDENTIFIER));
        b.rule(STRING_LITERAL).is(b.sequence(
                "\"",b.zeroOrMore(b.firstOf(
                        b.anyTokenButNot("\""),
                        b.anyTokenButNot( "\\"),
                        b.anyTokenButNot( ISOLATED_CR),
                        QUOTE_ESCAPE,
                        ASCII_ESCAPE,
                        UNICODE_ESCAPE,
                        STRING_CONTINUE

                )),"\""
        ));
    }

    /* https://doc.rust-lang.org/reference/lexical-structure.html */
    public static void lexical(LexerfulGrammarBuilder b) {

        b.rule(IDENTIFIER_OR_KEYWORD).is(b.firstOf(
                regexp(" [a-z A-Z] [a-z A-Z 0-9 _]*"),
                b.sequence("_", b.oneOrMore(regexp("[a-z A-Z 0-9 _]"))))
        );

        lexicaltoken(b);
        lexicalpath(b);

    }


}