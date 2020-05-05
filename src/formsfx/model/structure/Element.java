package formsfx.model.structure;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.UUID;

public class Element<E extends Element<E>> {

    protected final StringProperty id = new SimpleStringProperty(UUID.randomUUID().toString());
    protected final IntegerProperty span = new SimpleIntegerProperty(12);

    public E id(String newValue) {
        id.set(newValue);
        return (E) this;
    }

    public E span(int newValue) {
        span.setValue(newValue);
        return (E) this;
    }

    public int getSpan() {
        return span.get();
    }

    public IntegerProperty spanProperty() {
        return span;
    }


    public String getID() {
        return id.get();
    }

    public StringProperty idProperty() {
        return id;
    }

}
