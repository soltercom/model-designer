package model.core.attribute;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import model.core.Metadata;
import model.core.model.Model;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

public abstract class Attribute extends Metadata {

    private ObjectProperty<Model> type;

    Attribute(Metadata parent, String name, boolean predefined, Model type) {
        super(parent, name, predefined);
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

    @Override
    public void encode(XMLStreamWriter writer) throws XMLStreamException {
        writer.writeStartElement(getName());
        writer.writeAttribute("predefined", String.valueOf(isPredefined()));
        writer.writeAttribute("type", String.valueOf(getType()));
        writer.writeEndElement();
    }

}
