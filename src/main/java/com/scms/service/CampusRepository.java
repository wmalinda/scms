package com.scms.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.scms.domain.Administrator;
import com.scms.domain.AppNotification;
import com.scms.domain.Booking;
import com.scms.domain.MaintenanceRequest;
import com.scms.domain.MaintenanceStatus;
import com.scms.domain.MaintenanceUrgency;
import com.scms.domain.Room;
import com.scms.domain.RoomCategory;
import com.scms.domain.StaffMember;
import com.scms.domain.Student;
import com.scms.domain.User;
import com.scms.domain.UserRole;
import com.scms.pattern.factory.RoomFactory;


/**
 * Creational: Singleton — in-memory store with pre-loaded demo data (no database).
 */
public final class CampusRepository {

    private static final CampusRepository INSTANCE = new CampusRepository(true);

    private final List<User> users = new ArrayList<>();
    private final List<AppNotification> notificationLog = new ArrayList<>();
     private final List<Room> rooms = new ArrayList<>();
    private final List<Booking> bookings = new ArrayList<>();
    private final List<MaintenanceRequest> maintenanceRequests = new ArrayList<>();

    private CampusRepository(boolean withSeed) {
        if (withSeed) {
            seed();
        }
    }

    public static CampusRepository getInstance() {
        return INSTANCE;
    }

    /** Fresh in-memory repository for unit tests (not the singleton). */
    public static CampusRepository newEmptyForTest() {
        return new CampusRepository(false);
    }

    public void addUser(User u) throws com.scms.exception.DuplicateDataException {
        if (users.stream().anyMatch(x -> x.getId().equals(u.getId()) || x.getUsername().equalsIgnoreCase(u.getUsername()))) {
            throw new com.scms.exception.DuplicateDataException("Duplicate user id or username.");
        }
        users.add(u);
    }

    private void seed() {
        String adminId = User.newId();
        String staffId = User.newId();
        String staff2Id = User.newId();
        String studentId = User.newId();
        String student2Id = User.newId();

        users.add(new Administrator(adminId, "admin", "Alex Admin"));
        users.add(new StaffMember(staffId, "staff1", "Sam Staff"));
        users.add(new StaffMember(staff2Id, "staff2", "Sara Lecturer"));
        users.add(new Student(studentId, "student1", "Chris Student"));
        users.add(new Student(student2Id, "student2", "Jamie Learner"));

        Room r1 = RoomFactory.create(RoomCategory.LECTURE_HALL, "Lecture Theatre A", 120,
                List.of("4K projector", "PA", "Hearing loop"));
        Room r2 = RoomFactory.create(RoomCategory.LAB, "Computing Lab 2", 40,
                List.of("30 PCs", "Smart board"));
        Room r3 = RoomFactory.create(RoomCategory.MEETING_ROOM, "Meeting Pod 5", 8,
                List.of("Display", "Whiteboard"));
        rooms.add(r1);
        rooms.add(r2);
        rooms.add(r3);

        bookings.add(new Booking(Booking.newId(), r1.getId(), staffId, LocalDate.now().plusDays(1),
                LocalTime.of(10, 0), LocalTime.of(11, 30), false));
        bookings.add(new Booking(Booking.newId(), r2.getId(), studentId, LocalDate.now().plusDays(2),
                LocalTime.of(14, 0), LocalTime.of(16, 0), false));

        MaintenanceRequest m1 = new MaintenanceRequest(MaintenanceRequest.newId(), r1.getId(),
                "Projector flickering in front row", MaintenanceUrgency.HIGH, MaintenanceStatus.PENDING);
        MaintenanceRequest m2 = new MaintenanceRequest(MaintenanceRequest.newId(), r2.getId(),
                "One PC not booting", MaintenanceUrgency.MEDIUM, MaintenanceStatus.ASSIGNED);
        m2.setAssignedToUserId(staffId);
        maintenanceRequests.add(m1);
        maintenanceRequests.add(m2);

        notificationLog.add(new AppNotification(AppNotification.newId(), studentId,
                "Welcome to Smart Campus — your bookings and alerts appear here.", java.time.Instant.now(), true));

    }

    public Optional<User> findUserByUsername(String username) {
        return users.stream().filter(u -> u.getUsername().equalsIgnoreCase(username)).findFirst();
    }

    public Optional<User> findUserById(String id) {
        return users.stream().filter(u -> u.getId().equals(id)).findFirst();
    }

    public List<User> getUsers() {
        return Collections.unmodifiableList(users);
    }

    public List<Room> getRooms() {
        return Collections.unmodifiableList(rooms);
    }

    public Optional<Room> findRoomById(String id) {
        return rooms.stream().filter(r -> r.getId().equals(id)).findFirst();
    }

    public void addRoom(Room room) throws com.scms.exception.DuplicateDataException {
        if (rooms.stream().anyMatch(r -> r.getId().equals(room.getId()))) {
            throw new com.scms.exception.DuplicateDataException("Room id already exists: " + room.getId());
        }
        rooms.add(room);
    }

    public void addBooking(Booking b) throws com.scms.exception.DuplicateDataException {
        if (bookings.stream().anyMatch(x -> x.getId().equals(b.getId()))) {
            throw new com.scms.exception.DuplicateDataException("Duplicate booking id.");
        }
        bookings.add(b);
    }

    public List<Booking> getBookings() {
        return Collections.unmodifiableList(bookings);
    }

    public boolean removeBooking(String bookingId) {
        return bookings.removeIf(b -> b.getId().equals(bookingId));
    }

    public List<Booking> bookingsForRoom(String roomId) {
        return bookings.stream().filter(b -> b.getRoomId().equals(roomId)).collect(Collectors.toList());
    }

    public List<Booking> bookingsForUser(String userId) {
        return bookings.stream().filter(b -> b.getUserId().equals(userId)).collect(Collectors.toList());
    }

    public void addMaintenance(MaintenanceRequest r) throws com.scms.exception.DuplicateDataException {
        if (maintenanceRequests.stream().anyMatch(x -> x.getId().equals(r.getId()))) {
            throw new com.scms.exception.DuplicateDataException("Duplicate maintenance id.");
        }
        maintenanceRequests.add(r);
    }

    public List<MaintenanceRequest> getMaintenanceRequests() {
        return Collections.unmodifiableList(maintenanceRequests);
    }

    public Optional<MaintenanceRequest> findMaintenanceById(String id) {
        return maintenanceRequests.stream().filter(m -> m.getId().equals(id)).findFirst();
    }

    public List<String> getAdministratorIds() {
        return users.stream().filter(u -> u.getRole() == UserRole.ADMINISTRATOR).map(User::getId).toList();
    }

    public void appendNotification(AppNotification n) {
        notificationLog.add(n);
    }
    
    public List<AppNotification> notificationsForUser(String userId) {
        return notificationLog.stream().filter(n -> n.getUserId().equals(userId))
                .sorted(Comparator.comparing(AppNotification::getCreatedAt).reversed())
                .collect(Collectors.toList());
    }

}
