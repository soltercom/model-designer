package model.core.node;

import javafx.beans.property.Property;
import model.core.Metadata;
import model.core.attribute.SimpleAttribute;
import model.core.factory.MetadataFactory;
import model.core.model.Model;
import model.core.model.SimpleModel;
import model.core.model.StringModel;

import java.util.List;
import java.util.stream.Collectors;

public class RootNode extends Node {

    public static final String NAME = "Метаданные";

    public RootNode(String name) {
        super(null, name);

        MetadataFactory.getInstance().createPredefinedModels(this).forEach(super::add);
    }

    public Model getDefaultModel() {
        return (Model) getProperty(StringModel.NAME);
    }

    public boolean hasReferenceOnModel(SimpleModel model) {
       return propertiesProperty().stream()
            .filter(item -> item instanceof SimpleModel)
            .map(Metadata::propertiesProperty)
            .flatMap(List::stream)
            .filter(item -> item instanceof AttributeNode)
            .map(Metadata::propertiesProperty)
            .flatMap(List::stream)
            .filter(item -> item instanceof SimpleAttribute)
            .map(item -> ((SimpleAttribute) item).getType())
            .anyMatch(item -> item.equals(model));
    }

}
