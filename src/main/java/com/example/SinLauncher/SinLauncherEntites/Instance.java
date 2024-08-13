package com.example.SinLauncher.SinLauncherEntites;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import com.example.SinLauncher.App;
import com.example.SinLauncher.config.Config;

public class Instance {
    public static class InstanceNotFoundException extends Exception {
        public String name;

        public InstanceNotFoundException(String message, String name) {
            super(message);
            this.name = name;
        }
    }

    public static final Path PARENT_DIR = Paths.get(App.DIR, "instances/");
    public static final Path INSTANCES_FILE = Paths.get(App.DIR, "instances.json");

    public String name;
    public String version;
    public Path icon = null;
    public Config config = null;

    public Instance(String name, String version) {
        this.name = name;
        this.version = version;
    }

    /**
     * @return reads instances.json, desrialize it and returns all the instances,
     *         returns null if there is no instances.json which should never happen
     *         expect if there is a problem with `App.init`
     */
    public static Instance[] readInstances() {
        try {
            Instance[] instances = App.GSON.fromJson(Files.readString(INSTANCES_FILE), Instance[].class);

            return instances;
        } catch (IOException _e) {
            return null;
        }
    }

    /**
     * @param name the name of the instance to fetch in instances.json
     * @return returns an instance class deserialized from instances.json
     */
    public static Instance getInsance(String name) {
        Instance[] instances = readInstances();

        if (instances != null) {
            for (Instance instance : instances) {
                if (instance.name.equals(name)) {
                    instance.config = Config.getInstanceConfig(name);
                    return instance;
                }
            }
        }

        return null;
    }

    /**
     * updates instances.json replacing all the instances with serialized
     * {@code instances}
     */
    public static void writeInstances(Instance[] instances) throws IOException {
        String json = App.GSON.toJson(instances);
        Files.writeString(INSTANCES_FILE, json);
    }

    /**
     * updates instances.json replacing instance with name {@code name} with
     * {@code instance}
     */
    public static void updateInstance(String name, Instance instance) throws InstanceNotFoundException, IOException {
        boolean found = false;
        Instance[] instances = readInstances();

        for (int i = 0; i < instances.length; i++) {
            if (instances[i].name == name) {
                instances[i] = instance;
                found = true;
                break;
            }
        }

        if (!found) {
            throw new InstanceNotFoundException("couldn't find instance with such a name", name);
        }

        writeInstances(instances);
    }

    /**
     * appends serialized {@code instance} to instances.json
     */
    public static void addInstance(Instance instance) throws IOException {
        List<Instance> instances = List.of(readInstances());
        if (instance != null)
            instances.add(instance);

        writeInstances((Instance[]) instances.toArray());
    }
}
