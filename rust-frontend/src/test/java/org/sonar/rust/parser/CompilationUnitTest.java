package org.sonar.rust.parser;

import org.junit.Test;
import org.sonar.rust.RustGrammar;

import static org.sonar.sslr.tests.Assertions.assertThat;

public class CompilationUnitTest {

    @Test
    public void testCompilationUnit() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.COMPILATION_UNIT))
                .matches("fn main() {\n" +
                        "    println!(\"Hello, world!\");\n" +
                        "}")
                .matches("/* comment */ fn main() {\n" +
                        "    println!(\"Hello, world!\");\n" +
                        "}")
                .matches(" fn main() {\n" +
                        " /* comment */\n" +
                        "    println!(\"Hello, world!\");\n" +
                        "}")
                .matches(" fn main() {\n" +
                        " // line comment \n" +
                        "    println!(\"Hello, world!\");\n" +
                        "}")


        ;
    }
}
