package org.sonar.rust.ast.parser.grammar.lexical;

import org.junit.Test;
import org.sonar.rust.ast.parser.RustLexer;
import org.sonar.rust.ast.parser.grammar.GrammarTesting;
import org.sonar.sslr.tests.Assertions;

public class LitteralsTest extends GrammarTesting {

    @Test
    public void testUnicode() {
        Assertions.assertThat(g.rule(RustLexer.UNICODE_ESCAPE))
                .matches("\\u{0027}");
    }

    @Test
    public void testQuote() {
        Assertions.assertThat(g.rule(RustLexer.QUOTE_ESCAPE))
                .matches("\\'")
                .matches("\\\"")
        ;
    }

    @Test
    public void testAscii() {
        Assertions.assertThat(g.rule(RustLexer.ASCII_ESCAPE))
                .matches("\\x7f")
                .matches("\\r")
                .matches("\\t")
                .matches("\\")

        ;
    }


    @Test
    public void testChars() {
        Assertions.assertThat(g.rule(RustLexer.CHAR_LITERAL))
                .matches("'a'")
                .matches("'5'")
                .matches("'\\u{0027}'")
        ;
    }

    @Test
    public void testStrings() {
        Assertions.assertThat(g.rule(RustLexer.STRING_LITERAL))
                .matches("\"a\"")
                .matches("\"5\"")
                .matches("\"some text\"")
                .matches("\"some text with \\\" quote escape \"")
                .matches("\"\\'\"")
                .matches("\"\\\"\"")
                .matches("\"\\x7f\"")
                .matches("\"\\r\"")
                .matches("\"\\t\"")
                .matches("\"\\\"")

        ;
    }

    @Test
    public void testRawStrings() {
        Assertions.assertThat(g.rule(RustLexer.RAW_STRING_LITERAL))
                .matches("r\"foo\"")
                .matches("r#\"\"foo\"\"#")
                .matches("r\"R\"")
                .matches("r\"\\x52\"")

        ;
    }

    @Test
    public void testByteEscape() {
        Assertions.assertThat(g.rule(RustLexer.BYTE_ESCAPE))
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
    public void testAsciiChar() {
        Assertions.assertThat(g.rule(RustLexer.ASCII_FOR_CHAR))
                .matches("a")
                .matches("5")
                .notMatches("'")

        ;
    }


    @Test
    public void testByteLiteral() {
        Assertions.assertThat(g.rule(RustLexer.BYTE_LITERAL))
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
    public void testAsciiString() {
        Assertions.assertThat(g.rule(RustLexer.ASCII_FOR_STRING))
                .matches("a")
                .matches("abc string")
        ;
    }


    @Test
    public void testByteStringLiteral() {
        Assertions.assertThat(g.rule(RustLexer.BYTE_STRING_LITERAL))
                .matches("b\"a\"")
                .matches("b\"5\"")
                .matches("b\"a string\"")
                .matches("b\"\\xff\"")
        ;

    }

    @Test
    public void testRawByteStrings() {
        Assertions.assertThat(g.rule(RustLexer.RAW_BYTE_STRING_LITERAL))
                .matches("br\"foo\"")
                .matches("br#\"\"foo\"\"#")
                .matches("br\"R\"")
                .matches("br\"\\x52\"")

        ;
    }

    @Test
    public void testDecLiteral() {
        Assertions.assertThat(g.rule(RustLexer.DEC_LITERAL))
                .matches("123")
        ;

    }

    @Test
    public void testHexa() {
        Assertions.assertThat(g.rule(RustLexer.HEX_LITERAL))
                .matches("0xf") // type i32
                .matches("0xff")

        ;
    }

    @Test
    public void testOct() {
        Assertions.assertThat(g.rule(RustLexer.OCT_LITERAL))
                .matches("0o70")

        ;


    }

    @Test
    public void testBin() {
        Assertions.assertThat(g.rule(RustLexer.BIN_LITERAL))
                .matches("0b1111_1111")

        ;


    }

    @Test
    public void testInteger() {
        Assertions.assertThat(g.rule(RustLexer.INTEGER_LITERAL))
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

// integers too big for their type (they overflow)

        //FIXME .notMatches("128_i8")
        //FIXME .notMatches("256_u8")

// bin, hex, and octal literals must have at least one digit

        //FIXME .notMatches("0b_")
        //FIXME .notMatches("0b____")


        ;

    }

    @Test
    public void testFloatSuffix() {
        Assertions.assertThat(g.rule(RustLexer.FLOAT_SUFFIX))
                .matches("f32")
                .matches("f64")
        ;
    }

    @Test
    public void testFloatExponent() {
        Assertions.assertThat(g.rule(RustLexer.FLOAT_EXPONENT))
                .matches("E-33")
                .matches("e5_1")
                .matches("E+99_")
        ;
    }

    @Test
    public void testFloat() {
        Assertions.assertThat(g.rule(RustLexer.FLOAT_LITERAL))
                .matches("12E+99f32")
                .matches("12E+99_f64")      // type f64
                .matches("1.23")
                .matches("123.0f64")       // type f64
                .matches("0.1f64")          // type f64
                .matches("0.1f32")         // type f32

                .matches("2.")
        ;
    }

    @Test
    public void testBoolean() {
        Assertions.assertThat(g.rule(RustLexer.BOOLEAN_LITERAL))
                .matches("true")
                .matches("false");
    }

    @Test
    public void testLifetimeToken() {
        Assertions.assertThat(g.rule(RustLexer.LIFETIME_TOKEN))
                .matches("'_")
                .matches("'abc")
                .matches("'U123")
                .matches("'_42")
        ;
    }

    @Test
    public void testLifetimeOrLabel() {
        Assertions.assertThat(g.rule(RustLexer.LIFETIME_OR_LABEL))
                .matches("'a")
                .matches("'ABC")
                .notMatches("'as") //as is a keyword
        ;

    }

    @Test
    public void testPunctuation() {
        Assertions.assertThat(g.rule(RustLexer.PUNCTUATION))
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
        Assertions.assertThat(g.rule(RustLexer.DELIMITERS))
                .matches("{")
                .matches("}")
                .matches("(")
                .matches(")")
                .matches("[")
                .matches("]")
                ;

    }


}




