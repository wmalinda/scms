package com.scms;

import java.util.Locale;

public final class ScmsConsoleApp {

    public static void main(String[] args) {
        Locale.setDefault(Locale.UK);
        new ScmsConsoleApp().run();
    }

    private void run() {
        println("=== Smart Campus Management System (SCMS) ===");
    }

    private static void println(String s) {
        System.out.println(s);
    }
}
