package formsfx.model.event;

import formsfx.model.structure.Form;
import javafx.event.Event;
import javafx.event.EventType;

public final class FormEvent extends Event {

    public static final EventType<FormEvent> EVENT_FORM_PERSISTED = new EventType<>(ANY, "EVENT_FORM_PERSISTED");
    public static final EventType<FormEvent> EVENT_FORM_RESET = new EventType<>(ANY, "EVENT_FORM_RESET");

    public static FormEvent formPersistedEvent(Form form) {
        return new FormEvent(EVENT_FORM_PERSISTED, form);
    }

    public static FormEvent formResetEvent(Form form) {
        return new FormEvent(EVENT_FORM_RESET, form);
    }

    private final Form form;

    private FormEvent(EventType<? extends Event> eventType, Form form) {
        super(eventType);
        this.form = form;
    }

    public final Form getForm() {
        return form;
    }

}
