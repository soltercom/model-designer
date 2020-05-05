package model.core.attribute;

import model.core.model.Model;

public abstract class PredefinedAttribute extends Attribute {
    public PredefinedAttribute(String name, Model type) {
        super(name, true, type);
    }
}
