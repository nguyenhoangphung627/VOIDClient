package com.voidclient.gui;

import com.voidclient.module.Module;
import com.voidclient.module.ModuleManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.Arrays;
import java.util.List;

/** Lightweight HUD editor: drag enabled HUD modules, mouse/touch compatible. */
public class HudEditorScreen extends Screen {
    private final Screen parent;
    private Module dragging;
    private int offsetX, offsetY;

    public HudEditorScreen(Screen parent) { super(Text.literal("VOID HUD Editor")); this.parent=parent; }

    private List<String> names() { return Arrays.asList("FPS Display","CPS","Ping Display","Coordinates","Keystrokes","Armor Status","Potion Status","Direction","Clock","Session Stats"); }

    @Override public void render(DrawContext d,int mx,int my,float delta) {
        renderBackground(d);
        d.fill(0,0,width,height,0x66000000);
        d.drawText(textRenderer,"VOID HUD EDITOR",14,14,0xFFEAF2FF,true);
        d.drawText(textRenderer,"Drag enabled elements • ESC = back",14,30,0xFF7D899D,false);
        for(String name:names()) {
            Module m=ModuleManager.find(name); if(m==null||!m.isEnabled()) continue;
            int x=m.getX(),y=m.getY();
            int w=Math.max(70,textRenderer.getWidth(name)+18),h=22;
            UiUtil.rounded(d,x,y,w,h,5,0xAA182130);
            UiUtil.border(d,x,y,w,h,dragging==m?0xFF8EA8D8:0x665B6980);
            d.drawText(textRenderer,name,x+8,y+7,0xFFE0E7F2,true);
        }
        super.render(d,mx,my,delta);
    }

    @Override public boolean mouseClicked(double mx,double my,int button) {
        if(button!=0) return super.mouseClicked(mx,my,button);
        for(String name:names()) {
            Module m=ModuleManager.find(name); if(m==null||!m.isEnabled()) continue;
            int w=Math.max(70,textRenderer.getWidth(name)+18),h=22;
            if(mx>=m.getX()&&mx<=m.getX()+w&&my>=m.getY()&&my<=m.getY()+h){
                dragging=m; offsetX=(int)mx-m.getX(); offsetY=(int)my-m.getY(); return true;
            }
        }
        return super.mouseClicked(mx,my,button);
    }

    @Override public boolean mouseDragged(double mx,double my,int button,double dx,double dy) {
        if(dragging!=null){ dragging.setPosition((int)mx-offsetX,(int)my-offsetY); return true; }
        return super.mouseDragged(mx,my,button,dx,dy);
    }
    @Override public boolean mouseReleased(double mx,double my,int button){ dragging=null; return super.mouseReleased(mx,my,button); }
    @Override public boolean keyPressed(int keyCode,int scanCode,int modifiers){ if(keyCode==256){close();return true;} return super.keyPressed(keyCode,scanCode,modifiers); }
    private void close(){MinecraftClient.getInstance().setScreen(parent);}
    @Override public boolean shouldPause(){return false;}
}
