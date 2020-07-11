package org.sonar.rust.parser.statements;

import org.junit.Test;
import org.sonar.rust.RustGrammar;

import static org.sonar.sslr.tests.Assertions.assertThat;

public class StatementTest {

    @Test
    public void testLetStatement() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.LET_STATEMENT))
                .matches("let x;")
                .matches("let x ;")
                .matches("let y=42;")
                .matches("#[test]\n" +
                        "let y:i32=42;")
        ;
    }


    @Test
    public void testStatement() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.STATEMENT))
                .matches(";")
                .matches("extern crate pcre;")
                .matches("let y=42;")
        ;
    }
}
