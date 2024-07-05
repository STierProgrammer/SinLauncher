package com.sinlauncher.app;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.sinlauncher.app.Json.Manifest;

import kong.unirest.core.HttpResponse;
import kong.unirest.core.Unirest;

public class App {
    public static final String DIR;

    static {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            DIR = System.getenv("APPDATA") + "\\SinLauncher";
        } else if (os.contains("nix") || os.contains("nux") || os.contains("aix")) {
            DIR = System.getProperty("user.home") + "/.sinlauncher";
        } else {
            DIR = "SinLauncher";
        }
    }

    static void init_launcher_dir() throws IOException {
        if (!Files.exists(Paths.get(DIR))) {
            Files.createDirectories(Paths.get(DIR));
        }

        if (!Files.exists(Paths.get(DIR + "/assets"))) {
            Files.createDirectories(Paths.get(DIR + "/assets"));
        }

        if (!Files.exists(Paths.get(DIR + "/libraries"))) {
            Files.createDirectories(Paths.get(DIR + "/libraries"));
        }

        if (!Files.exists(Paths.get(DIR + "/instances"))) {
            Files.createDirectories(Paths.get(DIR + "/instances"));
        }

        // fetching manifest json or using an already downloaded one
        HttpResponse<String> response = Unirest.get("https://launchermeta.mojang.com/mc/game/version_manifest.json").asString();
        
        Path path = Paths.get(Manifest.PATH);

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

        System.out.println("Launcher Directory: " + DIR);
        
        try {
            Manifest manifest = Manifest.readManifest();
            System.out.println(manifest.latest.release);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
