package net.ent.entcopperrails.client;

import java.util.Locale;

import net.ent.entcopperrails.entity.MaxSpeedHolder;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.minecart.AbstractMinecart;

public final class CopperRailsHud {

    private static boolean enabled = false;

    public static boolean toggle() {
        enabled = !enabled;
        return enabled;
    }

    public static boolean isEnabled() {
        return enabled;
    }

    public static void render(GuiGraphicsExtractor graphics, DeltaTracker delta) {
        if (!enabled) {
            return;
        }
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player == null || !(player.getVehicle() instanceof AbstractMinecart cart)) {
            return;
        }

        int maxBps = ((MaxSpeedHolder) cart).entcopperrails$getMaxSpeedBps();

        double dx = cart.getX() - cart.xOld;
        double dz = cart.getZ() - cart.zOld;
        double nowBps = Math.sqrt(dx * dx + dz * dz) * 20.0D;

        String line1 = String.format(Locale.ROOT, "Max: %d BPS", maxBps);
        String line2 = String.format(Locale.ROOT, "Now: %.1f BPS", nowBps);

        Font font = mc.font;
        int pad = 4;
        int lineH = font.lineHeight;
        int textW = Math.max(font.width(line1), font.width(line2));
        int right = graphics.guiWidth() - pad;
        int left = right - textW - 6;
        int top = pad;
        int bottom = top + lineH * 2 + 7;

        graphics.fill(left, top, right, bottom, 0x90000000);
        int tx = left + 3;
        graphics.text(font, line1, tx, top + 3, 0xFFFFFFFF);
        graphics.text(font, line2, tx, top + 4 + lineH, 0xFF80C8FF);
    }

    private CopperRailsHud() {}
}
