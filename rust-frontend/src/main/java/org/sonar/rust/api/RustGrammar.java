package org.sonar.rust.api;

import com.sonar.sslr.api.Grammar;
import org.sonar.sslr.grammar.GrammarRuleKey;
import org.sonar.sslr.grammar.LexerfulGrammarBuilder;
import org.sonar.sslr.internal.vm.PatternExpression;

import static com.sonar.sslr.api.GenericTokenType.EOF;
import static com.sonar.sslr.api.GenericTokenType.IDENTIFIER;
import static org.sonar.rust.api.RustTokenType.NEWLINE;


public enum RustGrammar implements GrammarRuleKey {
    EXPRESSION,
    EXPRESSION_WITHOUT_BLOCK,
    EXPRESSION_WITH_BLOCK,
    FILE_INPUT,
    OUTER_ATTRIBUTE,
    LITTERAL_EXPRESSION,
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
    MACRO_INVOCATION,
    BLOCK_EXPRESSION,
    ASYNC_BLOCK_EXPRESSION,
    UNSAFE_BLOCK_EXPRESSION,
    LOOP_EXPRESSION,
    IF_EXPRESSION,
    IF_LET_EXPRESSION,
    MATCH_EXPRESSION,
    LIFETIME_OR_LABEL,
    CLOSURE_PARAMETERS,
    CALL_PARAMS,
    INNER_ATTRIBUTE,
    PATH_IN_EXPRESSION,
    QUALIFIED_PATH_IN_EXPRESSION,
    TUPLE_INDEX,
    ARRAY_ELEMENTS,
    TUPLE_ELEMENT,
    STRUCT_EXPR_STRUCT,
    STRUCT_EXPR_TUPLE,
    STRUCT_EXPR_UNIT,
    STRUCT_EXPR_FIELDS,
    STRUCT_BASE,
    STRUCT_EXPR_FIELD,
    ATTR,
    SIMPLE_PATH,
    ATTR_INPUT,
    DELIM_TOKEN_TREE,
    CLOSURE_PARAM,
    PATTERN,
    TYPE,
    TYPE_NO_BOUNDS,
    IMPL_TRAIT_TYPE,
    PARENTHERIZED_TYPE,
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
    TRAIT_OBJECT_TYPE,
    TYPE_PARAM_BOUNDS,
    TRAIT_BOUND,
    TYPE_PARAM_BOUND,
    LIFE_TIME,
    FOR_LIFE_TIMES,
    LIFE_TIME_BOUNDS,
    LIFE_TIME_OR_LABEL,
    NON_ZERO_DEC_DIGIT,
    DEC_DIGIT,
    RANGE_EXPR,
    RANGE_FROM_EXPR,
    RANGE_TO_EXPR,
    RANGE_FULL_EXPR,
    RANGE_INCLUSIVE_EXPR,
    RANGE_TO_INCLUSIVE_EXPR,
    QUALIFIED_PATH_TYPE,
    TYPE_PATH_SEGMENT,
    PATH_IDENT_SEGMENT,
    GENERIC_ARGS,
    TYPE_PATH_FN,
    TYPE_PATH_FN_INPUTS,
    MATCH_ARMS,
    MATCH_ARM,
    MATCH_ARM_PATTERNS,
    MATCH_ARM_GUARD,
    BORROW_EXPRESSION,
    DEREFERENCE_EXPRESSION,
    ERROR_PROPAGATION_EXPRESSION,
    NEGATION_EXPRESSION,
    ARITHMETIC_OR_LOGICAL_EXPRESSION,
    COMPARISON_EXPRESSION,
    LAZY_BOOLEAN_EXPRESSION,
    TYPE_CAST_EXPRESSION,
    ASSIGNMENT_EXPRESSION,
    COMPOUND_ASSIGNMENT_EXPRESSION,
    CHAR_LITERAL,
    STRING_LITERAL,
    RAW_STRING_LITERAL,
    BYTE_LITERAL,
    BYTE_STRING_LITERAL,
    RAW_BYTE_STRING_LITERAL,
    INTEGER_LITERAL,
    FLOAT_LITERAL,
    BOOLEAN_LITERAL,
    LIFE_TIME_TOKEN,
    IDENTIFIER_OR_KEYWORD,
    NON_KEYWORD_IDENTIFIER,
    STATEMENT,
    ITEM,
    LET_STATEMENT,
    EXPRESSION_STATEMENT,
    MACRO_INVOCATIONSEMI,
    STATEMENTS,
    ENUM_EXPR_STRUCT,
    ENUM_EXPR_TUPLE,
    ENUM_EXPR_FIELDLESS,
    ENUM_EXPR_FIELD,
    ENUM_EXPR_FIELDS,
    PATH_EXPRESSION_SEGMENT,
    LOOP_LABEL,
    INFINITE_LOOP_EXPRESSION,
    PREDICATE_LOOP_EXPRESSION,
    PREDICATE_PATTERN_LOOP_EXPRESSION,
    ITERATOR_LOOP_EXPRESSION;

