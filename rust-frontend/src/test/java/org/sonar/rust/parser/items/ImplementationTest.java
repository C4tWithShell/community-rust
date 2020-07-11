package org.sonar.rust.parser.items;

import org.junit.Test;
import org.sonar.rust.RustGrammar;

import static org.sonar.sslr.tests.Assertions.assertThat;

public class ImplementationTest {

    @Test
    public void testInherentImplItem() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.INHERENT_IMPL_ITEM))
                .matches("println!(\"hello\");") //macro invocation semi
                .matches("#[outer] println!(\"hello\");") //macro invocation semi
                .matches("const BIT2: u32 = 1 << 1;") //constant
                .matches("#[outer] fn answer_to_life_the_universe_and_everything() -> i32 {\n" +
                        "    let y=42;\n" +
                        "}") //function
                .matches("fn by_ref(self: &Self) {}") //method
                .matches("pub const BIT2: u32 = 1 << 1;") //constant



        ;

    }

    @Test
    public void testInherentImpl() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.INHERENT_IMPL))
                .matches("impl Color {}")
                .matches("impl <T> Color {}")
                .matches("impl Color where 'a : 'b +'c +'d {}")
                .matches("impl Color {#![inner]}")
                .matches("impl Color {fn by_ref(self: &Self) {}}")
                .matches("impl Color {#![inner] fn by_ref(self: &Self) {}}")
                .matches("impl Color where 'a : 'b +'c +'d {#![inner] fn by_ref(self: &Self) {}}")
                .matches("impl <U> Color where 'a : 'b +'c +'d {#![inner] fn by_ref(self: &Self) {}}")

        ;

    }


    @Test
    public void testTraitImpl() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.TRAIT_IMPL))
                .matches("impl abc::(isize) -> isize for Circle {}")
                .matches("impl abc::(isize) -> isize for Circle {println!(\"hello\");}")
                .matches("impl abc::(isize) -> isize for Circle {type Point = (u8, u8);}")
                .matches("impl abc::(isize) -> isize for Circle {fn answer_to_life_the_universe_and_everything() -> i32 {\n}" +
                        "    }")
                .matches("impl abc::(isize) -> isize for Circle {fn by_ref(self: &Self) {}}")
                .matches("unsafe impl abc::(isize) -> isize for Circle {}")
                .matches("impl <T>abc::(isize) -> isize for Circle {}")
                .matches("impl !abc::(isize) -> isize for Circle {}")


        ;

    }


    @Test
    public void testTraitImplItem() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.TRAIT_IMPL_ITEM))
                .matches("println!(\"hello\");") //macro invocation semi
                .matches("#[outer] println!(\"hello\");") //macro invocation semi
                .matches("type Point = (u8, u8);") //type alias
                .matches("#[outer] type Point = (u8, u8);") //type alias
                .matches("const BIT2: u32 = 1 << 1;") //constant
                .matches("#[outer] fn answer_to_life_the_universe_and_everything() -> i32 {\n" +
                        "    let y=42;\n" +
                        "}") //function
                .matches("fn by_ref(self: &Self) {}") //method

        ;

    }
    @Test
    public void testImplementation() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.IMPLEMENTATION))
                //trait impl
                .matches("impl abc::(isize) -> isize for Circle {}")
                .matches("impl abc::(isize) -> isize for Circle {println!(\"hello\");}")
                .matches("impl abc::(isize) -> isize for Circle {type Point = (u8, u8);}")
                .matches("impl abc::(isize) -> isize for Circle {fn answer_to_life_the_universe_and_everything() -> i32 {\n}" +
                        "    }")
                .matches("impl abc::(isize) -> isize for Circle {fn by_ref(self: &Self) {}}")
                .matches("unsafe impl abc::(isize) -> isize for Circle {}")
                .matches("impl <T>abc::(isize) -> isize for Circle {}")
                .matches("impl !abc::(isize) -> isize for Circle {}")
                //InherentImpl
                .matches("impl Color {}")
                .matches("impl <T> Color {}")
                .matches("impl Color where 'a : 'b +'c +'d {}")
                .matches("impl Color {#![inner]}")
                .matches("impl Color {fn by_ref(self: &Self) {}}")
                .matches("impl Color {#![inner] fn by_ref(self: &Self) {}}")
                .matches("impl Color where 'a : 'b +'c +'d {#![inner] fn by_ref(self: &Self) {}}")
                .matches("impl <U> Color where 'a : 'b +'c +'d {#![inner] fn by_ref(self: &Self) {}}")



        ;

    }
}
