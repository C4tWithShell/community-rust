package org.elegoff.plugins.rust.coverage.cobertura;
import com.ctc.wstx.stax.WstxInputFactory;

import org.codehaus.staxmate.SMInputFactory;
import org.codehaus.staxmate.in.SMHierarchicCursor;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class StaxParser {

    @FunctionalInterface
    public interface XmlStreamHandler {
        void stream(SMHierarchicCursor rootCursor) throws XMLStreamException;
    }

    private SMInputFactory inf;
    private XmlStreamHandler streamHandler;

    public StaxParser(XmlStreamHandler streamHandler) {
        this.streamHandler = streamHandler;
        XMLInputFactory xmlFactory = XMLInputFactory.newInstance();
        if (xmlFactory instanceof WstxInputFactory) {
            WstxInputFactory wstxInputfactory = (WstxInputFactory) xmlFactory;
            wstxInputfactory.configureForLowMemUsage();
            wstxInputfactory.getConfig().setUndeclaredEntityResolver((String publicID, String systemID, String baseURI, String namespace) -> namespace);
        }
        xmlFactory.setProperty(XMLInputFactory.IS_VALIDATING, false);
        xmlFactory.setProperty(XMLInputFactory.SUPPORT_DTD, false);
        xmlFactory.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE, false);
        inf = new SMInputFactory(xmlFactory);
    }

    public void parse(File xmlFile) throws XMLStreamException {
        try(FileInputStream input = new FileInputStream(xmlFile)) {
            parse(inf.rootElementCursor(input));
        } catch (IOException e) {
            throw new XMLStreamException(e);
        }
    }

    private void parse(SMHierarchicCursor rootCursor) throws XMLStreamException {
        try {
            streamHandler.stream(rootCursor);
        } finally {
            rootCursor.getStreamReader().closeCompletely();
        }
    }
}