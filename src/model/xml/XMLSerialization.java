package model.xml;

import model.core.factory.MetadataFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class XMLSerialization {

    private static XMLSerialization instance;
    private XMLSerialization() {}
    public static XMLSerialization getInstance() {
        if (instance == null) {
            instance = new XMLSerialization();
        }
        return instance;
    }

    public boolean serialize(File file) {
        MetadataFactory metadata = MetadataFactory.getInstance();

        try {
            XMLOutputFactory output = XMLOutputFactory.newInstance();
            XMLStreamWriter writer = output.createXMLStreamWriter(new FileWriter(file));
            writer.writeStartDocument("1.0");
            metadata.getRootNode().encode(writer);
            writer.writeEndDocument();
            writer.flush();
            writer.close();
        } catch (XMLStreamException | IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public boolean deserialize(File file) {

        Document document = null;
        try {
            document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file);
        } catch (SAXException | IOException | ParserConfigurationException e) {
            e.printStackTrace();
        }

        if (document == null) return false;

        return MetadataFactory.getInstance().deserialize(document.getDocumentElement());

    }

}
