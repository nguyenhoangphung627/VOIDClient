package com.voidclient;

import com.voidclient.config.ConfigManager;
import com.voidclient.hud.HudManager;
import com.voidclient.keybind.KeybindManager;
import com.voidclient.module.ModuleManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;

public class VoidClient implements ClientModInitializer {
    public static final String MOD_ID = "voidclient";

    @Override public void onInitializeClient() {
        ModuleManager.init();
        ConfigManager.init();
        KeybindManager.init();
        HudManager.init();
        ClientTickEvents.END_CLIENT_TICK.register(client -> KeybindManager.tick());
        System.out.println("[VOID Client] Initialized for Minecraft 1.20.0");
    }
}
