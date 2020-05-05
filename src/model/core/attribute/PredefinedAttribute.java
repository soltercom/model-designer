package model.core.attribute;

import model.core.Metadata;
import model.core.model.Model;

public abstract class PredefinedAttribute extends Attribute {
    public PredefinedAttribute(Metadata parent, String name, Model type) {
        super(parent, name, true, type);
    }
}
