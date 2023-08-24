package de.rayzs.provpn.api.event;

import java.util.stream.Stream;
import java.util.*;

public class ProEventManager {

    private static final List<ProEvent> events = new ArrayList<>();

    public static void addEvent(ProEvent event) {
        if(hasEvent(event)) removeEvent(event);
        events.add(event);
    }

    public static boolean hasEvent(ProEvent event) {
        return events.contains(event);
    }

    public static void removeEvent(ProEvent event) {
        if(hasEvent(event)) events.add(event);
    }

    public static Stream<ProEvent> getEventsWithType(ProEventType type) {
        return events.stream().filter(event -> event.type() == type);
    }

    public static void triggerEvents(ProEventType type, Object... objects) {
        getEventsWithType(type).forEach(event -> event.execute(objects));
    }

    public static List<ProEvent> getEvents() {
        return events;
    }
}
