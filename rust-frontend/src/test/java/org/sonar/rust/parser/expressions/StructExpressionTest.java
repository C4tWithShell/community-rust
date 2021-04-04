package org.sonar.rust.parser.expressions;

import org.junit.Test;
import org.sonar.rust.RustGrammar;

import static org.sonar.sslr.tests.Assertions.assertThat;

public class StructExpressionTest {

    //case 3
    @Test
    public void testStructExprUnit() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.STRUCT_EXPR_UNIT))
                .matches("Vec::<u8>::with_capacity")
                .matches("collect::<Vec<_>>")
                .matches("some_fn::<Cookie>")

        ;

    }

    //case 2
    @Test
    public void testStructExprTuple() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.STRUCT_EXPR_TUPLE))
                .matches("Vec::<u8>::with_capacity()")
                .matches("collect::<Vec<_>>()")
                .matches("some_fn::<Cookie>(#![inner])")
                .matches("some_fn::<Cookie>(Cookie)")
                .matches("some_fn::<Cookie>(#![inner] Cookie)")
        ;

    }


    @Test
    public void testStructExprBase() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.STRUCT_BASE))
                .matches("..foo")
                .matches(".. bar")


        ;

    }

    //case 1
    @Test
    public void testStructExprStruct() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.STRUCT_EXPR_STRUCT))
                .matches("game::User{name:\"Joe\",age:35,score:100_000}")
                .matches("game::User {name: \"Joe\", age: 35, score: 100_000}")
                .matches("TuplePoint { 0: 10.0, 1: 20.0}")
                .matches("TuplePoint { .. bar}")
                .matches("TuplePoint { age :35,.. structbase}")
        ;
    }


    @Test
    public void testStructExprFields() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.STRUCT_EXPR_FIELDS))
                .matches("name:\"Joe\",age:35,score:100_000")
                .matches("name: \"Joe\", age : 35, score : 100_000")
                .matches("0: 10.0, 1: 20.0")
          ;



    }

    @Test
    public void testStructExprField() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.STRUCT_EXPR_FIELD))
                .matches("name")
                .matches("name:\"Joe\"")
                .matches("age:35")
                .matches("score:100_000")
                .matches("score : 100_000")
                .matches("0: 10.0")
                .matches("name: user.getName()")
                .matches("name: user.name")
        ;



    }



    @Test
    public void testStructExpression() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.STRUCT_EXPRESSION))
                .matches("Point{x:10.0,y:20.0}")
                .matches("Point {x: 10.0, y: 20.0}")
                .matches("NothingInMe {}")
                .matches("TuplePoint { 0: 10.0, 1: 20.0 }")
                .matches("game::User {name: \"Joe\", age: 35, score: 100_000}")
                .matches("some_fn::<Cookie>(Cookie)")
                .matches("Version {\n" +
                        "            name:  user.firstName,\n" +
                        "            major: other.major,\n" +
                        "            minor: other.minor,\n" +
                        "            patch: other.patch,\n" +
                        "        }")
                .matches("MediaElementAudioSourceNode {\n" +
                        "            node,\n" +
                        "            media_element,\n" +
                        "        }")
                .notMatches("match path.parent() {\n" +
                "             Some(ref parent) => self.ensure_dir_exists(parent),\n" +
                "             None => Ok(()),\n" +
                "         }")



        ;



    }
}
