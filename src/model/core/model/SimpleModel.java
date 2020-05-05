package model.core.model;

import model.core.Metadata;
import model.core.attribute.Attribute;
import model.core.factory.MetadataFactory;
import model.core.node.AttributeNode;
import model.core.node.Node;

import java.util.List;
import java.util.Objects;

public class SimpleModel extends Model {

    public SimpleModel(String name) {
        super(name, false);

        MetadataFactory factory = MetadataFactory.getInstance();
        add(factory.createRefAttribute());
        add(factory.createNameAttribute());
        add(factory.createAttributeNode());
    }

    public boolean addAttribute(Attribute attribute) {
        Node node = Objects.requireNonNull((Node)getProperty(AttributeNode.name));
        return node.add(attribute);
    }

}
