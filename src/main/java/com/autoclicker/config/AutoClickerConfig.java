package com.autoclicker.config;

import net.minecraftforge.common.config.Configuration;

import java.io.File;

public class AutoClickerConfig {

    private static Configuration config;

    private static final String CATEGORY_GENERAL = "general";

    private static final String KEY_TOGGLE_KEY = "toggleKey";
    private static final String KEY_PLACE_BLOCK_KEY = "placeBlockKey";
    private static final String KEY_CLICK_INTERVAL = "clickIntervalMs";
    private static final String KEY_PLACE_INTERVAL = "placeIntervalMs";
    private static final String KEY_TIMEOUT_ENABLED = "timeoutEnabled";
    private static final String KEY_TIMEOUT_MINUTES = "timeoutMinutes";

    private static final int DEFAULT_TOGGLE_KEY = 46;        // LWJGL Keyboard.KEY_C
    private static final int DEFAULT_PLACE_BLOCK_KEY = 47;   // LWJGL Keyboard.KEY_V
    private static final int DEFAULT_CLICK_INTERVAL = 500;   // milliseconds
    private static final int DEFAULT_PLACE_INTERVAL = 250;   // milliseconds
    private static final boolean DEFAULT_TIMEOUT_ENABLED = false;
    private static final int DEFAULT_TIMEOUT_MINUTES = 30;

    private static int toggleKey;
    private static int placeBlockKey;
    private static int clickIntervalMs;
    private static int placeIntervalMs;
    private static boolean timeoutEnabled;
    private static int timeoutMinutes;

    public static void load(File configFile) {
        config = new Configuration(configFile);

        try {
            config.load();

            toggleKey = config.getInt(
                    KEY_TOGGLE_KEY,
                    CATEGORY_GENERAL,
                    DEFAULT_TOGGLE_KEY,
                    1,
                    255,
                    "LWJGL Keyboard key code that toggles auto-clicking on/off.\n" +
                            "Default: 46, which is C.\n" +
                            "Common codes: 46=C, 47=V, 19=R, 25=P, 41=GRAVE/TILDE.\n" +
                            "Changing this value requires a game restart because keybinds are registered at startup."
            );

            placeBlockKey = config.getInt(
                    KEY_PLACE_BLOCK_KEY,
                    CATEGORY_GENERAL,
                    DEFAULT_PLACE_BLOCK_KEY,
                    1,
                    255,
                    "LWJGL Keyboard key code that toggles repeated held-item placement/use on/off.\n" +
                            "Default: 47, which is V.\n" +
                            "Works with normal blocks and right-click-on-block items such as seeds, carrots, potatoes, and bonemeal.\n" +
                            "Your crosshair must target a valid block face and no GUI may be open.\n" +
                            "Changing this value requires a game restart because keybinds are registered at startup."
            );

            clickIntervalMs = config.getInt(
                    KEY_CLICK_INTERVAL,
                    CATEGORY_GENERAL,
                    DEFAULT_CLICK_INTERVAL,
                    50,
                    60000,
                    "Interval between auto-click attempts in milliseconds.\n" +
                            "Allowed range: 50 to 60000.\n" +
                            "Examples: 50=20 CPS, 100=10 CPS, 250=4 CPS, 500=2 CPS, 1000=1 CPS."
            );

            placeIntervalMs = config.getInt(
                    KEY_PLACE_INTERVAL,
                    CATEGORY_GENERAL,
                    DEFAULT_PLACE_INTERVAL,
                    50,
                    60000,
                    "Interval between auto-place attempts in milliseconds.\n" +
                            "Allowed range: 50 to 60000.\n" +
                            "Examples: 50=20 attempts/sec, 100=10/sec, 250=4/sec, 500=2/sec, 1000=1/sec.\n" +
                            "Placement still obeys vanilla rules: valid target block, valid side, item usability, server checks, and stack availability."
            );

            timeoutEnabled = config.getBoolean(
                    KEY_TIMEOUT_ENABLED,
                    CATEGORY_GENERAL,
                    DEFAULT_TIMEOUT_ENABLED,
                    "If true, AutoClicker and AutoPlacer automatically turn themselves off after timeoutMinutes.\n" +
                            "Each feature has its own timer that starts when that feature is toggled on."
            );

            timeoutMinutes = config.getInt(
                    KEY_TIMEOUT_MINUTES,
                    CATEGORY_GENERAL,
                    DEFAULT_TIMEOUT_MINUTES,
                    1,
                    1440,
                    "How long AutoClicker or AutoPlacer may run before automatically turning off, in minutes.\n" +
                            "Only used when timeoutEnabled=true.\n" +
                            "Allowed range: 1 to 1440 minutes."
            );
        } finally {
            config.save();
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

    public static void setToggleKey(int key) {
        toggleKey = key;

        if (config != null) {
            config.get(CATEGORY_GENERAL, KEY_TOGGLE_KEY, DEFAULT_TOGGLE_KEY).set(key);
            config.save();
        }
    }

    public static void setPlaceBlockKey(int key) {
        placeBlockKey = key;

        if (config != null) {
            config.get(CATEGORY_GENERAL, KEY_PLACE_BLOCK_KEY, DEFAULT_PLACE_BLOCK_KEY).set(key);
            config.save();
        }
    }

    public static void setClickIntervalMs(int ms) {
        clickIntervalMs = ms;

        if (config != null) {
            config.get(CATEGORY_GENERAL, KEY_CLICK_INTERVAL, DEFAULT_CLICK_INTERVAL).set(ms);
            config.save();
        }
    }

    public static void setPlaceIntervalMs(int ms) {
        placeIntervalMs = ms;

        if (config != null) {
            config.get(CATEGORY_GENERAL, KEY_PLACE_INTERVAL, DEFAULT_PLACE_INTERVAL).set(ms);
            config.save();
        }
    }

    public static void setTimeoutEnabled(boolean enabled) {
        timeoutEnabled = enabled;

        if (config != null) {
            config.get(CATEGORY_GENERAL, KEY_TIMEOUT_ENABLED, DEFAULT_TIMEOUT_ENABLED).set(enabled);
            config.save();
        }
    }

    public static void setTimeoutMinutes(int minutes) {
        timeoutMinutes = minutes;

        if (config != null) {
            config.get(CATEGORY_GENERAL, KEY_TIMEOUT_MINUTES, DEFAULT_TIMEOUT_MINUTES).set(minutes);
            config.save();
        }
    }
}