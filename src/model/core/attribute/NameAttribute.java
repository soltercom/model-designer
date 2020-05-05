package model.core.attribute;

import model.core.factory.MetadataFactory;

public class NameAttribute extends PredefinedAttribute {
    public NameAttribute() {
        super("Имя", MetadataFactory.getInstance().createStringModel());
    }
}
