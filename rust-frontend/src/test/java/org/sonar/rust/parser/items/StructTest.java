package org.sonar.rust.parser.items;

import org.junit.Test;
import org.sonar.rust.RustGrammar;

import static org.sonar.sslr.tests.Assertions.assertThat;

public class StructTest {
    @Test
    public void testStruct() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.STRUCT))
                .matches("struct Point {x:i32, y: i32}")


        ;

    }

}
