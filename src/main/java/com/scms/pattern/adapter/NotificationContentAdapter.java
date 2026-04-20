package com.scms.pattern.adapter;

import java.util.Optional;

import com.scms.domain.Booking;
import com.scms.domain.MaintenanceRequest;
import com.scms.domain.MaintenanceStatus;
import com.scms.domain.User;

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

    public static String maintenanceStatusChange(MaintenanceRequest req, MaintenanceStatus previous,
                                                   Optional<User> assignee) {
        String base = "Maintenance request " + req.getId().substring(0, Math.min(8, req.getId().length()))
                + " for room " + req.getRoomId() + ": " + previous + " → " + req.getStatus();
        if (req.getStatus() != MaintenanceStatus.ASSIGNED) {
            return base;
        }
        if (assignee.isPresent()) {
            User u = assignee.get();
            return base + " | assigned to " + u.getDisplayName() + " (id=" + u.getId() + ")";
        }
        String aid = req.getAssignedToUserId();
        if (aid != null && !aid.isBlank()) {
            return base + " | assigned user id: " + aid + " (user not found)";
        }
        return base;
    }

}
