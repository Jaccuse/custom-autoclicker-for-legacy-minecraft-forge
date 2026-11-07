package com.autoclicker.handler;

import com.autoclicker.config.AutoClickerConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class BlockPlaceHandler {

    public enum PlaceResult {
        SUCCESS,
        NOT_READY,
        SCREEN_OPEN,
        NO_HELD_ITEM,
        NO_BLOCK_TARGET,
        INVALID_TARGET,
        VANILLA_REJECTED
    }

    private boolean enabled = false;
    private long lastPlaceTime = 0L;
    private long enabledAtTime = 0L;

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.START) {
            return;
        }

        Minecraft mc = Minecraft.getMinecraft();

        if (mc == null || mc.thePlayer == null || mc.theWorld == null || mc.playerController == null) {
            return;
        }

        if (!enabled) {
            return;
        }

        if (hasTimedOut()) {
            setEnabled(false);
            return;
        }

        if (mc.currentScreen != null) {
            return;
        }

        long now = System.currentTimeMillis();
        int interval = AutoClickerConfig.getPlaceIntervalMs();

        if (now - lastPlaceTime >= interval) {
            lastPlaceTime = now;
            tryPlaceHeldItemOnTargetBlock(mc);
        }
    }

    public PlaceResult placeOnceNow() {
        lastPlaceTime = System.currentTimeMillis();

        Minecraft mc = Minecraft.getMinecraft();

        if (mc == null || mc.thePlayer == null || mc.theWorld == null || mc.playerController == null) {
            return PlaceResult.NOT_READY;
        }

        return tryPlaceHeldItemOnTargetBlock(mc);
    }

    private PlaceResult tryPlaceHeldItemOnTargetBlock(Minecraft mc) {
        if (mc.currentScreen != null) {
            return PlaceResult.SCREEN_OPEN;
        }

        ItemStack heldStack = mc.thePlayer.getHeldItem();

        if (heldStack == null) {
            return PlaceResult.NO_HELD_ITEM;
        }

        MovingObjectPosition mop = mc.objectMouseOver;

        if (mop == null || mop.typeOfHit != MovingObjectPosition.MovingObjectType.BLOCK) {
            return PlaceResult.NO_BLOCK_TARGET;
        }

        BlockPos pos = mop.getBlockPos();
        EnumFacing side = mop.sideHit;
        Vec3 hitVec = mop.hitVec;

        if (pos == null || side == null || hitVec == null) {
            return PlaceResult.INVALID_TARGET;
        }

        /*
         * This is the same public controller path used for vanilla right-click-on-block.
         *
         * Important:
         * - Do not require ItemBlock here.
         * - Seeds, carrots, potatoes, bonemeal, etc. are right-click-on-block items,
         *   not necessarily ItemBlock instances.
         */
        boolean handled = mc.playerController.onPlayerRightClick(
                mc.thePlayer,
                mc.theWorld,
                heldStack,
                pos,
                side,
                hitVec
        );

        if (!handled) {
            return PlaceResult.VANILLA_REJECTED;
        }

        mc.thePlayer.swingItem();
        return PlaceResult.SUCCESS;
    }

    private boolean hasTimedOut() {
        if (!AutoClickerConfig.isTimeoutEnabled()) {
            return false;
        }

        long timeoutMs = AutoClickerConfig.getTimeoutDurationMs();
        long now = System.currentTimeMillis();

        return enabledAtTime > 0L && now - enabledAtTime >= timeoutMs;
    }

    public void toggle() {
        setEnabled(!enabled);
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean state) {
        enabled = state;
        lastPlaceTime = 0L;
        enabledAtTime = state ? System.currentTimeMillis() : 0L;
    }
}