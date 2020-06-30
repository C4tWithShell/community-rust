package org.sonar.rust.parser.lexer;

import org.junit.Test;
import org.sonar.rust.RustGrammar;

import static org.sonar.sslr.tests.Assertions.assertThat;


public class SpacingTest {

    @Test
    public void reallife() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.SPACING))
                .matches("/*foo*/")
                .matches("/*foo \n bar*/")
                .notMatches("/*foo*/*/")

                .matches(" ")
                .matches("")
                .matches("\t")
                .matches("\r")
                .matches("\n")
                .matches("\r\n");
    }

}
