package com.autoclicker;

import com.autoclicker.config.AutoClickerConfig;
import com.autoclicker.handler.BlockPlaceHandler;
import com.autoclicker.handler.ClickHandler;
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
    /** Replaced with the version from build.gradle at build time. */
    public static final String VERSION = "@VERSION@";

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        AutoClickerConfig.load(event.getSuggestedConfigurationFile());
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        ClickHandler clicker = new ClickHandler();
        BlockPlaceHandler placer = new BlockPlaceHandler();

        MinecraftForge.EVENT_BUS.register(clicker);
        MinecraftForge.EVENT_BUS.register(placer);
        MinecraftForge.EVENT_BUS.register(new KeyBindHandler(clicker, placer));
    }
}
