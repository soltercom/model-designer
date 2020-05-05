package model.core.factory;

import model.core.Metadata;
import model.core.attribute.Attribute;
import model.core.attribute.NameAttribute;
import model.core.attribute.RefAttribute;
import model.core.attribute.SimpleAttribute;
import model.core.model.*;
import model.core.node.AttributeNode;
import model.core.node.Node;
import model.core.node.RootNode;

import java.util.List;
import java.util.Optional;

public class MetadataFactory {

    private RootNode root;
    public RootNode getRootNode() {
        if (root == null) {
            root = new RootNode("Метаданные");
        }
        return root;
    }

    private static MetadataFactory instance;
    private MetadataFactory() {}
    public static MetadataFactory getInstance() {
        if (instance == null) {
            instance = new MetadataFactory();
        }
        return instance;
    }

    public Model createBooleanModel() { return new BooleanModel(); }
    public Model createStringModel() { return new StringModel(); }
    public Model createDateModel() { return new DateModel(); }
    public Model createNumberModel() { return new NumberModel(); }
    public Model createUUIDModel() { return new UUIDModel(); }

    public Model createNewSimpleModel() {
        return createNewSimpleModel(getRootNode().getUniqueName());
    }
    public Model createNewSimpleModel(String name) {
        Model model = new SimpleModel(name);
        if (!getRootNode().add(model)) {
            System.out.println("Error");
            return null;
        }
        return model;
    }
    public Attribute createNewSimpleAttribute(SimpleModel modelOwner, String name, Model type) {
        Attribute attribute = new SimpleAttribute(name, type);
        if (!modelOwner.addAttribute(attribute)) {
            System.out.println("Error");
            return null;
        }
        return attribute;
    }
    public Attribute createNewSimpleAttribute(SimpleModel modelOwner) {
        return createNewSimpleAttribute(modelOwner, getRootNode().getUniqueName(), createStringModel());
    }

    public Attribute createNameAttribute() { return new NameAttribute(); }
    public Attribute createRefAttribute() { return new RefAttribute(); }

    public Node createAttributeNode() { return new AttributeNode(); }

    public Metadata createNewMetadata(Metadata currentMetadata) {
        if (currentMetadata instanceof RootNode)
            return createNewSimpleModel();
        if (currentMetadata instanceof SimpleModel)
            return createNewSimpleAttribute((SimpleModel)currentMetadata);
        if (currentMetadata instanceof AttributeNode)
            return null;//createNewSimpleAttribute(currentMetadata);
        if (currentMetadata instanceof Attribute)
            return null;
        else
            return null;
    }
}
