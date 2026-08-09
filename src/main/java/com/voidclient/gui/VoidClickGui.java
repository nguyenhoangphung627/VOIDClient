package com.voidclient.gui;

import com.voidclient.ModuleCategory;
import com.voidclient.config.ConfigManager;
import com.voidclient.module.Module;
import com.voidclient.module.ModuleManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.util.List;

public class VoidClickGui extends Screen {
    private ModuleCategory category = ModuleCategory.COMBAT;
    private Module selected;
    private TextFieldWidget search;
    private int scroll;
    private boolean dragging;
    private int dragStartX, dragStartY, originalX, originalY;

    public VoidClickGui(Module selected) {
        super(Text.literal("VOID Client"));
        this.selected = selected;
    }

    @Override protected void init() {
        search = new TextFieldWidget(textRenderer, 190, 22, Math.min(250, width-410), 22, Text.literal("Search"));
        search.setPlaceholder(Text.literal("Search modules..."));
        search.setDrawsBackground(false);
        addDrawableChild(search);
    }

    @Override public void render(DrawContext d, int mouseX, int mouseY, float delta) {
        renderBackground(d);
        int panelX = Math.max(12, (width - Math.min(1100, width-24)) / 2);
        int panelY = Math.max(10, (height - Math.min(650, height-20)) / 2);
        int panelW = Math.min(1100, width-24), panelH = Math.min(650, height-20);
        UiUtil.rounded(d, panelX, panelY, panelW, panelH, 12, 0xE9101420);
        UiUtil.border(d, panelX, panelY, panelW, panelH, 0x503A4356);

        if (selected != null) {
            renderSettings(d, mouseX, mouseY, panelX, panelY, panelW, panelH);
            return;
        }

        // Sidebar
        UiUtil.rounded(d, panelX+10, panelY+10, 150, panelH-20, 10, 0xE9161B29);
        d.drawText(textRenderer, "VOID CLIENT", panelX+25, panelY+24, 0xFFEAF2FF, true);
        d.drawText(textRenderer, "v1.0.0", panelX+25, panelY+40, 0xFF7D8BA8, false);
        ModuleCategory[] cats = ModuleCategory.values();
        int sy = panelY + 72;
        for (ModuleCategory c : cats) {
            boolean active = c == category;
            if (active) UiUtil.rounded(d, panelX+18, sy-6, 134, 29, 7, 0xFF263A5C);
            d.drawText(textRenderer, c.name(), panelX+28, sy+2, active ? 0xFFEAF3FF : 0xFF8E9AB0, active);
            sy += 38;
        }
        d.drawText(textRenderer, "RSHIFT", panelX+25, panelY+panelH-42, 0xFF9DA9BD, false);
        d.drawText(textRenderer, "ClickGUI", panelX+25, panelY+panelH-27, 0xFF5E6C84, false);

        // Header
        d.drawText(textRenderer, "Modules", panelX+180, panelY+20, 0xFFF2F6FF, true);
        d.drawText(textRenderer, "Click left side to toggle • right side for settings", panelX+180, panelY+38, 0xFF69758B, false);
        search.setX(panelX + panelW - Math.min(250, width-410) - 20);
        search.setY(panelY+18);
        UiUtil.rounded(d, search.getX()-4, search.getY()-3, search.getWidth()+8, 28, 7, 0xFF171D2A);
        d.drawText(textRenderer, "⌕", search.getX()+6, search.getY()+6, 0xFF73819A, false);

        List<Module> modules = ModuleManager.byCategory(category);
        String q = search.getText().toLowerCase();
        int cardW = Math.max(200, (panelW - 205) / 2);
        int cardH = 78;
        int gap = 10;
        int startX = panelX+180;
        int startY = panelY+70 + scroll;
        int col = 0;
        for (Module m : modules) {
            if (!q.isBlank() && !m.getName().toLowerCase().contains(q) && !m.getDescription().toLowerCase().contains(q)) continue;
            int x = startX + col * (cardW+gap);
            int y = startY;
            if (y+cardH > panelY+55 && y < panelY+panelH-15) renderCard(d, m, x, y, cardW, cardH, mouseX, mouseY);
            col++;
            if (col == 2) { col = 0; startY += cardH+gap; }
        }
        if (modules.isEmpty()) d.drawText(textRenderer, "No modules", startX, panelY+90, 0xFF7D879A, false);
        super.render(d, mouseX, mouseY, delta);
    }

