package com.autoclicker.keybind;

import com.autoclicker.config.AutoClickerConfig;
import com.autoclicker.handler.BlockPlaceHandler;
import com.autoclicker.handler.ClickHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

public class KeyBindHandler {

    private static final String CATEGORY = "AutoClicker";

    private final KeyBinding clickKey = new KeyBinding("Toggle AutoClicker", AutoClickerConfig.getToggleKey(), CATEGORY);
    private final KeyBinding placeKey = new KeyBinding("Toggle AutoPlacer", AutoClickerConfig.getPlaceBlockKey(), CATEGORY);

    private final ClickHandler clicker;
    private final BlockPlaceHandler placer;

    public KeyBindHandler(ClickHandler clicker, BlockPlaceHandler placer) {
        this.clicker = clicker;
        this.placer = placer;
        ClientRegistry.registerKeyBinding(clickKey);
        ClientRegistry.registerKeyBinding(placeKey);
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        Minecraft mc = Minecraft.getMinecraft();

        // Key presses are counted even while a GUI (chat, inventory, menus) is open or no world is loaded.
        // Discard those so typing a "c" in chat doesn't toggle anything.
        if (mc.thePlayer == null || mc.currentScreen != null) {
            while (clickKey.isPressed() || placeKey.isPressed()) {
                // drain
            }
            return;
        }

        if (clickKey.isPressed()) {
            clicker.toggle();
            send(mc, status("AutoClicker", clicker.isEnabled(), AutoClickerConfig.getClickIntervalMs(), "CPS"));
        }

        if (placeKey.isPressed()) {
            placer.toggle();
            String msg = status("AutoPlacer", placer.isEnabled(), AutoClickerConfig.getPlaceIntervalMs(), "attempts/sec");

            if (placer.isEnabled()) {
                BlockPlaceHandler.PlaceResult result = placer.placeOnceNow();
                EnumChatFormatting color = result == BlockPlaceHandler.PlaceResult.SUCCESS
                        ? EnumChatFormatting.GREEN
                        : EnumChatFormatting.RED;
                msg += separator() + "First attempt: " + color + result.name();
            }

            send(mc, msg);
        }
    }

    private static String status(String name, boolean enabled, int intervalMs, String rateUnit) {
        String state = enabled ? EnumChatFormatting.GREEN + "ON" : EnumChatFormatting.RED + "OFF";
        String timeout = AutoClickerConfig.isTimeoutEnabled()
                ? EnumChatFormatting.GREEN + "" + AutoClickerConfig.getTimeoutMinutes() + "m"
                : EnumChatFormatting.RED + "OFF";

        return EnumChatFormatting.GOLD + "[" + name + "] "
                + EnumChatFormatting.WHITE + "Toggled " + state
                + separator() + "Interval: " + intervalMs + "ms"
                + EnumChatFormatting.GRAY + String.format(" (~%.1f %s)", 1000.0f / intervalMs, rateUnit)
                + separator() + "Timeout: " + timeout;
    }

    private static String separator() {
        return EnumChatFormatting.GRAY + " | " + EnumChatFormatting.WHITE;
    }

    private static void send(Minecraft mc, String msg) {
        mc.thePlayer.addChatMessage(new ChatComponentText(msg));
    }
}
