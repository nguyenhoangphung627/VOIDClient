package com.voidclient.keybind;

import com.voidclient.gui.VoidClickGui;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public final class KeybindManager {
    public static KeyBinding clickGui;

    private KeybindManager() {}

    public static void init() {
        clickGui = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.voidclient.clickgui", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_RIGHT_SHIFT,
                "category.voidclient"));
    }

    public static void tick() {
        MinecraftClient mc = MinecraftClient.getInstance();
        while (clickGui.wasPressed()) {
            if (mc.currentScreen instanceof VoidClickGui) mc.setScreen(null);
            else if (mc.player != null) mc.setScreen(new VoidClickGui(null));
        }
    }
}
