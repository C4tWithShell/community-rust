/**
 * Community Rust Plugin
 * Copyright (C) 2021-2024 Vladimir Shelkovnikov
 * https://github.com/C4tWithShell/community-rust
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

import org.junit.jupiter.api.Test;
import org.sonar.rust.RustGrammar;


import static org.sonar.sslr.tests.Assertions.assertThat;

class RangeExpressionTest {

  @Test
  void testRangeExpr() {
//        assertThat(RustGrammar.create().build().rule(RustGrammar.RANGE_EXPR))
    assertThat(RustGrammar.create().build().rule(RustGrammar.EXPRESSION))
      .matches("1..2")
      .matches("start..end")
      .matches("0..(4 - (len & 3)) & 3")

    ;
  }

  @Test
  void testRangeFrom() {
    //assertThat(RustGrammar.create().build().rule(RustGrammar.RANGE_FROM_EXPR))
    assertThat(RustGrammar.create().build().rule(RustGrammar.EXPRESSION))
      .matches("1..")

    ;
  }

  @Test
  void testRangeTo() {
    assertThat(RustGrammar.create().build().rule(RustGrammar.RANGE_TO_EXPR))
      .matches("..42")

    ;
  }

  @Test
  void testRangeFull() {
    assertThat(RustGrammar.create().build().rule(RustGrammar.RANGE_FULL_EXPR))
      .matches("..")

    ;
  }

  @Test
  void testRangeInclusive() {
    //assertThat(RustGrammar.create().build().rule(RustGrammar.RANGE_INCLUSIVE_EXPR))
    assertThat(RustGrammar.create().build().rule(RustGrammar.EXPRESSION))
      .matches("40..=42")

    ;
  }

  @Test
  void testRangeToInclusive() {
    assertThat(RustGrammar.create().build().rule(RustGrammar.RANGE_TO_INCLUSIVE_EXPR))
      .matches("..=7")

    ;
  }

  @Test
  void testRangeExpression() {
//        assertThat(RustGrammar.create().build().rule(RustGrammar.RANGE_EXPRESSION))
    assertThat(RustGrammar.create().build().rule(RustGrammar.EXPRESSION))
      .matches("1..2")// std::ops::Range
      .matches("..4")// std::ops::RangeTo
      .matches("..")// std::ops::RangeFull
      .matches("3..")// std::ops::RangeFrom
      .matches("..=7")// std::ops::RangeToInclusive
      .matches("0..top()")
      .matches("0..(4 - (len & 3)) & 3")
      .matches("5..=6")// std::ops::RangeInclusive
    ;
  }

}