    public static Grammar create() {
        LexerfulGrammarBuilder b = LexerfulGrammarBuilder.create();
        b.rule(FILE_INPUT).is(b.zeroOrMore(b.firstOf(NEWLINE, EXPRESSION)), EOF);
        lexical(b);
        attributes(b);
        statement(b);
        expressions(b);
        types(b);

        b.setRootRule(FILE_INPUT);
        return b.buildWithMemoizationOfMatchesForAllRules();
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
                "loop",BLOCK_EXPRESSION));
        b.rule(PREDICATE_LOOP_EXPRESSION).is(b.sequence(
                "while", EXPRESSION, //except struct expression
                BLOCK_EXPRESSION
        ));
        b.rule(PREDICATE_PATTERN_LOOP_EXPRESSION).is(b.sequence(
                "while","let",MATCH_ARM_PATTERNS,"=",EXPRESSION, //except struct expression
                BLOCK_EXPRESSION
        ));
        b.rule(ITERATOR_LOOP_EXPRESSION).is(b.sequence(
                "for",PATTERN,"IN",EXPRESSION, //except struct expression
                BLOCK_EXPRESSION
        ));
        b.rule(LOOP_LABEL).is(b.sequence(LIFE_TIME_OR_LABEL,":"));
        b.rule(BREAK_EXPRESSION).is(b.sequence("break", b.optional(LIFETIME_OR_LABEL), b.optional(EXPRESSION)));
        b.rule(CONTINUE_EXPRESSION).is(b.sequence(
                "continue",b.optional(LIFE_TIME_OR_LABEL)));


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
                PARENTHERIZED_TYPE,
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
        b.rule(PARENTHERIZED_TYPE).is(b.sequence("(", TYPE, ")"));
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
        b.rule(TYPE_PARAM_BOUND).is(b.firstOf(LIFE_TIME, TRAIT_BOUND));
        b.rule(TRAIT_BOUND).is(b.firstOf(
                b.sequence(b.optional("?"), b.optional(FOR_LIFE_TIMES), TYPE_PATH),
                b.sequence("(", b.optional("?"), b.optional(FOR_LIFE_TIMES), TYPE_PATH, ")")
        ));
        b.rule(LIFE_TIME_BOUNDS).is(b.sequence(
                b.zeroOrMore(b.sequence(LIFE_TIME, "+")),
                b.optional(LIFE_TIME)
        ));
        b.rule(LIFE_TIME).is(b.firstOf(LIFE_TIME_OR_LABEL, "'static", "'_"));
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
        b.rule(REFERENCE_TYPE).is(b.sequence("&", b.optional(LIFE_TIME),
                b.optional("mut"), TYPE_PARAM_BOUNDS));
    }

    /* https://doc.rust-lang.org/reference/paths.html */
    public static void lexicalpath(LexerfulGrammarBuilder b) {
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
        b.rule(LIFE_TIME_OR_LABEL).is(b.sequence("'", NON_KEYWORD_IDENTIFIER));
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