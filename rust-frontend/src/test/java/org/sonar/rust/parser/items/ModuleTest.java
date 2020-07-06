package org.sonar.rust.parser.items;

import org.junit.Test;
import org.sonar.rust.RustGrammar;

import static org.sonar.sslr.tests.Assertions.assertThat;

public class ModuleTest {

    @Test
    public void testModule() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.MODULE))
                .matches("mod foo ;")
                .matches("mod bar {}")
                //TODO more needed for mod foo { item }

                ;

    }
}
