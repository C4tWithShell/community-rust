package org.sonar.rust.parser.items;

import org.junit.Test;
import org.sonar.rust.RustGrammar;

import static org.sonar.sslr.tests.Assertions.assertThat;

public class ExternCrateTest {

    @Test
    public void testCrateRef(){
        assertThat(RustGrammar.create().build().rule(RustGrammar.CRATE_REF))
                .matches("abc")
                .matches("self")
                ;
    }

    @Test
    public void testAsClause(){
        assertThat(RustGrammar.create().build().rule(RustGrammar.AS_CLAUSE))
                .matches("as foo")
                .matches("as _")
        ;
    }


    @Test
    public void testExternCrates() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.EXTERN_CRATE))
                .matches("extern crate pcre;")
                .matches("extern crate std;") // equivalent to: extern crate std as std
                .matches("extern crate std as ruststd;") // linking to 'std' under another name
        ;


    }
}
