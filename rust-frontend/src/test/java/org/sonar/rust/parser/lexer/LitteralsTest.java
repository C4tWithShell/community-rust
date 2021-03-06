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
package org.sonar.rust.parser.lexer;

import org.junit.Test;
import org.sonar.rust.RustGrammar;

import static org.sonar.sslr.tests.Assertions.assertThat;

public class LitteralsTest {
    @Test
    public void charLitterals() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.CHAR_LITERAL))
                .matches("'foo'")
                .matches("'foo''bar'")
                .matches("'foo\"bar'")
                .matches("'C:\\SonarSource\\foo.rs'");
    }


    @Test
    public void testUnicode() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.UNICODE_ESCAPE))
                .matches("\\u{0027}");
    }

    @Test
    public void testQuote() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.QUOTE_ESCAPE))
                .matches("\\'")
                .matches("\\\"")
        ;
    }

    @Test
    public void testAscii() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.ASCII_ESCAPE))
                .matches("\\x7f")
                .matches("\\r")
                .matches("\\t")
                .matches("\\")

        ;
    }


    @Test
    public void testChars() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.CHAR_LITERAL))
                .matches("'a'")
                .matches("'5'")
                .matches("'\\u{0027}'")
        ;
    }

    @Test
    public void testStringContent(){
        assertThat(RustGrammar.create().build().rule(RustGrammar.STRING_CONTENT))
                .matches("abc")
                .matches("abc,def!@")
                .matches("\r\n")
                //.notMatches("\r")
                .notMatches("\"")
                .notMatches("\"hello")
                .notMatches("hello\"")
                .notMatches("hello\"world")
                .notMatches("\"hello\"")
                ;
    }

    @Test
    public void testStrings() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.STRING_LITERAL))
                .matches("\"a\"")
                .matches("\"5\"")
                .matches("\"some text\"")
                .matches("\"hello,world!\"")
                //FIXME .matches("\"some text with \\\" quote escape \"")
                .matches("\"\\'\"")
                .matches("\"\\\"\"")
                .matches("\"\\x7f\"")
                .matches("\"\\r\"")
                .matches("\"\\t\"")
                .notMatches("\"\\\"")
                .notMatches("\"hello\")")


        ;
    }

    @Test
    public void testRawStrings() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.RAW_STRING_LITERAL))
                .matches("r\"foo\"")
                .matches("r#\"\"foo\"\"#")
                .matches("r\"R\"")
                .matches("r\"\\x52\"")

        ;
    }

    @Test
    public void testByteEscape() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.BYTE_ESCAPE))
                .matches("\\xff")
                .matches("\\xBB")
                .matches("\\x00")
                .matches("\\n")
                .matches("\\r")
                .matches("\\t")
                .matches("\\")

        ;
    }


    @Test
    public void testByteLiteral() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.BYTE_LITERAL))
                .matches("b'a'")
                .matches("b'5'")
                .notMatches("b'',")
                .notMatches("b''\\")
                .notMatches("b''\\n")
                .notMatches("b''\\r")
                .notMatches("b''\\t")
                .matches("b'\\xff'")
        ;

    }



    @Test
    public void testByteStringLiteral() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.BYTE_STRING_LITERAL))
                .matches("b\"a\"")
                .matches("b\"5\"")
                .matches("b\"a string\"")
                .matches("b\"\\xff\"")
        ;

    }

    @Test
    public void testRawByteStrings() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.RAW_BYTE_STRING_LITERAL))
                .matches("br\"foo\"")
                .matches("br#\"\"foo\"\"#")
                .matches("br\"R\"")
                .matches("br\"\\x52\"")

        ;
    }

    @Test
    public void testDecLiteral() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.DEC_LITERAL))
                .matches("123")
        ;

    }

    @Test
    public void testHexa() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.HEX_LITERAL))
                .matches("0xf") // type i32
                .matches("0xff")

        ;
    }

    @Test
    public void testOct() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.OCT_LITERAL))
                .matches("0o70")

        ;


    }

    @Test
    public void testBin() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.BIN_LITERAL))
                .matches("0b1111_1111")

        ;


    }

    @Test
    public void testInteger() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.INTEGER_LITERAL))
                .matches("123") // type i32


                .matches("123i32")                           // type i32
                .matches("123u32")                           // type u32
                .matches("123_u32")                          // type u32


                .matches("0xff")                             // type i32
                .matches("0xff_u8")                           // type u8

                .matches("0o70")                             // type i32
                .matches("0o70_i16")                          // type i16

                .matches("0b1111_1111_1001_0000")            // type i32
                .matches("0b1111_1111_1001_0000i64")          // type i64
                .matches("0b________1")                       // type i32

                .matches("0usize")  // type usize
                // invalid suffixes

                .notMatches("0invalidSuffix")

// uses numbers of the wrong base

                .notMatches("123AFB43")
                .notMatches("0b0102")
                .notMatches("0o0581")

// Case not supported integers too big for their type (they overflow)

        //example .notMatches("128_i8")
        //example .notMatches("256_u8")

// Not supported bin, hex, and octal literals must have at least one digit

        //example .notMatches("0b_")
        //example .notMatches("0b____")


        ;

    }

    @Test
    public void testFloatSuffix() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.FLOAT_SUFFIX))
                .matches("f32")
                .matches("f64")
        ;
    }

    @Test
    public void testFloatExponent() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.FLOAT_EXPONENT))
                .matches("E-33")
                .matches("e5_1")
                .matches("E+99_")
        ;
    }

    @Test
    public void testFloat() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.FLOAT_LITERAL))
                .matches("12E+99f32")
                .matches("12E+99_f64")      // type f64
                .matches("1.23")
                .matches("123.0f64")       // type f64
                .matches("0.1f64")          // type f64
                .matches("0.1f32")         // type f32
                .matches("3.1415927")
                .matches("2.")
        ;
    }

    @Test
    public void testBoolean() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.BOOLEAN_LITERAL))
                .matches("true")
                .matches("false");
    }



    @Test
    public void testPunctuation() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.PUNCTUATION))
                .matches("+")
                .matches("-")
                .matches("*")
                .matches("/")
                .matches("%")
                .matches("^")
                .matches("!")
                .matches("&")
                .matches("||")
                .matches("<<")
                .matches(">>")
                .matches("+=")
                .matches("-=")
                .matches("*=")
                .matches("/=")
                .matches("%=")
                .matches("^=")
                .matches("&=")
                .matches("|=")
                .matches("<<=")
                .matches(">>=")
                .matches("=")
                .matches("==")
                .matches("!=")
                .matches(">")
                .matches("<")
                .matches(">=")
                .matches("<=")
                .matches("@")
                .matches("_")
                .matches(".")
                .matches("..")
                .matches("...")
                .matches("..=")
                .matches(",")
                .matches(";")
                .matches(":")
                .matches("::")
                .matches("->")
                .matches("=>")
                .matches("#")
                .matches("$")
                .matches("?")
        ;

    }

    @Test
    public void testDelimiters(){
        assertThat(RustGrammar.create().build().rule(RustGrammar.DELIMITERS))
                .matches("{")
                .matches("}")
                .matches("(")
                .matches(")")
                .matches("[")
                .matches("]")
        ;

    }


}
