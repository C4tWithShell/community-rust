/**
 * Community Rust Plugin
 * Copyright (C) 2021 Eric Le Goff
 * mailto:community-rust AT pm DOT me
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
package org.sonar.rust;

import com.google.common.base.Charsets;
import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.api.Token;
import com.sonar.sslr.impl.ast.AstXmlPrinter;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.junit.Test;
import org.sonar.sslr.parser.LexerlessGrammar;
import org.sonar.sslr.parser.ParserAdapter;
import org.sonar.sslr.tests.Assertions;

import static org.fest.assertions.Assertions.assertThat;

public class RustLexerTest {
  @Test
  public void testSize() {
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
  public void testTokens() {
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
  public void testParsing() {
    String sexpr = "impl Display for MetricsAllocatorStatistics<AtomicUsize> {\n" +
      "    fn fmt(&self, f: &mut Formatter<'_>) -> std::fmt::Result {\n" +
      "        self.fmt(&MetricsAllocatorStatistics::<usize> {\n" +
      "            allocations_count:           self.allocations_count.load(Ordering::Relaxed),\n" +
      "            deallocations_count:         self.deallocations_count.        load(Ordering::Relaxed),\n" +
      "            zeroed_allocations_count:    self.zeroed_allocations_count.   load(Ordering::Relaxed),\n" +
      "            reallocations_count:         self.reallocations_count.        load(Ordering::Relaxed),\n" +
      "            allocated_bytes:             self.allocated_bytes.            load(Ordering::Relaxed),\n" +
      "            deallocated_bytes:           self.deallocated_bytes.          load(Ordering::Relaxed),\n" +
      "            zeroed_allocated_bytes:      self.zeroed_allocated_bytes.     load(Ordering::Relaxed),\n" +
      "            reallocated_originals_bytes: self.reallocated_originals_bytes.load(Ordering::Relaxed),\n" +
      "            reallocated_news_bytes:      self.reallocated_news_bytes.     load(Ordering::Relaxed),\n" +
      "            current_used_memory:         self.current_used_memory.        load(Ordering::Relaxed),\n" +
      "            min_used_memory:             self.min_used_memory.            load(Ordering::Relaxed),\n" +
      "            max_used_memory:             self.max_used_memory.            load(Ordering::Relaxed),\n" +
      "        }, f)\n" +
      "    }\n" +
      "}";

    // Print out Ast node content for debugging purpose

    ParserAdapter<LexerlessGrammar> parser = new ParserAdapter<>(StandardCharsets.UTF_8, RustGrammar.create().build());
    AstNode rootNode = parser.parse(sexpr);
    org.fest.assertions.Assertions.assertThat(rootNode.getType()).isSameAs(RustGrammar.COMPILATION_UNIT);
    AstNode astNode = rootNode;
    // org.fest.assertions.Assertions.assertThat(astNode.getNumberOfChildren()).isEqualTo(4);
    System.out.println(AstXmlPrinter.print(astNode));

  }
}
