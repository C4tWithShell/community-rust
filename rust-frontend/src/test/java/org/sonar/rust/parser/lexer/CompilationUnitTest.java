package org.sonar.rust.parser.lexer;

import org.junit.Test;
import org.sonar.rust.RustGrammar;

import static org.sonar.sslr.tests.Assertions.assertThat;

public class CompilationUnitTest {
    @Test
    public void reallife() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.COMPILATION_UNIT))
                .matches("")
                .matches("println!(\"hello\");")
                .matches("let n=42;")


                ;
    }
}
