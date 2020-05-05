package formsfx.model.event;

import formsfx.model.structure.Group;
import javafx.event.Event;
import javafx.event.EventType;

public final class GroupEvent extends Event {

    public static final EventType<GroupEvent> EVENT_GROUP_PERSISTED = new EventType<>(ANY, "EVENT_GROUP_PERSISTED");
    public static final EventType<GroupEvent> EVENT_GROUP_RESET = new EventType<>(ANY, "EVENT_GROUP_RESET");

    public static GroupEvent groupPersistedEvent(Group group) {
        return new GroupEvent(EVENT_GROUP_PERSISTED, group);
    }

    public static GroupEvent groupResetEvent(Group group) {
        return new GroupEvent(EVENT_GROUP_RESET, group);
    }

    private final Group group;

    private GroupEvent(EventType<? extends Event> eventType, Group group) {
        super(eventType);
        this.group = group;
    }

    public final Group getGroup() {
        return group;
    }
}
