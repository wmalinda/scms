package com.scms.domain;

public final class Student extends User {

    public Student(String id, String username, String displayName) {
        super(id, username, displayName, UserRole.STUDENT);
    }

    @Override
    public String getDashboardTitle() {
        return "Student — Smart Campus";
    }
}
