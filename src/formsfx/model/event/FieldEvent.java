package formsfx.model.event;

import formsfx.model.structure.Field;
import javafx.event.Event;
import javafx.event.EventType;

public final class FieldEvent extends Event {

    public static final EventType<FieldEvent> EVENT_FIELD_PERSISTED = new EventType<>(ANY, "EVENT_FIELD_PERSISTED");
    public static final EventType<FieldEvent> EVENT_FIELD_RESET = new EventType<>(ANY, "EVENT_FIELD_RESET");
    public static final EventType<FieldEvent> EVENT_FIELD_START_SELECTION = new EventType<>(ANY, "EVENT_FIELD_START_SELECTION");

    private final Field field;

    private FieldEvent(EventType<? extends Event> eventType, Field field) {
        super(eventType);
        this.field = field;
    }

    public static FieldEvent fieldPersistedEvent(Field field) {
        return new FieldEvent(EVENT_FIELD_PERSISTED, field);
    }

    public static FieldEvent fieldStartSelection(Field field) {
        return new FieldEvent(EVENT_FIELD_START_SELECTION, field);
    }

    public static FieldEvent fieldResetEvent(Field field) {
        return new FieldEvent(EVENT_FIELD_RESET, field);
    }


    public final Field getField() {
        return field;
    }

}
