package com.scms.pattern.strategy;

import com.scms.domain.Booking;
import com.scms.domain.Room;
import com.scms.domain.User;
import com.scms.exception.InvalidBookingException;

import java.util.List;

public final class DefaultBookingValidationStrategy implements BookingValidationStrategy {

    @Override
    public void validate(User actor, Room room, Booking candidate, List<Booking> existingSameRoom)
            throws InvalidBookingException {
        if (!room.isActive()) {
            throw new InvalidBookingException("Room is not available for booking.");
        }
        if (candidate.getEnd().isBefore(candidate.getStart()) || candidate.getEnd().equals(candidate.getStart())) {
            throw new InvalidBookingException("Invalid time range.");
        }
        for (Booking b : existingSameRoom) {
            if (b.overlapsSameDay(candidate)) {
                throw new InvalidBookingException("That slot overlaps an existing booking.");
            }
        }
    }
}
