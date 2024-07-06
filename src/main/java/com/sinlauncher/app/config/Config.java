package com.sinlauncher.app.config;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.google.gson.Gson;
import com.sinlauncher.app.App;
import com.sun.management.OperatingSystemMXBean;

public class Config {
    public static final String PATH = App.DIR + "/config.json";

    public long MIN_RAM;
    public long MAX_RAM;
    public Java JAVA;

    public Config() {
        OperatingSystemMXBean os = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
        long total = os.getTotalMemorySize();

        this.MIN_RAM = total / 6;
        this.MAX_RAM = total / 4;

        this.JAVA = null;
    }

    public static Config getInstanceConfig(String instance) {
        Path path = Paths.get(App.DIR, instance, "config.json");
        
        try {
            return new Gson().fromJson(Files.readString(path), Config.class);
        } catch (IOException _e) {
            return App.CONFIG;
        }
    }

    public void writeConfig() throws IOException {
        String json = new Gson().toJson(this);
        Path path = Path.of(PATH);
        Files.writeString(path, json);
    }

    public static Config readConfig() {
        try {
            Path path = Path.of(PATH);
            return new Gson().fromJson(Files.readString(path), Config.class);
        } catch (IOException _e) {
            Config config = new Config();
            
            try {
                config.writeConfig();
            } catch (IOException e) {
                System.err.println("Failed to write config");
                System.exit(1);
            }


            return config;
        }
    }
}
