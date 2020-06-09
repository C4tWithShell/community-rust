package org.sonar.rust.api;

import com.sonar.sslr.api.Grammar;
import org.sonar.sslr.grammar.GrammarRuleKey;
import org.sonar.sslr.grammar.LexerfulGrammarBuilder;

import static com.sonar.sslr.api.GenericTokenType.EOF;
import static com.sonar.sslr.api.GenericTokenType.IDENTIFIER;
import static org.sonar.rust.api.RustTokenType.NEWLINE;


public enum RustGrammar implements GrammarRuleKey {
    ARITHMETIC_OR_LOGICAL_EXPRESSION,
    ARRAY_ELEMENTS,
    ARRAY_EXPRESSION,
    ARRAY_TYPE,
    ASSIGNMENT_EXPRESSION,
    ASYNC_BLOCK_EXPRESSION,
    ATTR,
    ATTR_INPUT,
    AWAIT_EXPRESSION,
    BARE_FUNCTION_TYPE,
    BLOCK_EXPRESSION,
    BORROW_EXPRESSION,
    BREAK_EXPRESSION,
    CALL_EXPRESSION,
    CALL_PARAMS,
    CLOSURE_EXPRESSION,
    CLOSURE_PARAM,
    CLOSURE_PARAMETERS,
    COMPARISON_EXPRESSION,
    COMPOUND_ASSIGNMENT_EXPRESSION,
    CONTINUE_EXPRESSION,
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
    FOR_LIFETIMES,
    GENERICS,
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
    MACRO_ITEM,
    MATCH_ARM,
    MATCH_ARMS,
    MATCH_ARM_GUARD,
    MATCH_ARM_PATTERNS,
    MATCH_EXPRESSION,
    METHOD_CALL_EXPRESSION,
    NEGATION_EXPRESSION,
    NEVER_TYPE,
    NON_KEYWORD_IDENTIFIER,
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
    QUALIFIED_PATH_IN_EXPRESSION,
    QUALIFIED_PATH_IN_TYPE,
    QUALIFIED_PATH_TYPE,
    RANGE_EXPR,
    RANGE_EXPRESSION,
    RANGE_FROM_EXPR,
    RANGE_FULL_EXPR,
    RANGE_INCLUSIVE_EXPR,
    RANGE_PATTERN,
    RANGE_PATTERN_BOUND,
    RANGE_TO_EXPR,
    RANGE_TO_INCLUSIVE_EXPR,
    RAW_POINTER_TYPE,
    REFERENCE_PATTERN,
    REFERENCE_TYPE,
    RETURN_EXPRESSION,
    SIMPLE_PATH,
    SIMPLE_PATH_SEGMENT,
    SLICE_PATTERN,
    SLICE_TYPE,
    STATEMENT,
    STATEMENTS,
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
    FUNCTION_RETURN_TYPE,
    WHERE_CLAUSE,
    TUPLE_EXPRESSION,
    TUPLE_INDEX,
    NAMED_FUNCTION_PARAMETERS,
    NAMED_FUNCTION_PARAMETERS_WITH_VARIADICS,
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
    NAMED_FUNCTION_PARAM,
    TYPE_PATH_FN,
    TYPE_PATH_FN_INPUTS,
    TYPE_PATH_SEGMENT,
    UNSAFE_BLOCK_EXPRESSION,
    USE_TREE,
    VISIT_ITEM,
    VISIBILITY,
    WILDCARD_PATTERN,
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
    CRATE_REF,
    ABI,
    EXTERNAL_STATIC_ITEM,
    EXTERNAL_FUNCTION_ITEM,
    EXTERNAL_ITEM,
    AS_CLAUSE,
    MACRO_RULE,
    MACRO_RULES,
    MACRO_TRANSCRIBER,
    MACRO_MATCHER,
    MACRO_MATCH,
    MACRO_FRAG_SPEC,
    MACRO_REP_SEP,
    MACRO_REP_OP,
    MACRO_RULES_DEFINITION,
    MACRO_RULES_DEF,
    EXTERN_BLOCK, FUNCTION_QUALIFIERS,
    FUNCTION_PARAMETERS_MAYBE_NAMED_VARIADIC,
    BARE_FUNCTION_RETURN_TYPE,
    MAYBE_NAMED_FUNCTION_PARAMETERS,
    MAYBE_NAMED_FUNCTION_PARAMETERS_VARIADIC, MAYBE_NAMED_PARAM;



