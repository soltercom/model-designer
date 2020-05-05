package model.core.node;

import model.core.Metadata;

public class AttributeNode extends Node {

    public static final String NAME = "Реквизиты";

    public AttributeNode(Metadata parent) {
        super(parent, NAME);
    }
}
