package model.core;

import formsfx.model.structure.Group;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.scene.control.TreeItem;
import model.core.node.Node;

import java.util.Objects;

public abstract class Metadata {

    protected StringProperty name;
    protected BooleanProperty predefined;
    protected ListProperty<Metadata> properties;
    protected Metadata parent;

    protected Metadata(Metadata parent, String name, boolean predefined) {
        this.parent = parent;
        this.name = new SimpleStringProperty(Objects.requireNonNull(name));
        this.predefined = new ReadOnlyBooleanWrapper(predefined);
        if (predefined) {
            if (this instanceof Node) {
                this.properties = new SimpleListProperty<>(FXCollections.observableArrayList());
            } else {
                this.properties = new SimpleListProperty<>(FXCollections.emptyObservableList());
            }
        } else {
            this.properties = new SimpleListProperty<>(FXCollections.observableArrayList());
        }
    }

    public String getName() {
        return name.get();
    }
    public StringProperty nameProperty() {
        return name;
    }
    public BooleanProperty predefinedProperty() {
        return predefined;
    }
    public ListProperty<Metadata> propertiesProperty() {
        return properties;
    }

    public Metadata getParent() { return parent; }

    public boolean add(Metadata property) {
        if (propertiesProperty().contains(property)) {
            return false;
        } else {
            return propertiesProperty().add(property);
        }
    }

    public boolean remove(Metadata property) {
        return propertiesProperty().remove(property);
    }

    public String getUniqueName() {
        int counter = 1;
        boolean isUnique;
        do {
            isUnique = true;
            for (Metadata property: properties) {
                if (property.getName().equals("Новый " + counter)) {
                    counter++;
                    isUnique = false;
                    break;
                }
            }
        } while(!isUnique);
        return "Новый " + counter;
    }

    public String printMetadata(int level) {
        StringBuilder result = new StringBuilder("-".repeat(level) + getName() + "\n");
        level++;
        for (Metadata property: propertiesProperty()) {
            result.append(property.printMetadata(level));
        }
        return result.toString();
    }

    public Metadata getProperty(String name) {
        for (Metadata property: propertiesProperty()) {
            if (property.getName().equals(name)) return property;
        }
        return null;
    }

    public Metadata getRootNode() {
        if (getParent() == null) {
            return this;
        }
        return getParent().getRootNode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Metadata)) return false;
        Metadata metadata = (Metadata) o;
        return getName().equals(metadata.getName()) &&
                Objects.equals(getParent(), metadata.getParent());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getParent());
    }
}
