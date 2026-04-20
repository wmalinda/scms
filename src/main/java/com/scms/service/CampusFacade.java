package com.scms.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import com.scms.domain.Booking;
import com.scms.domain.MaintenanceRequest;
import com.scms.domain.MaintenanceStatus;
import com.scms.domain.MaintenanceUrgency;
import com.scms.domain.Room;
import com.scms.domain.RoomCategory;
import com.scms.domain.User;
import com.scms.domain.UserRole;
import com.scms.exception.DuplicateDataException;
import com.scms.exception.InvalidBookingException;
import com.scms.exception.ScmsSystemException;
import com.scms.exception.UnauthorizedActionException;
import com.scms.pattern.factory.RoomFactory;
/**
 * Structural: Facade — single entry point for the GUI and tests, coordinating repository and services.
 */
public class CampusFacade {

    private final CampusRepository repository;
    private final NotificationService notificationService;
    private final BookingService bookingService;
    private final MaintenanceService maintenanceService;

    public CampusFacade() {
        this(CampusRepository.getInstance(), new NotificationService(CampusRepository.getInstance()));
    }

    public CampusFacade(CampusRepository repository, NotificationService notificationService) {
        this.repository = repository;
        this.notificationService = notificationService;
        this.bookingService = new BookingService(repository, notificationService);
        this.maintenanceService = new MaintenanceService(repository, notificationService);
    }

    public CampusRepository getRepository() {
        return repository;
    }

    public NotificationService getNotificationService() {
        return notificationService;
    }

    public BookingService getBookingService() {
        return bookingService;
    }

    public User authenticate(String username) throws UnauthorizedActionException {
        return repository.findUserByUsername(username)
                .orElseThrow(() -> new UnauthorizedActionException("Unknown user: " + username));
    }

    public Room addRoom(User actor, RoomCategory category, String name, int capacity, List<String> equipment)
            throws UnauthorizedActionException, DuplicateDataException {
        if (actor.getRole() != UserRole.ADMINISTRATOR) {
            throw new UnauthorizedActionException("Only administrators can add rooms.");
        }
        Room room = RoomFactory.create(category, name, capacity, equipment);
        repository.addRoom(room);
        return room;
    }

    public void updateRoom(User actor, String roomId, String name, Integer capacity, List<String> equipment,
                           Boolean active)
            throws UnauthorizedActionException, ScmsSystemException {
        if (actor.getRole() != UserRole.ADMINISTRATOR) {
            throw new UnauthorizedActionException("Only administrators can edit rooms.");
        }
        Room r = repository.findRoomById(roomId)
                .orElseThrow(() -> new ScmsSystemException("Room not found."));
        if (name != null) {
            r.setName(name);
        }
        if (capacity != null && capacity > 0) {
            r.setCapacity(capacity);
        }
        if (equipment != null) {
            r.setEquipment(equipment);
        }
        if (active != null) {
            r.setActive(active);
        }
    }

    public List<Room> listActiveRooms() {
        return repository.getRooms().stream().filter(Room::isActive).collect(Collectors.toList());
    }

    public List<Room> listAllRoomsForAdmin() {
        return repository.getRooms();
    }

    public Booking bookRoom(User actor, String roomId, LocalDate date, LocalTime start, LocalTime end, boolean recurring)
            throws InvalidBookingException, UnauthorizedActionException {
        return bookingService.createBooking(actor, roomId, date, start, end, recurring);
    }

    public void cancelBooking(User actor, String bookingId, String reason)
            throws InvalidBookingException, UnauthorizedActionException {
        bookingService.cancelBooking(actor, bookingId, reason);
    }

    public MaintenanceRequest submitMaintenance(User actor, String roomId, String description,
                                                MaintenanceUrgency urgency)
            throws DuplicateDataException {
        return maintenanceService.reportIssue(actor, roomId, description, urgency);
    }

    public void assignMaintenance(User actor, String requestId, MaintenanceStatus status, String assignToUserId)
            throws UnauthorizedActionException, ScmsSystemException {
        maintenanceService.updateStatus(actor, requestId, status, assignToUserId);
    }

    /** Simple analytics: room booking counts and maintenance keywords. */
    public Map<String, Long> roomBookingCounts() {
        return repository.getBookings().stream()
                .collect(Collectors.groupingBy(Booking::getRoomId, Collectors.counting()));
    }

    public Map<String, Long> maintenanceDescriptionTokens() {
        return repository.getMaintenanceRequests().stream()
                .map(r -> r.getDescription().toLowerCase(Locale.ROOT).split("\\W+"))
                .flatMap(Arrays::stream)
                .filter(s -> s.length() > 3)
                .collect(Collectors.groupingBy(s -> s, Collectors.counting()));
    }

}
