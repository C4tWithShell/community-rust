package org.sonar.rust.parser.items;

import org.junit.Test;
import org.sonar.rust.RustGrammar;

import static org.sonar.sslr.tests.Assertions.assertThat;

public class VisibilityTest {
    @Test
    public void testVisibility() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.VISIBILITY))
                .matches("pub")
                .matches("pub (crate)")
                .matches("pub (self)")
                .matches("pub(in outer_mod)")


        ;

    }
}
