package com.scms;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.scms.domain.Booking;
import com.scms.domain.Room;
import com.scms.domain.RoomCategory;
import com.scms.domain.User;
import com.scms.exception.DuplicateDataException;
import com.scms.exception.InvalidBookingException;
import com.scms.exception.ScmsSystemException;
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
                        case 1 -> listRoomsAdmin();
                        case 2 -> addRoom();
                        case 3 -> deactivateRoom();
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
                        case 1 -> listRoomsActive();
                        case 2 -> bookRoom();
                        case 3 -> myBookings();
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
                        case 1 -> listRoomsActive();
                        case 2 -> bookRoom();
                        case 3 -> myBookings();
                        // case 4 -> reportMaintenance();
                        // case 5 -> viewNotifications();
                    }
                }
            }
        }
    }

    private void listRoomsAdmin() {
        println("\n-- All rooms --");
        for (Room r : facade.listAllRoomsForAdmin()) {
            println(String.format("  %s | %s | cap=%d | %s | active=%s | %s",
                    r.getId(), r.getName(), r.getCapacity(), r.getCategory(),
                    r.isActive(), String.join(", ", r.getEquipment())));
        }
    }

    private void listRoomsActive() {
        println("\n-- Available (active) rooms --");
        for (Room r : facade.listActiveRooms()) {
            println(String.format("  %s | %s | cap=%d | %s",
                    r.getId(), r.getName(), r.getCapacity(), String.join(", ", r.getEquipment())));
        }
    }

    private void addRoom() {
        println("\n-- Add room --");
        RoomCategory cat = pickEnum("Category", RoomCategory.values());
        String name = readLine("Room name: ");
        int cap = readInt("Capacity: ", 1, 10_000);
        String eqLine = readLine("Equipment (comma-separated): ");
        List<String> eq = Arrays.stream(eqLine.split(","))
                .map(String::trim).filter(s -> !s.isEmpty()).collect(Collectors.toList());
        try {
            Room r = facade.addRoom(currentUser, cat, name, cap, eq);
            println("Added room " + r.getId() + " — " + r.getName());
        } catch (DuplicateDataException | UnauthorizedActionException e) {
            println("Error: " + e.getMessage());
        }
    }

    private void deactivateRoom() {
        List<Room> rooms = facade.listAllRoomsForAdmin();
        if (rooms.isEmpty()) {
            println("No rooms.");
            return;
        }
        println("\n-- Deactivate room --");
        IntStream.range(0, rooms.size()).forEach(i -> println("  " + (i + 1) + ". " + rooms.get(i).getName() + " (" + rooms.get(i).getId() + ")"));
        int idx = readInt("Room #: ", 1, rooms.size()) - 1;
        Room r = rooms.get(idx);
        try {
            facade.updateRoom(currentUser, r.getId(), null, null, null, false);
            println("Deactivated " + r.getName());
        } catch (UnauthorizedActionException | ScmsSystemException e) {
            println("Error: " + e.getMessage());
        }
    }

    private void bookRoom() {
        List<Room> rooms = facade.listActiveRooms();
        if (rooms.isEmpty()) {
            println("No active rooms.");
            return;
        }
        println("\n-- Book room --");
        IntStream.range(0, rooms.size()).forEach(i -> println("  " + (i + 1) + ". " + rooms.get(i).getName() + " (" + rooms.get(i).getId() + ")"));
        int idx = readInt("Room #: ", 1, rooms.size()) - 1;
        Room room = rooms.get(idx);
        LocalDate date = readDate("Date (yyyy-MM-dd): ");
        LocalTime start = readTime("Start time (HH:mm): ");
        LocalTime end = readTime("End time (HH:mm): ");
        boolean weekly = readYesNo("Weekly recurring?");
        try {
            Booking b = facade.bookRoom(currentUser, room.getId(), date, start, end, weekly);
            println("Booking created: " + b.getId());
        } catch (InvalidBookingException | UnauthorizedActionException e) {
            println("Error: " + e.getMessage());
        }
    }

    private void myBookings() {
        List<Booking> list = facade.getRepository().bookingsForUser(currentUser.getId());
        println("\n-- My bookings --");
        if (list.isEmpty()) {
            println("  (none)");
            return;
        }
        IntStream.range(0, list.size()).forEach(i -> {
            Booking b = list.get(i);
            println("  " + (i + 1) + ". " + b.getId());
            println("      room=" + b.getRoomId() + " | " + b.getDate() + " " + b.getStart() + "–" + b.getEnd()
                    + (b.isRecurringWeekly() ? " [weekly]" : ""));
        });
        if (readYesNo("Cancel a booking?")) {
            int i = readInt("Booking #: ", 1, list.size()) - 1;
            Booking b = list.get(i);
            try {
                facade.cancelBooking(currentUser, b.getId(), "Cancelled via console");
                println("Cancelled.");
            } catch (InvalidBookingException | UnauthorizedActionException e) {
                println("Error: " + e.getMessage());
            }
        }
    }

    private LocalDate readDate(String prompt) {
        while (true) {
            try {
                return LocalDate.parse(readLine(prompt).trim(), DATE_FMT);
            } catch (DateTimeParseException e) {
                println("Use format yyyy-MM-dd.");
            }
        }
    }

    private LocalTime readTime(String prompt) {
        while (true) {
            try {
                return LocalTime.parse(readLine(prompt).trim(), TIME_FMT);
            } catch (DateTimeParseException e) {
                println("Use format HH:mm (e.g. 09:30).");
            }
        }
    }

    private boolean readYesNo(String prompt) {
        while (true) {
            String s = readLine(prompt + " (y/n): ").trim().toLowerCase(Locale.ROOT);
            if (s.equals("y") || s.equals("yes")) {
                return true;
            }
            if (s.equals("n") || s.equals("no")) {
                return false;
            }
        }
    }

    private <E extends Enum<E>> E pickEnum(String label, E[] values) {
        for (int i = 0; i < values.length; i++) {
            println("  " + (i + 1) + ". " + values[i].name());
        }
        int c = readInt(label + " (1-" + values.length + "): ", 1, values.length);
        return values[c - 1];
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
