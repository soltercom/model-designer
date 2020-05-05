package formsfx.model.structure;

import formsfx.model.event.FormEvent;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.EventHandler;
import javafx.event.EventType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class Form {

    protected final List<Group> groups = new ArrayList<>();

    protected final StringProperty title = new SimpleStringProperty("");

    protected final BooleanProperty changed = new SimpleBooleanProperty(false);
    protected final BooleanProperty persistable = new SimpleBooleanProperty(false);

    private final Map<EventType<FormEvent>, List<EventHandler<? super FormEvent>>> eventHandlers = new ConcurrentHashMap<>();

    private Form(Group... groups) {
        Collections.addAll(this.groups, groups);

        this.groups.forEach(s -> s.changedProperty().addListener((observable, oldValue, newValue) -> setChangedProperty()));

        setChangedProperty();
        setPersistableProperty();

    }

    public static Form of(Group... sections) {
        return new Form(sections);
    }

    public Form title(String newValue) {
        title.set(newValue);
        return this;
    }

    public void persist() {
        if (!isPersistable()) {
            return;
        }

        groups.forEach(Group::persist);

        fireEvent(FormEvent.formPersistedEvent(this));
    }

    public void reset() {
        if (!hasChanged()) {
            return;
        }

        groups.forEach(Group::reset);

        fireEvent(FormEvent.formResetEvent(this));
    }

    protected void setChangedProperty() {
        changed.setValue(groups.stream().anyMatch(Group::hasChanged));
        setPersistableProperty();
    }

    protected void setPersistableProperty() {
        persistable.setValue(groups.stream().anyMatch(Group::hasChanged));
    }

    public List<Group> getGroups() {
        return groups;
    }

    public List<Element> getElements() {
        return groups.stream()
                .map(Group::getElements)
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    public List<Field> getFields() {
        return groups.stream()
                .map(Group::getElements)
                .flatMap(List::stream)
                .filter(e -> e instanceof Field)
                .map(e -> (Field) e)
                .collect(Collectors.toList());
    }

    public boolean hasChanged() {
        return changed.get();
    }

    public BooleanProperty changedProperty() {
        return changed;
    }

    public boolean isPersistable() {
        return persistable.get();
    }

    public BooleanProperty persistableProperty() {
        return persistable;
    }

    public String getTitle() {
        return title.get();
    }

    public StringProperty titleProperty() {
        return title;
    }

    public Form addEventHandler(EventType<FormEvent> eventType, EventHandler<? super FormEvent> eventHandler) {
        if (eventType == null) {
            throw new NullPointerException("Argument eventType must not be null");
        }
        if (eventHandler == null) {
            throw new NullPointerException("Argument eventHandler must not be null");
        }

        this.eventHandlers.computeIfAbsent(eventType, k -> new CopyOnWriteArrayList<>()).add(eventHandler);

        return this;
    }

    public Form removeEventHandler(EventType<FormEvent> eventType, EventHandler<? super FormEvent> eventHandler) {
        if (eventType == null) {
            throw new NullPointerException("Argument eventType must not be null");
        }
        if (eventHandler == null) {
            throw new NullPointerException("Argument eventHandler must not be null");
        }

        List<EventHandler<? super FormEvent>> list = this.eventHandlers.get(eventType);
        if (list != null) {
            list.remove(eventHandler);
        }

        return this;
    }

    protected void fireEvent(FormEvent event) {
        List<EventHandler<? super FormEvent>> list = this.eventHandlers.get(event.getEventType());
        if (list == null) {
            return;
        }
        for (EventHandler<? super FormEvent> eventHandler : list) {
            if (!event.isConsumed()) {
                eventHandler.handle(event);
            }
        }
    }

}
