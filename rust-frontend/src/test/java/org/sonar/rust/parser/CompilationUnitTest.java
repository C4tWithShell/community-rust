package org.sonar.rust.parser;

import org.junit.Test;
import org.sonar.rust.RustGrammar;
import org.sonar.sslr.parser.LexerlessGrammar;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.sonar.sslr.tests.Assertions.assertThat;

public class CompilationUnitTest {
    @Test
    public void matchingEmpty() {
        LexerlessGrammar g = RustGrammar.create().build();
        RustGrammar[] rustGrammars = RustGrammar.values();

        Set<RustGrammar> shouldMatch = new HashSet<RustGrammar>(Arrays.asList(
                RustGrammar.ADDITION_EXPRESSION,
                RustGrammar.ANYTHING,
                RustGrammar.ARITHMETIC_OR_LOGICAL_EXPRESSION,
                RustGrammar.ARRAY_ELEMENTS,
                RustGrammar.ARRAY_ELEMENTS_EXPRESSION1,
                RustGrammar.ARRAY_ELEMENTS_EXPRESSION2,
                RustGrammar.ASSIGNMENT_EXPRESSION,
                RustGrammar.ASSIGNMENT_EXPRESSION_TERM,
                RustGrammar.BITAND_EXPRESSION,
                RustGrammar.BITOR_EXPRESSION,
                RustGrammar.BITXOR_EXPRESSION,
                RustGrammar.CALL_EXPRESSION,
                RustGrammar.CALL_EXPRESSION_TERM,
                RustGrammar.CALL_PARAMS,
                RustGrammar.CALL_PARAMS_TERM,
                RustGrammar.COMPARISON_EXPRESSION,
                RustGrammar.COMPILATION_UNIT,
                RustGrammar.DIVISION_EXPRESSION,
                RustGrammar.EOF,
                RustGrammar.EQ_EXPRESSION,
                RustGrammar.EXPRESSION,
                RustGrammar.EXPRESSION_WITHOUT_BLOCK,
                RustGrammar.FUNCTION_QUALIFIERS,
                RustGrammar.GENERIC_PARAMS,
                RustGrammar.GE_EXPRESSION,
                RustGrammar.GT_EXPRESSION,
                RustGrammar.LAZY_BOOLEAN_EXPRESSION,
                RustGrammar.LE_EXPRESSION,
                RustGrammar.LIFETIME_BOUNDS,
                RustGrammar.LIFETIME_PARAMS,
                RustGrammar.LITERALS,
                RustGrammar.LT_EXPRESSION,
                RustGrammar.MACRO_MATCH,
                RustGrammar.MACRO_REP_SEP,
                RustGrammar.METHOD_CALL_EXPRESSION,
                RustGrammar.METHOD_CALL_EXPRESSION_TERM,
                RustGrammar.MULTIPLICATION_EXPRESSION,
                RustGrammar.NEQ_EXPRESSION,
                RustGrammar.OPERATOR_EXPRESSION,
                RustGrammar.PUNCTUATION,
                RustGrammar.REMAINDER_EXPRESSION,
                RustGrammar.SHL_EXPRESSION,
                RustGrammar.SHR_EXPRESSION,
                RustGrammar.SPACING,
                RustGrammar.STATEMENTS,
                RustGrammar.SUBTRACTION_EXPRESSION,
                RustGrammar.TOKEN,
                RustGrammar.TOKEN_TREE,
                RustGrammar.TUPLE_STRUCT_ITEMS,
                RustGrammar.TYPE_PARAMS


        ));

        for (RustGrammar r : rustGrammars) {
            if (shouldMatch.contains(r)) {
                assertThat(RustGrammar.create().build().rule(r))
                        .matches("");
            } else {

                    assertThat(RustGrammar.create().build().rule(r))
                            .notMatches("");


            }
        }

    }
}
