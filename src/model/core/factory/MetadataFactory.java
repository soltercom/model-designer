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

import java.util.ArrayList;
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

    public List<Model> createPredefinedModels(Metadata parent) {
        List<Model> list = new ArrayList<>();
        list.add(new BooleanModel(parent));
        list.add(new StringModel(parent));
        list.add(new DateModel(parent));
        list.add(new NumberModel(parent));
        list.add(new UUIDModel(parent));
        return list;
    }

    public Model createNewSimpleModel() {
        return createNewSimpleModel(getRootNode().getUniqueName());
    }
    public Model createNewSimpleModel(String name) {
        Model model = new SimpleModel(getRootNode(), name);
        if (!getRootNode().add(model)) {
            System.out.println("Error");
            return null;
        }
        return model;
    }
    public Attribute createNewSimpleAttribute(SimpleModel parent, String name, Model type) {
        Attribute attribute = new SimpleAttribute(parent, name, type);
        if (!parent.addAttribute(attribute)) {
            System.out.println("Error");
            return null;
        }
        return attribute;
    }
    public Attribute createNewSimpleAttribute(SimpleModel parent) {
        return createNewSimpleAttribute(parent, getRootNode().getUniqueName(), getRootNode().getDefaultModel());
    }

    public List<Attribute> createPredefinedAttributes(Metadata parent) {
        List<Attribute> list = new ArrayList<>();
        list.add(new NameAttribute(parent));
        list.add(new RefAttribute(parent));
        return list;
    }

    public Node createAttributeNode(Metadata parent) { return new AttributeNode(parent); }

    public Model getPredefinedModel(String name) {
        return (Model) getRootNode().getProperty(name);
    }

    public Metadata createNewMetadata(Metadata currentMetadata) {
        if (currentMetadata instanceof RootNode)
            return createNewSimpleModel();
        if (currentMetadata instanceof SimpleModel)
            return createNewSimpleAttribute((SimpleModel)currentMetadata);
        if (currentMetadata instanceof AttributeNode)
            return createNewSimpleAttribute((SimpleModel)currentMetadata.getParent());
        if (currentMetadata instanceof Attribute)
            return createNewSimpleAttribute((SimpleModel)currentMetadata.getParent().getParent());
        else
            return null;
    }
}
