package com.example.SinLauncher;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.logging.Level;
import java.util.logging.Logger;

import java.util.List;

import com.example.SinLauncher.SinLauncherEntites.Instance;
import com.example.SinLauncher.SinLauncherEntites.Os;
import com.example.SinLauncher.SinLauncherEntites.Instance.InstanceAlreadyExistsException;
import com.example.SinLauncher.config.Config;
import com.example.SinLauncher.config.Java;
import com.example.SinLauncher.json.Client;
import com.example.SinLauncher.json.Manifest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import kong.unirest.core.HttpResponse;
import kong.unirest.core.Unirest;

public class App {
    public static final Logger LOGGER = Logger.getLogger(App.class.getName());
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting()
            .registerTypeAdapter(Client.Argument.class, new Client.ArgumentDeserializer()).create();

    public static final String DIR;
    public static final Os OS;

    public static Config CONFIG;

    static {
        String os = System.getProperty("os.name").toLowerCase();

        if (os.contains("win")) {
            DIR = System.getenv("APPDATA") + "\\SinLauncher";
            OS = Os.Windows;
        }

        else if (os.contains("nix") || os.contains("nux") || os.contains("aix")) {
            DIR = System.getProperty("user.home") + "/.sinlauncher";
            OS = Os.Linux;
        }

        else {
            DIR = "SinLauncher";
            OS = Os.Linux;
        }

        try {
            App.init();
        } catch (IOException e) {
            LOGGER.info("failed to init launcher");
        }

        CONFIG = Config.readConfig();
    }

    static void initInstances() throws IOException {
        if (!Files.exists(Instance.PARENT_DIR))
            Files.createDirectories(Instance.PARENT_DIR);

        if (!Files.exists(Instance.INSTANCES_FILE)) {
            Path file = Files.createFile(Instance.INSTANCES_FILE);
            Files.writeString(file, "[]");
        }
    }

    static void initLauncherDir() throws IOException {
        if (!Files.exists(Paths.get(DIR)))
            Files.createDirectories(Paths.get(DIR));

        if (!Files.exists(Paths.get(DIR + "/assets")))
            Files.createDirectories(Paths.get(DIR + "/assets"));

        if (!Files.exists(Paths.get(DIR + "/libraries")))
            Files.createDirectories(Paths.get(DIR + "/libraries"));

        App.initInstances();

        HttpResponse<String> response = Unirest
                .get("https://launchermeta.mojang.com/mc/game/version_manifest.json")
                .asString();

        if (response.getStatus() == 200)
            Files.write(Manifest.PATH, response
                    .getBody()
                    .getBytes());

        else {
            if (!Files.exists(Manifest.PATH))
                throw new IOException("Failed to fetch manifest JSON. Response code: " + response.getStatus());
        }
    }

    public static void init() throws IOException {
        initLauncherDir();
        LOGGER.info("Launcher initialized");
    }

    public static void main(String[] args) {
        try {
            Manifest manifest = Manifest.readManifest();

            System.out.println(CONFIG.MAX_RAM);
            System.out.println(manifest.latest.release);

            List<Java> cups = Java.getAvailableJavaCups();

            for (Java cup : cups)
                System.out.println(cup.version + ": " + cup.path);

            try {
                Instance.createInstance("test", manifest.latest.release);
            } catch (InstanceAlreadyExistsException _e) {
            }
            try {
                Instance.createInstance("old", "1.6.4");
            } catch (InstanceAlreadyExistsException _e) {
            }

            System.out.println("instances: ");
            for (Instance instance : Instance.readInstances()) {
                System.out.println(instance.toString());
            }

            Instance testInstance = Instance.readInstances()[0];
            Instance testInstance1 = Instance.readInstances()[1];

            Client client = GSON.fromJson(Files.readString(Paths.get(testInstance.Dir().toString(), "client.json")),
                    Client.class);

            Client client1 = GSON.fromJson(Files.readString(Paths.get(testInstance1.Dir().toString(), "client.json")),
                    Client.class);

            System.out.println(GSON.toJson(client));
            System.out.println("\n\n\nCLIENT1: ");
            System.out.println(GSON.toJson(client1));
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "exception: ", e);
        }
    }
}
