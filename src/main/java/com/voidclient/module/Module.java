package com.voidclient.module;

import com.voidclient.ModuleCategory;
import java.util.LinkedHashMap;
import java.util.Map;

public class Module {
    private final String name;
    private final String description;
    private final ModuleCategory category;
    private boolean enabled;
    private int x = 10, y = 10;
    private float scale = 1.0f, opacity = 1.0f;
    private final Map<String, Object> settings = new LinkedHashMap<>();

    public Module(String name, String description, ModuleCategory category) {
        this.name = name;
        this.description = description;
        this.category = category;
    }

    public String getName() { return name; }
    public String getDescription() { return description; }
    public ModuleCategory getCategory() { return category; }
    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
    public void toggle() { enabled = !enabled; }
    public int getX() { return x; }
    public int getY() { return y; }
    public void setPosition(int x, int y) { this.x = x; this.y = y; }
    public float getScale() { return scale; }
    public void setScale(float scale) { this.scale = Math.max(0.5f, Math.min(3.0f, scale)); }
    public float getOpacity() { return opacity; }
    public void setOpacity(float opacity) { this.opacity = Math.max(0.1f, Math.min(1.0f, opacity)); }
    public Map<String, Object> getSettings() { return settings; }

    public Module setting(String key, Object value) {
        settings.put(key, value);
        return this;
    }
}
