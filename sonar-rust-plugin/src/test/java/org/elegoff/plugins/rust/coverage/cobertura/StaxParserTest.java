package org.elegoff.plugins.rust.coverage.cobertura;
import org.codehaus.staxmate.in.SMHierarchicCursor;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javax.xml.stream.XMLStreamException;

import java.io.File;

public class StaxParserTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void should_fail_parsing_ig_file_does_not_exist() throws Exception {
        thrown.expect(XMLStreamException.class);
        StaxParser parser = new StaxParser(rootCursor -> {});
        parser.parse(new File("fake.xml"));
    }

    @Test
    public void test_XML_with_DTD() throws XMLStreamException {
        StaxParser parser = new StaxParser(getTestHandler());
        parser.parse(getClass().getClassLoader().getResourceAsStream("org/elegoff/plugins/rust/cobertura/dtd-test.xml"));
    }

    @Test
    public void test_XML_with_XSD() throws XMLStreamException {
        StaxParser parser = new StaxParser(getTestHandler());
        parser.parse(getClass().getClassLoader().getResourceAsStream("org/elegoff/plugins/rust/cobertura/xsd-test.xml"));
    }

    @Test
    public void test_XML_with_XSD_and_ampersand() throws XMLStreamException {
        StaxParser parser = new StaxParser(getTestHandler());
        parser.parse(getClass().getClassLoader().getResourceAsStream("org/elegoff/plugins/rust/cobertura/xsd-test-with-entity.xml"));
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