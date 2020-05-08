package model.core.node;

import model.core.Metadata;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

public class AttributeNode extends Node {

    public static final String NAME = "Реквизиты";

    public AttributeNode(Metadata parent) {
        super(parent, NAME);
    }

    @Override
    public void encode(XMLStreamWriter writer) throws XMLStreamException {
        writer.writeStartElement(getName());
        for (Metadata metadata: propertiesProperty()) {
            metadata.encode(writer);
        }
        writer.writeEndElement();
    }

}
