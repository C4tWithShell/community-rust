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

public class DottedExpressionTest {

    @Test
    public void testDottedExpression() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.DOTTED_EXPRESSION))
                .matches("\"Some string\".to_string()")
                .matches("\"3.14\".parse()")
                .matches("a.b()")
                .matches("a.b().c()")
                .matches("pi.unwrap_or(1.0).log(2.72)")
                .matches("j.set(i.get())")
                .matches("j.set(1)")
                .matches("Some::<i32>.calc()")
                .matches("a.foo()")
                .matches("b.abc()")
                .matches("obj.add(1i32,2i32)")
                .matches("callme().now()")
                .matches("callme()\n" +
                        ".now()")
                .matches("idf\n" +
                        ".fun()")
                .matches("d::mycall(a.clone(), b.clone()).unwrap()")
                .matches("node_fetch::create_http_client(user_agent.clone(), my_data.clone()).unwrap()")
                .matches("node_fetch::create_http_client(user_agent.clone(), my_data.clone())\n" +
                        "        .unwrap()")
                .matches("couple[0].to_lowercase()")
                .matches("self.0.iter()")
                .matches("(state.borrow().get_error_class_fn)(&error).as_bytes()")
                .matches("resp_header[0..8].foo()")
                .matches("mystruct.myfield")
                .matches("other.major")
                .matches("foo().x")
                //FIXME.matches("(Struct {a: 10, b: 20}).a")
                .matches("t.get_error_class")
                .matches("state.borrow().get_error_class_fn")
                .matches("self.get_cache_filename(url)")
                .matches("(disk_byte as char).to_string()")
                .matches("async move {}.local()")
                .matches("async move {\n" +
                        "            if check {\n" +
                        "                check_source_files(config, paths).await?;\n" +
                        "            } else {\n" +
                        "                format_source_files(config, paths).await?;\n" +
                        "            }\n" +
                        "            Ok(())\n" +
                        "        }\n" +
                        "            .boxed_local()")
                .matches("self.state")




                ;

    }
}