    public static Grammar create() {
        LexerfulGrammarBuilder b = LexerfulGrammarBuilder.create();
        b.rule(FILE_INPUT).is(b.zeroOrMore(b.firstOf(NEWLINE, EXPRESSION)), EOF);
        lexical(b);
        macros(b);
        items(b);
        attributes(b);
        statement(b);
        expressions(b);
        patterns(b);
        types(b);

        b.setRootRule(FILE_INPUT);
        return b.buildWithMemoizationOfMatchesForAllRules();
    }

    /* recurring grammar pattern */
    private static Object seq(LexerfulGrammarBuilder b, GrammarRuleKey g, String sep){
        return b.sequence(g, b.sequence(b.zeroOrMore(sep,g), b.optional(sep)));
    }

    private static void items(LexerfulGrammarBuilder b) {
        b.rule(ITEM).is(b.sequence(b.zeroOrMore(OUTER_ATTRIBUTE),
                b.firstOf(VISIT_ITEM, MACRO_ITEM)));
        b.rule(VISIT_ITEM).is(b.sequence(b.optional(VISIBILITY), b.firstOf(
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
        )));
        b.rule(MACRO_ITEM).is(b.firstOf(MACRO_INVOCATION_SEMI, MACRO_RULES_DEFINITION));
        modules(b);
        externcrates(b);
        use_item(b);
        alias_item(b);
        functions_item(b);
        structs_item(b);
        enumerations_item(b);
        unions_item(b);
        constants_item(b);
        static_item(b);
        traits_item(b);
        impl_item(b);
        extblocks_item(b);
        type_item(b);
        assoc_item(b);
        visibility_item(b);
    }

    private static void traits_item(LexerfulGrammarBuilder b) {
    }

    private static void enumerations_item(LexerfulGrammarBuilder b) {
    }

    private static void alias_item(LexerfulGrammarBuilder b) {
    }

