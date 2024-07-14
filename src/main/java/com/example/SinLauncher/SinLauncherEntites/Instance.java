package com.example.SinLauncher.SinLauncherEntites;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.example.SinLauncher.App;
import com.example.SinLauncher.config.Config;


public class Instance {
    public static final String PARENT_DIR = App.DIR + "/instances";

    public String name;
    public String version;
    public String icon;
    public Config config = null;

    public static Instance getInstance(String name) {
        try {
            Path path = Paths.get(App.DIR, "instances.json");
            
            Instance[] instances = App.GSON.fromJson(Files.readString(path), Instance[].class);

            for (Instance instance : instances) {
                if (instance.name.equals(name)) {
                    instance.config = Config.getInstanceConfig(name);
                    return instance;
                }
            }

            return null;
        } catch (IOException _e) {
            return null;
        }
    }
}
