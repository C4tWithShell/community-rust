package org.sonar.rust.parser.items;

import org.junit.Test;
import org.sonar.rust.RustGrammar;

import static org.sonar.sslr.tests.Assertions.assertThat;

public class AssociatedTest {


    @Test
    public void testTypedSelf() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.TYPED_SELF))
                .matches("self:i32")
                .matches("self: Self")
                .matches("self : i32")
                .matches("mut self:f64")
                .matches("mut self : f64")
                .matches("self: &mut Arc<Rc<Box<Alias>>>")
                .matches("self: &'a Arc<Rc<Box<Alias>>>")

        ;
    }

    @Test
    public void testShortHandSelf() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.SHORTHAND_SELF))
                .matches("self")
                .matches("mut self")
                .matches("&self")
                .matches("&mut self")
                .matches("&'ABC self")



        ;
    }

    @Test
    public void testSelfParam() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.SELF_PARAM))
                .matches("self")
                .matches("mut self")
                .matches("&self")
                .matches("&mut self")
                .matches("&'ABC self")
                .matches("self:i32")
                .matches("self: Self")
                .matches("self : i32")
                .matches("#[test] mut self:f64")
                .matches("#[test] mut self : f64")
                .matches("self: &Self")
                .matches("self: Self")
                .matches("self: &mut Self")
                .matches("self: Box<Self>")
                .matches("self: Rc<Self>")
                .matches("self: Arc<Self>")
                .matches("self: Pin<&Self>")
                .matches("self: Arc<Example>")
                .matches("self: &'a Self")
                .matches("self: &mut  Arc<Rc<Box<Alias>>>")
                .matches("self: &'a  Arc<Rc<Box<Alias>>>")
                .matches("self: <Example as Trait>::Output")

        ;
    }


    @Test
    public void testMethod() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.METHOD))
                .matches("fn by_value(self: Self) {}")
                .matches("fn by_ref(self: &Self) {}")
                .matches("fn by_ref_mut(self: &mut Self) {}")
                .matches("fn by_box(self: Box<Self>) {}")
                .matches("fn by_rc(self: Rc<Self>) {}")
                .matches("fn by_arc(self: Arc<Self>) {}")
                .matches("async fn by_pin(self: Pin<&Self>) {}")
                .matches("const fn explicit_type(self: Arc<Example>) {}")
                .matches("fn with_lifetime<'a>(self: &'a Self) {}")
                .matches("fn nested<'a>(self: &mut  Arc<Rc<Box<Alias>>>) {}")
                .matches("fn nested<'a>(self: &'a  Arc<Rc<Box<Alias>>>) {}")
                .matches("fn via_projection(self: <Example as Trait>::Output) {}")

        ;

    }
}
