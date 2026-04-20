package com.scms.service;

import com.scms.domain.MaintenanceRequest;
import com.scms.domain.MaintenanceStatus;
import com.scms.domain.MaintenanceUrgency;
import com.scms.domain.User;
import com.scms.domain.UserRole;
import com.scms.exception.UnauthorizedActionException;
import com.scms.pattern.adapter.NotificationContentAdapter;

import java.util.Optional;

public class MaintenanceService {

    private final CampusRepository repo;
    private final NotificationService notifications;

    public MaintenanceService(CampusRepository repo, NotificationService notifications) {
        this.repo = repo;
        this.notifications = notifications;
    }

    public MaintenanceRequest reportIssue(User actor, String roomId, String description, MaintenanceUrgency urgency)
            throws com.scms.exception.DuplicateDataException {
        MaintenanceRequest req = new MaintenanceRequest(MaintenanceRequest.newId(), roomId, description,
                urgency, MaintenanceStatus.PENDING);
        repo.addMaintenance(req);
        for (String adminId : repo.getAdministratorIds()) {
            notifications.publish(adminId, "New maintenance: " + description + " (room " + roomId + ")");
        }
        notifications.publish(actor.getId(), "Your maintenance report was submitted.");
        return req;
    }

    public void updateStatus(User actor, String requestId, MaintenanceStatus newStatus, String assignToUserId)
            throws UnauthorizedActionException, com.scms.exception.ScmsSystemException {
        if (actor.getRole() != UserRole.ADMINISTRATOR) {
            throw new UnauthorizedActionException("Only administrators can update maintenance.");
        }
        MaintenanceRequest req = repo.findMaintenanceById(requestId)
                .orElseThrow(() -> new com.scms.exception.ScmsSystemException("Request not found."));
        MaintenanceStatus prev = req.getStatus();
        req.setStatus(newStatus);
        if (assignToUserId != null && !assignToUserId.isBlank()) {
            req.setAssignedToUserId(assignToUserId);
        }
        Optional<User> assignee = Optional.ofNullable(req.getAssignedToUserId())
                .filter(id -> !id.isBlank())
                .flatMap(repo::findUserById);
        String msg = NotificationContentAdapter.maintenanceStatusChange(req, prev, assignee);
        for (String uid : repo.getUsers().stream().map(User::getId).toList()) {
            if (uid.equals(req.getAssignedToUserId()) || repo.getAdministratorIds().contains(uid)) {
                notifications.publish(uid, msg);
            }
        }
    }
}
