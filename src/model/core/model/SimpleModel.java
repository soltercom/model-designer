package model.core.model;

import model.core.Metadata;
import model.core.attribute.Attribute;
import model.core.factory.MetadataFactory;
import model.core.node.AttributeNode;
import model.core.node.Node;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.util.List;
import java.util.Objects;

public class SimpleModel extends Model {

    public SimpleModel(Metadata parent, String name) {
        super(parent, name, false);

        MetadataFactory.getInstance()
            .createPredefinedAttributes(this).forEach(super::add);
        add(MetadataFactory.getInstance().createAttributeNode(this));
    }

    public boolean addAttribute(Attribute attribute) {
        Node node = Objects.requireNonNull((Node)getProperty(AttributeNode.NAME));
        return node.add(attribute);
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
