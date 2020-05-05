package model.core.node;

import model.core.Metadata;
import model.core.factory.MetadataFactory;
import model.core.model.Model;
import model.core.model.StringModel;

public class RootNode extends Node {

    public RootNode(String name) {
        super(null, name);

        MetadataFactory.getInstance().createPredefinedModels(this).forEach(super::add);
    }

    public Model getDefaultModel() {
        return (Model) getProperty(StringModel.NAME);
    }

}
