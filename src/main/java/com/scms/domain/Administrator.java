package com.scms.domain;

public final class Administrator extends User {

    public Administrator(String id, String username, String displayName) {
        super(id, username, displayName, UserRole.ADMINISTRATOR);
    }

    @Override
    public String getDashboardTitle() {
        return "Administrator — Smart Campus";
    }
}
