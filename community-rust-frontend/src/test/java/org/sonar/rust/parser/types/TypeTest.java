/**
 * Community Rust Plugin
 * Copyright (C) 2021-2024 Vladimir Shelkovnikov
 * mailto:community-rust AT pm DOT me
 * http://github.com/C4tWithShell/community-rust
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
package org.sonar.rust.parser.types;

import org.junit.jupiter.api.Test;
import org.sonar.rust.RustGrammar;


import static org.sonar.sslr.tests.Assertions.assertThat;

class TypeTest {

  @Test
  void testParenthesisType() {
    assertThat(RustGrammar.create().build().rule(RustGrammar.PARENTHESIZED_TYPE))
      .matches("(i32)")
      .matches("( i32 )")

    ;
  }

  @Test
  void testImplTraitTypeOneBound() {
    assertThat(RustGrammar.create().build().rule(RustGrammar.IMPL_TRAIT_TYPE_ONE_BOUND))
      .matches("impl ? abc::def")
      .matches("impl for <'a> abc::def")
      .matches("impl ? for <'a> abc::def<T>")
      .matches("impl (abc::def::ghi)")
      .matches("impl (? abc::def)")
      .matches("impl ( for <'a> abc::def )")
      .matches("impl (? for <'a> abc::def)")
      .matches("impl Fn(&mut OpState, u32, &mut [ZeroCopyBuf]) -> Result<R, AnyError>")

    ;
  }

  @Test
  void testTraitObjectTypeOneBound() {
    assertThat(RustGrammar.create().build().rule(RustGrammar.TRAIT_OBJECT_TYPE_ONE_BOUND))
      .matches("? abc::def")
      .matches("for <'a> abc::def")
      .matches("? for <'a> abc::def<T>")
      .matches("(abc::def::ghi)")
      .matches("(? abc::def)")
      .matches("( for <'a> abc::def )")
      .matches("(? for <'a> abc::def)")
      .matches("Fn(&mut OpState, u32, &mut [ZeroCopyBuf]) -> Result<R, AnyError>")
      .matches("dyn ? abc::def")
      .matches("dyn for <'a> abc::def")
      .matches("dyn ? for <'a> abc::def<T>")
      .matches("dyn (abc::def::ghi)")
      .matches("dyn (? abc::def)")
      .matches("dyn ( for <'a> abc::def )")
      .matches("dyn (? for <'a> abc::def)")
      .matches("dyn Fn(&mut OpState, u32, &mut [ZeroCopyBuf]) -> Result<R, AnyError>")
      .matches("dyn Future<Input = Result<CachedModule, (ModuleSpecifier, AnyError)>>")

    ;
  }

  @Test
  void testMaybeNamedParam() {
    assertThat(RustGrammar.create().build().rule(RustGrammar.MAYBE_NAMED_PARAM))
      .matches("this: *mut iasset")
      .matches("::c_int")
      .matches("*const ::c_char")

    ;
  }

  @Test
  void testMaybeNamedFunctionParam() {
    assertThat(RustGrammar.create().build().rule(RustGrammar.MAYBE_NAMED_FUNCTION_PARAMETERS))
      .matches("this: *mut iasset")
      .matches("::c_int")
      .matches("*const ::c_char")
      .matches("*const ::c_char, ::c_int, *const ::c_char")

    ;
  }

  @Test
  void testFunctionParameterMaybeNamedVariadic() {
    assertThat(RustGrammar.create().build().rule(RustGrammar.FUNCTION_PARAMETERS_MAYBE_NAMED_VARIADIC))
      .matches("this: *mut iasset")
      .matches("::c_int, *const ::c_char")
      .matches("::c_int, *const ::c_char, ...")

    ;
  }

  @Test
  void testBareFunctionType() {
    assertThat(RustGrammar.create().build().rule(RustGrammar.BARE_FUNCTION_TYPE))
      .matches("extern \"C\" fn(this: *mut iasset) -> i32")
      .matches("unsafe extern \"C\" fn(::c_int, *const ::c_char)")
      .matches("unsafe extern \"C\" fn(::c_int, *const ::c_char, ...)")

    ;
  }

  @Test
  void testTypeNoBounds() {
    assertThat(RustGrammar.create().build().rule(RustGrammar.TYPE_NO_BOUNDS))
      .matches("i32")
      .matches("(i32, u8)")
      .matches("Circle")
      .notMatches("Circle{")
      .matches("[u8]")
      .matches("extern \"C\" fn(this: *mut iasset) -> i32")
      .matches("Token![#]")

    ;
  }

  @Test
  void testimplTraitType() {
    assertThat(RustGrammar.create().build().rule(RustGrammar.IMPL_TRAIT_TYPE))
      .matches("impl Foo")
      .matches("impl Foo")
      .matches("impl FnOnce()")
      .matches("impl Future<Output = ()> + 'm")

    ;
  }

  @Test
  void testTraitObjectType() {
    assertThat(RustGrammar.create().build().rule(RustGrammar.TRAIT_OBJECT_TYPE))
      .matches("'a")
      .matches("'a+'a")
      .matches("'a + 'b + 'c")
      .matches("'ABC")
      .matches("'ABC+'DCE")
      .notMatches("'trait") // no keyword allowed
      .matches("'static")
      .matches("'_")
      .matches("'_+'a")
      .matches("'_+'a+'ABC")
      .notMatches("'a self")
      .matches("(? for <'a> abc::def)+'a+abc::def")
      .matches("ValueOrVector")
      .matches("Fn(&mut OpState, u32, &mut [ZeroCopyBuf]) -> Result<R, AnyError> + 'static")
      .matches("dyn 'a")
      .matches("dyn 'a+'a")
      .matches("dyn 'a + 'b + 'c")
      .matches("dyn 'ABC")
      .matches("dyn 'ABC+'DCE")
      .notMatches("dyn 'trait") // no keyword allowed
      .matches("dyn 'static")
      .matches("dyn '_")
      .matches("dyn '_+'a")
      .matches("dyn '_+'a+'ABC")
      .notMatches("dyn 'a self")
      .matches("dyn (? for <'a> abc::def)+'a+abc::def")
      .matches("dyn ValueOrVector")
      .matches("dyn Fn(&mut OpState, u32, &mut [ZeroCopyBuf]) -> Result<R, AnyError> + 'static")
      .matches("dyn Future<Output = i32>\n" +
        "       + 'static\n" +
        "       + Send")
      .matches("dyn Future<Output = Result<CachedModule, (ModuleSpecifier, AnyError)>>\n" +
        "    + 'static\n" +
        "    + Send")

    ;
  }

  @Test
  void testReferenceType() {
    assertThat(RustGrammar.create().build().rule(RustGrammar.REFERENCE_TYPE))
      .matches("&i32")
      .matches("&(i32, u8)")
      .matches("&Circle")
      .notMatches("&Circle{")
      .matches("&[u8]")
      .matches("&Token![#]")
      .matches("&'a i32")
      .matches("&mut Circle")
      .matches("&'b mut Circle")
      .matches("&'a AF")

    ;
  }

  @Test
  void testType() {
    assertThat(RustGrammar.create().build().rule(RustGrammar.TYPE))
      .matches("extern \"C\" fn(this: *mut iasset) -> i32")
      .matches("i32")
      .matches("(i32, u8)")
      .matches("Circle")
      .notMatches("Circle{")
      .matches("semver_parser::version")
      .matches("semver_parser::version::Identifier")
      .matches("From<semver_parser::version::Identifier>")
      .matches("&hhh")
      .matches("&u8")
      .matches("&[u8]")
      .matches("Result<CachedModule, ModuleSpecifier>")
      .matches("Result<(CachedModule, ModuleSpecifier)>")
      .matches("Result<(CachedModule, ModuleSpecifier),AnyError >")
      .matches("Result<T,(U,V)>")
      .matches("Result<T, (U, V)>")
      .matches("Result<CachedModule, (ModuleSpecifier, AnyError)>")
      .matches("dyn Future<Input = Result<CachedModule, (ModuleSpecifier, AnyError)>>")
      .matches("dyn Future<Output = Result<CachedModule, (ModuleSpecifier, AnyError)>>\n" +
        "    + 'static\n" +
        "    + Send")
      .matches("impl FnOnce()")
      .matches("<X as Default>::default()")
      .matches("Token![#]")
      .matches("Self")
      .matches("impl Future<Output = ()> + 'm")
      .matches("::Option<unsafe extern \"C\" fn(::c_int, *const ::c_char)>")
      .matches("::Option<unsafe extern \"C\" fn(::c_int, *const ::c_char, ...)>")
      .matches("&'a AF")

    ;
  }
}
