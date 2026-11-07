package com.autoclicker;

import com.autoclicker.config.AutoClickerConfig;
import com.autoclicker.keybind.KeyBindHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(
        modid = AutoClickerMod.MODID,
        name = AutoClickerMod.NAME,
        version = AutoClickerMod.VERSION,
        clientSideOnly = true
)
public class AutoClickerMod {

    public static final String MODID = "autoclicker";
    public static final String NAME = "AutoClicker";
    public static final String VERSION = "1.2.0";

    @Mod.Instance(MODID)
    public static AutoClickerMod instance;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        AutoClickerConfig.load(event.getSuggestedConfigurationFile());
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        KeyBindHandler.register();

        KeyBindHandler keyBindHandler = new KeyBindHandler();

        MinecraftForge.EVENT_BUS.register(keyBindHandler);
        MinecraftForge.EVENT_BUS.register(KeyBindHandler.getClickHandler());
        MinecraftForge.EVENT_BUS.register(KeyBindHandler.getBlockPlaceHandler());
    }
}