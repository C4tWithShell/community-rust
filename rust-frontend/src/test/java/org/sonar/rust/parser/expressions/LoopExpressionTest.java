package org.sonar.rust.parser.expressions;

import org.junit.Test;
import org.sonar.rust.RustGrammar;

import static org.sonar.sslr.tests.Assertions.assertThat;

public class LoopExpressionTest {

    @Test
    public void testLoopExpression() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.LOOP_EXPRESSION))
                .matches("while i < 10 {\n" +
                        "    println!(\"hello\");\n" +
                        "    i = i + 1;\n" +
                        "}")
                .matches("while let Some(y) = x.pop() {\n" +
                        "    println!(\"y = {}\", y);\n" +
                        "}")
                .matches("while let _ = 5 {\n" +
                        "    println!(\"Irrefutable patterns are always true\");\n" +
                        "    break;\n" +
                        "}")
                .matches("while let Some(v @ 1) | Some(v @ 2) = vals.pop() {\n" +
                        "    // Prints 2, 2, then 1\n" +
                        "    println!(\"{}\", v);\n" +
                        "}")
                .matches("for text in v {\n" +
                        "    println!(\"I like {}.\", text);\n" +
                        "}")
                .matches("for n in 1..11 {\n" +
                        "    sum += n;\n" +
                        "}")
        ;
    }
}
