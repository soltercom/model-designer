package model.core.node;

import model.core.Metadata;
import model.core.factory.MetadataFactory;
import model.core.model.Model;

public class RootNode extends Node {

    public RootNode(String name) {
        super(name);

        MetadataFactory factory = MetadataFactory.getInstance();
        addAll(factory.createStringModel(),
               factory.createNumberModel(),
               factory.createBooleanModel(),
               factory.createDateModel(),
               factory.createUUIDModel());
    }

}
