/**
 *
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

import org.junit.Test;
import org.sonar.rust.RustGrammar;

import static org.sonar.sslr.tests.Assertions.assertThat;

public class MatchExpressionTest {


    @Test
    public void tesMatchArmGuard() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.MATCH_ARM_GUARD))
                .matches("if { i.set(i.get() + 1); false }")

        ;
    }

    @Test
    public void tesMatchArmPattern() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.MATCH_ARM_PATTERNS))
                .matches("42")
                .matches("foo")
                .matches("S(z @ 1, _)")


        ;
    }

    @Test
    public void tesMatchArm() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.MATCH_ARM))
                .matches("1")
                .matches("a|b")
                .matches("a|b|c")
                .matches("1 | _ if { i.set(i.get() + 1); false }")
                .matches("_")
        ;
    }


    @Test
    public void tesMatchArms() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.MATCH_ARMS))
                .matches("1 => println!(\"one\")")
                .matches("_ => println!(\"anything else\")")
                .matches("1 => println!(\"one\")," +
                         "7 => println!(\"seven\")")
                .matches("1 => println!(\"one\")," +
                         "_ => println!(\"other\")")
                .matches("1 => println!(\"one\")," +
                        "2 => println!(\"two\")," +
                        "_ => println!(\"other\")")
                .matches("1 => println!(\"one\")," +
                        "2 => println!(\"two\"),\n" +
                        "3 => println!(\"three\"),\n" +
                        "4 => println!(\"four\"),\n" +
                        "_ => println!(\"other\")")
                .matches("S(z @ 1, _) => assert_eq!(z, 1)")
                .matches("S(z @ 1, _) | S(_, z @ 2) => assert_eq!(z, 1)")
                .matches("_ => {}")
                .matches("1 | _ if { i.set(i.get() + 1); false } => {}" )
                .matches("1 | _ if { i.set(i.get() + 1); false } => {}\n" +
                        "_ => {}")

                ;



    }

    @Test
    public void tesMatchExpression() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.MATCH_EXPRESSION))
                .matches("match x {\n" +
                        "    1 => println!(\"one\"),\n" +
                        "    2 => println!(\"two\"),\n" +
                        "    3 => println!(\"three\"),\n" +
                        "    4 => println!(\"four\"),\n" +
                        "    5 => println!(\"five\"),\n" +
                        "    _ => println!(\"something else\")" +
                        "}")
                .matches("match S(1, 2) {\n" +
                        "    S(z @ 1, _) | S(_, z @ 2) => assert_eq!(z, 1),\n" +
                        "    _ => panic!()" +
                        "}")
                .matches("match foo {\n" +
                        "    1 | _ if { i.set(i.get() + 1); false } => {}\n" +
                        "    \n" +
                        "}")

        ;
    }
}