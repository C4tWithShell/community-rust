package org.sonar.rust.parser.macro;

import org.junit.Test;
import org.sonar.rust.RustGrammar;

import static org.sonar.sslr.tests.Assertions.assertThat;

public class MacroTest {


    @Test
    public void testDelimTokenTree() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.DELIM_TOKEN_TREE))
                .matches("()")
                .matches("(abc)")
                .notMatches("")

        ;
    }


    @Test
    public void testTokenTree() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.TOKEN_TREE))
                .matches("abc")
                //FIXME .matches("(abc)")
                //FIXME .matches("()")
                .notMatches("")
        ;
    }

    @Test
    public void testMacroInvocationSemi() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.MACRO_INVOCATION_SEMI))
            .matches("j!(AS);")
        .notMatches("")
        ;
    }
}
