package com.autoclicker.handler;

import com.autoclicker.config.AutoClickerConfig;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

/**
 * A toggleable action that runs every {@link #getIntervalMs()} while enabled, in-game, with no GUI open,
 * and switches itself off after the configured timeout.
 */
public abstract class RepeatingAction {

    private boolean enabled;
    private long enabledAt;
    private long lastRunAt;

    protected abstract int getIntervalMs();

    protected abstract void run(Minecraft mc);

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.START || !enabled) {
            return;
        }

        long now = System.currentTimeMillis();

        if (AutoClickerConfig.isTimeoutEnabled() && now - enabledAt >= AutoClickerConfig.getTimeoutDurationMs()) {
            setEnabled(false);
            return;
        }

        Minecraft mc = Minecraft.getMinecraft();

        if (mc.thePlayer == null || mc.theWorld == null || mc.currentScreen != null) {
            return;
        }

        if (now - lastRunAt >= getIntervalMs()) {
            lastRunAt = now;
            run(mc);
        }
    }

    /** Restarts the interval, so the next scheduled run waits a full interval from now. */
    protected void markRun() {
        lastRunAt = System.currentTimeMillis();
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void toggle() {
        setEnabled(!enabled);
    }

    public void setEnabled(boolean state) {
        enabled = state;
        enabledAt = System.currentTimeMillis();
        lastRunAt = 0L;
    }
}
