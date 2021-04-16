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
package org.sonar.rust.parser.attributes;

import org.junit.Test;
import org.sonar.rust.RustGrammar;

import static org.sonar.sslr.tests.Assertions.assertThat;

public class AttributeTest {


    @Test
    public void testAttribute() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.ATTR))
                .matches("foo")
                .matches("foo_bar")
                .matches("foo_type")
                .matches("crate_type")

        ;
    }


    @Test
    public void testInnerAttribute() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.INNER_ATTRIBUTE))
                .matches("#![crate_type = \"lib\"]")
        ;
    }

    @Test
    public void testOuterAttribute() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.OUTER_ATTRIBUTE))
                .matches("#[test]")
                .matches("#[cfg(feature = \"serde\")]")
                .matches("#[inline]")
                .matches("#[allow(unrooted_must_root)]")
        ;
    }
}