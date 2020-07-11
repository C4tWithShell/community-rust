package org.sonar.rust.parser.items;

import org.junit.Test;
import org.sonar.rust.RustGrammar;

import static org.sonar.sslr.tests.Assertions.assertThat;

public class ExternBlockTest {
    @Test
    public void testExternBlock() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.EXTERN_BLOCK))
                .matches("extern \"stdcall\" { }")


        ;

    }
}
