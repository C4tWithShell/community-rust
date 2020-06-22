package org.sonar.rust.ast.parser.grammar.lexical;

import org.junit.Test;
import org.sonar.rust.ast.parser.RustLexer;
import org.sonar.rust.ast.parser.grammar.GrammarTesting;
import org.sonar.sslr.tests.Assertions;

public class PathTest extends GrammarTesting {

    @Test
    public void testSimplePathSegment() {
        Assertions.assertThat(g.rule(RustLexer.SIMPLE_PATH_SEGMENT))
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
        Assertions.assertThat(g.rule(RustLexer.SIMPLE_PATH))
                .matches("std::io::Write")
                .matches("std::io::super")
        ;
    }

    @Test
    public void testPathInExpression() {
        Assertions.assertThat(g.rule(RustLexer.PATH_IN_EXPRESSION))
                .matches("Vec::<u8>::with_capacity")
                .matches("collect::<Vec<_>>")
        ;
    }

    @Test
    public void testQualifiedPathInExpression() {
        Assertions.assertThat(g.rule(RustLexer.QUALIFIED_PATH_IN_EXPRESSION))
                .matches("<S as T1>::f")

        ;
    }


    @Test
    public void testPathIdentSegment() {
        Assertions.assertThat(g.rule(RustLexer.PATH_IDENT_SEGMENT))
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
        Assertions.assertThat(g.rule(RustLexer.TYPE_PATH_FN_INPUTS))
                .matches("isize")

        ;
    }

    @Test
    public void testTypePathFn() {
        Assertions.assertThat(g.rule(RustLexer.TYPE_PATH_FN))
                .matches("(isize) -> isize")

        ;
    }

    @Test
    public void testTypePathSegment() {
        Assertions.assertThat(g.rule(RustLexer.TYPE_PATH_SEGMENT))
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
        Assertions.assertThat(g.rule(RustLexer.TYPE_PATH))
                .matches("std::boxed::Box<dyn std::ops::FnOnce(isize) -> isize>")
                .matches("abc::abc")

        ;
    }


}
