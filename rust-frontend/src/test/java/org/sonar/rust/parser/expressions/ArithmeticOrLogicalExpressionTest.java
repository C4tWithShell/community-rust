/**
 *
 * Sonar Rust Plugin (Community)
 * Copyright (C) 2020 Eric Le Goff
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

import org.junit.Test;
import org.sonar.rust.RustGrammar;
import org.sonar.rust.api.RustPunctuator;
import org.sonar.sslr.grammar.GrammarRuleKey;

import static org.sonar.sslr.tests.Assertions.assertThat;

public class ArithmeticOrLogicalExpressionTest {

    @Test
    public void testShlExpression() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.SHL_EXPRESSION))
                .matches("1")
                .matches("<< 0")
                .matches("<<0 <<1")
                .matches("<<0<< 1 <<2")
        ;
    }


    private void testArithmetic(GrammarRuleKey g, String op) {
        assertThat(RustGrammar.create().build().rule(g))
                .matches("1")
                .matches(op+"0")
                .matches(op+"0"+op+ " 1")
                .matches(op+"0"+op+" 1 "+ op+"2")
        ;
    }


    @Test
    public void testExpressions() {
        testArithmetic(RustGrammar.SHL_EXPRESSION, RustPunctuator.SHL.getValue());
        testArithmetic(RustGrammar.SHR_EXPRESSION, RustPunctuator.SHR.getValue());
        testArithmetic(RustGrammar.ADDITION_EXPRESSION, RustPunctuator.PLUS.getValue());
        testArithmetic(RustGrammar.SUBTRACTION_EXPRESSION, RustPunctuator.MINUS.getValue());
        testArithmetic(RustGrammar.DIVISION_EXPRESSION, RustPunctuator.SLASH.getValue());
        testArithmetic(RustGrammar.REMAINDER_EXPRESSION, RustPunctuator.PERCENT.getValue());
        testArithmetic(RustGrammar.BITAND_EXPRESSION, RustPunctuator.AND.getValue());
        testArithmetic(RustGrammar.BITOR_EXPRESSION, RustPunctuator.OR.getValue());
        testArithmetic(RustGrammar.BITXOR_EXPRESSION, RustPunctuator.CARET.getValue());
    }





    @Test
    public void testArithmeticOrLogicalExpression() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.ARITHMETIC_OR_LOGICAL_EXPRESSION))
                .matches("1<<0")
        ;
    }
}
