package model.controller;

import formsfx.model.event.FormEvent;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import model.core.Metadata;
import model.core.attribute.Attribute;
import model.core.attribute.SimpleAttribute;
import model.core.factory.MetadataFactory;
import model.core.model.Model;
import model.core.model.SimpleModel;
import model.core.node.AttributeNode;
import model.core.node.RootNode;
import model.forms.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class MetadataController {

    private final Metadata rootNode;
    private Metadata getRootNode() {
        return rootNode;
    }
    private TreeView<Metadata> treeView;
    public TreeView<Metadata> getTreeView() {
        if (treeView == null) {
            treeView = initTreeView();
        }
        return treeView;
    }

    private TreeItem<Metadata> selectedTreeItem;

    private static MetadataController instance;
    private MetadataController() {
        rootNode = MetadataFactory.getInstance().getRootNode();
    }
    public static MetadataController getInstance() {
        if (instance == null) {
            instance = new MetadataController();
        }
        return instance;
    }

    protected final Map<EventType<MetadataControllerEvent>, List<EventHandler<? super MetadataControllerEvent>>> eventHandlers = new ConcurrentHashMap<>();

    public void addEventHandler(EventType<MetadataControllerEvent> eventType, EventHandler<? super MetadataControllerEvent> eventHandler) {
        if (eventType == null) {
            throw new NullPointerException("Argument eventType must not be null");
        }
        if (eventHandler == null) {
            throw new NullPointerException("Argument eventHandler must not be null");
        }

        this.eventHandlers.computeIfAbsent(eventType, k -> new CopyOnWriteArrayList<>()).add(eventHandler);
    }

    public void removeEventHandler(EventType<MetadataControllerEvent> eventType, EventHandler<? super MetadataControllerEvent> eventHandler) {
        if (eventType == null) {
            throw new NullPointerException("Argument eventType must not be null");
        }
        if (eventHandler == null) {
            throw new NullPointerException("Argument eventHandler must not be null");
        }

        List<EventHandler<? super MetadataControllerEvent>> list = this.eventHandlers.get(eventType);
        if (list != null) {
            list.remove(eventHandler);
        }
    }

    protected void fireEvent(MetadataControllerEvent event) {
        List<EventHandler<? super MetadataControllerEvent>> list = this.eventHandlers.get(event.getEventType());
        if (list == null) {
            return;
        }
        for (EventHandler<? super MetadataControllerEvent> eventHandler : list) {
            if (!event.isConsumed()) {
                eventHandler.handle(event);
            }
        }
    }

    private TreeView<Metadata> initTreeView() {

        TreeItem<Metadata> rootItem = new TreeItem<>(getRootNode());
        rootItem.setExpanded(true);

        for (Metadata model: getRootNode().propertiesProperty()) {
            if (model instanceof SimpleModel) {
                TreeItem<Metadata> modelItem = new TreeItem<>(model);
                for (Metadata node: model.propertiesProperty()) {
                    if (node instanceof AttributeNode) {
                        TreeItem<Metadata> nodeItem = new TreeItem<>(node);
                        for (Metadata attribute: node.propertiesProperty()) {
                            if (attribute instanceof SimpleAttribute) {
                                TreeItem<Metadata> attributeItem = new TreeItem<>(attribute);
                                nodeItem.getChildren().add(attributeItem);
                            }
                        }
                        modelItem.getChildren().add(nodeItem);
                    }
                }
                rootItem.getChildren().add(modelItem);
            }
        }

        TreeView<Metadata> treeView = new TreeView<>(rootItem);

        final ContextMenu contextMenu = new ContextMenu();
        MenuItem itemAdd = new MenuItem("Добавить");
        itemAdd.setOnAction(event -> {});
        contextMenu.getItems().add(itemAdd);
        treeView.setContextMenu(contextMenu);

        treeView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TreeItem<Metadata>>() {
            @Override
            public void changed(ObservableValue<? extends TreeItem<Metadata>> observableValue, TreeItem<Metadata> oldValue, TreeItem<Metadata> newValue) {
                selectedTreeItem = newValue;
                fireEvent(MetadataControllerEvent.selectedMetadataChanged());
            }
        });

        return treeView;
    }

    public BorderPane getNewPropertyForm() {
        return null;
    }

    public BorderPane getPropertyForm() {
        return getPropertyForm(selectedTreeItem == null? null: selectedTreeItem.getValue());
    }

    private BorderPane getPropertyForm(Metadata metadata) {

        MetadataForm metadataForm = null;
        if (metadata instanceof RootNode)
            metadataForm = new RootNodeForm(metadata);
        else if (metadata instanceof Model)
            metadataForm = new ModelForm(metadata);
        else if (metadata instanceof Attribute)
            metadataForm = new AttributeForm((Attribute) metadata);
        else return new BorderPane();

        metadataForm.getFormInstance()
                .addEventHandler(FormEvent.EVENT_FORM_PERSISTED, e -> getTreeView().refresh());
        return new MetadataPane(metadataForm);

    }


}
