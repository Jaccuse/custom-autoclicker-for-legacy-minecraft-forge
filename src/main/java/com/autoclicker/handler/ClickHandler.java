package com.autoclicker.handler;

import com.autoclicker.config.AutoClickerConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;

/** Repeated left click: attacks the targeted entity or hits the targeted block. */
public class ClickHandler extends RepeatingAction {

    @Override
    protected int getIntervalMs() {
        return AutoClickerConfig.getClickIntervalMs();
    }

    @Override
    protected void run(Minecraft mc) {
        // Same order as vanilla Minecraft#clickMouse: swing first, then act on the target.
        mc.thePlayer.swingItem();

        MovingObjectPosition target = mc.objectMouseOver;

        if (target == null) {
            return;
        }

        if (target.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY) {
            mc.playerController.attackEntity(mc.thePlayer, target.entityHit);
        } else if (target.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
            BlockPos pos = target.getBlockPos();

            if (!mc.theWorld.isAirBlock(pos)) {
                mc.playerController.clickBlock(pos, target.sideHit);
            }
        }
    }
}
