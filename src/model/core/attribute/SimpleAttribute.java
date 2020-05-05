package model.core.attribute;

import model.core.model.Model;

public class SimpleAttribute extends Attribute {
    public SimpleAttribute(String name, Model type) {
        super(name, false, type);
    }
}
