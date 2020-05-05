package model.core.attribute;

import model.core.Metadata;
import model.core.factory.MetadataFactory;
import model.core.model.Model;
import model.core.model.StringModel;

public class NameAttribute extends PredefinedAttribute {

    final public static String NAME = "Имя";

    public NameAttribute(Metadata parent) {
        super(parent, NAME, (Model)parent.getRootNode().getProperty(StringModel.NAME));
    }
}
