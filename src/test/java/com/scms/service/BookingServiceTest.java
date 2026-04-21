package com.scms.service;

import com.scms.domain.*;
import com.scms.exception.DuplicateDataException;
import com.scms.exception.InvalidBookingException;
import com.scms.exception.UnauthorizedActionException;
import com.scms.pattern.factory.RoomFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BookingServiceTest {

    private CampusRepository repo;
    private NotificationService notifications;
    private BookingService bookingService;
    private User staff;
    private Room room;

    @BeforeEach
    void setUp() throws DuplicateDataException {
        repo = CampusRepository.newEmptyForTest();
        notifications = new NotificationService(repo);
        bookingService = new BookingService(repo, notifications);
        String sid = User.newId();
        staff = new StaffMember(sid, "tstaff", "Test Staff");
        repo.addUser(staff);
        room = RoomFactory.create(RoomCategory.MEETING_ROOM, "Test room", 10, List.of("Screen"));
        repo.addRoom(room);
    }

    @Test
    void createBooking_success() throws Exception {
        LocalDate d = LocalDate.now().plusDays(1);
        Booking b = bookingService.createBooking(staff, room.getId(), d,
                LocalTime.of(9, 0), LocalTime.of(10, 0), false);
        assertEquals(room.getId(), b.getRoomId());
        assertEquals(1, repo.getBookings().size());
    }

    @Test
    void overlap_throwsInvalidBookingException() {
        LocalDate d = LocalDate.now().plusDays(3);
        Booking existing = new Booking(Booking.newId(), room.getId(), staff.getId(), d,
                LocalTime.of(10, 0), LocalTime.of(12, 0), false);
        try {
            repo.addBooking(existing);
        } catch (DuplicateDataException e) {
            fail(e);
        }
        assertThrows(InvalidBookingException.class, () ->
                bookingService.createBooking(staff, room.getId(), d,
                        LocalTime.of(11, 0), LocalTime.of(13, 0), false));
    }

    @Test
    void adminCannotCreateBooking_throwsUnauthorized() {
        User admin = new Administrator(User.newId(), "adm", "Admin");
        assertThrows(UnauthorizedActionException.class, () ->
                bookingService.createBooking(admin, room.getId(), LocalDate.now().plusDays(1),
                        LocalTime.of(8, 0), LocalTime.of(9, 0), false));
    }
}
