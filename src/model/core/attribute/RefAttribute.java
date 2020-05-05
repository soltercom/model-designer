package model.core.attribute;

import model.core.factory.MetadataFactory;

public class RefAttribute extends PredefinedAttribute {
    public RefAttribute() {
        super("Ссылка", MetadataFactory.getInstance().createUUIDModel());
    }
}
