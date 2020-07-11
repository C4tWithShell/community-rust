package org.sonar.rust.parser.attributes;

import org.junit.Test;
import org.sonar.rust.RustGrammar;

import static org.sonar.sslr.tests.Assertions.assertThat;

public class AttributeTest {


    @Test
    public void testAttribute() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.ATTR))
                .matches("foo")
                .matches("foo_bar")
                .matches("foo_type")
                .matches("crate_type")

        ;
    }


    @Test
    public void testInnerAttribute() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.INNER_ATTRIBUTE))
                .matches("#![crate_type = \"lib\"]")
                ;
    }

    @Test
    public void testOuterAttribute() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.OUTER_ATTRIBUTE))
                .matches("#[test]")
        ;
    }
}
