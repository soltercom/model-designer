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
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class MetadataFactory {

    private RootNode root;
    public RootNode getRootNode() {
        if (root == null) {
            root = new RootNode(RootNode.NAME);
        }
        return root;
    }
    public RootNode newRootNode() {
        root = new RootNode("Метаданные");
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
        return createNewSimpleAttribute(parent, parent.getProperty(AttributeNode.NAME).getUniqueName(), getRootNode().getDefaultModel());
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

    public boolean removeMetadata(Metadata currentMetadata) {

        if (currentMetadata instanceof SimpleModel) {
            boolean hasReference = getRootNode().hasReferenceOnModel((SimpleModel) currentMetadata);
            if (!hasReference) currentMetadata.getParent().remove(currentMetadata);
            return !hasReference;
        }

        if (currentMetadata instanceof SimpleAttribute) {
            currentMetadata.getParent().remove(currentMetadata);
            return true;
        }

        return false;
    }

    private HashMap<String, Model> deserializeModels(Element documentElement) {
        HashMap<String, Model> modelMap = new HashMap<>();
        for (int i = 0; i < documentElement.getChildNodes().getLength(); i++) {
            Element modelElement = (Element) documentElement.getChildNodes().item(i);
            modelMap.put(modelElement.getTagName(), createNewSimpleModel(modelElement.getTagName()));
        }
        return modelMap;
    }

    public void deserializeAttributes(Element documentElement, HashMap<String, Model> modelMap) {
        NodeList listNode = documentElement.getElementsByTagName(AttributeNode.NAME);
        for (int i = 0; i < listNode.getLength(); i++) {
            Element attributeNode = (Element) listNode.item(i);
            SimpleModel model = (SimpleModel) modelMap.get(attributeNode.getParentNode().getNodeName());
            for (int j = 0; j < attributeNode.getChildNodes().getLength(); j++) {
                Element attribute = (Element) attributeNode.getChildNodes().item(j);
                String typeAttribute = attribute.getAttribute("type");
                Model type = modelMap.get(typeAttribute);
                if (type == null) {
                    type = getPredefinedModel(typeAttribute);
                }
                createNewSimpleAttribute(model, attribute.getTagName(), type);
            }
        }
    }

    public boolean deserialize(Element documentElement) {
        root = new RootNode(documentElement.getTagName());
        deserializeAttributes(documentElement, deserializeModels(documentElement));
        return true;
    }
}
