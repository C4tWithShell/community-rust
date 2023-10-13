/**
 * Community Rust Plugin
 * Copyright (C) 2021-2023 Eric Le Goff
 * mailto:community-rust AT pm DOT me
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

import org.junit.jupiter.api.Test;
import org.sonar.rust.RustGrammar;


import static org.sonar.sslr.tests.Assertions.assertThat;

class ExternBlockTest {

  @Test
  void testExternalItem() {
    assertThat(RustGrammar.create().build().rule(RustGrammar.EXTERNAL_ITEM))
      .matches("println!(\"hi\");") // macro invocation semi
      .matches("#[outer] println!(\"hi\");")
      .matches("static fdf : f64;") // external static item
      .matches("pub static fdf : f64;")
      .matches("#[outer] pub static fdf : f64;")
      .matches("fn draw()->Circle;") // external function item
      .matches("pub fn draw()->Circle;")
      .matches("#[outer] pub fn draw()->Circle;")

    ;

  }

  @Test
  void testExternBlock() {
    assertThat(RustGrammar.create().build().rule(RustGrammar.EXTERN_BLOCK))
      .matches("extern \"stdcall\" {}")
      .matches("extern \"stdcall\" {\n}")
      .matches("extern {}")
      .matches("extern r\"foo\" {}") // raw string
      .matches("extern \"foo\" {#![inner]}")
      // with external item
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
      .matches("extern \"C\" {\n" +
        "    type Global; // Return type of js_sys::global()\n" +
        "\n" +
        "    // Web Crypto API (https://www.w3.org/TR/WebCryptoAPI/)\n" +
        "    #[wasm_bindgen(method, getter, js_name = \"msCrypto\")]\n" +
        "    fn ms_crypto(this: &Global) -> BrowserCrypto;\n" +
        "    #[wasm_bindgen(method, getter)]\n" +
        "    fn crypto(this: &Global) -> BrowserCrypto;\n" +
        "    type BrowserCrypto;\n" +
        "    #[wasm_bindgen(method, js_name = getRandomValues, catch)]\n" +
        "    fn get_random_values(this: &BrowserCrypto, buf: &Uint8Array) -> Result<(), JsValue>;\n" +
        "\n" +
        "    // We use a \"module\" object here instead of just annotating require() with\n" +
        "    // js_name = \"module.require\", so that Webpack doesn't give a warning. See:\n" +
        "    //   https://github.com/rust-random/getrandom/issues/224\n" +
        "    type NodeModule;\n" +
        "    #[wasm_bindgen(js_name = module)]\n" +
        "    static NODE_MODULE: NodeModule;\n" +
        "    // Node JS crypto module (https://nodejs.org/api/crypto.html)\n" +
        "    #[wasm_bindgen(method, catch)]\n" +
        "    fn require(this: &NodeModule, s: &str) -> Result<NodeCrypto, JsValue>;\n" +
        "    type NodeCrypto;\n" +
        "    #[wasm_bindgen(method, js_name = randomFillSync, catch)]\n" +
        "    fn random_fill_sync(this: &NodeCrypto, buf: &mut [u8]) -> Result<(), JsValue>;\n" +
        "\n" +
        "    // Node JS process Object (https://nodejs.org/api/process.html)\n" +
        "    #[wasm_bindgen(method, getter)]\n" +
        "    fn process(this: &Global) -> Process;\n" +
        "    type Process;\n" +
        "    #[wasm_bindgen(method, getter)]\n" +
        "    fn versions(this: &Process) -> Versions;\n" +
        "    type Versions;\n" +
        "    #[wasm_bindgen(method, getter)]\n" +
        "    fn node(this: &Versions) -> JsValue;\n" +
        "}")
      .matches("extern \"C\" {\n" +
        "    pub fn esetfunc(\n" +
        "        cb: ::Option<unsafe extern \"C\" fn(::c_int, *const ::c_char, ...)>,\n" +
        "    ) -> ::Option<unsafe extern \"C\" fn(::c_int, *const ::c_char, ...)>;\n" +
        "}")

    ;

  }
}