    private void renderCard(DrawContext d, Module m, int x, int y, int w, int h, int mx, int my) {
        boolean hover = mx >= x && mx <= x+w && my >= y && my <= y+h;
        int bg = m.isEnabled() ? 0xE91E2B3E : (hover ? 0xE91B2231 : 0xE9161B27);
        UiUtil.rounded(d, x, y, w, h, 9, bg);
        UiUtil.border(d, x, y, w, h, m.isEnabled() ? 0x704E75A0 : 0x45333B4C);
        d.drawText(textRenderer, m.getName(), x+14, y+13, 0xFFE8EEF8, true);
        d.drawText(textRenderer, trim(m.getDescription(), Math.max(18, w/7)), x+14, y+31, 0xFF7D879A, false);
        d.drawText(textRenderer, ">", x+w-18, y+13, 0xFF65718A, false);
        int sx = x+w-56, sy=y+h-27;
        UiUtil.rounded(d, sx, sy, 42, 18, 9, m.isEnabled() ? 0xFF5E7CC0 : 0xFF343B49);
        d.fill(m.isEnabled() ? sx+25 : sx+4, sy+4, m.isEnabled() ? sx+38 : sx+17, sy+14, 0xFFEAF1FF);
    }

    private String trim(String s, int n) { return s.length() <= n ? s : s.substring(0, Math.max(1,n-1)) + "…"; }

    private void renderSettings(DrawContext d, int mx, int my, int x, int y, int w, int h) {
        UiUtil.rounded(d, x+10, y+10, w-20, h-20, 10, 0xE9161B29);
        d.drawText(textRenderer, "<", x+28, y+26, 0xFF9BA8BE, true);
        d.drawText(textRenderer, selected.getName(), x+55, y+25, 0xFFF0F4FC, true);
        d.drawText(textRenderer, "X", x+w-40, y+25, 0xFF748096, false);
        d.drawText(textRenderer, selected.getDescription(), x+28, y+52, 0xFF707D93, false);
        int yy = y+86;
        yy = settingRow(d, "Enabled", selected.isEnabled(), x+28, yy);
        yy = slider(d, "Scale", selected.getScale(), 0.5f, 3f, x+28, yy);
        yy = slider(d, "Opacity", selected.getOpacity(), 0.1f, 1f, x+28, yy);
        for (var e : selected.getSettings().entrySet()) {
            Object v = e.getValue();
            if (v instanceof Boolean) yy = settingRow(d, e.getKey(), (Boolean)v, x+28, yy);
            else if (v instanceof Number) yy = slider(d, e.getKey(), ((Number)v).floatValue(), 0, Math.max(1, ((Number)v).floatValue()*2), x+28, yy);
            else yy = dropdown(d, e.getKey(), String.valueOf(v), x+28, yy);
            if (yy > y+h-70) break;
        }
        d.drawText(textRenderer, "Position / HUD", x+w/2+10, y+86, 0xFFB2BCD0, true);
        d.drawText(textRenderer, "X: " + selected.getX() + "   Y: " + selected.getY(), x+w/2+10, y+110, 0xFF78859B, false);
        d.drawText(textRenderer, "Drag in HUD Editor to reposition", x+w/2+10, y+132, 0xFF5F6C82, false);
        d.drawText(textRenderer, "Preview", x+w/2+10, y+170, 0xFFB2BCD0, true);
        UiUtil.rounded(d, x+w/2+10, y+190, Math.min(300,w/2-35), 130, 10, 0xFF10151F);
        d.drawText(textRenderer, selected.getName(), x+w/2+28, y+210, 0xFFE8EEF8, true);
        if (selected.getName().toLowerCase().contains("crosshair")) renderPreviewCrosshair(d, x+w*3/4, y+255);
    }

    private int settingRow(DrawContext d, String name, boolean on, int x, int y) {
        d.drawText(textRenderer, name, x, y, 0xFFD6DEEC, false);
        UiUtil.rounded(d, x+235, y-4, 42, 18, 9, on ? 0xFF5E7CC0 : 0xFF343B49);
        d.fill(on ? x+260 : x+239, y, on ? x+273 : x+252, y+10, 0xFFEAF1FF);
        return y+34;
    }

