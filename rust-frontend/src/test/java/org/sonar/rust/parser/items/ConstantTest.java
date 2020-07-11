package org.sonar.rust.parser.items;

import org.junit.Test;
import org.sonar.rust.RustGrammar;

import static org.sonar.sslr.tests.Assertions.assertThat;

public class ConstantTest {

    @Test
    public void testConstant() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.CONSTANT_ITEM))
                .matches("const BIT1: u32 = 1 << 0;")
                .matches("const BIT2: u32 = 1 << 1;")
                .matches("const BITS: [u32; 2] = [BIT1, BIT2];")
                .matches("const STRING: &'static str = \"bitstring\";")
                //FIXME.matches("const WHITE: Color = Color(255, 255, 255);")
        ;

    }
}
