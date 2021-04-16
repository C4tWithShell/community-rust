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
package org.sonar.rust.parser.types;

import org.junit.Test;
import org.sonar.rust.RustGrammar;

import static org.sonar.sslr.tests.Assertions.assertThat;

public class TypeTest {



    @Test
    public void testTypeNoBounds() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.TYPE_NO_BOUNDS))
                .matches("i32")
                .matches("(i32, u8)")
                .matches("Circle")
                .notMatches("Circle{")
                .matches("[u8]")


        ;
    }

    @Test
    public void testimplTraitType() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.IMPL_TRAIT_TYPE))
        //TODO


        ;
    }

    @Test
    public void testTraitObjectType() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.TRAIT_OBJECT_TYPE))
        //TODO


        ;
    }


    @Test
    public void testType() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.TYPE))
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

        ;
    }
}
