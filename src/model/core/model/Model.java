package model.core.model;

import form.validator.ValidateProperty;
import form.validator.ValidatorType;
import model.core.Metadata;
import model.core.attribute.Attribute;

public abstract class Model extends Metadata {
    protected Model(Metadata parent, String name, boolean predefined) {
        super(parent, name, predefined);
    }

    @Override
    public String toString() {
        return getName();
    }
}
