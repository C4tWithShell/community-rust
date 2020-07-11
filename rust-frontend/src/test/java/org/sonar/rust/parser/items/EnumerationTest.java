package org.sonar.rust.parser.items;

import org.junit.Test;
import org.sonar.rust.RustGrammar;

import static org.sonar.sslr.tests.Assertions.assertThat;

public class EnumerationTest {

    @Test
    public void testEnumeration() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.ENUMERATION))
                .matches("enum Animal {\n" +
                        "    Dog,\n" +
                        "    Cat,\n" +
                        "}")


        ;

    }

}
