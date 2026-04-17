package com.scms;

import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Scanner;

import com.scms.domain.User;
import com.scms.exception.UnauthorizedActionException;
import com.scms.service.CampusFacade;
import com.scms.service.NotificationService;

public final class ScmsConsoleApp {

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ISO_LOCAL_DATE;
    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("HH:mm");

    private final CampusFacade facade = new CampusFacade();
    private final Scanner in = new Scanner(System.in);
    private User currentUser;

    public static void main(String[] args) {
        Locale.setDefault(Locale.UK);
        new ScmsConsoleApp().run();
    }

    private void run() {
        println("=== Smart Campus Management System (SCMS) ===");
        while (true) {
            if (currentUser == null) {
                if (!login()) {
                    println("Goodbye.");
                    return;
                }
                wireNotifications();
            }
            boolean stay = mainMenu();
            if (!stay) {
                currentUser = null;
            }
        }
    }

    private void wireNotifications() {
        NotificationService ns = facade.getNotificationService();
        ns.subscribe(currentUser.getId(), n -> println("[Notification] " + n.getMessage()));
    }

    private boolean login() {
        println("\n--- Login (demo users, no password) ---");
        println("  1. admin");
        println("  2. staff1");
        println("  3. staff2");
        println("  4. student1");
        println("  5. student2");
        println("  0. Exit");
        int c = readInt("Choose: ", 0, 5);
        if (c == 0) {
            return false;
        }
        String[] users = {"", "admin", "staff1", "staff2", "student1", "student2"};
        try {
            currentUser = facade.authenticate(users[c]);
            println("Signed in as " + currentUser.getDisplayName() + " (" + currentUser.getRole() + ").");
            return true;
        } catch (UnauthorizedActionException e) {
            println("Error: " + e.getMessage());
            return login();
        }
    }

    private boolean mainMenu() {
        while (true) {
            println("\n--- Main menu: " + currentUser.getDisplayName() + " ---");
            switch (currentUser.getRole()) {
                case ADMINISTRATOR -> {
                    println("  1. List all rooms");
                    println("  2. Add room");
                    println("  3. Deactivate room");
                    println("  4. Maintenance requests (list / assign / update)");
                    println("  5. Analytics");
                    println("  6. View my notifications");
                    println("  0. Sign out");
                    int a = readInt("Choose: ", 0, 6);
                    switch (a) {
                        case 0 -> {
                            return false;
                        }
                        // case 1 -> listRoomsAdmin();
                        // case 2 -> addRoom();
                        // case 3 -> deactivateRoom();
                        // case 4 -> adminMaintenance();
                        // case 5 -> analytics();
                        // case 6 -> viewNotifications();
                    }
                }
                case STAFF -> {
                    println("  1. List available rooms");
                    println("  2. Book a room");
                    println("  3. My bookings (list / cancel)");
                    println("  4. Report maintenance issue");
                    println("  5. View my notifications");
                    println("  0. Sign out");
                    int s = readInt("Choose: ", 0, 5);
                    switch (s) {
                        case 0 -> {
                            return false;
                        }
                        // case 1 -> listRoomsActive();
                        // case 2 -> bookRoom();
                        // case 3 -> myBookings();
                        // case 4 -> reportMaintenance();
                        // case 5 -> viewNotifications();
                    }
                }
                case STUDENT -> {
                    println("  1. List available rooms");
                    println("  2. Request a booking");
                    println("  3. My bookings (list / cancel)");
                    println("  4. Report maintenance issue");
                    println("  5. View my notifications");
                    println("  0. Sign out");
                    int t = readInt("Choose: ", 0, 5);
                    switch (t) {
                        case 0 -> {
                            return false;
                        }
                        // case 1 -> listRoomsActive();
                        // case 2 -> bookRoom();
                        // case 3 -> myBookings();
                        // case 4 -> reportMaintenance();
                        // case 5 -> viewNotifications();
                    }
                }
            }
        }
    }

    private int readInt(String prompt, int min, int max) {
        while (true) {
            print(prompt);
            try {
                int v = Integer.parseInt(in.nextLine().trim());
                if (v >= min && v <= max) {
                    return v;
                }
                println("Enter a number between " + min + " and " + max + ".");
            } catch (NumberFormatException e) {
                println("Invalid number.");
            }
        }
    }

    private String readLine(String prompt) {
        print(prompt);
        return in.nextLine();
    }

    private static void print(String s) {
        System.out.print(s);
        System.out.flush();
    }

    private static void println(String s) {
        System.out.println(s);
    }
}
