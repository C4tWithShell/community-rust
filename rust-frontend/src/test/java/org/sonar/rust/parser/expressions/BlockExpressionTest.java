/**
 * Sonar Rust Plugin (Community)
 * Copyright (C) 2020 Eric Le Goff
 * http://github.com/elegoff/sonar-rust
 * <p>
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.sonar.rust.parser.expressions;

import org.junit.Test;
import org.sonar.rust.RustGrammar;

import static org.sonar.sslr.tests.Assertions.assertThat;

public class BlockExpressionTest {

    /*
    BlockExpression :
   {
      InnerAttribute*
      Statements?
   }

Statements :
      Statement+
   | Statement+ ExpressionWithoutBlock
   | ExpressionWithoutBlock
     */


    @Test
    public void testStatements() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.STATEMENTS))
                .matches("println!(\"hi there\");")
                .matches("println!(\"hi there\");\n" +
                        "println!(\"how are you today ?\");")
                .matches("j.set(i.get()); false")
                .matches("j.set(i.get() + 1); false")
                .matches("deno_fetch::create_http_client(user_agent.clone(), ca_data.clone())")
                .matches("deno_fetch::create_http_client(user_agent.clone(), ca_data.clone()).unwrap()")
                ;
    }

    @Test
    public void testBlockExpression() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.BLOCK_EXPRESSION))
                .matches("{}")
                .matches("{let y=42;}")
                .matches("{println!(\"hi there\");}")
                .matches("{abc()}")
                .matches("{\n" +
                        "    println!(\"Hello, world!\");\n" +
                        "    abc()\n" +
                        "}")
                .matches("{\n" +
                        "    fn_call();\n" +
                        "}")
                .matches("{\n" +
                        "    fn_call();\n" +
                        "    5\n" +
                        "}")
                .matches("{println!(\"hello,{}\",k)}")
                .matches("{ println!(\"hello, {}\", j); }")
                .matches("{ i.set(i.get()); false }")
                .matches("{ i.set(i.get() + 1); false }")
                .matches("{deno_fetch::create_http_client(user_agent.clone(), ca_data.clone())}")
                .matches("{deno_fetch::create_http_client(user_agent.clone(), ca_data.clone()).unwrap()}")


        ;
    }
}
