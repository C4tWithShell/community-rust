/**
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
package org.sonar.rust.parser;

import org.junit.Test;
import org.sonar.rust.RustGrammar;

import static org.sonar.sslr.tests.Assertions.assertThat;

public class CompilationUnitTest {

    @Test
    public void testCompilationUnit() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.COMPILATION_UNIT))
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


        ;
    }
}
