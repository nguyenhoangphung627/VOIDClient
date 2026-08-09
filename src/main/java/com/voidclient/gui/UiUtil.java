package com.voidclient.gui;

import net.minecraft.client.gui.DrawContext;

public final class UiUtil {
    private UiUtil() {}
    public static void rounded(DrawContext d, int x, int y, int w, int h, int radius, int color) {
        if (w <= 0 || h <= 0) return;
        d.fill(x + radius, y, x + w - radius, y + h, color);
        d.fill(x, y + radius, x + w, y + h - radius, color);
        d.fill(x + 1, y + 1, x + radius, y + radius, color);
        d.fill(x + w - radius, y + 1, x + w - 1, y + radius, color);
        d.fill(x + 1, y + h - radius, x + radius, y + h - 1, color);
        d.fill(x + w - radius, y + h - radius, x + w - 1, y + h - 1, color);
    }
    public static void border(DrawContext d, int x, int y, int w, int h, int color) {
        d.fill(x, y, x+w, y+1, color);
        d.fill(x, y+h-1, x+w, y+h, color);
        d.fill(x, y, x+1, y+h, color);
        d.fill(x+w-1, y, x+w, y+h, color);
    }
}
