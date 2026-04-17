package com.scms.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.scms.domain.Administrator;
import com.scms.domain.AppNotification;
import com.scms.domain.StaffMember;
import com.scms.domain.Student;
import com.scms.domain.User;

/**
 * Creational: Singleton — in-memory store with pre-loaded demo data (no database).
 */
public final class CampusRepository {

    private static final CampusRepository INSTANCE = new CampusRepository(true);

    private final List<User> users = new ArrayList<>();
    private final List<AppNotification> notificationLog = new ArrayList<>();

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

}
