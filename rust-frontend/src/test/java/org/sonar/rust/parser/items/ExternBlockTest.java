package org.sonar.rust.parser.items;

import org.junit.Test;
import org.sonar.rust.RustGrammar;

import static org.sonar.sslr.tests.Assertions.assertThat;

public class ExternBlockTest {

    @Test
    public void testNamedFunctionParam() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.NAMED_FUNCTION_PARAM))
                .matches("foo : i32")
                .matches("_ : f64")
                .matches("#[test] foo : i32")
                .notMatches("foo : i32...")
                .notMatches("foo : i32,")

        ;

    }

    @Test
    public void testNamedFunctionParamVariadics() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.NAMED_FUNCTION_PARAMETERS_WITH_VARIADICS))
                .matches("foo : i32,...")
                .matches("foo : i32,bar : f64,...")
                .matches("_ : f64,...")
                .matches("#[test] foo : i32,...")
                .matches("foo : i32,bar : f64, #[outer]...")


        ;

    }

    @Test
    public void testExternBlock() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.EXTERN_BLOCK))
                .matches("extern \"stdcall\" {}")
                .matches("extern \"stdcall\" {\n}")
                .matches("extern {}")
                .matches("extern r\"foo\" {}") //raw string
                .matches("extern \"foo\" {#![inner]}")
                //todo use external item


        ;

    }
}
