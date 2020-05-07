package formsfx.model.structure;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.UUID;

public class Element<E extends Element<E>> {

    protected final StringProperty id = new SimpleStringProperty(UUID.randomUUID().toString());
    protected final ListProperty<String> styleClass = new SimpleListProperty<>(FXCollections.observableArrayList());
    protected final IntegerProperty span = new SimpleIntegerProperty(12);

    public E id(String newValue) {
        id.set(newValue);
        return (E) this;
    }

    public E span(int newValue) {
        span.setValue(newValue);
        return (E) this;
    }

    public E styleClass(String... newValue) {
        styleClass.setAll(newValue);
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

    public ObservableList<String> getStyleClass() {
        return styleClass.get();
    }

    public ListProperty<String> styleClassProperty() {
        return styleClass;
    }

}
