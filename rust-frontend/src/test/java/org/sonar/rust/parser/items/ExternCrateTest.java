package org.sonar.rust.parser.items;

import org.junit.Test;
import org.sonar.rust.RustGrammar;

import static org.sonar.sslr.tests.Assertions.assertThat;

public class ExternCrateTest {
    @Test
    public void testExternCrates() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.EXTERN_CRATE))
                .matches("extern crate pcre;")
                .matches("extern crate std;") // equivalent to: extern crate std as std
                .matches("extern crate std as ruststd;") // linking to 'std' under another name
        ;


    }
}
