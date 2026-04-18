package com.scms.pattern.adapter;

import com.scms.domain.Booking;

/**
 * Structural: Adapter — converts domain events into user-facing notification text.
 */
public final class NotificationContentAdapter {

    private NotificationContentAdapter() {
    }

    public static String bookingConfirmed(Booking b, String roomName) {
        return "Booking confirmed: " + roomName + " on " + b.getDate()
                + " " + b.getStart() + "–" + b.getEnd()
                + (b.isRecurringWeekly() ? " (weekly)" : "");
    }

    public static String bookingCancelled(String roomName, String reason) {
        return "Booking cancelled for " + roomName + (reason != null ? ": " + reason : "");
    }

}
