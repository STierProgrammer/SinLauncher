package com.example.SinLauncher.SinLauncherEntites;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.example.SinLauncher.App;
import com.example.SinLauncher.config.Config;
import com.example.SinLauncher.json.Manifest;
import com.example.SinLauncher.json.Manifest.Version;

import kong.unirest.core.HttpResponse;
import kong.unirest.core.Unirest;

public class Instance {
    public static class InstanceNotFoundException extends Exception {
        public String name;

        public InstanceNotFoundException(String message, String name) {
            super(message);
            this.name = name;
        }
    }

    public static class InstanceAlreadyExistsException extends Exception {
        public String name;

        public InstanceAlreadyExistsException(String message, String name) {
            super(message);
            this.name = name;
        }
    }

    public static class InvaildInstanceVersionException extends Exception {
        public String version;

        public InvaildInstanceVersionException(String message, String version) {
            super(message);
            this.version = version;
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

    public Path Dir() {
        return Paths.get(PARENT_DIR.toString(), this.name);
    }

    /**
     * creates instance dir if it doesn't exist
     */
    public void initDir() throws IOException {
        if (!Files.exists(this.Dir()))
            Files.createDirectory(this.Dir());
    }

    /**
     * creates a new vaild instance,
     * creates it's folder, downloads it's client.json, serializes and appends it to
     * instances.json then returns it
     * 
     * @throws InvaildInstanceVersionException if manifest doesn't contain the
     *                                         provided {@code version}
     * @throws IOException                     if failed to fetch client.json from
     *                                         the manifest or failed to append
     *                                         instance to instances.json
     */
    // FIXME: there is a bug in here which you will never see, expect if you were me
    public static Instance createInstance(String name, String version)
            throws InstanceAlreadyExistsException, InvaildInstanceVersionException, IOException {
        Instance instance = new Instance(name, version);

        Manifest manifest = Manifest.readManifest();

        String url = null;
        for (Version man_version : manifest.versions) {
            if (man_version.id.equals(version)) {
                url = man_version.url;
                break;
            }
        }

        if (url == null)
            throw new InvaildInstanceVersionException("unexpected version while creating instance '" + version + '\'',
                    version);

        instance.initDir();
        Path client_path = Paths.get(instance.Dir().toString(), "client.json");

        HttpResponse<String> client = Unirest.get(url).asString();

        if (client.getStatus() == 200)
            Files.write(client_path, client.getBody().getBytes());
        else
            throw new IOException("Failed to fetch client.json for version '" + version + '\'');

        addInstance(instance);
        return instance;
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
     * appends serialized {@code new_instance} to instances.json
     */
    public static void addInstance(Instance new_instance) throws InstanceAlreadyExistsException, IOException {
        List<Instance> instances = new ArrayList<Instance>(Arrays.asList(readInstances()));

        for (Instance instance : instances) {
            if (instance.name.equals(new_instance.name)) {
                throw new InstanceAlreadyExistsException(
                        "failed to add instance '" + new_instance.name + '\''
                                + ", because there is an instance with the same name...",
                        new_instance.name);
            }
        }

        if (new_instance != null)
            instances.add(new_instance);

        writeInstances(instances.toArray(new Instance[0]));
    }

    @Override
    public String toString() {
        return this.name + ": " + this.version;
    }
}
