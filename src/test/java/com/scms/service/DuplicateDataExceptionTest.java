package com.scms.service;

import com.scms.domain.Room;
import com.scms.domain.RoomCategory;
import com.scms.domain.StaffMember;
import com.scms.domain.User;
import com.scms.exception.DuplicateDataException;
import com.scms.pattern.factory.RoomFactory;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Demonstrates expected failure: duplicate room id is rejected (assertThrows verifies handling).
 */
class DuplicateDataExceptionTest {

    @Test
    void duplicateRoomId_throwsDuplicateDataException() throws DuplicateDataException {
        CampusRepository repo = CampusRepository.newEmptyForTest();
        var r1 = RoomFactory.create(RoomCategory.MEETING_ROOM, "A", 5, List.of());
        repo.addRoom(r1);
        var r2 = new Room(r1.getId(), RoomCategory.MEETING_ROOM, "B", 5, List.of(), true);
        assertThrows(DuplicateDataException.class, () -> repo.addRoom(r2));
    }

    @Test
    void duplicateUsername_throwsDuplicateDataException() throws DuplicateDataException {
        CampusRepository repo = CampusRepository.newEmptyForTest();
        User a = new StaffMember(User.newId(), "same", "One");
        repo.addUser(a);
        User b = new StaffMember(User.newId(), "same", "Two");
        assertThrows(DuplicateDataException.class, () -> repo.addUser(b));
    }
}
