package org.sonar.rust.parser;

import org.junit.Test;
import org.sonar.rust.RustGrammar;
import org.sonar.sslr.parser.LexerlessGrammar;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.sonar.sslr.tests.Assertions.assertThat;

public class RustGrammarTest {
    @Test
    public void matchingEmpty() {
        LexerlessGrammar g = RustGrammar.create().build();
        RustGrammar[] rustGrammars = RustGrammar.values();

        Set<RustGrammar> couldMatch = new HashSet<RustGrammar>(Arrays.asList(
                RustGrammar.CALL_PARAMS_TERM,
                RustGrammar.COMPILATION_UNIT,
                RustGrammar.EOF,
                RustGrammar.FUNCTION_QUALIFIERS,
                RustGrammar.GENERIC_PARAMS,
                RustGrammar.LIFETIME_BOUNDS,
                RustGrammar.LIFETIME_PARAMS,
                RustGrammar.SPC,
                RustGrammar.TUPLE_STRUCT_ITEMS,
                RustGrammar.TYPE_PARAMS
        ));

        for (RustGrammar r : rustGrammars) {
                if (couldMatch.contains(r)) {
                    assertThat(RustGrammar.create().build().rule(r))
                            .matches("");
                } else {
                    assertThat(RustGrammar.create().build().rule(r))
                            .notMatches("");
                }



        }

    }
}
