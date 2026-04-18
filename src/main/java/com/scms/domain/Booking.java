package com.scms.domain;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;
import java.util.UUID;

public class Booking {

    private final String id;
    private final String roomId;
    private final String userId;
    private final LocalDate date;
    private final LocalTime start;
    private final LocalTime end;
    private boolean recurringWeekly;

    public Booking(String id, String roomId, String userId, LocalDate date,
                   LocalTime start, LocalTime end, boolean recurringWeekly) {
        this.id = Objects.requireNonNull(id);
        this.roomId = Objects.requireNonNull(roomId);
        this.userId = Objects.requireNonNull(userId);
        this.date = Objects.requireNonNull(date);
        this.start = Objects.requireNonNull(start);
        this.end = Objects.requireNonNull(end);
        this.recurringWeekly = recurringWeekly;
        if (!end.isAfter(start)) {
            throw new IllegalArgumentException("End time must be after start time");
        }
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

    public String getUserId() {
        return userId;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getStart() {
        return start;
    }

    public LocalTime getEnd() {
        return end;
    }

    public boolean isRecurringWeekly() {
        return recurringWeekly;
    }

    public void setRecurringWeekly(boolean recurringWeekly) {
        this.recurringWeekly = recurringWeekly;
    }

    /** True if intervals [start,end) overlap on the same calendar day. */
    public boolean overlapsSameDay(Booking other) {
        if (!date.equals(other.date) || !roomId.equals(other.roomId)) {
            return false;
        }
        return start.isBefore(other.end) && other.start.isBefore(end);
    }
}
