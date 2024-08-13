package com.example.SinLauncher.config;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.context.annotation.Configuration;

import com.example.SinLauncher.App;
import com.google.gson.Gson;
import com.sun.management.OperatingSystemMXBean;

@Configuration
public class Config {
    public static final Path PATH = Paths.get(App.DIR, "config.json");

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
        Files.writeString(PATH, json);
    }

    public static Config readConfig() {
        try {
            System.out.println("Config & App.DIR Path: " + PATH);

            return new Gson().fromJson(Files.readString(PATH), Config.class);
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
