package model.core.attribute;

import model.core.Metadata;
import model.core.model.Model;
import model.core.node.AttributeNode;

public class SimpleAttribute extends Attribute {
    public SimpleAttribute(Metadata parent, String name, Model type) {
        super(parent.getProperty(AttributeNode.NAME), name, false, type);
    }
}
