package org.sonar.rust.api;

import com.sonar.sslr.api.Grammar;
import org.sonar.sslr.grammar.GrammarRuleKey;
import org.sonar.sslr.grammar.LexerfulGrammarBuilder;

import static com.sonar.sslr.api.GenericTokenType.EOF;
import static com.sonar.sslr.api.GenericTokenType.IDENTIFIER;
import static org.sonar.rust.api.RustTokenType.NEWLINE;


public enum RustGrammar implements GrammarRuleKey {
    EXPRESSION,
    EXPRESSIONWITHOUTBLOCK,
    EXPRESSIONWITHBLOCK,
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
    STRUCT_EXPRSSION,
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
    LIFE_TIME_OR_LABEL;

    public static Grammar create() {
        LexerfulGrammarBuilder b = LexerfulGrammarBuilder.create();
        b.rule(FILE_INPUT).is(b.zeroOrMore(b.firstOf(NEWLINE, EXPRESSION)), EOF);
        expressions(b);
        b.setRootRule(FILE_INPUT);
        return b.buildWithMemoizationOfMatchesForAllRules();
    }

    /* https://doc.rust-lang.org/reference/expressions.html */
    public static void expressions(LexerfulGrammarBuilder b) {

        b.rule(EXPRESSION).is(b.firstOf(EXPRESSIONWITHOUTBLOCK, EXPRESSIONWITHBLOCK));
        b.rule(EXPRESSIONWITHOUTBLOCK).is(b.zeroOrMore(OUTER_ATTRIBUTE),
                b.firstOf(LITTERAL_EXPRESSION,
                        PATH_EXPRESSION,
                        OPERATOR_EXPRESSION,
                        GROUPED_EXPRESSION,
                        ARRAY_EXPRESSION,
                        AWAIT_EXPRESSION,
                        INDEX_EXPRESSION,
                        TUPLE_EXPRESSION,
                        TUPLE_INDEXING_EXPRESSION,
                        STRUCT_EXPRSSION,
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
        b.rule(EXPRESSIONWITHBLOCK).is(b.zeroOrMore(OUTER_ATTRIBUTE),
                b.firstOf(
                        BLOCK_EXPRESSION,
                        ASYNC_BLOCK_EXPRESSION,
                        UNSAFE_BLOCK_EXPRESSION,
                        LOOP_EXPRESSION,
                        IF_EXPRESSION,
                        IF_LET_EXPRESSION,
                        MATCH_EXPRESSION
                ));
        b.rule(BREAK_EXPRESSION).is("break", b.optional(LIFETIME_OR_LABEL), b.optional(EXPRESSION));

        b.rule(CALL_EXPRESSION).is(b.sequence(EXPRESSION, "(", b.optional(CALL_PARAMS), ")"));
        b.rule(CALL_PARAMS).is(b.sequence(EXPRESSION,
                b.zeroOrMore(b.sequence("(", EXPRESSION, ")")), b.optional(",")));
        b.rule(GROUPED_EXPRESSION).is(b.sequence("(", b.zeroOrMore(INNER_ATTRIBUTE), EXPRESSION));
        b.rule(PATH_EXPRESSION).is(b.firstOf(PATH_IN_EXPRESSION, QUALIFIED_PATH_IN_EXPRESSION));
        b.rule(TUPLE_INDEXING_EXPRESSION).is(b.sequence(EXPRESSION, ".", TUPLE_INDEX));
        b.rule(INDEX_EXPRESSION).is(b.sequence(EXPRESSION, "[", EXPRESSION, "]"));
        b.rule(AWAIT_EXPRESSION).is(b.sequence(EXPRESSION, ".", "await"));
        b.rule(ARRAY_EXPRESSION).is(b.sequence("[", b.zeroOrMore(INNER_ATTRIBUTE), b.optional(ARRAY_ELEMENTS)));
        b.rule(ARRAY_ELEMENTS).is(b.firstOf(
                b.sequence(EXPRESSION, b.zeroOrMore(b.sequence(",", EXPRESSION)), b.optional(",")),
                b.sequence(EXPRESSION, ";", EXPRESSION)
        ));
        b.rule(TUPLE_EXPRESSION).is(b.sequence("(", b.zeroOrMore(INNER_ATTRIBUTE), b.optional(TUPLE_ELEMENT), ")"));
        b.rule(TUPLE_ELEMENT).is(b.oneOrMore(b.sequence(EXPRESSION, ",")), b.optional(EXPRESSION));
        structExpressions(b);
        attributes(b);
        b.rule(ASYNC_BLOCK_EXPRESSION).is(b.sequence("async", b.optional("move"), BLOCK_EXPRESSION));
        closures(b);
        type(b);
        trait(b);
    }

    /* https://doc.rust-lang.org/reference/expressions/struct-expr.html */
    public static void structExpressions(LexerfulGrammarBuilder b) {
        b.rule(STRUCT_EXPRSSION).is(b.firstOf(
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
    public static void closures(LexerfulGrammarBuilder b) {
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
        b.rule(PARENTHERIZED_TYPE).is(b.sequence("(",TYPE,")"));
        b.rule(TRAIT_OBJECT_TYPE).is(b.sequence(b.optional("dyn"), TYPE_PARAM_BOUNDS));
        b.rule(TRAIT_OBJECT_TYPE_ONE_BOUND).is(b.sequence(b.optional("dyn"), TRAIT_BOUND));
        b.rule(RAW_POINTER_TYPE).is(b.sequence("*",b.firstOf("mut","const"),TYPE_NO_BOUNDS));

    }

    /* https://doc.rust-lang.org/reference/trait-bounds.html */
    public static void trait(LexerfulGrammarBuilder b){
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
        b.rule(LIFE_TIME).is(b.firstOf(LIFE_TIME_OR_LABEL, "'static","'_"));
    }

}