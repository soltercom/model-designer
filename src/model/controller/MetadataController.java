package model.controller;

import formsfx.model.event.FormEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.ImageView;
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
import model.utils.Images;

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

    private TreeItem<Metadata> getTreeItems() {
        TreeItem<Metadata> rootItem = new TreeItem<>(getRootNode(), Images.DOMAIN_IMAGE);
        rootItem.setExpanded(true);

        for (Metadata model: getRootNode().propertiesProperty()) {
            if (model instanceof SimpleModel) {
                TreeItem<Metadata> modelItem = new TreeItem<>(model, Images.getFolderImage());
                for (Metadata node: model.propertiesProperty()) {
                    if (node instanceof AttributeNode) {
                        TreeItem<Metadata> nodeItem = new TreeItem<>(node);
                        nodeItem.setExpanded(true);
                        for (Metadata attribute: node.propertiesProperty()) {
                            if (attribute instanceof SimpleAttribute) {
                                TreeItem<Metadata> attributeItem = new TreeItem<>(attribute, Images.getCropImage());
                                nodeItem.getChildren().add(attributeItem);
                            }
                        }
                        modelItem.getChildren().add(nodeItem);
                    }
                }
                rootItem.getChildren().add(modelItem);
            }
        }

        return rootItem;
    }

    private TreeItem<Metadata> getTreeItem(TreeItem<Metadata> parent, Metadata metadata) {
        for(TreeItem<Metadata> item: parent.getChildren()) {
            if (item.getValue().equals(metadata)) {
                return item;
            }
            TreeItem<Metadata> res = getTreeItem(item, metadata);
            if (res != null) return res;
        }
        return null;
    }

    private TreeView<Metadata> initTreeView() {

        TreeView<Metadata> treeView = new TreeView<>(getTreeItems());

        final ContextMenu contextMenu = new ContextMenu();
        MenuItem itemAdd = new MenuItem("Добавить", Images.ADD_IMAGE);
        itemAdd.setOnAction(event -> {
            Metadata metadata = MetadataFactory.getInstance().createNewMetadata(selectedTreeItem.getValue());
            treeView.setRoot(getTreeItems());
            treeView.getSelectionModel().select(getTreeItem(treeView.getRoot(), metadata));
        });
        MenuItem itemRemove = new MenuItem("Удалить", Images.REMOVE_IMAGE);
        itemRemove.setOnAction(event -> {
            Metadata parentMetadata = selectedTreeItem.getValue().getParent();
            if (MetadataFactory.getInstance().removeMetadata(selectedTreeItem.getValue())) {
                treeView.setRoot(getTreeItems());
                treeView.getSelectionModel().select(getTreeItem(treeView.getRoot(), parentMetadata));
                treeView.refresh();
            }
        });
        contextMenu.getItems().addAll(itemAdd, itemRemove);
        treeView.setContextMenu(contextMenu);

        treeView.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            selectedTreeItem = newValue;
            fireEvent(MetadataControllerEvent.selectedMetadataChanged());
        });

        return treeView;
    }

    public BorderPane getPropertyForm() {
        return getPropertyForm(selectedTreeItem == null? null: selectedTreeItem.getValue());
    }

    private BorderPane getPropertyForm(Metadata metadata) {
        MetadataForm metadataForm;
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
