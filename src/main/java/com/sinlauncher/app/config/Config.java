package com.sinlauncher.app.config;

import java.lang.management.ManagementFactory;

import com.sinlauncher.app.App;
import com.sun.management.OperatingSystemMXBean;

public class Config {
    public long MIN_RAM = App.CONFIG.MIN_RAM;
    public long MAX_RAM = App.CONFIG.MAX_RAM;
    public Java JAVA = App.CONFIG.JAVA;

    public Config() {
        OperatingSystemMXBean os = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
        long total = os.getTotalMemorySize();

        this.MIN_RAM = total / 6;
        this.MAX_RAM = total / 4;

        this.JAVA = null;
    }
}
