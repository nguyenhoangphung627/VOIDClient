package com.voidclient.config;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.voidclient.module.Module;
import com.voidclient.module.ModuleManager;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public final class ConfigManager {
    private static final Path DIR = FabricLoader.getInstance().getConfigDir().resolve("voidclient");
    private static final GsonBuilder GSON = new GsonBuilder().setPrettyPrinting();
    private static String current = "default";

    private ConfigManager() {}

    public static void init() {
        try { Files.createDirectories(DIR); } catch (IOException ignored) {}
        if (!Files.exists(file(current))) save(current);
        else load(current);
    }

    private static Path file(String name) { return DIR.resolve(name + ".json"); }

    public static void save(String name) {
        try {
            JsonObject root = new JsonObject();
            root.addProperty("version", 1);
            JsonObject modules = new JsonObject();
            for (Module m : ModuleManager.all()) {
                JsonObject o = new JsonObject();
                o.addProperty("enabled", m.isEnabled());
                o.addProperty("x", m.getX()); o.addProperty("y", m.getY());
                o.addProperty("scale", m.getScale()); o.addProperty("opacity", m.getOpacity());
                modules.add(m.getName(), o);
            }
            root.add("modules", modules);
            Files.writeString(file(name), GSON.create().toJson(root), StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            current = name;
        } catch (IOException ignored) {}
    }

    public static void load(String name) {
        Path p = file(name);
        if (!Files.exists(p)) return;
        try {
            JsonObject root = JsonParser.parseString(Files.readString(p)).getAsJsonObject();
            JsonObject modules = root.has("modules") ? root.getAsJsonObject("modules") : new JsonObject();
            for (Module m : ModuleManager.all()) {
                if (!modules.has(m.getName())) continue;
                JsonObject o = modules.getAsJsonObject(m.getName());
                if (o.has("enabled")) m.setEnabled(o.get("enabled").getAsBoolean());
                if (o.has("x") && o.has("y")) m.setPosition(o.get("x").getAsInt(), o.get("y").getAsInt());
                if (o.has("scale")) m.setScale(o.get("scale").getAsFloat());
                if (o.has("opacity")) m.setOpacity(o.get("opacity").getAsFloat());
            }
            current = name;
        } catch (Exception ignored) {}
    }

    public static void reset() {
        for (Module m : ModuleManager.all()) {
            m.setEnabled(false); m.setPosition(10, 10); m.setScale(1); m.setOpacity(1);
        }
        save(current);
    }

    public static void create(String name) { save(name); }
    public static void delete(String name) {
        if (name.equals("default")) return;
        try { Files.deleteIfExists(file(name)); } catch (IOException ignored) {}
    }
    public static List<String> list() {
        List<String> names = new ArrayList<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(DIR, "*.json")) {
            for (Path p : stream) names.add(p.getFileName().toString().replaceFirst("\\.json$", ""));
        } catch (IOException ignored) {}
        return names;
    }
    public static String current() { return current; }
}
