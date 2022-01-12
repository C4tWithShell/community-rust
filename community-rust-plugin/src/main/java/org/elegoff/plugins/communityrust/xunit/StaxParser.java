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
package org.elegoff.plugins.communityrust.xunit;

import org.apache.commons.lang.StringUtils;
import org.codehaus.staxmate.SMInputFactory;
import org.codehaus.staxmate.in.SMHierarchicCursor;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLResolver;
import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
public class StaxParser {

  private SMInputFactory inf;

  private XmlStreamHandler streamHandler;

  public StaxParser(XmlStreamHandler streamHandler) {
    this.streamHandler = streamHandler;
    XMLInputFactory xmlFactory = XMLInputFactory.newInstance();

    inf = new SMInputFactory(xmlFactory);
  }

  public void parse(File xmlFile) throws XMLStreamException {
    try (FileInputStream input = new FileInputStream(xmlFile)) {
      parse(input);
    } catch (IOException e) {
      throw new XMLStreamException(e);
    }
  }

  public void parse(InputStream xmlInput) throws XMLStreamException {
    SMHierarchicCursor rootCursor = inf.rootElementCursor(xmlInput);
    try {
      streamHandler.stream(rootCursor);
    } finally {
      rootCursor.getStreamReader().closeCompletely();
    }
  }

  private static class UndeclaredEntitiesXMLResolver implements XMLResolver {

    @Override
    public Object resolveEntity(String arg0, String arg1, String fileName, String undeclaredEntity) throws XMLStreamException {
      String undeclared = undeclaredEntity;
      // avoid problems with XML docs containing undeclared entities.. return the entity under its raw form if not a Unicode expression
      if (StringUtils.startsWithIgnoreCase(undeclaredEntity, "u") && undeclaredEntity.length() == 5) {
        int unicodeCharHexValue = Integer.parseInt(undeclaredEntity.substring(1), 16);
        if (Character.isDefined(unicodeCharHexValue)) {
          undeclared = new String(new char[] {(char) unicodeCharHexValue});
        }
      }
      return undeclared;
    }
  }

  /**
   * Simple interface for handling XML stream to parse.
   */
  @FunctionalInterface
  public interface XmlStreamHandler {
    void stream(SMHierarchicCursor rootCursor) throws XMLStreamException;
  }

}