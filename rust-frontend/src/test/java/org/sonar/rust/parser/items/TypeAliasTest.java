package org.sonar.rust.parser.items;

import org.junit.Test;
import org.sonar.rust.RustGrammar;

import static org.sonar.sslr.tests.Assertions.assertThat;

public class TypeAliasTest {

    @Test
    public void testTypeAlias() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.TYPE_ALIAS))
                .matches("type Point = (u8, u8);")


        ;

    }
}
