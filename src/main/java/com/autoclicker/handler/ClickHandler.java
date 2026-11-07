package com.autoclicker.handler;

import com.autoclicker.config.AutoClickerConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class ClickHandler {

    private boolean enabled = false;
    private long lastClickTime = 0L;
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

        if (mc.currentScreen != null) {
            return;
        }

        if (!enabled) {
            return;
        }

        if (hasTimedOut()) {
            setEnabled(false);
            return;
        }

        long now = System.currentTimeMillis();
        int interval = AutoClickerConfig.getClickIntervalMs();

        if (now - lastClickTime >= interval) {
            lastClickTime = now;
            performLeftClick(mc);
        }
    }

    private void performLeftClick(Minecraft mc) {
        MovingObjectPosition mop = mc.objectMouseOver;

        if (mop != null) {
            if (mop.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY) {
                if (mop.entityHit != null) {
                    mc.playerController.attackEntity(mc.thePlayer, mop.entityHit);
                }
            } else if (mop.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                BlockPos pos = mop.getBlockPos();
                EnumFacing side = mop.sideHit;

                if (pos != null && side != null && !mc.theWorld.isAirBlock(pos)) {
                    mc.playerController.clickBlock(pos, side);
                }
            }
        }

        mc.thePlayer.swingItem();
    }

    private boolean hasTimedOut() {
        if (!AutoClickerConfig.isTimeoutEnabled()) {
            return false;
        }

        long now = System.currentTimeMillis();
        long timeoutMs = AutoClickerConfig.getTimeoutDurationMs();

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
        lastClickTime = 0L;
        enabledAtTime = state ? System.currentTimeMillis() : 0L;
    }
}