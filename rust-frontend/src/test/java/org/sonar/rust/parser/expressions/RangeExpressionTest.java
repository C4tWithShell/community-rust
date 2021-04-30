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
package org.sonar.rust.parser.expressions;

import com.sonar.sslr.api.AstNode;
import org.fest.assertions.Assertions;
import org.junit.Test;
import org.sonar.rust.RustGrammar;
import org.sonar.sslr.parser.LexerlessGrammar;
import org.sonar.sslr.parser.ParserAdapter;

import java.nio.charset.StandardCharsets;

import static org.sonar.sslr.tests.Assertions.assertThat;

public class RangeExpressionTest {

    @Test
    public void testRangeExpr() {
//        assertThat(RustGrammar.create().build().rule(RustGrammar.RANGE_EXPR))
        assertThat(RustGrammar.create().build().rule(RustGrammar.EXPRESSION))
                .matches("1..2")
                .matches("start..end")
                //FIXME .matches("0..(4 - (len & 3)) & 3")

        ;
    }

    @Test
    public void testRangeFrom() {
        //assertThat(RustGrammar.create().build().rule(RustGrammar.RANGE_FROM_EXPR))
        assertThat(RustGrammar.create().build().rule(RustGrammar.EXPRESSION))
                .matches("1..")

        ;
    }

    @Test
    public void testRangeTo() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.RANGE_TO_EXPR))
                .matches("..42")

        ;
    }

    @Test
    public void testRangeFull() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.RANGE_FULL_EXPR))
                .matches("..")

        ;
    }

    @Test
    public void testRangeInclusive() {
        //assertThat(RustGrammar.create().build().rule(RustGrammar.RANGE_INCLUSIVE_EXPR))
       assertThat(RustGrammar.create().build().rule(RustGrammar.EXPRESSION))
                .matches("40..=42")

        ;
    }

    @Test
    public void testRangeToInclusive() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.RANGE_TO_INCLUSIVE_EXPR))
                .matches("..=7")

        ;
    }


    @Test
    public void testRangeExpression() {
//        assertThat(RustGrammar.create().build().rule(RustGrammar.RANGE_EXPRESSION))
        assertThat(RustGrammar.create().build().rule(RustGrammar.EXPRESSION))
                .matches("1..2")// std::ops::Range

                .matches("..4")// std::ops::RangeTo
                .matches("..")// std::ops::RangeFull
                .matches("3..")// std::ops::RangeFrom
                .matches("..=7")// std::ops::RangeToInclusive
                .matches("0..top()")
                //FIXME.matches("0..(4 - (len & 3)) & 3")

                .matches("5..=6")// std::ops::RangeInclusive
        ;
    }

    @Test
    public void testParsing(){
        ParserAdapter<LexerlessGrammar> parser = new ParserAdapter<>(StandardCharsets.UTF_8,RustGrammar.create().build());
        AstNode rootNode = parser.parse("let a = 1..5;");
        System.out.printf("[ELG]" + rootNode.getType());
        Assertions.assertThat(rootNode.getType()).isSameAs(RustGrammar.COMPILATION_UNIT);
        AstNode astNode = rootNode;
        Assertions.assertThat(astNode.getNumberOfChildren()).isEqualTo(4);
        System.out.println("[ELG]" + astNode.getType());
        for (AstNode n : astNode.getChildren()){
            System.out.println("[ELG] child" + n.getType());
        }
        AstNode st = astNode.getChildren().get(1);
        System.out.println("[ELG] st is " + st);
        for (AstNode n : st.getChildren()){
            System.out.println("[L1]" + n.getType());
            for (AstNode n2 : n.getChildren()){
                System.out.println("   [L2]" + n2.getType());
                for (AstNode n3 : n2.getChildren()){
                    System.out.println("      [L3]" + n3.getType());
                    for (AstNode n4 : n3.getChildren()){
                        System.out.println("         [L4]" + n4.getType());
                    }
                }

            }
        }
    }
}
