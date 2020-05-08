package model.xml;

import model.core.Metadata;
import model.core.factory.MetadataFactory;
import model.core.model.Model;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

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

}
