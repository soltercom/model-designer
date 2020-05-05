package model.core.attribute;

import model.core.Metadata;
import model.core.factory.MetadataFactory;
import model.core.model.Model;
import model.core.model.UUIDModel;

public class RefAttribute extends PredefinedAttribute {

    public final static String NAME = "Ссылка";

    public RefAttribute(Metadata parent) {
        super(parent, NAME, (Model) parent.getRootNode().getProperty(UUIDModel.NAME));
    }
}
