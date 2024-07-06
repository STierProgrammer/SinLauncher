package com.example.SinLauncher.config;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.google.gson.Gson;
import com.example.SinLauncher.App;

import com.sun.management.OperatingSystemMXBean;

public class Config {
    public static final String PATH = App.DIR + "/config.json";

    public long MIN_RAM = 0;
    public long MAX_RAM = 0;
    public Java JAVA = null;

    public Config() {
        OperatingSystemMXBean os = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
        long total = os.getTotalMemorySize();

        this.MAX_RAM = total / 4 / 1024 / 1024;
        this.MIN_RAM = this.MAX_RAM / 2;

        this.JAVA = null;
    }

    public static Config getInstanceConfig(String instance) {
        Path path = Paths.get(App.DIR, instance, "config.json");
        
        try {
            Config config = App.GSON.fromJson(Files.readString(path), Config.class);
            if (config.JAVA == null) {
                config.JAVA = App.CONFIG.JAVA;
            }

            if (config.MIN_RAM == 0) {
                config.MIN_RAM = App.CONFIG.MIN_RAM;
            } 
            
            if (config.MAX_RAM == 0) {
                config.MAX_RAM = App.CONFIG.MAX_RAM;
            }

            return config;
        } catch (IOException _e) {
            return App.CONFIG;
        }
    }

    public void writeConfig() throws IOException {
        String json = App.GSON.toJson(this);
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

