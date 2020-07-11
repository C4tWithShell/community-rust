package org.sonar.rust.parser.items;

import org.junit.Test;
import org.sonar.rust.RustGrammar;

import static org.sonar.sslr.tests.Assertions.assertThat;

public class StaticTest {
    @Test
    public void testStatic() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.STATIC_ITEM))
                .matches("static mut LEVELS: u32 = 0;")


        ;

    }
}
