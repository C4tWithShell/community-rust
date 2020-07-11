package org.sonar.rust.parser.items;

import org.junit.Test;
import org.sonar.rust.RustGrammar;

import static org.sonar.sslr.tests.Assertions.assertThat;

public class UnionTest {
    @Test
    public void testUnion() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.UNION))
                .matches("union MyUnion {\n" +
                        "    f1: u32,\n" +
                        "    f2: f32,\n" +
                        "}")


        ;

    }
}
