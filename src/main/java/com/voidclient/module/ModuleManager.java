package com.voidclient.module;

import com.voidclient.ModuleCategory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class ModuleManager {
    private static final List<Module> MODULES = new ArrayList<>();

    private ModuleManager() {}

    public static void init() {
        MODULES.clear();
        add("CPS", "Clicks per second", ModuleCategory.COMBAT);
        add("Hit Color", "Tint attack feedback", ModuleCategory.COMBAT);
        add("Hit Animation", "Lightweight hit animation", ModuleCategory.COMBAT);
        add("Target HUD", "Compact target information", ModuleCategory.COMBAT);
        add("Crosshair", "Custom center crosshair", ModuleCategory.COMBAT)
                .setting("Style", "Classic").setting("Size", 8).setting("Thickness", 2).setting("Gap", 3)
                .setting("Color", 0xFFFFFFFF).setting("Opacity", 1.0f).setting("Dynamic", true)
                .setting("Dot", false).setting("Outline", true);
        add("Attack Indicator", "Attack cooldown indicator", ModuleCategory.COMBAT);
        add("Combo Counter", "Track consecutive hits", ModuleCategory.COMBAT);
        add("Reach Display", "Display interaction distance", ModuleCategory.COMBAT);

        add("Fullbright", "Raise brightness without shader mods", ModuleCategory.VISUAL);
        add("FPS Display", "Display current FPS", ModuleCategory.VISUAL);
        add("Ping Display", "Display network latency", ModuleCategory.VISUAL);
        add("Coordinates", "Display XYZ coordinates", ModuleCategory.VISUAL);
        add("Armor Status", "Display equipped armor", ModuleCategory.VISUAL);
        add("Potion Status", "Display active effects", ModuleCategory.VISUAL);
        add("Keystrokes", "Display WASD and mouse buttons", ModuleCategory.VISUAL);
        add("Direction", "Display facing direction", ModuleCategory.VISUAL);
        add("Block Outline", "Customize block outline", ModuleCategory.VISUAL).setting("Color", 0xFFFFFFFF);
        add("Item Info", "Display held item details", ModuleCategory.VISUAL);
        add("Zoom", "Smooth camera zoom", ModuleCategory.VISUAL).setting("FOV", 30).setting("Smooth", true);
        add("Custom Crosshair", "Alternative crosshair preset", ModuleCategory.VISUAL);

        add("Render Distance Control", "Client-side render distance control", ModuleCategory.FPS).setting("Distance", 8);
        add("Entity Culling", "Lightweight visibility optimization", ModuleCategory.FPS);
        add("Particle Control", "Reduce visual particles", ModuleCategory.FPS);
        add("Animation Control", "Reduce animation workload", ModuleCategory.FPS);
        add("Weather Effects", "Reduce weather rendering", ModuleCategory.FPS);
        add("Entity Render Distance", "Limit entity render range", ModuleCategory.FPS).setting("Distance", 64);
        add("Dynamic FPS", "Reduce idle rendering work", ModuleCategory.FPS);
        add("Low Animation Mode", "Use low-cost UI animations", ModuleCategory.FPS);

        add("Toggle Sprint", "Remember sprint state", ModuleCategory.OTHER);
        add("Toggle Sneak", "Remember sneak state", ModuleCategory.OTHER);
        add("Auto GG", "Convenience chat message after a match", ModuleCategory.OTHER);
        add("Chat Timestamps", "Add local timestamps to chat", ModuleCategory.OTHER);
        add("FPS Counter", "Compact FPS counter", ModuleCategory.OTHER);
        add("CPS Counter", "Compact CPS counter", ModuleCategory.OTHER);
        add("Ping", "Compact ping display", ModuleCategory.OTHER);
        add("Coordinates", "Compact coordinates display", ModuleCategory.OTHER);
        add("Server Info", "Display server information", ModuleCategory.OTHER);
        add("Keystrokes", "Compact keystrokes HUD", ModuleCategory.OTHER);
        add("Armor HUD", "Compact armor HUD", ModuleCategory.OTHER);
        add("Potion HUD", "Compact potion HUD", ModuleCategory.OTHER);
        add("Time Display", "Display local time", ModuleCategory.OTHER);
        add("Direction", "Compact direction HUD", ModuleCategory.OTHER);
        add("Session Stats", "Session counters", ModuleCategory.OTHER);
        add("Playtime", "Current playtime", ModuleCategory.OTHER);
        add("Clock", "Compact clock", ModuleCategory.OTHER);
        add("Item Counter", "Count selected item", ModuleCategory.OTHER);
        add("Keybind Manager", "Manage module keybinds", ModuleCategory.OTHER);
        add("Screenshot", "Open Minecraft screenshot flow", ModuleCategory.OTHER);
        add("GUI Editor", "Open the VOID GUI editor", ModuleCategory.OTHER);
        add("HUD Editor", "Move and scale HUD modules", ModuleCategory.OTHER);

        add("Save Config", "Save current configuration", ModuleCategory.CONFIG);
        add("Load Config", "Load a configuration", ModuleCategory.CONFIG);
        add("Reset Config", "Reset current configuration", ModuleCategory.CONFIG);
        add("Create Config", "Create a named configuration", ModuleCategory.CONFIG);
        add("Delete Config", "Delete a named configuration", ModuleCategory.CONFIG);
        add("Config List", "List available configurations", ModuleCategory.CONFIG);
    }

    private static Module add(String name, String description, ModuleCategory category) {
        Module m = new Module(name, description, category);
        MODULES.add(m);
        return m;
    }

    public static List<Module> all() { return Collections.unmodifiableList(MODULES); }
    public static List<Module> byCategory(ModuleCategory category) {
        List<Module> out = new ArrayList<>();
        for (Module m : MODULES) if (m.getCategory() == category) out.add(m);
        return out;
    }
    public static Module find(String name) {
        for (Module m : MODULES) if (m.getName().equalsIgnoreCase(name)) return m;
        return null;
    }
}
