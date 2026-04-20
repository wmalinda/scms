package com.scms.domain;

import java.util.Objects;
import java.util.UUID;

public class MaintenanceRequest {

    private final String id;
    private final String roomId;
    private String description;
    private MaintenanceUrgency urgency;
    private MaintenanceStatus status;
    private String assignedToUserId;

    public MaintenanceRequest(String id, String roomId, String description,
                              MaintenanceUrgency urgency, MaintenanceStatus status) {
        this.id = Objects.requireNonNull(id);
        this.roomId = Objects.requireNonNull(roomId);
        this.description = Objects.requireNonNull(description);
        this.urgency = Objects.requireNonNull(urgency);
        this.status = Objects.requireNonNull(status);
    }

    public static String newId() {
        return UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public String getRoomId() {
        return roomId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = Objects.requireNonNull(description);
    }

    public MaintenanceUrgency getUrgency() {
        return urgency;
    }

    public void setUrgency(MaintenanceUrgency urgency) {
        this.urgency = Objects.requireNonNull(urgency);
    }

    public MaintenanceStatus getStatus() {
        return status;
    }

    public void setStatus(MaintenanceStatus status) {
        this.status = Objects.requireNonNull(status);
    }

    public String getAssignedToUserId() {
        return assignedToUserId;
    }

    public void setAssignedToUserId(String assignedToUserId) {
        this.assignedToUserId = assignedToUserId;
    }
}