    private int slider(DrawContext d, String name, float value, float min, float max, int x, int y) {
        d.drawText(textRenderer, name, x, y, 0xFFD6DEEC, false);
        int bx=x+235, bw=180;
        d.fill(bx, y+5, bx+bw, y+7, 0xFF343B49);
        float t=(value-min)/(max-min); t=Math.max(0,Math.min(1,t));
        d.fill(bx, y+5, bx+(int)(bw*t), y+7, 0xFF6C88C8);
        d.fill(bx+(int)(bw*t)-3, y+1, bx+(int)(bw*t)+3, y+11, 0xFFEAF1FF);
        d.drawText(textRenderer, String.format("%.2f", value), bx+bw+10, y, 0xFF7F8BA0, false);
        return y+38;
    }

    private int dropdown(DrawContext d, String name, String value, int x, int y) {
        d.drawText(textRenderer, name, x, y, 0xFFD6DEEC, false);
        UiUtil.rounded(d, x+235, y-6, 180, 22, 6, 0xFF242B38);
        d.drawText(textRenderer, value, x+245, y, 0xFF9DA9BD, false);
        d.drawText(textRenderer, "⌄", x+396, y, 0xFF78859A, false);
        return y+38;
    }

    private void renderPreviewCrosshair(DrawContext d, int cx, int cy) {
        d.fill(cx-1,cy-15,cx+2,cy-4,0xFFEAF1FF); d.fill(cx-1,cy+4,cx+2,cy+15,0xFFEAF1FF);
        d.fill(cx-15,cy-1,cx-4,cy+2,0xFFEAF1FF); d.fill(cx+4,cy-1,cx+15,cy+2,0xFFEAF1FF);
    }

    @Override public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int x = Math.max(12, (width - Math.min(1100, width-24)) / 2);
        int y = Math.max(10, (height - Math.min(650, height-20)) / 2);
        int w = Math.min(1100, width-24), h = Math.min(650, height-20);
        if (selected != null) {
            if (mouseX >= x+15 && mouseX <= x+48 && mouseY >= y+12 && mouseY <= y+45) { selected=null; return true; }
            if (mouseX >= x+w-65 && mouseY >= y+10 && mouseY <= y+48) { selected=null; return true; }
            if (mouseX >= x+28 && mouseX <= x+330 && mouseY >= y+76 && mouseY <= y+120) { selected.toggle(); return true; }
            return super.mouseClicked(mouseX, mouseY, button);
        }
        // Sidebar category selection
        int sy=y+72;
        for (ModuleCategory c:ModuleCategory.values()) {
            if (mouseX>=x+15 && mouseX<=x+160 && mouseY>=sy-8 && mouseY<=sy+28) { category=c; scroll=0; return true; }
            sy+=38;
        }
        List<Module> modules=ModuleManager.byCategory(category);
        String q=search==null?"":search.getText().toLowerCase();
        int cardW=Math.max(200,(w-205)/2), cardH=78,gap=10,startX=x+180,startY=y+70+scroll,col=0;
        for(Module m:modules){
            if(!q.isBlank()&&!m.getName().toLowerCase().contains(q)&&!m.getDescription().toLowerCase().contains(q)) continue;
            int cx=startX+col*(cardW+gap),cy=startY;
            if(mouseX>=cx&&mouseX<=cx+cardW&&mouseY>=cy&&mouseY<=cy+cardH){
                if(mouseX>=cx+cardW-70) selected=m; else m.toggle();
                return true;
            }
            col++; if(col==2){col=0;startY+=cardH+gap;}
        }
        return super.mouseClicked(mouseX,mouseY,button);
    }

    @Override public boolean mouseScrolled(double mouseX,double mouseY,double horizontal,double vertical){
        if(selected==null){scroll += (int)(vertical*24); scroll=Math.min(0,Math.max(-500,scroll)); return true;}
        return super.mouseScrolled(mouseX,mouseY,horizontal,vertical);
    }

    @Override public boolean keyPressed(int keyCode,int scanCode,int modifiers){
        if(keyCode==GLFW.GLFW_KEY_ESCAPE){ if(selected!=null)selected=null; else close(); return true; }
        return super.keyPressed(keyCode,scanCode,modifiers);
    }

    @Override public boolean shouldPause(){ return false; }
}
