package com.scms.service;

import com.scms.domain.Booking;
import com.scms.domain.Room;
import com.scms.domain.User;
import com.scms.domain.UserRole;
import com.scms.exception.InvalidBookingException;
import com.scms.exception.UnauthorizedActionException;
import com.scms.pattern.adapter.NotificationContentAdapter;
import com.scms.pattern.strategy.BookingValidationStrategy;
import com.scms.pattern.strategy.DefaultBookingValidationStrategy;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class BookingService {

    private final CampusRepository repo;
    private final NotificationService notifications;
    private BookingValidationStrategy validationStrategy = new DefaultBookingValidationStrategy();

    public BookingService(CampusRepository repo, NotificationService notifications) {
        this.repo = repo;
        this.notifications = notifications;
    }

    public void setValidationStrategy(BookingValidationStrategy validationStrategy) {
        this.validationStrategy = validationStrategy != null ? validationStrategy : new DefaultBookingValidationStrategy();
    }

    public Booking createBooking(User actor, String roomId, LocalDate date, LocalTime start, LocalTime end,
                                 boolean recurring) throws InvalidBookingException, UnauthorizedActionException {
        if (actor.getRole() != UserRole.STUDENT && actor.getRole() != UserRole.STAFF) {
            throw new UnauthorizedActionException("Only staff or students can create bookings.");
        }
        Room room = repo.findRoomById(roomId).orElseThrow(() -> new InvalidBookingException("Room not found."));
        List<Booking> sameRoom = repo.bookingsForRoom(roomId);
        Booking candidate = new Booking(Booking.newId(), roomId, actor.getId(), date, start, end, recurring);
        validationStrategy.validate(actor, room, candidate, sameRoom);
        try {
            repo.addBooking(candidate);
        } catch (com.scms.exception.DuplicateDataException e) {
            throw new InvalidBookingException(e.getMessage());
        }
        notifications.publish(actor.getId(), NotificationContentAdapter.bookingConfirmed(candidate, room.getName()));
        return candidate;
    }

    public void cancelBooking(User actor, String bookingId, String reason)
            throws InvalidBookingException, UnauthorizedActionException {
        Booking target = repo.getBookings().stream().filter(b -> b.getId().equals(bookingId)).findFirst()
                .orElseThrow(() -> new InvalidBookingException("Booking not found."));
        boolean owner = target.getUserId().equals(actor.getId());
        boolean admin = actor.getRole() == UserRole.ADMINISTRATOR;
        if (!owner && !admin) {
            throw new UnauthorizedActionException("You cannot cancel this booking.");
        }
        Room room = repo.findRoomById(target.getRoomId()).orElse(null);
        String roomName = room != null ? room.getName() : target.getRoomId();
        if (!repo.removeBooking(bookingId)) {
            throw new InvalidBookingException("Booking could not be removed.");
        }
        notifications.publish(actor.getId(), NotificationContentAdapter.bookingCancelled(roomName, reason));
    }
}
