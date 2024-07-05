package com.sinlauncher.app;

public class App {
    private static final String LAUNCHER_DIR;

    static {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            LAUNCHER_DIR = System.getenv("APPDATA") + "\\SinLauncher";
        } else if (os.contains("nix") || os.contains("nux") || os.contains("aix")) {
            LAUNCHER_DIR = System.getProperty("user.home") + "/.sinlauncher";
        } else {
            LAUNCHER_DIR = "SinLauncher";
        }
    }

    public static void main(String[] args) {
        System.out.println("Launcher Directory: " + LAUNCHER_DIR);
    }
}
