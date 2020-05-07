package formsfx.model.structure;

import formsfx.model.event.FieldEvent;
import formsfx.view.controls.SimpleControl;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.event.EventType;
import model.core.model.Model;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class Field<F extends Field<F>> extends Element<F> implements FormElement {

    protected final StringProperty label = new SimpleStringProperty("");

    protected final StringProperty requiredError = new SimpleStringProperty("");
    protected final BooleanProperty required = new SimpleBooleanProperty(false);

    protected final BooleanProperty valid = new SimpleBooleanProperty(true);
    protected final BooleanProperty changed = new SimpleBooleanProperty(false);

    protected final ListProperty<String> errorMessages = new SimpleListProperty<>(FXCollections.observableArrayList());

    protected SimpleControl<F> renderer;

    protected final Map<EventType<FieldEvent>, List<EventHandler<? super FieldEvent>>> eventHandlers = new ConcurrentHashMap<>();

    public static StringField ofStringType(String defaultValue) {
        return new StringField(new SimpleStringProperty(defaultValue), new SimpleStringProperty(defaultValue));
    }

    public static StringField ofStringType(StringProperty binding) {
        return new StringField(new SimpleStringProperty(binding.getValue()), new SimpleStringProperty(binding.getValue())).bind(binding);
    }

    public static ModelField ofModelType(Model defaultValue) {
        return new ModelField(new SimpleObjectProperty<Model>(defaultValue), new SimpleObjectProperty<Model>(defaultValue));
    }

    public static ModelField ofModelType(ObjectProperty<Model> binding) {
        return new ModelField(new SimpleObjectProperty<Model>(binding.getValue()), new SimpleObjectProperty<Model>(binding.getValue())).bind(binding);
    }

    public F label(String newValue) {
        label.set(newValue);
        return (F) this;
    }

    public F render(SimpleControl<F> newValue) {
        renderer = newValue;
        return (F) this;
    }

    public F required(boolean newValue) {
        required.set(newValue);
        validate();

        return (F) this;
    }

    public F required(String errorMessage) {
        required.set(true);

        requiredError.set(errorMessage);

        validate();

        return (F) this;
    }

    protected abstract boolean validate();

    public String getLabel() {
        return label.get();
    }

    public StringProperty labelProperty() {
        return label;
    }

    public boolean hasChanged() {
        return changed.get();
    }

    public BooleanProperty changedProperty() {
        return changed;
    }

    public boolean isRequired() {
        return required.get();
    }

    public BooleanProperty requiredProperty() {
        return required;
    }

    public boolean isValid() {
        return valid.get();
    }

    public BooleanProperty validProperty() {
        return valid;
    }

    public SimpleControl<F> getRenderer() {
        return renderer;
    }

    public Field addEventHandler(EventType<FieldEvent> eventType, EventHandler<? super FieldEvent> eventHandler) {
        if (eventType == null) {
            throw new NullPointerException("Argument eventType must not be null");
        }
        if (eventHandler == null) {
            throw new NullPointerException("Argument eventHandler must not be null");
        }

        this.eventHandlers.computeIfAbsent(eventType, k -> new CopyOnWriteArrayList<>()).add(eventHandler);

        return this;
    }

    public Field removeEventHandler(EventType<FieldEvent> eventType, EventHandler<? super FieldEvent> eventHandler) {
        if (eventType == null) {
            throw new NullPointerException("Argument eventType must not be null");
        }
        if (eventHandler == null) {
            throw new NullPointerException("Argument eventHandler must not be null");
        }

        List<EventHandler<? super FieldEvent>> list = this.eventHandlers.get(eventType);
        if (list != null) {
            list.remove(eventHandler);
        }

        return this;
    }

    protected void fireEvent(FieldEvent event) {
        List<EventHandler<? super FieldEvent>> list = this.eventHandlers.get(event.getEventType());
        if (list == null) {
            return;
        }
        for (EventHandler<? super FieldEvent> eventHandler : list) {
            if (!event.isConsumed()) {
                eventHandler.handle(event);
            }
        }
    }

}
