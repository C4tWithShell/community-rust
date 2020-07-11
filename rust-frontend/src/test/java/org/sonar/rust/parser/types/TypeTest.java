package org.sonar.rust.parser.types;

import org.junit.Test;
import org.sonar.rust.RustGrammar;

import static org.sonar.sslr.tests.Assertions.assertThat;

public class TypeTest {

    @Test
    public void testType() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.TYPE))
                .matches("i32")
                .matches("(i32, u8)")
                .matches("Circle")
                .notMatches("Circle{")

        ;
    }
}
