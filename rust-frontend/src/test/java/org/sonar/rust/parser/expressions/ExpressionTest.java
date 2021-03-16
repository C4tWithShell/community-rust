/**
 *
 * Sonar Rust Plugin (Community)
 * Copyright (C) 2020 Eric Le Goff
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
package org.sonar.rust.parser.expressions;

import org.junit.Test;
import org.sonar.rust.RustGrammar;

import static org.sonar.sslr.tests.Assertions.assertThat;

public class ExpressionTest {

    @Test
    public void testExpression() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.EXPRESSION))
                .matches("{let y=42;}")
                .matches("{;}")
                .matches("0")
                .matches("3+2")
                .matches("1 << 1")
                .matches("foo")
                .matches("Some(42)")
                .matches("panic!()")
                .notMatches("== b")
                .matches("len(values)")
                .matches("\"123\".parse()")
                .matches("\"Some string\".to_string()")
                .matches("42")
                .matches("0.0")
                .matches("pi.unwrap_or(1.0).log(2.72)")
                .matches("callme()")
                .matches("println!(\"{}, {}\", word, j)")
                .matches("{}")
                .matches("i.get()")
                .matches("m.get(i) + 1")
                .matches("dest.write_char('n')")
                .matches("Identifier::Numeric")
                .matches("Vec::new")



        ;
    }
}
