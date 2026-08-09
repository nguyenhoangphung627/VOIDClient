package com.voidclient.hud;

import com.voidclient.module.Module;
import com.voidclient.module.ModuleManager;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;

public final class HudManager {
    private HudManager() {}

    public static void init() {
        HudRenderCallback.EVENT.register(HudManager::render);
    }

    private static void render(DrawContext ctx, float tickDelta) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.options.hudHidden || mc.player == null) return;
        Module fps = ModuleManager.find("FPS Display");
        Module ping = ModuleManager.find("Ping Display");
        Module coords = ModuleManager.find("Coordinates");
        Module cross = ModuleManager.find("Crosshair");
        if (fps != null && fps.isEnabled()) draw(ctx, "FPS: " + MinecraftClient.getInstance().getCurrentFps(), fps);
        if (ping != null && ping.isEnabled()) draw(ctx, "Ping: " + pingValue(mc.player) + "ms", ping);
        if (coords != null && coords.isEnabled()) draw(ctx,
                String.format("XYZ: %.1f %.1f %.1f", mc.player.getX(), mc.player.getY(), mc.player.getZ()), coords);
        if (cross != null && cross.isEnabled()) renderCrosshair(ctx, cross);
    }

    private static int pingValue(ClientPlayerEntity player) {
        if (player.networkHandler.getPlayerListEntry(player.getUuid()) == null) return 0;
        return player.networkHandler.getPlayerListEntry(player.getUuid()).getLatency();
    }

    private static void draw(DrawContext ctx, String text, Module m) {
        int a = (int)(m.getOpacity() * 220) << 24;
        int color = a | 0xE8F1FF;
        ctx.drawText(MinecraftClient.getInstance().textRenderer, text, m.getX(), m.getY(), color, true);
    }

    private static void renderCrosshair(DrawContext ctx, Module m) {
        int cx = ctx.getScaledWindowWidth() / 2, cy = ctx.getScaledWindowHeight() / 2;
        int size = 8, thick = 2, gap = 3;
        Object s = m.getSettings().get("Size"); if (s instanceof Number) size = ((Number)s).intValue();
        Object t = m.getSettings().get("Thickness"); if (t instanceof Number) thick = ((Number)t).intValue();
        Object g = m.getSettings().get("Gap"); if (g instanceof Number) gap = ((Number)g).intValue();
        int color = 0xFFFFFFFF;
        Object c = m.getSettings().get("Color"); if (c instanceof Number) color = ((Number)c).intValue();
        ctx.fill(cx - thick/2, cy - gap - size, cx + (thick+1)/2, cy - gap, color);
        ctx.fill(cx - thick/2, cy + gap, cx + (thick+1)/2, cy + gap + size, color);
        ctx.fill(cx - gap - size, cy - thick/2, cx - gap, cy + (thick+1)/2, color);
        ctx.fill(cx + gap, cy - thick/2, cx + gap + size, cy + (thick+1)/2, color);
        if (Boolean.TRUE.equals(m.getSettings().get("Dot"))) ctx.fill(cx-1, cy-1, cx+1, cy+1, color);
    }
}
