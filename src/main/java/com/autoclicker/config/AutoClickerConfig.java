package com.autoclicker.config;

import net.minecraftforge.common.config.Configuration;

import java.io.File;

/** Reads config/autoclicker.cfg once at startup. Forge appends each value's range and default to its comment. */
public final class AutoClickerConfig {

    private static final String CATEGORY = Configuration.CATEGORY_GENERAL;

    private static int toggleKey;
    private static int placeBlockKey;
    private static int clickIntervalMs;
    private static int placeIntervalMs;
    private static boolean timeoutEnabled;
    private static int timeoutMinutes;

    private AutoClickerConfig() {
    }

    public static void load(File file) {
        Configuration config = new Configuration(file);

        try {
            config.load();

            toggleKey = config.getInt("toggleKey", CATEGORY, 46, 1, 255,
                    "Default LWJGL key code for toggling the auto-clicker (46 = C).\n"
                            + "A key set in Options > Controls overrides this. Needs a restart.");

            placeBlockKey = config.getInt("placeBlockKey", CATEGORY, 47, 1, 255,
                    "Default LWJGL key code for toggling the auto-placer (47 = V).\n"
                            + "A key set in Options > Controls overrides this. Needs a restart.");

            clickIntervalMs = config.getInt("clickIntervalMs", CATEGORY, 500, 50, 60000,
                    "Milliseconds between auto-clicks (50 = 20 CPS, 500 = 2 CPS, 1000 = 1 CPS).");

            placeIntervalMs = config.getInt("placeIntervalMs", CATEGORY, 250, 50, 60000,
                    "Milliseconds between auto-place attempts (250 = 4 per second).\n"
                            + "Placement still follows vanilla rules: a valid block face, a usable held item, server checks.");

            timeoutEnabled = config.getBoolean("timeoutEnabled", CATEGORY, false,
                    "Turn the auto-clicker and auto-placer off automatically after timeoutMinutes.\n"
                            + "Each has its own timer, started when it is toggled on.");

            timeoutMinutes = config.getInt("timeoutMinutes", CATEGORY, 30, 1, 1440,
                    "Minutes before the timeout switches a feature off. Only used when timeoutEnabled=true.");
        } finally {
            config.save(); // always rewrite, so comments stay current
        }
    }

    public static int getToggleKey() {
        return toggleKey;
    }

    public static int getPlaceBlockKey() {
        return placeBlockKey;
    }

    public static int getClickIntervalMs() {
        return clickIntervalMs;
    }

    public static int getPlaceIntervalMs() {
        return placeIntervalMs;
    }

    public static boolean isTimeoutEnabled() {
        return timeoutEnabled;
    }

    public static int getTimeoutMinutes() {
        return timeoutMinutes;
    }

    public static long getTimeoutDurationMs() {
        return timeoutMinutes * 60_000L;
    }
}
