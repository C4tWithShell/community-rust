/**
 * Community Rust Plugin
 * Copyright (C) 2021 Eric Le Goff
 * mailto:community-rust AT pm DOT me
 * http://github.com/elegoff/sonar-rust
 * <p>
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.sonar.rust.parser.lexer;

import org.junit.Test;
import org.sonar.rust.RustGrammar;
import org.sonar.sslr.tests.Assertions;

public class IdentifierTest {


    @Test
    public void checkRawIdentifier() {
        Assertions.assertThat(RustGrammar.create().build().rule(RustGrammar.RAW_IDENTIFIER))
                .notMatches("r#")
                .matches("r#a")
                .matches("r#_52")
                .matches("r#V123")
                .notMatches("s#52")
                //corner cases
                .notMatches("r#crate")
                .notMatches("r#self")
                .notMatches("r#super")
                .notMatches("r#Self");
    }

    @Test
    public void testNonKeywords() {
        Assertions.assertThat(RustGrammar.create().build().rule(RustGrammar.NON_KEYWORD_IDENTIFIER))
                .matches("a")
                .matches("bc")
                .matches("Abc")
                .notMatches("as")
                .notMatches("trait") //keyword
                .matches("prefix_trait")
                .matches("traitsuffix")
                .matches("foo_bar")
                .notMatches("a b")
                .notMatches("a self")
                .matches("_context")
        ;
    }

    @Test
    public void testIdentifier() {
        Assertions.assertThat(RustGrammar.create().build().rule(RustGrammar.IDENTIFIER))
                .matches("a")
                .matches("bc")
                .matches("Abc")
                .notMatches("as")
                .notMatches("trait")
                .notMatches("super")
                .notMatches("foo ")
                .notMatches("r#")
                .matches("r#a")
                .matches("r#_52")
                .matches("r#V123")
                .notMatches("s#52")
                .matches("phenotype")
                .matches("crate_type")
                .matches("await_token")
                .matches("if_ok")
                .matches("foo")
                .matches("_identifier")
                .matches("r#true")
                //.matches("Москва")
                //.matches("東京")

        ;

    }
}
