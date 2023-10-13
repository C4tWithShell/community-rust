/**
 * Community Rust Plugin
 * Copyright (C) 2021-2023 Eric Le Goff
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
package org.elegoff.plugins.communityrust.coverage.cobertura;

import java.io.File;
import javax.xml.stream.XMLStreamException;
import org.codehaus.staxmate.in.SMHierarchicCursor;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class StaxParserTest {

  @Test
  void should_fail_parsing_if_file_does_not_exist() throws Exception {
    StaxParser parser = new StaxParser(rootCursor -> {
    });
    assertThrows(
      XMLStreamException.class,
      () -> parser.parse(new File("fake.xml")));
  }

  private void test_grammar_parsable(String resource) throws XMLStreamException {
    StaxParser parser = new StaxParser(getTestHandler());
    parser.parse(getClass().getClassLoader().getResourceAsStream(resource));
  }

  @Test
  void test_grammars() throws XMLStreamException {
    assertDoesNotThrow(() -> test_grammar_parsable("org/elegoff/plugins/communityRust/cobertura/dtd-test.xml"));
    assertDoesNotThrow(() -> test_grammar_parsable("org/elegoff/plugins/communityRust/cobertura/xsd-test.xml"));
    assertDoesNotThrow(() -> test_grammar_parsable("org/elegoff/plugins/communityRust/cobertura/xsd-test-with-entity.xml"));
  }

  private static StaxParser.XmlStreamHandler getTestHandler() {
    return new StaxParser.XmlStreamHandler() {
      public void stream(SMHierarchicCursor rootCursor) throws XMLStreamException {
        rootCursor.advance();
        while (rootCursor.getNext() != null) {
          // do nothing intentionally
        }
      }
    };
  }

}
