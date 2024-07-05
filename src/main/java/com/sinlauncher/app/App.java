package com.sinlauncher.app;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import kong.unirest.core.HttpResponse;
import kong.unirest.core.Unirest;

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

    static void init_launcher_dir() throws IOException {
        if (!Files.exists(Paths.get(LAUNCHER_DIR))) {
            Files.createDirectories(Paths.get(LAUNCHER_DIR));
        }

        if (!Files.exists(Paths.get(LAUNCHER_DIR + "/assets"))) {
            Files.createDirectories(Paths.get(LAUNCHER_DIR + "/assets"));
        }

        if (!Files.exists(Paths.get(LAUNCHER_DIR + "/libraries"))) {
            Files.createDirectories(Paths.get(LAUNCHER_DIR + "/libraries"));
        }

        if (!Files.exists(Paths.get(LAUNCHER_DIR + "/instances"))) {
            Files.createDirectories(Paths.get(LAUNCHER_DIR + "/instances"));
        }

        // fetching manifest json or using an already downloaded one
        HttpResponse<String> response = Unirest.get("https://launchermeta.mojang.com/mc/game/version_manifest.json").asString();
        
        Path path = Paths.get(LAUNCHER_DIR + "/version_manifest.json");

        if (response.getStatus() == 200) {
            Files.write(path, response.getBody().getBytes());
        } else {
            if (!Files.exists(path)) {
                throw new IOException("Failed to fetch manifest JSON. Response code: " + response.getStatus());
            }
        }
    }

    public static void main(String[] args) {
        try {
            init_launcher_dir();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Launcher Directory: " + LAUNCHER_DIR);
    }
}
