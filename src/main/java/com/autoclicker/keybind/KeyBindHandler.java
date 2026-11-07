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

    private static KeyBinding toggleKeyBind;
    private static KeyBinding placeBlockKeyBind;

    private static final ClickHandler clickHandler = new ClickHandler();
    private static final BlockPlaceHandler blockPlaceHandler = new BlockPlaceHandler();

    public static void register() {
        toggleKeyBind = new KeyBinding(
                "Toggle AutoClicker",
                AutoClickerConfig.getToggleKey(),
                "AutoClicker"
        );

        placeBlockKeyBind = new KeyBinding(
                "Toggle AutoPlacer",
                AutoClickerConfig.getPlaceBlockKey(),
                "AutoClicker"
        );

        ClientRegistry.registerKeyBinding(toggleKeyBind);
        ClientRegistry.registerKeyBinding(placeBlockKeyBind);
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        Minecraft mc = Minecraft.getMinecraft();

        if (toggleKeyBind.isPressed()) {
            clickHandler.toggle();

            String status = clickHandler.isEnabled()
                    ? EnumChatFormatting.GREEN + "ON"
                    : EnumChatFormatting.RED + "OFF";

            float cps = 1000.0f / AutoClickerConfig.getClickIntervalMs();

            String msg = EnumChatFormatting.GOLD + "[AutoClicker] "
                    + EnumChatFormatting.WHITE + "Toggled "
                    + status
                    + EnumChatFormatting.GRAY + " | "
                    + EnumChatFormatting.WHITE + "Interval: "
                    + AutoClickerConfig.getClickIntervalMs() + "ms"
                    + EnumChatFormatting.GRAY + " (~"
                    + String.format("%.1f", cps) + " CPS)"
                    + getTimeoutChatSuffix();

            sendChat(mc, msg);
        }

        if (placeBlockKeyBind.isPressed()) {
            blockPlaceHandler.toggle();

            String status = blockPlaceHandler.isEnabled()
                    ? EnumChatFormatting.GREEN + "ON"
                    : EnumChatFormatting.RED + "OFF";

            float aps = 1000.0f / AutoClickerConfig.getPlaceIntervalMs();

            String msg = EnumChatFormatting.GOLD + "[AutoPlacer] "
                    + EnumChatFormatting.WHITE + "Toggled "
                    + status
                    + EnumChatFormatting.GRAY + " | "
                    + EnumChatFormatting.WHITE + "Interval: "
                    + AutoClickerConfig.getPlaceIntervalMs() + "ms"
                    + EnumChatFormatting.GRAY + " (~"
                    + String.format("%.1f", aps) + " attempts/sec)"
                    + getTimeoutChatSuffix();

            if (blockPlaceHandler.isEnabled()) {
                BlockPlaceHandler.PlaceResult result = blockPlaceHandler.placeOnceNow();

                msg += EnumChatFormatting.GRAY + " | "
                        + EnumChatFormatting.WHITE + "First attempt: "
                        + formatPlaceResult(result);
            }

            sendChat(mc, msg);
        }
    }

    private String formatPlaceResult(BlockPlaceHandler.PlaceResult result) {
        if (result == BlockPlaceHandler.PlaceResult.SUCCESS) {
            return EnumChatFormatting.GREEN + "SUCCESS";
        }

        return EnumChatFormatting.RED + result.name();
    }

    private String getTimeoutChatSuffix() {
        if (!AutoClickerConfig.isTimeoutEnabled()) {
            return EnumChatFormatting.GRAY + " | "
                    + EnumChatFormatting.WHITE + "Timeout: "
                    + EnumChatFormatting.RED + "OFF";
        }

        return EnumChatFormatting.GRAY + " | "
                + EnumChatFormatting.WHITE + "Timeout: "
                + EnumChatFormatting.GREEN + AutoClickerConfig.getTimeoutMinutes() + "m";
    }

    private void sendChat(Minecraft mc, String msg) {
        if (mc != null && mc.thePlayer != null) {
            mc.thePlayer.addChatMessage(new ChatComponentText(msg));
        }
    }

    public static ClickHandler getClickHandler() {
        return clickHandler;
    }

    public static BlockPlaceHandler getBlockPlaceHandler() {
        return blockPlaceHandler;
    }
}