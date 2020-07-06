package org.sonar.rust.parser.items;

import org.junit.Test;
import org.sonar.rust.RustGrammar;

import static org.sonar.sslr.tests.Assertions.assertThat;

public class UseTest {


    @Test
    public void testUseDeclaration() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.USE_DECLARATION))
                .matches("use std::option::Option::{Some, None};")
                .matches("use std::collections::hash_map::{self, HashMap};")


        ;

    }
}
