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
package org.sonar.rust.parser.patterns;

import org.junit.Test;
import org.sonar.rust.RustGrammar;

import static org.sonar.sslr.tests.Assertions.assertThat;

public class PatternTest {

    @Test
    public void testLitteralPattern() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.LITERAL_PATTERN))
                .matches("42")

        ;
    }


    @Test
    public void testIdentifierPattern(){
        assertThat(RustGrammar.create().build().rule(RustGrammar.IDENTIFIER_PATTERN))
                .matches("e @ 1..=5")
                .matches("f @ 'a'..='z'")
                .matches("None")

        ;

    }
    @Test
    public void testWildcardPattern(){
        assertThat(RustGrammar.create().build().rule(RustGrammar.WILDCARD_PATTERN))
                .matches("_")

        ;

    }

    @Test
    public void testRestPattern(){
        assertThat(RustGrammar.create().build().rule(RustGrammar.REST_PATTERN))
                .matches("..")

        ;

    }

    @Test
    public void testRangePatternBound(){
        assertThat(RustGrammar.create().build().rule(RustGrammar.RANGE_PATTERN_BOUND))
                .matches("1")
                .matches("'a'")
                .matches("'z'")

        ;

    }

    @Test
    public void testRangePattern(){
        assertThat(RustGrammar.create().build().rule(RustGrammar.RANGE_PATTERN))
                .matches("1..=9")



        ;

    }

    @Test
    public void testObsoleteRangePattern(){
        assertThat(RustGrammar.create().build().rule(RustGrammar.OBSOLETE_RANGE_PATTERN))
                .matches("1...9")



        ;

    }

    @Test
    public void testReferencePattern(){
        assertThat(RustGrammar.create().build().rule(RustGrammar.REFERENCE_PATTERN))
                .matches("&42")
                .matches("&&42")
                .matches("&mut 42")
                .matches("&[u8]")

        ;

    }

    @Test
    public void testStructPatternElements(){
        assertThat(RustGrammar.create().build().rule(RustGrammar.STRUCT_PATTERN_ELEMENTS))
                .matches("..")
                .matches("x: 10")
                .matches("x: 10, y : 20")
                .matches("x")
                .matches("x,y")
                .matches("x,y")


        ;

    }

    @Test
    public void testStructPatternField(){
        assertThat(RustGrammar.create().build().rule(RustGrammar.STRUCT_PATTERN_FIELD))
                .matches("1:42")
                .matches("#[outer]1:42")
                .matches("foo:42")
                .matches("bar")
                .matches("ref mut bar")
                .matches("x:10")


        ;

    }

    @Test
    public void testStructPattern(){
        assertThat(RustGrammar.create().build().rule(RustGrammar.STRUCT_PATTERN))
                .matches("Point{}")
                .matches("Point{..}")
                .matches("TokenOrComment::Comment{..}")
                .matches("TokenOrComment::Comment { .. }")
                .matches("ModuleSource{code,module_url_specified,module_url_found,}")

        ;

    }
    @Test
    public void testTupleStructPattern(){
        assertThat(RustGrammar.create().build().rule(RustGrammar.TUPLE_STRUCT_PATTERN))
                .matches("local_var()")
                .matches("S(z @ 1, _)")



        ;

    }

    @Test
    public void testTuplePatternItems(){
        assertThat(RustGrammar.create().build().rule(RustGrammar.TUPLE_PATTERN_ITEMS))
                .matches("42,")
                .matches("..")
                .matches("\"bacon\",")
                .matches("\"bacon\",42")
                .matches("\"bacon\", foo")
                .matches("\"bacon\" , foo")


        ;

    }



    @Test
    public void testTuplePattern(){
        assertThat(RustGrammar.create().build().rule(RustGrammar.TUPLE_PATTERN))
                .matches("()")
                .matches("(42,)")
                .matches("(..)")
                .matches("(\"bacon\" ,)")
                .matches("(\"bacon\",42)")

        ;

    }

    @Test
    public void testGroupedPattern(){
        assertThat(RustGrammar.create().build().rule(RustGrammar.GROUPED_PATTERN))
                .matches("(42)")
                .matches("( foo )")

        ;

    }

    @Test
    public void testSlicePattern(){
        assertThat(RustGrammar.create().build().rule(RustGrammar.SLICE_PATTERN))
                .matches("[42]")
                .matches("[42,foo, bar]")
                .matches("[42,foo, bar,]")


        ;

    }

    @Test
    public void testPathPattern(){
        assertThat(RustGrammar.create().build().rule(RustGrammar.PATH_PATTERN))
                .matches("Vec::<u8>::with_capacity")
                .matches("<S as T1>::f")
                .matches("Token::BackQuote")

        ;

    }



    @Test
    public void testPattern() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.PATTERN))
                .matches("Token::BackQuote") //path pattern
                //range patterns
                .matches("1..=9")
                .matches("1...9") // obsolete
                //literal
                .matches("42")
                .matches("foo")
                //macro invocation
                .matches("std::io::Write!()")
                .matches("panic!()")
                .matches("println!(\"{}, {}\", word, j)")
                .notMatches("")
                //identifier pattern
                .matches("f @ 'a'..='z'")
                .matches("_") //wildcard
                .matches("..") //rest
                //reference
                .matches("&mut 42")
                .matches("&[u8]")
                //struct
                .matches("Point{..}")
                .matches("ModuleSource{code,module_url_specified,module_url_found,}")
                //tuple struct
                .matches("local_var()")
                .matches("S(z @ 1, _)")
                .matches("(\"Bacon\", b)")






        ;
    }
}