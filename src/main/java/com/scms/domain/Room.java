package com.scms.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Room {

    private final String id;
    private final RoomCategory category;
    private String name;
    private int capacity;
    private final List<String> equipment;
    private boolean active;

    public Room(String id, RoomCategory category, String name, int capacity, List<String> equipment, boolean active) {
        this.id = Objects.requireNonNull(id);
        this.category = Objects.requireNonNull(category);
        this.name = Objects.requireNonNull(name);
        this.capacity = capacity;
        this.equipment = new ArrayList<>(equipment != null ? equipment : List.of());
        this.active = active;
    }

    public String getId() {
        return id;
    }

    public RoomCategory getCategory() {
        return category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = Objects.requireNonNull(name);
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public List<String> getEquipment() {
        return Collections.unmodifiableList(equipment);
    }

    public void setEquipment(List<String> items) {
        equipment.clear();
        if (items != null) {
            equipment.addAll(items);
        }
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Room room = (Room) o;
        return id.equals(room.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
