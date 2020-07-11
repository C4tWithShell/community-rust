package org.sonar.rust.parser.types;

import org.junit.Test;
import org.sonar.rust.RustGrammar;

import static org.sonar.sslr.tests.Assertions.assertThat;

public class TupleTest {

    @Test
    public void testTuple() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.TUPLE_TYPE))
                .matches("()")
                .matches("(i32,i32)")
                .matches("(i32, u8)")

        ;
    }

}
