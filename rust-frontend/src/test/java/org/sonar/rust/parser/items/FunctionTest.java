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

public class FunctionTest {

    @Test
    public void testFunctionQualifiers() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.FUNCTION_QUALIFIERS))
                .matches("const")
                .matches("async")
                .matches("")
                .matches("const unsafe")
                .matches("async unsafe")
                .matches("async")

        ;

    }

    @Test
    public void testAsyncConst() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.ASYNC_CONST_QUALIFIERS))
                .matches("const")
                .matches("async")
                .notMatches("constitution")
                .notMatches("prefixasync")
                .notMatches("constasync")
        ;

    }


    @Test
    public void testAbi() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.ABI))
                .matches("r\"foo\"")
                .matches("r#\"\"foo\"\"#")
                .matches("\"abc\"")
        ;

    }


    @Test
    public void testFunctionReturnType() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.FUNCTION_RETURN_TYPE))
                .matches("->i32")
                .matches("-> i32")
        ;

    }


    @Test
    public void testFunctionParam() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.FUNCTION_PARAM))
                .matches("x : i32")
                .matches("y:i64")
                .matches("f: &mut fmt::Formatter")



        ;

    }

    @Test
    public void testFunctionParameters() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.FUNCTION_PARAMETERS))
                .matches("y:i64")
                .matches("x : i32")
                .matches("x : i32, y:i64")

        ;

    }


    @Test
    public void testFunction() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.FUNCTION))
                .matches("fn same(x : i32)->i32 {;}")
                .matches("fn answer_to_life_the_universe_and_everything() -> i32 {\n" +
                        "    let y=42;\n" +
                        "}")
                .matches("fn main() {\n" +
                        "    println!(\"Hello, world!\");\n" +
                        "}")
                .matches("fn main() {\n" +
                        "    abc()\n" +
                        "}")
                .matches("fn main() {\n" +
                        "    println!(\"Hello, world!\");\n" +
                        "    println!(\"A second line \");\n" +
                        "}")
                .matches("fn main() {\n" +
                        "    println!(\"Hello, world!\");\n" +
                        "    abc()\n" +
                        "}")


        ;

    }
}