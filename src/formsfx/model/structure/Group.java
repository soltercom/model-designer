package formsfx.model.structure;

import formsfx.model.event.GroupEvent;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.EventHandler;
import javafx.event.EventType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class Group {

    protected final List<Element> elements = new ArrayList<>();

    protected final BooleanProperty valid = new SimpleBooleanProperty(true);
    protected final BooleanProperty changed = new SimpleBooleanProperty(false);

    private final Map<EventType<GroupEvent>,List<EventHandler<? super GroupEvent>>> eventHandlers = new ConcurrentHashMap<>();

    protected Group(Element... elements) {
        Collections.addAll(this.elements, elements);

        this.elements.stream()
            .filter(e -> e instanceof Field)
            .map(e -> (Field) e)
            .forEach(f -> f.changedProperty().addListener((observable, oldValue, newValue) -> setChangedProperty()));

        this.elements.stream()
                .filter(e -> e instanceof Field)
                .map(e -> (Field) e)
                .forEach(f -> f.validProperty().addListener((observable, oldValue, newValue) -> setValidProperty()));

        setValidProperty();
        setChangedProperty();
    }

    public static Group of(Element... elements) {
        return new Group(elements);
    }

    public void persist() {
        if (!isValid()) {
            return;
        }

        elements.stream()
                .filter(e -> e instanceof FormElement)
                .map(e -> (FormElement) e)
                .forEach(FormElement::persist);

        fireEvent(GroupEvent.groupPersistedEvent(this));
    }

    public void reset() {
        if (!hasChanged()) {
            return;
        }

        elements.stream()
                .filter(e -> e instanceof FormElement)
                .map(e -> (FormElement) e)
                .forEach(FormElement::reset);
    }

    private void setChangedProperty() {
        changed.setValue(elements.stream()
                .filter(e -> e instanceof Field)
                .map(e -> (Field) e)
                .anyMatch(Field::hasChanged));
    }

    private void setValidProperty() {
        valid.setValue(elements.stream()
                .filter(e -> e instanceof Field)
                .map(e -> (Field) e)
                .allMatch(Field::isValid));
    }

    public boolean isValid() {
        return valid.get();
    }

    public BooleanProperty validProperty() {
        return valid;
    }

    public List<Element> getElements() {
        return elements;
    }

    public boolean hasChanged() {
        return changed.get();
    }

    public BooleanProperty changedProperty() {
        return changed;
    }

    public Group addEventHandler(EventType<GroupEvent> eventType, EventHandler<? super GroupEvent> eventHandler) {
        if (eventType == null) {
            throw new NullPointerException("Argument eventType must not be null");
        }
        if (eventHandler == null) {
            throw new NullPointerException("Argument eventHandler must not be null");
        }

        this.eventHandlers.computeIfAbsent(eventType, k -> new CopyOnWriteArrayList<>()).add(eventHandler);

        return this;
    }

    public Group removeEventHandler(EventType<GroupEvent> eventType, EventHandler<? super GroupEvent> eventHandler) {
        if (eventType == null) {
            throw new NullPointerException("Argument eventType must not be null");
        }
        if (eventHandler == null) {
            throw new NullPointerException("Argument eventHandler must not be null");
        }

        List<EventHandler<? super GroupEvent>> list = this.eventHandlers.get(eventType);
        if (list != null) {
            list.remove(eventHandler);
        }

        return this;
    }

    protected void fireEvent(GroupEvent event) {
        List<EventHandler<? super GroupEvent>> list = this.eventHandlers.get(event.getEventType());
        if (list == null) {
            return;
        }
        for (EventHandler<? super GroupEvent> eventHandler : list) {
            if (!event.isConsumed()) {
                eventHandler.handle(event);
            }
        }
    }

}
