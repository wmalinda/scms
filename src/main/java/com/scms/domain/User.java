package com.scms.domain;

import java.util.Objects;
import java.util.UUID;

/**
 * Base user type — polymorphism via subclasses ({@link Administrator}, {@link StaffMember}, {@link Student}).
 */
public abstract class User {

    private final String id;
    private final String username;
    private final String displayName;
    private final UserRole role;

    protected User(String id, String username, String displayName, UserRole role) {
        this.id = Objects.requireNonNull(id);
        this.username = Objects.requireNonNull(username);
        this.displayName = Objects.requireNonNull(displayName);
        this.role = Objects.requireNonNull(role);
    }

    public static String newId() {
        return UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getDisplayName() {
        return displayName;
    }

    public UserRole getRole() {
        return role;
    }

    /** Role-specific dashboard label for the GUI. */
    public abstract String getDashboardTitle();

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User user = (User) o;
        return id.equals(user.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