    private static void use_item(LexerfulGrammarBuilder b) {
        b.rule(USE_DECLARATION).is(b.sequence("use", USE_TREE, ";"));
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

    private static void functions_item(LexerfulGrammarBuilder b) {
    }

    private static void structs_item(LexerfulGrammarBuilder b) {
    }

    private static void unions_item(LexerfulGrammarBuilder b) {
    }

    /* https://doc.rust-lang.org/reference/items/constant-items.html */
    private static void constants_item(LexerfulGrammarBuilder b) {
        b.rule(CONSTANT_ITEM).is(b.sequence(
                "const", b.firstOf(IDENTIFIER,"_"),
                ":", TYPE, "=", EXPRESSION, ";"
        ));

    }

    /* https://doc.rust-lang.org/reference/items/static-items.html */
    private static void static_item(LexerfulGrammarBuilder b) {
        b.rule(STATIC_ITEM).is(b.sequence(
                "static", b.optional("mut"),IDENTIFIER, ":", TYPE,"=", EXPRESSION, ";"
        ));
    }

    private static void impl_item(LexerfulGrammarBuilder b) {
    }

    /* https://doc.rust-lang.org/reference/items/external-blocks.html */
    private static void extblocks_item(LexerfulGrammarBuilder b) {
        b.rule(EXTERN_BLOCK).is(b.sequence(
                "extern",b.optional(ABI), "{",
                b.zeroOrMore(INNER_ATTRIBUTE),
                b.zeroOrMore(EXTERNAL_ITEM),"}"
        ));
        b.rule(EXTERNAL_ITEM).is(b.sequence(
                b.zeroOrMore(OUTER_ATTRIBUTE), "(",
                b.firstOf(MACRO_INVOCATION_SEMI,
                        b.sequence(
                                b.optional(VISIBILITY),
                                b.firstOf(EXTERNAL_STATIC_ITEM, EXTERNAL_FUNCTION_ITEM)
                        ))));
        b.rule(EXTERNAL_STATIC_ITEM).is(b.sequence(
                "static", b.optional("mut"), IDENTIFIER, ":", TYPE, ";"
        ));
        b.rule(EXTERNAL_FUNCTION_ITEM).is(b.sequence(
                "fn",IDENTIFIER, b.optional(GENERICS), "(",
                b.optional(b.firstOf(NAMED_FUNCTION_PARAMETERS,NAMED_FUNCTION_PARAMETERS_WITH_VARIADICS )),
                ")",b.optional(FUNCTION_RETURN_TYPE),b.optional(WHERE_CLAUSE),";"
        ));
        b.rule(NAMED_FUNCTION_PARAMETERS).is(seq(b, NAMED_FUNCTION_PARAM, ","));
        b.rule(NAMED_FUNCTION_PARAM).is(b.sequence(
                b.zeroOrMore(OUTER_ATTRIBUTE),
                b.firstOf(IDENTIFIER, "_"),
                ":",TYPE
        ));
        b.rule(NAMED_FUNCTION_PARAMETERS_WITH_VARIADICS).is(b.sequence(
                b.zeroOrMore(b.sequence(NAMED_FUNCTION_PARAM, ",")),
                NAMED_FUNCTION_PARAM, ",",b.zeroOrMore(OUTER_ATTRIBUTE),"..."
                ));
    }



    private static void type_item(LexerfulGrammarBuilder b) {
    }

    private static void assoc_item(LexerfulGrammarBuilder b) {
    }

    private static void visibility_item(LexerfulGrammarBuilder b) {
        b.rule(VISIBILITY).is(b.firstOf(
                "pub",
                b.sequence("pub", "(", "crate", ")"),
                b.sequence("pub", "(", "self", ")"),
                b.sequence("pub", "(", "super", ")"),
                b.sequence("pub", "(", "in", SIMPLE_PATH, ")")

        ));
    }


    private static void externcrates(LexerfulGrammarBuilder b) {
        b.rule(EXTERN_CRATE).is(b.sequence(
                "extern", "crate", CRATE_REF, b.optional(AS_CLAUSE)
        ));
        b.rule(CRATE_REF).is(b.firstOf(IDENTIFIER, "self"));
        b.rule(AS_CLAUSE).is(b.sequence("as", b.firstOf(IDENTIFIER, "_")));

    }

    private static void modules(LexerfulGrammarBuilder b) {
        b.rule(MODULE).is(b.firstOf(
                b.sequence("mod", IDENTIFIER, ";"),
                b.sequence("mod", IDENTIFIER, "{",
                        b.zeroOrMore(INNER_ATTRIBUTE),
                        b.zeroOrMore(ITEM), "}"
                )));

    }

    private static void lexical(LexerfulGrammarBuilder b) {
        //not explicit in reference
        b.rule(TOKEN).is(b.oneOrMore(
                (b.sequence(b.zeroOrMore(RustTokenType.STRING),
                        b.zeroOrMore(RustTokenType.NUMBER)))));

        lexicalpath(b);
    }

    /* https://doc.rust-lang.org/reference/macros.html */
    private static void macros(LexerfulGrammarBuilder b) {
        b.rule(MACRO_INVOCATION).is(b.sequence(
                SIMPLE_PATH, "!", DELIM_TOKEN_TREE
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
        macros_by_example(b);
        procedural_macros(b);
    }

    /* https://doc.rust-lang.org/reference/macros-by-example.html */
    private static void macros_by_example(LexerfulGrammarBuilder b) {
        b.rule(MACRO_RULES_DEFINITION).is(b.sequence(
                "macro_rule", "!", IDENTIFIER, MACRO_RULES_DEF
        ));
        b.rule(MACRO_RULES_DEF).is(b.firstOf(
                b.sequence("(", MACRO_RULES, ")", ";"),
                b.sequence("[", MACRO_RULES, "]", ";"),
                b.sequence("{", MACRO_RULES, "}")
                ));
        b.rule(MACRO_RULES).is(b.sequence(
                MACRO_RULE, b.zeroOrMore(b.sequence(",",MACRO_RULE)), b.optional(",")
        ));
        b.rule(MACRO_RULE).is(b.sequence(MACRO_MATCHER, "=>", MACRO_TRANSCRIBER));
        b.rule(MACRO_MATCH).is(b.firstOf(
                TOKEN, //except $ and delimiters
                MACRO_MATCHER,
                b.sequence("$", IDENTIFIER, ":", MACRO_FRAG_SPEC),
                b.sequence("$", "(", b.oneOrMore(MACRO_MATCH), ")"
                        , b.optional(MACRO_REP_SEP), MACRO_REP_OP)
        ));
        b.rule(MACRO_FRAG_SPEC).is(b.firstOf(
                "block" ,"expr" , "ident" , "item" , "lifetime","literal"
                        , "meta" ,"pat","path","stmt","tt","ty","vis"
        ));
        b.rule(MACRO_REP_SEP).is(TOKEN); //except $ and delimiters
        b.rule(MACRO_REP_OP).is(b.firstOf("*", "+", "?"));
        b.rule(MACRO_TRANSCRIBER).is(DELIM_TOKEN_TREE);





    }

    private static void procedural_macros(LexerfulGrammarBuilder b) {
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
                RustTokenType.NUMBER,
                RustTokenType.STRING,
                b.sequence(b.optional("-"), RustTokenType.NUMBER)
        ));
        b.rule(IDENTIFIER_PATTERN).is(b.sequence(
                b.optional("ref"),
                b.optional("mut"),
                IDENTIFIER,
                b.optional(b.sequence("@", PATTERN))
        ));
        b.rule(WILDCARD_PATTERN).is("_");
        b.rule(RANGE_PATTERN).is(b.firstOf(
                b.sequence(RANGE_PATTERN_BOUND, "..=", RANGE_PATTERN_BOUND),
                b.sequence(RANGE_PATTERN_BOUND, "...", RANGE_PATTERN_BOUND)
        ));
        b.rule(RANGE_PATTERN_BOUND).is(b.firstOf(
                RustTokenType.STRING,
                b.sequence(b.optional("-"), RustTokenType.NUMBER),
                PATH_IN_EXPRESSION, QUALIFIED_PATH_IN_EXPRESSION
        ));

        b.rule(REFERENCE_PATTERN).is(b.sequence(
                b.firstOf("&", "&&"),
                b.optional("mut"),
                PATTERN
        ));
        b.rule(STRUCT_PATTERN).is(b.sequence(
                PATH_IN_EXPRESSION, "{", b.optional(STRUCT_PATTERN_ELEMENTS), "}"
        ));
        b.rule(STRUCT_PATTERN_ELEMENTS).is(b.firstOf(
                b.sequence(STRUCT_PATTERN_FIELDS,
                        b.optional(b.firstOf(
                                "'",
                                b.sequence(",", STRUCT_PATTERN_ETCETERA)
                        ))), STRUCT_PATTERN_ETCETERA

        ));
        b.rule(STRUCT_PATTERN_FIELDS).is(b.sequence(
                STRUCT_PATTERN_FIELD, b.zeroOrMore(b.sequence(",", STRUCT_PATTERN_FIELD))
        ));
        b.rule(STRUCT_PATTERN_FIELD).is(b.sequence(
                b.zeroOrMore(OUTER_ATTRIBUTE), "(",
                b.firstOf(
                        b.sequence(TUPLE_INDEX, ":", PATTERN),
                        b.sequence(IDENTIFIER, ":", PATTERN),
                        b.sequence(b.optional("ref"), b.optional("mut"), IDENTIFIER)
                )));
        b.rule(STRUCT_PATTERN_ETCETERA).is(b.sequence(b.zeroOrMore(OUTER_ATTRIBUTE), ".."));

        b.rule(TUPLE_STRUCT_PATTERN).is(b.sequence(
                PATH_IN_EXPRESSION, "(", b.optional(TUPLE_STRUCT_ITEMS), ")"
        ));
        b.rule(TUPLE_STRUCT_ITEMS).is(b.firstOf(
                PATTERN, b.zeroOrMore(b.sequence(",", PATTERN), b.optional("'")),
                b.sequence(b.zeroOrMore(b.sequence(PATTERN, ",")), "..",
                        b.optional(b.sequence(b.zeroOrMore(b.sequence(",", PATTERN)),
                                b.optional(",")
                        )))));
        b.rule(TUPLE_PATTERN).is(b.sequence("(", b.optional(TUPLE_PATTERN_ITEMS), ")"));
        b.rule(TUPLE_PATTERN_ITEMS).is(b.firstOf(
                b.sequence(PATTERN, ","),
                b.sequence(PATTERN, b.oneOrMore(b.sequence(",", PATTERN)), b.optional(",")),
                b.sequence(b.zeroOrMore(b.sequence(",", PATTERN)), "..",
                        b.optional(b.sequence(
                                b.oneOrMore(b.sequence(",", PATTERN)),
                                b.optional(","))))));
        b.rule(GROUPED_PATTERN).is(b.sequence("(", PATTERN, ")"));
        b.rule(SLICE_PATTERN).is(b.sequence("[", PATTERN,
                b.zeroOrMore(b.sequence(",", PATTERN)), b.optional(","), "]"
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
        //14
        functionpointer(b);
        //15
        trait(b);


    }

    /* https://doc.rust-lang.org/reference/types/function-pointer.html */
    private static void functionpointer(LexerfulGrammarBuilder b) {
        b.rule(BARE_FUNCTION_TYPE).is(b.sequence(
                b.optional(FOR_LIFETIMES),FUNCTION_QUALIFIERS, "fn",
                "(", b.optional(FUNCTION_PARAMETERS_MAYBE_NAMED_VARIADIC),")",
                b.optional(BARE_FUNCTION_RETURN_TYPE)
        ));
        b.rule(BARE_FUNCTION_RETURN_TYPE).is(b.sequence("->", TYPE_NO_BOUNDS));
        b.rule(FUNCTION_PARAMETERS_MAYBE_NAMED_VARIADIC).is(b.firstOf(
                MAYBE_NAMED_FUNCTION_PARAMETERS , MAYBE_NAMED_FUNCTION_PARAMETERS_VARIADIC
        ));
        b.rule(MAYBE_NAMED_FUNCTION_PARAMETERS).is(seq(b, MAYBE_NAMED_PARAM,","));
        b.rule(MAYBE_NAMED_PARAM).is(b.sequence(
                b.zeroOrMore(OUTER_ATTRIBUTE),
                b.optional(b.sequence(
                        b.firstOf(IDENTIFIER, "_"), ":"
                )), TYPE
        ));
        b.rule(MAYBE_NAMED_FUNCTION_PARAMETERS_VARIADIC).is(b.sequence(
                b.zeroOrMore(b.sequence(MAYBE_NAMED_PARAM, ",")),
                MAYBE_NAMED_PARAM, ",",b.zeroOrMore(OUTER_ATTRIBUTE),"..."
        ));


    }

    public static void statement(LexerfulGrammarBuilder b) {
        b.rule(STATEMENT).is(b.firstOf(
                ";",
                ITEM,
                LET_STATEMENT,
                EXPRESSION_STATEMENT,
                MACRO_INVOCATION_SEMI
        ));
        b.rule(LET_STATEMENT).is(b.sequence(
                b.zeroOrMore(OUTER_ATTRIBUTE),
                "let", PATTERN,
                b.optional(b.sequence(":", TYPE)),
                b.optional(b.sequence("=", EXPRESSION)),
                ";"));
        b.rule(EXPRESSION_STATEMENT).is(b.firstOf(
                b.sequence(EXPRESSION_WITHOUT_BLOCK, ";"),
                b.sequence(EXPRESSION_WITH_BLOCK, b.optional(";"))
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
                EXPRESSION, ".", PATH_EXPR_SEGMENT,
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
                RustTokenType.STRING,
                RustTokenType.NUMBER
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

    }

    /* https://doc.rust-lang.org/reference/trait-bounds.html */
    public static void trait(LexerfulGrammarBuilder b) {
        b.rule(TYPE_PARAM_BOUNDS).is(b.sequence(TYPE_PARAM_BOUND,
                b.zeroOrMore(b.sequence("+", TYPE_PARAM_BOUND)),
                b.optional("+")
        ));
        b.rule(TYPE_PARAM_BOUND).is(b.firstOf(LIFETIME, TRAIT_BOUND));
        b.rule(TRAIT_BOUND).is(b.firstOf(
                b.sequence(b.optional("?"), b.optional(FOR_LIFETIMES), TYPE_PATH),
                b.sequence("(", b.optional("?"), b.optional(FOR_LIFETIMES), TYPE_PATH, ")")
        ));
        b.rule(LIFETIME_BOUNDS).is(b.sequence(
                b.zeroOrMore(b.sequence(LIFETIME, "+")),
                b.optional(LIFETIME)
        ));
        b.rule(LIFETIME).is(b.firstOf(LIFETIME_OR_LABEL, "'static", "'_"));
        b.rule(FOR_LIFETIMES).is("for"); //not sure since reference does not say explcitly
    }

    public static void tupletype(LexerfulGrammarBuilder b) {

        b.rule(TUPLE_TYPE).is(b.firstOf(
                b.sequence("(", ")"),
                b.sequence("(", b.oneOrMore(b.sequence(TYPE, ",")), b.optional(TYPE), ")")
        ));

        b.rule(TUPLE_INDEX).is(b.firstOf("0", RustTokenType.NUMBER
        ));
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
                IDENTIFIER, "super", "self", "crate", "$crate"
        ));

        b.rule(PATH_IN_EXPRESSION).is(b.sequence(
                b.optional("::"),
                PATH_EXPR_SEGMENT,
                b.zeroOrMore(b.sequence("::", PATH_EXPR_SEGMENT))
        ));

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

        b.rule(QUALIFIED_PATH_IN_EXPRESSION).is(b.sequence(
                QUALIFIED_PATH_TYPE, b.oneOrMore(b.sequence("::", PATH_EXPR_SEGMENT))));

        b.rule(QUALIFIED_PATH_TYPE).is(b.sequence(
                "<", TYPE, b.optional(b.sequence("as", TYPE_PATH)), ">"
        ));


    }

    public static void lexicaltoken(LexerfulGrammarBuilder b) {
        b.rule(LIFE_TIME_TOKEN).is(b.firstOf(
                b.sequence("'", IDENTIFIER_OR_KEYWORD),
                b.sequence("'", "_")
        ));
        b.rule(LIFETIME_OR_LABEL).is(b.sequence("'", NON_KEYWORD_IDENTIFIER));
    }


}