package com.example.SinLauncher.config;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public class Java {
    public String version;
    public String path;

    public Java(String version, String path) {
        this.version = version;
        this.path = path;
    }

    public static List<Java> getAvailableJavaInstallations() {
        List<Java> cups = new ArrayList<>();

        // TODO!: FACTOR INTO SMALLER FUNCTIONS, DIFFERENT FUNCTIONS FOR DIFFERENT SYSTEM

        // Check JAVA_HOME environment var
        String javaHome = System.getenv("JAVA_HOME");

        if (javaHome != null) {
            File javaHomeDir = new File(javaHome);

            if (javaHomeDir.exists() && javaHomeDir.isDirectory()) {
                Path path = Paths.get(javaHomeDir.getAbsolutePath(), "bin", "java.exe");

                if (Files.exists(path))
                    cups.add(new Java("", path.toString()));
            }
        }

        // Check common installation directories
        String systemDrive = System.getenv("SystemDrive");

        File[] directories = {
            new File(systemDrive + "/Program Files/Java"),
            new File(systemDrive + "/Program Files (x86)/Java")
        };

        for (File dir : directories) {
            if (dir == null)
                continue;

            File[] files = dir.listFiles();

            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        Path path = Paths.get(file.getAbsolutePath(), "bin", "java.exe");

                        if (Files.exists(path))
                            cups.add(new Java("DIR", path.toString()));
                    }
                }
            }
        }

        // Check PATH environment variable
        String path = System.getenv("PATH");
        if (path != null) {
            for (String p : path.split(File.pathSeparator)) {
                File javaFile = new File(p, "java.exe");

                if (javaFile.exists()) {
                    cups.add(new Java("ENV", javaFile.getAbsolutePath()));
                }
            }
        }

        // Check registry
        Preferences runtime = Preferences.userRoot().node("Software/JavaSoft/Java Runtime Environment");
        Preferences jdk = Preferences.userRoot().node("Software/JavaSoft/Java Development Kit");

        List<Java> runtimes = search_reg(runtime);
        List<Java> jdks = search_reg(jdk);

        cups.addAll(runtimes);
        cups.addAll(jdks);

        return cups;
    }

    public static List<Java> search_reg(Preferences node) {
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
