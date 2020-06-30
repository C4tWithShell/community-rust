package org.sonar.rust.parser.lexer;

import org.junit.Test;
import org.sonar.rust.RustGrammar;

import static org.sonar.sslr.tests.Assertions.assertThat;

public class PathTest {
    @Test
    public void testSimplePathSegment() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.SIMPLE_PATH_SEGMENT))
                .matches("super")
                .matches("self")
                .matches("crate")
                .matches("$crate")
                .matches("abc")
                .matches("r#a")
                .matches("U213")
        ;
    }

    @Test
    public void testSimplePath() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.SIMPLE_PATH))
                .matches("std::io::Write")
                .matches("std::io::super")
        ;
    }

    @Test
    public void testPathInExpression() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.PATH_IN_EXPRESSION))
                .matches("Vec::<u8>::with_capacity")
                .matches("collect::<Vec<_>>")
        ;
    }

    @Test
    public void testQualifiedPathInExpression() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.QUALIFIED_PATH_IN_EXPRESSION))
                .matches("<S as T1>::f")

        ;
    }


    @Test
    public void testPathIdentSegment() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.PATH_IDENT_SEGMENT))
                .matches("super")
                .matches("self")
                .matches("crate")
                .matches("$crate")
                .matches("abc")
                .matches("r#a")
                .matches("U213")
        ;


    }

    @Test
    public void testTypePathFnInputs() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.TYPE_PATH_FN_INPUTS))
                .matches("isize")

        ;
    }

    @Test
    public void testTypePathFn() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.TYPE_PATH_FN))
                .matches("(isize) -> isize")

        ;
    }

    @Test
    public void testTypePathSegment() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.TYPE_PATH_SEGMENT))
                .matches("super")
                .matches("abc")
                .matches("r#a")
                .matches("U213")
                .matches("abc::(isize) -> isize")
                .matches("abc::<>")
        ;
    }

    @Test
    public void testTypePath() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.TYPE_PATH))
                .matches("std::boxed::Box<dyn std::ops::FnOnce(isize) -> isize>")
                .matches("abc::abc")

        ;
    }
}
