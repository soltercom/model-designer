package model.core.model;

import model.core.Metadata;
import model.core.attribute.Attribute;

public abstract class Model extends Metadata {
    protected Model(String name, boolean predefined) {
        super(name, predefined);
    }

    @Override
    public String toString() {
        return getName();
    }
}
