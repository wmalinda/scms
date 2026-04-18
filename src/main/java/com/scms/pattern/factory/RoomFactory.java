package com.scms.pattern.factory;

import com.scms.domain.Room;
import com.scms.domain.RoomCategory;

import java.util.List;
import java.util.UUID;

/**
 * Creational: Factory Method — builds {@link Room} instances by category with sensible defaults.
 */
public final class RoomFactory {

    private RoomFactory() {
    }

    public static Room create(RoomCategory category, String name, int capacity, List<String> equipment) {
        String id = "room-" + UUID.randomUUID().toString().substring(0, 8);
        List<String> gear = equipment != null ? equipment : defaultEquipment(category);
        return new Room(id, category, name, capacity, gear, true);
    }

    private static List<String> defaultEquipment(RoomCategory category) {
        return switch (category) {
            case LECTURE_HALL -> List.of("Projector", "PA system", "Whiteboard");
            case LAB -> List.of("PCs", "Network", "Safety equipment");
            case MEETING_ROOM -> List.of("TV screen", "Whiteboard", "Video conferencing");
        };
    }
}
