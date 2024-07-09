package com.example.SinLauncher.config;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import com.example.SinLauncher.App;
import com.example.SinLauncher.SinLauncherClasses.Os;

public class Java {
    public String version;
    public String path;

    public Java(String version, String path) {
        this.version = version;
        this.path = path;
    }

    public static List<Java> getAvailableJavaCups() {
        List<Java> cups = new ArrayList<>();

        switch (App.OS) {
            case Windows -> cups.addAll(getCommonWindowsCups());
            case Linux -> cups.addAll(getCommonLinuxCups());
            default -> {}
        }

        cups.addAll(getPATHCups());

        if (App.OS == Os.Windows) {
            // Check registry
            Preferences runtime = Preferences.userRoot().node("Software/JavaSoft/Java Runtime Environment");
            Preferences jdk = Preferences.userRoot().node("Software/JavaSoft/Java Development Kit");

            List<Java> runtimes = getRegCups(runtime);
            List<Java> jdks = getRegCups(jdk);

            cups.addAll(runtimes);
            cups.addAll(jdks);
        }

        return cups;
    }

    public static Java getJavaHomeCup() {
        Java home = null;
        String javaHome = System.getenv("JAVA_HOME");

        if (javaHome != null) {
            File javaHomeDir = new File(javaHome);

            if (javaHomeDir.exists() && javaHomeDir.isDirectory()) {
                String binary = "java.exe";

                if (App.OS == Os.Linux) {
                    binary = "java";
                }

                Path path = Paths.get(javaHomeDir.getAbsolutePath(), "bin", binary);

                if (Files.exists(path))
                    home = new Java("", path.toString());
            }
        }
        return home;
    }

    public static List<Java> getCommonLinuxCups() {
        File[] directories = {
            new File("/usr/lib/jvm"),
        };
        return getCupsInDirs(directories);
    }

    public static List<Java> getCommonWindowsCups() {
        String systemDrive = System.getenv("SystemDrive");

        File[] directories = {
            new File(systemDrive + "/Program Files/Java"),
            new File(systemDrive + "/Program Files (x86)/Java")
        };


        return getCupsInDirs(directories);
    }

    public static List<Java> getCupsInDirs(File[] directories) {
        List<Java> cups = new ArrayList<>();

        for (File dir : directories) {
            if (dir == null)
                continue;

            findJavaBinaries(dir, cups);
        }

        return cups;
    }

    private static void findJavaBinaries(File dir, List<Java> cups) {
        File[] files = dir.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    String binary = "java.exe";
                    if (App.OS == Os.Linux)
                        binary = "java";

                    Path path = Paths.get(file.getAbsolutePath(), "bin", binary);

                    if (Files.exists(path))
                        cups.add(new Java("DIR", path.toString()));
                    else
                        findJavaBinaries(file, cups);
                }
            }
        }
    }

    public static List<Java> getPATHCups() {
        List<Java> cups = new ArrayList<>();

        // Check PATH environment variable
        String path = System.getenv("PATH");
        if (path != null) {
            for (String p : path.split(File.pathSeparator)) {
                String binary = "java.exe";

                if (App.OS == Os.Linux) {
                    binary = "java";
                }

                File javaFile = new File(p, binary);
                if (javaFile.exists()) {
                    cups.add(new Java("ENV", javaFile.getAbsolutePath()));
                }
            }
        }
        return cups;
    }

    public static List<Java> getRegCups(Preferences node) {
        List<Java> cups = new ArrayList<>();
        try {
            String[] children = node.childrenNames();

            for (String child : children) {
                if (child.equals("CurrentVersion"))
                    continue;
                Preferences childNode = node.node(child);
                String javaHomeDir = childNode.get("JavaHome", "");

                if (javaHomeDir != null && !javaHomeDir.isEmpty()) {
                    Path javaPath = Paths.get(javaHomeDir, "bin", "java.exe");

                    if (Files.exists(javaPath))
                        cups.add(new Java("reg" + child, javaPath.toString())); // TODO! REMOVE REG IT IS JUST TO CHECK IF IT IS WORKING
                }
            }
        }
        catch(BackingStoreException _e) {}

        return cups;
    }
}
