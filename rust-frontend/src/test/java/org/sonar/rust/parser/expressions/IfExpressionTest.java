package org.sonar.rust.parser.expressions;

import org.junit.Test;
import org.sonar.rust.RustGrammar;

import static org.sonar.sslr.tests.Assertions.assertThat;

public class IfExpressionTest {

    @Test
    public void tesIfExpression() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.IF_EXPRESSION))
                .matches("if x == 4 {\n" +
                        "    println!(\"x is four\");\n" +
                        "} else if x == 3 {\n" +
                        "    println!(\"x is three\");\n" +
                        "} else {\n" +
                        "    println!(\"x is something else\");\n" +
                        "}")

        ;
    }

    @Test
    public void tesIfLetExpression() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.IF_LET_EXPRESSION))
                .matches("if let (\"Bacon\",b) = dish {\n" +
                        "    println!(\"Bacon is served with {}\", b);\n" +
                        "} else {\n" +
                        "    // This block is evaluated instead.\n" +
                        "    println!(\"No bacon will be served\");\n" +
                        "}")

        ;
    }
}