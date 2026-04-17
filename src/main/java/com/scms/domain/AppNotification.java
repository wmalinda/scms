package com.scms.domain;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public class AppNotification {

    private final String id;
    private final String userId;
    private final String message;
    private final Instant createdAt;
    private boolean read;

    public AppNotification(String id, String userId, String message, Instant createdAt, boolean read) {
        this.id = Objects.requireNonNull(id);
        this.userId = Objects.requireNonNull(userId);
        this.message = Objects.requireNonNull(message);
        this.createdAt = Objects.requireNonNull(createdAt);
        this.read = read;
    }

    public static String newId() {
        return UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getMessage() {
        return message;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }
}
