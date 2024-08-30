// App.java

package com.example.SinLauncher;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import com.example.SinLauncher.SinLauncherEntites.Arch;
import com.example.SinLauncher.SinLauncherEntites.Instance;
import com.example.SinLauncher.SinLauncherEntites.Os;
import com.example.SinLauncher.SinLauncherEntites.Instance.InstanceAlreadyExistsException;
import com.example.SinLauncher.SinLauncherEntites.Instance.InvaildInstanceVersionException;
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
    public static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(Client.Argument.class, new Client.ArgumentDeserializer()).create();

    public static final String DIR;
    public static final Path ASSETS_DIR;
    public static final Path LIBRARIES_DIR;
    public static final Path NATIVES_DIR;

    public static final Os OS;
    public static final Arch ARCH;

    public static Config CONFIG;

    static {
        String os = System.getProperty("os.name").toLowerCase();

        if (os.contains("win")) {
            DIR = System.getenv("APPDATA") + "\\SinLauncher";
            OS = Os.Windows;
        } else if (os.contains("nix") || os.contains("nux") || os.contains("aix")) {
            DIR = System.getProperty("user.home") + "/.sinlauncher";
            OS = Os.Linux;
        } else {
            DIR = "SinLauncher";
            OS = Os.Linux;
        }

        String arch = System.getProperty("os.arch").toLowerCase();

        if (arch == "amd64")
            ARCH = Arch.X86_64;

        else if (arch == "aarch64")
            ARCH = Arch.Arm64;

        else if (arch == "arm")
            ARCH = Arch.Arm;

        else if (arch == "x86")
            ARCH = Arch.X86;

        else
            ARCH = Arch.X86_64;

        ASSETS_DIR = Paths.get(DIR, "assets");
        LIBRARIES_DIR = Paths.get(DIR, "libraries");
        NATIVES_DIR = Paths.get(DIR, "natives");

        try {
            App.initialize();
        } catch (IOException e) {
            LOGGER.info("Failed to initialize the Launcher");
        }

        CONFIG = Config.readConfig();
    }

    static void initializeInstances() throws IOException {
        if (!Files.exists(Instance.PARENT_DIR))
            Files.createDirectories(Instance.PARENT_DIR);

        if (!Files.exists(Instance.INSTANCES_FILE)) {
            Path file = Files.createFile(Instance.INSTANCES_FILE);

            Files.writeString(file, "[]");
        }
    }

    static void initializeLauncherDir() throws IOException {

        if (!Files.exists(Paths.get(DIR)))
            Files.createDirectories(Paths.get(DIR));

        if (!Files.exists(ASSETS_DIR))
            Files.createDirectories(ASSETS_DIR);

        if (!Files.exists(LIBRARIES_DIR))
            Files.createDirectories(LIBRARIES_DIR);

        App.initializeInstances();

        HttpResponse<String> response = Unirest
                .get("https://launchermeta.mojang.com/mc/game/version_manifest.json")
                .asString();

        if (response.getStatus() == 200)
            Files.write(
                    Manifest.PATH,
                    response
                            .getBody()
                            .getBytes());

        else {
            if (!Files.exists(Manifest.PATH))
                throw new IOException("Failed to fetch Manifest.JSON; Response code: " + response.getStatus());
        }
    }

    public static void initialize() throws IOException {
        initializeLauncherDir();

        LOGGER.info("Launcher initialized!");
    }

    public static void intallationManager(String installationName, String version) throws IOException {
        try {
            Instance.createInstance(installationName, version);

            Instance createdInstance = Instance.getInstance(installationName);

            SwingUtilities.invokeLater(() -> {
                int dialogResult = JOptionPane.showConfirmDialog(null,
                        "Warning: 1GB of data is about to be installed! Do you want to continue?",
                        "Confirm Installation", JOptionPane.YES_NO_OPTION);

                if (dialogResult == JOptionPane.YES_OPTION) {
                    try {
                        createdInstance.install();
                        JOptionPane.showMessageDialog(null,
                                "Installation is complete!",
                                "Installation Complete",
                                JOptionPane.INFORMATION_MESSAGE);
                    } catch (IOException e) {
                        LOGGER.info(e.getLocalizedMessage());
                    }
                } else {
                    JOptionPane.showMessageDialog(null,
                            "Installation has been cancelled!",
                            "Installation Cancelled",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            });

        } catch (InstanceAlreadyExistsException e) {
            LOGGER.info(e.getMessage());

            SwingUtilities.invokeLater(() -> {
                Instance existingInstance = Instance.getInstance(installationName);

                if (existingInstance != null) {
                    try {
                        existingInstance.install();
                        existingInstance.launch();

                        JOptionPane.showMessageDialog(null,
                                "Instance has been launched successfully!",
                                "Instance launched",
                                JOptionPane.INFORMATION_MESSAGE);
                    } catch (IOException __e) {
                        LOGGER.info(__e.getMessage());
                    }
                } else {
                    System.out.println("Failed to retrieve the instance for launching.");
                }
            });

        } catch (InvaildInstanceVersionException e) {
            LOGGER.info(e.getMessage());
        }
    }

    public static void launchingManager(String installationName) throws IOException {
        Instance existingInstance = Instance.getInstance(installationName);

        if (existingInstance != null) {
            try {
                existingInstance.launch();
            } catch (IOException ___e) {
                LOGGER.info(___e.getMessage());
            }
        }
    }

    public static void Debugging(String InstanceName) throws IOException {
        System.out.println("Instances: ");

        for (Instance instance : Instance.readInstances())
            System.out.println(instance.toString());

        Instance testInstance = Instance.getInstance(InstanceName);

        Client client = testInstance.readClient();

        System.out.println(GSON.toJson(client));
    }

    public static final Path CURRENT_USER_FILE = Paths.get(App.DIR, "currentUser.txt");

    public static String currentUser;

    static String initializeCurrentUser() throws IOException {
        if (Files.exists(CURRENT_USER_FILE) && Files.readString(CURRENT_USER_FILE).length() <= 16
                && Files.readString(CURRENT_USER_FILE).length() >= 3)
            return Files.readString(CURRENT_USER_FILE).trim();
        else {
            String defaultUser = "Dev001";

            Files.write(
                    CURRENT_USER_FILE,
                    defaultUser.getBytes(StandardCharsets.UTF_8),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING);

            return defaultUser;
        }
    }

    public static String setCurrentUser(String username) throws IOException {
        if (username.length() >= 3 && username.length() <= 16) {
            Files.write(CURRENT_USER_FILE,
                    username.getBytes(StandardCharsets.UTF_8),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING);

            currentUser = username;
        } else {
            String DummyUser = "Dev001";

            Files.write(
                    CURRENT_USER_FILE,
                    DummyUser.getBytes(StandardCharsets.UTF_8),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING);

        }

        return username;

    }

    public static void main(String[] args) {
        try {
            Manifest manifest = Manifest.readManifest();

            System.out.println(CONFIG.max_ram);
            System.out.println(manifest.latest.release);

            var cups = Java.getAvailableJavaCups();

            for (Java cup : cups)
                System.out.println(cup.version + ": " + cup.path);

            System.out.println(currentUser);

            try {
                currentUser = initializeCurrentUser();
            } catch (IOException e) {
                App.LOGGER.info("Failed to initialize the current user");
            }

            intallationManager("NewNameTest1", "1.21");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Exception: ", e);
        }
    }
}
