/*
 * Community Rust Plugin
 * Copyright (C) 2021-2025 Vladimir Shelkovnikov
 * mailto:community-rust AT pm DOT me
 * http://github.com/C4tWithShell/community-rust
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
package org.sonar.rust;

import com.google.common.base.Charsets;
import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.api.Token;
import com.sonar.sslr.impl.ast.AstXmlPrinter;
import org.junit.jupiter.api.Test;
import org.sonar.sslr.parser.LexerlessGrammar;
import org.sonar.sslr.parser.ParserAdapter;
import org.sonar.sslr.tests.Assertions;

import java.nio.charset.StandardCharsets;
import java.util.List;


import static org.assertj.core.api.Assertions.assertThat;

class RustLexerTest {
  @Test
  void testSize() {
    assertThat(lex("")).hasSize(1);
    assertThat(lex("   ")).hasSize(1);
    assertThat(lex("foo")).hasSize(2);
  }

  private List<Token> lex(String source) {
    List<Token> li = RustLexer.create(RustParserConfiguration.builder()
        .setCharset(Charsets.UTF_8)
        .build())
      .parse(source)
      .getTokens();

    return li;
  }

  @Test
  void testTokens() {
    Assertions.assertThat(RustLexer.create().build().rule(RustLexer.TOKENS))
      .matches("")
      .matches("fn")
      .matches("main()")
      .matches("fn main() {\n" +
        "    println!(\"Hello, world!\");\n" +
        "}")

    ;
  }

  @Test
  void testParsing() {
    String sexpr = "fn update_rates(\n" +
      "         rates: BoundedVec<(T::Symbol, u64), T::MaxRelaySymbols>,\n" +
      "         resolve_time: u64,\n" +
      "         request_id: u64,\n" +
      "         f: impl Fn( &mut Option<BandRate<BlockNumberFor<T>>>))\n" +
      "             {\n" +
      "             todo!()\n" +
      "             }";

    // Print out Ast node content for debugging purpose

    ParserAdapter<LexerlessGrammar> parser = new ParserAdapter<>(StandardCharsets.UTF_8, RustGrammar.create().build());
    AstNode rootNode = parser.parse(sexpr);
    assertThat(rootNode.getType()).isSameAs(RustGrammar.COMPILATION_UNIT);
    AstNode astNode = rootNode;
    // org.fest.assertions.Assertions.assertThat(astNode.getNumberOfChildren()).isEqualTo(4);
    // System.out.println(AstXmlPrinter.print(astNode));

  }

  @Test
  void testSelfInsideImportTokens() {
      testImportParsing("selfish");
      testImportParsing("self_api");
  }

  private void testImportParsing(String filename) {
    String sexpr = "\n" +
        "use api::" + filename + "::MyService; \n" +
        "       fn update_rates(){" +
        "             todo!()\n" +
        "             }";

    ParserAdapter<LexerlessGrammar> parser = new ParserAdapter<>(
        StandardCharsets.UTF_8, RustGrammar.create().build());
    
    AstNode rootNode = parser.parse(sexpr);
    assertThat(rootNode).isNotNull();
  }

  @Test
  void testNumRange() {
    String sexpr = "\n" +
        "fn get_referral_tier(referrals_count: i64) -> Result<i64, CustomError> {\n" +
        "       match referrals_count {\n" +
        "             ..0 => Ok(0),\n" +
        "             1 => Ok(1),\n" +
        "             2..5 => Ok(2)\n" +
        "             5.. => Ok(3)\n" +
        "       }\n" +
        "}";
    ParserAdapter<LexerlessGrammar> parser = new ParserAdapter<>(
        StandardCharsets.UTF_8, RustGrammar.create().build());
    
    AstNode rootNode = parser.parse(sexpr);
    assertThat(rootNode).isNotNull();
  }

  @Test
  void testContinueProcessingCondition() {
    String source = 
        "fn process_event() {\n" +
        "    let reward_fut = client.transfer_rewards(process_reward_rx);\n" +
        "    let (_res1, _res2) = tokio::join!(process_event_fut, reward_fut);\n" +
        "\n" +
        "    if let Err(err) = super::state::del(\"quest\", \".\", redis_pool.clone()).await {\n" +
        "        tracing::error!(\"fail to remove quest from queue due to {err}\");\n" +
        "    }\n" +
        "\n" +
        "    tracing::info!(\"process event sleep for 15 sec\");\n" +
        "    tokio::time::sleep(std::time::Duration::from_secs(15)).await;\n" +
        "\n" +
        "    let continue_processing = true;\n" +
        "    if !continue_processing {\n" +
        "        println!(\"Stopping processing\");\n" +
        "    }\n" +
        "}";

    ParserAdapter parser = new ParserAdapter<>(
        StandardCharsets.UTF_8, RustGrammar.create().build()
    );

    // Expect this to be parsed successfully or reproduce the issue
    AstNode rootNode = parser.parse(source);
    // Ensure parsing is successful
    assertThat(rootNode).isNotNull();
  }

}
