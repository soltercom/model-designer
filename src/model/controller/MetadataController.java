package model.controller;

import form.Form;
import form.FormEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.Pane;
import model.core.Metadata;
import model.core.attribute.Attribute;
import model.core.attribute.SimpleAttribute;
import model.core.factory.MetadataFactory;
import model.core.model.Model;
import model.core.model.SimpleModel;
import model.core.node.AttributeNode;
import model.core.node.RootNode;
import model.images.Images;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class MetadataController {

    private Metadata rootNode;
    private Metadata getRootNode() {
        return rootNode;
    }
    private TreeView<Metadata> treeView;
    private TreeView<Metadata> getTreeView() {
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

    public TreeView<Metadata> newRootNode() {
        rootNode = MetadataFactory.getInstance().newRootNode();
        treeView = initTreeView();
        return getTreeView();
    }

    public TreeView<Metadata> openRootNode() {
        rootNode = MetadataFactory.getInstance().getRootNode();
        treeView = initTreeView();
        return getTreeView();
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

    public Pane getPropertyForm() {
        return getPropertyForm(selectedTreeItem == null? null: selectedTreeItem.getValue());
    }

    private Pane getPropertyForm(Metadata metadata) {
        Form form;
        if (metadata instanceof RootNode) {
            form = new Form<RootNode>((RootNode)metadata);
        } else if (metadata instanceof Model) {
            form = new Form<Model>((Model)metadata);
        } else if (metadata instanceof Attribute) {
            form = new Form<Attribute>((Attribute)metadata);
        } else {
            return Form.empty();
        }

        form.addEventHandler(FormEvent.FORM_EVENT_PERSISTED, e -> {
            getTreeView().refresh();
        });
        return form;
    }

}
