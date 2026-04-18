package com.scms.pattern.strategy;

import com.scms.domain.Booking;
import com.scms.domain.Room;
import com.scms.domain.User;
import com.scms.exception.InvalidBookingException;

import java.util.List;

/**
 * Behavioural: Strategy — pluggable rules for validating bookings before they are accepted.
 */
@FunctionalInterface
public interface BookingValidationStrategy {

    void validate(User actor, Room room, Booking candidate, List<Booking> existingSameRoom)
            throws InvalidBookingException;
}
