package com.voidclient.gui;

import com.voidclient.keybind.KeybindManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class KeybindScreen extends Screen {
    private final Screen parent;
    private boolean listening;

    public KeybindScreen(Screen parent) {
        super(Text.literal("VOID Client Key Settings"));
        this.parent = parent;
    }

    @Override public void render(DrawContext d, int mouseX, int mouseY, float delta) {
        renderBackground(d);
        int w=430,h=220,x=(width-w)/2,y=(height-h)/2;
        UiUtil.rounded(d,x,y,w,h,12,0xF0161B27);
        UiUtil.border(d,x,y,w,h,0x603A4356);
        d.drawText(textRenderer,"KEY SETTINGS",x+24,y+24,0xFFEAF2FF,true);
        d.drawText(textRenderer,"ClickGUI",x+24,y+64,0xFFD8E0ED,false);
        d.drawText(textRenderer,"Current: "+(KeybindManager.clickGui == null ? "RSHIFT" : KeybindManager.clickGui.getBoundKeyLocalizedText().getString()),x+24,y+85,0xFF718096,false);
        UiUtil.rounded(d,x+230,y+55,170,34,8,listening?0xFF304A78:0xFF242C3A);
        d.drawText(textRenderer,listening?"Press any key…":"Change key",x+252,y+67,0xFFE8EEF8,true);
        d.drawText(textRenderer,"Default: Right Shift",x+24,y+125,0xFF69768B,false);
        d.drawText(textRenderer,"ESC = cancel / back",x+24,y+155,0xFF59667B,false);
        super.render(d,mouseX,mouseY,delta);
    }

    @Override public boolean mouseClicked(double mouseX,double mouseY,int button) {
        int x=(width-430)/2,y=(height-220)/2;
        if(mouseX>=x+225&&mouseX<=x+410&&mouseY>=y+50&&mouseY<=y+100){ listening=true; return true; }
        if(mouseX>=x+20&&mouseX<=x+120&&mouseY>=y+145&&mouseY<=y+190){ close(); return true; }
        return super.mouseClicked(mouseX,mouseY,button);
    }

    @Override public boolean keyPressed(int keyCode,int scanCode,int modifiers) {
        if(keyCode==GLFW.GLFW_KEY_ESCAPE){ close(); return true; }
        if(listening){
            if(KeybindManager.clickGui!=null){
                KeybindManager.clickGui.setBoundKey(net.minecraft.client.util.InputUtil.fromKeyCode(keyCode,scanCode));
                MinecraftClient.getInstance().options.write();
            }
            listening=false;
            return true;
        }
        return super.keyPressed(keyCode,scanCode,modifiers);
    }

    private void close(){ MinecraftClient.getInstance().setScreen(parent); }
    @Override public boolean shouldPause(){return false;}
}
