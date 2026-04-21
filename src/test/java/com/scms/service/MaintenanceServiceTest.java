package com.scms.service;

import com.scms.domain.*;
import com.scms.exception.DuplicateDataException;
import com.scms.exception.UnauthorizedActionException;
import com.scms.pattern.factory.RoomFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MaintenanceServiceTest {

    private CampusRepository repo;
    private MaintenanceService maintenanceService;
    private User staff;
    private User admin;
    private Room room;

    @BeforeEach
    void setUp() throws DuplicateDataException {
        repo = CampusRepository.newEmptyForTest();
        NotificationService ns = new NotificationService(repo);
        maintenanceService = new MaintenanceService(repo, ns);
        staff = new StaffMember(User.newId(), "mstaff", "Maint Staff");
        admin = new Administrator(User.newId(), "madmin", "Maint Admin");
        repo.addUser(staff);
        repo.addUser(admin);
        room = RoomFactory.create(RoomCategory.LAB, "Lab X", 20, List.of("PCs"));
        repo.addRoom(room);
    }

    @Test
    void reportIssue_createsPending() throws DuplicateDataException {
        MaintenanceRequest r = maintenanceService.reportIssue(staff, room.getId(), "AC noisy", MaintenanceUrgency.HIGH);
        assertEquals(MaintenanceStatus.PENDING, r.getStatus());
        assertEquals(1, repo.getMaintenanceRequests().size());
    }

    @Test
    void nonAdminCannotUpdateStatus() throws DuplicateDataException {
        MaintenanceRequest r = maintenanceService.reportIssue(staff, room.getId(), "Leak", MaintenanceUrgency.CRITICAL);
        assertThrows(UnauthorizedActionException.class, () ->
                maintenanceService.updateStatus(staff, r.getId(), MaintenanceStatus.ASSIGNED, staff.getId()));
    }

    @Test
    void adminUpdatesStatus() throws Exception {
        MaintenanceRequest r = maintenanceService.reportIssue(staff, room.getId(), "Door", MaintenanceUrgency.LOW);
        maintenanceService.updateStatus(admin, r.getId(), MaintenanceStatus.COMPLETED, null);
        assertEquals(MaintenanceStatus.COMPLETED,
                repo.findMaintenanceById(r.getId()).orElseThrow().getStatus());
    }
}
