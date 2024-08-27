package com.example.SinLauncher.config;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.context.annotation.Configuration;

import com.example.SinLauncher.App;
import com.example.SinLauncher.SinLauncherEntites.Instance;
import com.example.SinLauncher.SinLauncherEntites.Os;
import com.example.SinLauncher.json.Client;
import com.google.gson.Gson;
import com.sun.management.OperatingSystemMXBean;

@Configuration
public class Config {
    public static final Path PATH = Paths.get(App.DIR, "config.json");

    public long min_ram = 0;
    public long max_ram = 0;
    public Java java = null;
    public String username = null;

    // The default config
    public Config() {
        OperatingSystemMXBean os = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
        long total = os.getTotalMemorySize();

        this.max_ram = total / 4 / 1024 / 1024;
        this.min_ram = this.max_ram / 2;

        this.java = Java.getAvailableJavaCups()[0];
        this.username = App.currentUser;
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

    // Launches Minecraft using {@code this} as a {@link Config} doesn't handle
    // downloading

    // I'm thinking to create a class for UserSettings...
    public void launch(Instance instance) throws IOException {
        Client client = instance.readClient();
        Path[] paths = client.getLibrariesList();

        String classpath = "";

        for (Path path : paths) {
            classpath += path;

            if (App.OS == Os.Windows)
                classpath += ';';
            else
                classpath += ':';
        }

        classpath += instance.Dir().resolve("client.jar");

        String mainClass = client.mainClass;

        // TODO: Use client.arguments instead
        // TODO: Account arguments...
        ProcessBuilder javaProcess = new ProcessBuilder(
                this.java.path,
                "-Xms" + Long.toString(this.min_ram) + "M",
                "-Xmx" + Long.toString(this.max_ram) + "M",
                "-cp", classpath,
                mainClass,
                "--username", this.username,
                "--gameDir", instance.Dir().toString(),
                "--assetsDir", App.ASSETS_DIR.toString(),
                "--assetIndex", client.assets,
                "--version", client.id,
                "--accessToken", "0");

        javaProcess.redirectErrorStream(true);
        javaProcess.redirectOutput(ProcessBuilder.Redirect.INHERIT);

        System.out.println("Running:");
        System.out.println(javaProcess.command().toString());

        try {
            javaProcess.start().waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
