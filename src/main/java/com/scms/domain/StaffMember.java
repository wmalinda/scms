package com.scms.domain;

public final class StaffMember extends User {

    public StaffMember(String id, String username, String displayName) {
        super(id, username, displayName, UserRole.STAFF);
    }

    @Override
    public String getDashboardTitle() {
        return "Staff — Smart Campus";
    }
}
