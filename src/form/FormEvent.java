package form;

import javafx.event.Event;
import javafx.event.EventType;
import model.controller.MetadataControllerEvent;

public class FormEvent extends Event {

    public static final EventType<FormEvent> FORM_EVENT_PERSISTED = new EventType<>(ANY, "FORM_EVENT_PERSISTED");

    public FormEvent(EventType<? extends Event> eventType) {
        super(eventType);
    }

    public static FormEvent formEventPersisted() {
        return new FormEvent(FORM_EVENT_PERSISTED);
    }
}
