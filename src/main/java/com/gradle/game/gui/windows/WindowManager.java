package com.gradle.game.gui.windows;

import de.gurkenlabs.litiengine.Game;

import java.util.concurrent.ConcurrentHashMap;

public class WindowManager {
    private WindowManager() {
    }

    private static boolean initialized = false;
    private static final ConcurrentHashMap<String, Window> windows = new ConcurrentHashMap<>();

    public static void init() {
        // do stuff here
        initialized = true;
    }

    public static boolean isInitialized() {
        return initialized;
    }

    public static void add(Window window) {
        windows.put(window.getName(), window);
        Game.screens().get("INGAME-SCREEN").getComponents().add(window);
    }

    public static Window get(String name) {
        return windows.get(name);
    }

    public static void display(String name) {
        Window window = windows.get(name);
        if(window.isSuspended()) {
            window.toggleSuspension();
        }
    }

    public static void toggleDisplay(String name) {
        windows.get(name).toggleSuspension();
    }

    public static void suspendAll() {
        windows.forEach((key, value) -> value.suspend());
    }
}
