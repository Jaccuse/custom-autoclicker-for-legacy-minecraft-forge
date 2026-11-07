package com.autoclicker.handler;

import com.autoclicker.config.AutoClickerConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;

/**
 * Repeated right click of the held item on the targeted block face.
 * Uses the vanilla right-click-on-block path, so it works for blocks and for items such as seeds or bonemeal.
 */
public class BlockPlaceHandler extends RepeatingAction {

    public enum PlaceResult {
        SUCCESS,
        NO_HELD_ITEM,
        NO_BLOCK_TARGET,
        VANILLA_REJECTED
    }

    @Override
    protected int getIntervalMs() {
        return AutoClickerConfig.getPlaceIntervalMs();
    }

    @Override
    protected void run(Minecraft mc) {
        place(mc);
    }

    /** Places once immediately (the caller must be in-game) and restarts the interval. */
    public PlaceResult placeOnceNow() {
        markRun();
        return place(Minecraft.getMinecraft());
    }

    private PlaceResult place(Minecraft mc) {
        ItemStack held = mc.thePlayer.getHeldItem();

        if (held == null) {
            return PlaceResult.NO_HELD_ITEM;
        }

        MovingObjectPosition target = mc.objectMouseOver;

        if (target == null || target.typeOfHit != MovingObjectPosition.MovingObjectType.BLOCK) {
            return PlaceResult.NO_BLOCK_TARGET;
        }

        boolean handled = mc.playerController.onPlayerRightClick(
                mc.thePlayer, mc.theWorld, held, target.getBlockPos(), target.sideHit, target.hitVec);

        if (!handled) {
            return PlaceResult.VANILLA_REJECTED;
        }

        mc.thePlayer.swingItem();
        return PlaceResult.SUCCESS;
    }
}
