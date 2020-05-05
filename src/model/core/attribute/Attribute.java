package model.core.attribute;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import model.core.Metadata;
import model.core.model.Model;

public abstract class Attribute extends Metadata {

    private ObjectProperty<Model> type;

    Attribute(String name, boolean predefined, Model type) {
        super(name, predefined);
        this.type = new SimpleObjectProperty<>(type);
    }

    public boolean add(Metadata property) { return false; }
    public boolean remove(Metadata property) { return false; }

    public String printMetadata(int level) {
        String result = "-".repeat(level) + getName() + ": ";
        result += "(" + getType().getName() + ")\n";
        return result;
    }

    public Model getType() {
        return type.get();
    }
    public ObjectProperty<Model> typeProperty() {
        return type;
    }

    @Override
    public String toString() {
        return getName();
    }
}
