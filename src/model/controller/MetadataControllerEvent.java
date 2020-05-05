package model.controller;

import javafx.event.Event;
import javafx.event.EventType;

public class MetadataControllerEvent extends Event {

    public static final EventType<MetadataControllerEvent> SELECTED_METADATA_CHANGED = new EventType<>(ANY, "SELECTED_METADATA_CHANGED");
    public static final EventType<MetadataControllerEvent> NEW_METADATA = new EventType<>(ANY, "NEW_METADATA");

    public MetadataControllerEvent(EventType<? extends Event> eventType) {
        super(eventType);
    }

    public static MetadataControllerEvent selectedMetadataChanged() {
        return new MetadataControllerEvent(SELECTED_METADATA_CHANGED);
    }

    public static MetadataControllerEvent newMetadata() {
        return new MetadataControllerEvent(NEW_METADATA);
    }

}
