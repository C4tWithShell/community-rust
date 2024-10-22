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
package org.sonar.rust.parser;

import org.junit.jupiter.api.Test;
import org.sonar.rust.RustGrammar;


import static org.sonar.sslr.tests.Assertions.assertThat;

class CompilationUnitTest {

  @Test
  void testAnyTokens() {
    assertThat(RustGrammar.create().build().rule(RustGrammar.ANY_TOKEN))
      .matches("u")
      .matches("us")
      .notMatches("fn")
      .notMatches("use")
      .matches("foo")
      .matches("fnac")
      .matches("test");
  }

  @Test
  void testCompilationUnit() {
    assertThat(RustGrammar.create().build().rule(RustGrammar.COMPILATION_UNIT))
      .matches("use std::fmt;")
      .matches("use std::hash;")
      .matches("use std::fmt; \n" +
        "use std::hash;")
      .matches("fn main() {println!(\"Hello, world!\");}")
      .matches("fn main() {\n" +
        "    println!(\"Hello, world!\");\n" +
        "}")
      .matches("/* comment */ fn main() {\n" +
        "    println!(\"Hello, world!\");\n" +
        "}")
      .matches(" fn main() {\n" +
        " /* comment */\n" +
        "    println!(\"Hello, world!\");\n" +
        "}")
      .matches(" fn main() {\n" +
        " // line comment \n" +
        "    println!(\"Hello, world!\");\n" +
        "}")
      .matches("mod foobar{#![crate_type = \"lib\"]\n" +
        "}")
      .matches("test();")
      .matches("test() ;")
      .matches("syn_dev::r#mod! {\n" +
        "    // Write Rust code here and run `cargo check` to have Syn parse it.\n" +
        "\n" +
        "}\n");
  }
}
