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

import org.junit.Test;
import org.sonar.rust.RustGrammar;

import static org.sonar.sslr.tests.Assertions.assertThat;

public class ExpressionTest {

    @Test
    public void testExpressionExceptStruct() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.EXPRESSION_EXCEPT_STRUCT))
                .matches("a")
                .matches("a || b")
                .matches("a || b && c")
                .notMatches("my_struct{}")
                .notMatches("my_struct{}.field")
                .notMatches("a || not_struct {}")
                .matches("matches.value_of(\"log-level\").unwrap()")

        ;
    }

    @Test
    public void testExpression() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.EXPRESSION))
                .matches("{let y=42;}")
                .matches("{;}")
                .matches("0")
                .matches("3+2")
                .matches("1 << 1")
                .matches("foo")
                .matches("Some(42)")
                .matches("panic!()")
                .notMatches("== b")
                .matches("len(values)")
                .matches("\"123\".parse()")
                .matches("\"Some string\".to_string()")
                .matches("42")
                .matches("return 42")
                .matches("return None")
                .matches("0.0")
                .matches("pi.unwrap_or(1.0).log(2.72)")
                .matches("pi.into_iter(1.0).log(2.72)")
                .matches("other.unwrap_or(1.0).map(From::from).collect()")
                .matches("callme()")
                .matches("println!(\"{}, {}\", word, j)")
                .matches("{}")
                .matches("i.get()")
                .matches("m.get(i) + 1")
                .matches("dest.write_char('n')")
                .matches("Identifier::Numeric")
                .matches("Vec::new")
                .matches("MediaElementAudioSourceNode {\n" +
                "            node,\n" +
                "            media_element,\n" +
                "        }")
                .matches("StepPosition::JumpEnd")
                .matches("*position == StepPosition::JumpEnd || *position == StepPosition::End")
                .matches("move |state : Rc<RefCell<OpState>>, bufs: BufVec| -> Op {\n" +
                        "        let mut b = 42;\n" +
                        "    }")
                .matches("match path.parent() {\n" +
                        "             Some(ref parent) => self.ensure_dir_exists(parent),\n" +
                        "             None => Ok(()),\n" +
                        "         }")
                .matches("if run_coverage {\n" +
                        "        println!(\"Coverage is running\");" +
                        " } ")
                .matches("(4 - (len & 3)) & 3")

                .matches("async move {\n" +
                        "            if check {\n" +
                        "                check_source_files(config, paths).await?;\n" +
                        "            } else {\n" +
                        "                format_source_files(config, paths).await?;\n" +
                        "            }\n" +
                        "            Ok(())\n" +
                        "        }\n" +
                        "            .boxed_local()")
                .notMatches("is_ok {\n" +
                "        // empty block" +
                " } ")
                .matches("..")
                .matches("break 42")
                .matches("break Ok(Poll::Pending)")

     ;
    }
}
