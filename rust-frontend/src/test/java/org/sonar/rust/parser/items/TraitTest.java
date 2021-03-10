/**
 *
 * Sonar Rust Plugin (Community)
 * Copyright (C) 2021 Eric Le Goff
 * http://github.com/elegoff/sonar-rust
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.sonar.rust.parser.items;

import org.junit.Test;
import org.sonar.rust.RustGrammar;

import static org.sonar.sslr.tests.Assertions.assertThat;

public class TraitTest {

    @Test
    public void testTraitType() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.TRAIT_TYPE))
                .matches("type foo;")
                .matches("type Impl: SelectorImpl;")

        ;
    }

    @Test
    public void testTraitConst() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.TRAIT_CONST))
                .matches("const foo : i32;")
                .matches("const ID: i32 = 1;")

        ;
    }

    @Test
    public void testTraitFunctionParam() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.TRAIT_FUNCTION_PARAM))
                .matches("i32")
                .matches("foo:i32")
                .matches("foo : i32")
                .matches("#[test]foo:i32")
                .matches("#[test] foo : i32")
        ;
    }

    @Test
    public void testTraitFunctionParameters() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.TRAIT_FUNCTION_PARAMETERS))
                .matches("i32")
                .matches("i32,f64,i32")
                .matches("foo:i32")
                .matches("foo:i32,bar:f64")
                .matches("foo : i32,f64,f64")
                .matches("#[test] foo : i32 , bar : f64")
        ;
    }

    @Test
    public void testTraitMethodDecl() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.TRAIT_METHOD_DECL))
                .matches("fn by_ref(self: &Self)")
                .matches("fn by_ref(self: &Self)->i32")
        ;
    }

    @Test
    public void testTraitFunctionDecl() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.TRAIT_FUNCTION_DECL))
                .matches("const fn by_ref()")
                .matches("async fn by_ref()->f64")

        ;
    }

    @Test
    public void testTraitMethod() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.TRAIT_METHOD))
                .matches("fn by_ref(self: &Self);")
                .matches("fn by_ref(self: &Self)->i32{42}")
        ;
    }

    @Test
    public void testTraitFunction() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.TRAIT_FUNC))
                .matches("const fn by_ref();")
                .matches("async fn by_ref()->f64;")
                .matches("const fn by_ref(){42}")
                .matches("async fn by_ref()->bool{true}")
        ;
    }

    @Test
    public void testTraitItem() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.TRAIT_ITEM))
                .matches("const fn by_ref();")
                .matches("async fn by_ref()->f64;")
                .matches("const fn by_ref(){42}")
                .matches("async fn by_ref()->bool{true}")
                .matches("#[test] const fn by_ref();")
                .matches("#[test] async fn by_ref();")
                .matches("#[test] pub async fn by_ref()->f64;")
                .matches("pub fn by_ref(){42}")
                .matches("pub (crate) fn by_ref(){42}")
                .matches("#[test] pub (crate) async fn by_ref()->bool{true}")
                .matches("fn len(&self) -> u32;")
                .matches("type Impl: SelectorImpl;")
                .matches("#[test] pub type Impl: SelectorImpl;")
        ;
    }


    @Test
    public void testTrait() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.TRAIT))
                .matches("trait Seq<T> {\n" +
                        "    fn len(&self) -> u32;\n" +
                        "    fn elt_at(&self, n: u32) -> T;\n" +
                        "    fn iter<F>(&self, f: F) where F: Fn(T);\n" +
                        "}")


        ;

    }
}