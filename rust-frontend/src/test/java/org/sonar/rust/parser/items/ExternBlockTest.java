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
    public void testNamedFunctionParameters() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.NAMED_FUNCTION_PARAMETERS))
                .matches("foo : i32")
                .matches("foo : i32,bar:f64")
                .matches("_ : f64")
                .matches("_ : f64,bar:f64")
                .matches("#[test] foo : i32,bar:f64, baz: Circle")
                .notMatches("foo : i32,...")
        ;

    }

    @Test
    public void testNamedFunctionParamVariadics() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.NAMED_FUNCTION_PARAMETERS_WITH_VARIADICS))
                .matches("foo : i32,...")
                .matches("foo : i32,bar : f64,...")
                .matches("_ : f64,...")
                .matches("#[test] foo : i32, ...")
                .matches("foo : i32,bar : f64, #[outer]...")


        ;

    }

    @Test
    public void testExternalFunctionItem() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.EXTERNAL_FUNCTION_ITEM))
                .matches("fn draw();")
                .matches("fn draw ( ) ;")
                .matches("fn draw <T>( ) ;")
                .matches("fn draw(foo : i32,bar:f64);") //named function parameters
                .matches("fn draw(foo : i32,bar : f64,...);") //with variadics
                .matches("fn draw()->Circle;")
                .matches("fn draw()-> Circle where 'a:'b+'c+'d;")
     ;

    }


    @Test
    public void testExternalStaticItem() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.EXTERNAL_STATIC_ITEM))
                .matches("static idf:i32;")
                .matches("static fdf : f64;")
                .matches("static mut fdf : f64;")


        ;

    }

    @Test
    public void testExternalItem() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.EXTERNAL_ITEM))
                .matches("println!(\"hi\");") //macro invocation semi
                .matches("#[outer] println!(\"hi\");")
                .matches("static fdf : f64;") //external static item
                .matches("pub static fdf : f64;")
                .matches("#[outer] pub static fdf : f64;")
                .matches("fn draw()->Circle;") //external function item
                .matches("pub fn draw()->Circle;")
                .matches("#[outer] pub fn draw()->Circle;")


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
                //with external item
                 .matches("extern {\n" +
                "    fn foo(x: i32, ...);\n" +
                "}")
                .matches("extern {pub fn draw()->Circle;}")
                .matches("extern {\n" +
                        "    pub fn draw()->Circle;\n" +
                        "    }")
                .matches("extern {\n" +
                        "    pub fn draw()->Circle;\n" +
                        "    #[outer] pub fn draw()->Circle;\n" +
                        "    static fdf : f64;\n" +
                        "    }")


        ;

    }
}
