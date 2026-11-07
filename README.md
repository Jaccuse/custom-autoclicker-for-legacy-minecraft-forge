# AutoClicker for Minecraft Forge 1.8.9

A small client-side Forge mod for Minecraft 1.8.9 with two toggleable helpers:

- **AutoClicker** (default key **C**): left-clicks at a fixed interval, attacking the entity or hitting the block under your crosshair.
- **AutoPlacer** (default key **V**): right-clicks your held item on the targeted block face at a fixed interval. It works with blocks and with items you use on a block, such as seeds, carrots, potatoes and bonemeal.

Both pause while a GUI is open and report their state in chat when toggled. An optional timeout turns each one off after a set number of minutes.

> Many multiplayer servers forbid auto-clickers. Use it in singleplayer or where it's allowed.

## Configuration

Settings live in `config/autoclicker.cfg` and are created on first launch:

| Key | Default | Range | Notes |
| --- | --- | --- | --- |
| `toggleKey` | 46 (C) | 1–255 | Default LWJGL key code; restart required |
| `placeBlockKey` | 47 (V) | 1–255 | Default LWJGL key code; restart required |
| `clickIntervalMs` | 500 | 50–60000 | 500 ms ≈ 2 clicks/sec |
| `placeIntervalMs` | 250 | 50–60000 | 250 ms ≈ 4 attempts/sec |
| `timeoutEnabled` | false | | Auto-off after `timeoutMinutes` |
| `timeoutMinutes` | 30 | 1–1440 | Separate timer per feature |

You can also rebind both keys in-game under **Controls → AutoClicker**. A key set there takes priority over the config values.

## Building

ForgeGradle 2.1 / Gradle 2.7 only runs on **Java 8**.

```
gradlew.bat build
```

The jar is written to `build/libs/AutoClicker-<version>.jar`. Drop it into your `.minecraft/mods` folder alongside Forge 1.8.9 (11.15.1.2318).

`gradlew.bat` automatically uses `C:\Program Files\Java\jdk1.8.0_181` when it exists. Otherwise, point `JAVA_HOME` at a JDK 8 before building.

## License

[MIT](LICENSE). Minecraft Forge itself is licensed separately and is not included in this repository.
