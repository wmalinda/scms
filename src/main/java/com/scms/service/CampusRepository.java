package com.scms.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.scms.domain.Administrator;
import com.scms.domain.AppNotification;
import com.scms.domain.Booking;
import com.scms.domain.Room;
import com.scms.domain.RoomCategory;
import com.scms.domain.StaffMember;
import com.scms.domain.Student;
import com.scms.domain.User;
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

    }

    public Optional<User> findUserByUsername(String username) {
        return users.stream().filter(u -> u.getUsername().equalsIgnoreCase(username)).findFirst();
    }

    public Optional<User> findUserById(String id) {
        return users.stream().filter(u -> u.getId().equals(id)).findFirst();
    }

    public void appendNotification(AppNotification n) {
        notificationLog.add(n);
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

}
