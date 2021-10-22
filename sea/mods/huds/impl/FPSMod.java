// 
// Decompiled by Procyon v0.5.36
// 

package sea.mods.huds.impl;

import net.minecraft.client.gui.Gui;
import java.awt.Color;
import sea.mods.huds.settings.color.ColorList;
import net.minecraft.client.Minecraft;
import sea.mods.huds.settings.Settings;
import sea.config.settings.ModeSetting;
import sea.config.settings.BooleanSetting;
import sea.mods.huds.HudMod;

public class FPSMod extends HudMod
{
    BooleanSetting test;
    ModeSetting testMode;
    
    public FPSMod() {
        super("FPSMod", 50, 60, "Render ur FPS");
        this.test = new BooleanSetting("test", this.isEnabled());
        this.testMode = new ModeSetting("Test", "Mode1", new String[] { "Mode1", "Mode2", "Mod3" });
    }
    
    @Override
    public void draw() {
        try {
            if (Settings.getFps1().isEnabled()) {
                this.drawFps1();
                Settings.fps2.setEnabled(false);
                Settings.fps3.setEnabled(false);
            }
            else if (Settings.getFps2().isEnabled()) {
                this.drawFps2();
                Settings.fps3.setEnabled(false);
                Settings.fps1.setEnabled(false);
            }
            else if (Settings.getFps3().isEnabled()) {
                this.drawFps3();
                Settings.fps2.setEnabled(false);
                Settings.fps1.setEnabled(false);
            }
            else {
                this.drawFps();
                Settings.fps2.setEnabled(false);
                Settings.fps3.setEnabled(false);
                Settings.fps1.setEnabled(false);
            }
        }
        catch (NullPointerException ex) {}
        super.draw();
    }
    
    private void drawFps1() {
        this.drawbg();
        this.fr.drawStringWithShadow("§3[§bFPS §f" + Minecraft.getDebugFPS() + "§3]§f", (float)this.getX(), (float)this.getY(), -1);
        if (Minecraft.getDebugFPS() < 60) {
            this.fr.drawStringWithShadow("§3[§bFPS §f§c" + Minecraft.getDebugFPS() + "§f" + "§3]§f", (float)this.getX(), (float)this.getY(), -1);
        }
        if (Minecraft.getDebugFPS() <= 30) {
            this.fr.drawStringWithShadow("§3[§bFPS §f§4" + Minecraft.getDebugFPS() + "§f" + "§3]§f", (float)this.getX(), (float)this.getY(), -1);
        }
        if (Minecraft.getDebugFPS() >= 60) {
            this.fr.drawStringWithShadow("§3[§bFPS §f§2" + Minecraft.getDebugFPS() + "§f" + "§3]§f", (float)this.getX(), (float)this.getY(), -1);
        }
        if (Minecraft.getDebugFPS() >= 100) {
            this.fr.drawStringWithShadow("§3[§bFPS §f§a" + Minecraft.getDebugFPS() + "§f" + "§3]§f", (float)this.getX(), (float)this.getY(), -1);
        }
        if (Minecraft.getDebugFPS() >= 200) {
            this.fr.drawStringWithShadow("§3[§bFPS §f" + Minecraft.getDebugFPS() + "§3]§f", (float)this.getX(), (float)this.getY(), -1);
        }
    }
    
    private void drawFps2() {
        this.drawbg();
        if (ColorList.getFPSaqua().isEnabled()) {
            this.fr.drawStringWithShadow("§bFPS§f " + Minecraft.getDebugFPS(), (float)this.getX(), (float)this.getY(), -1);
            this.fr.drawStringWithShadow("§bFPS§f " + Minecraft.getDebugFPS(), (float)this.getX(), (float)this.getY(), -1);
        }
        else if (ColorList.getFPSblue().isEnabled()) {
            this.fr.drawStringWithShadow("§9FPS§f " + Minecraft.getDebugFPS(), (float)this.getX(), (float)this.getY(), -1);
            this.fr.drawStringWithShadow("§9FPS§f " + Minecraft.getDebugFPS(), (float)this.getX(), (float)this.getY(), -1);
        }
        else if (ColorList.getFPSgreen().isEnabled()) {
            this.fr.drawStringWithShadow("§aFPS§f " + Minecraft.getDebugFPS(), (float)this.getX(), (float)this.getY(), -1);
            this.fr.drawStringWithShadow("§aFPS§f " + Minecraft.getDebugFPS(), (float)this.getX(), (float)this.getY(), -1);
        }
        else if (ColorList.getFPSpink().isEnabled()) {
            this.fr.drawStringWithShadow("§dFPS§f " + Minecraft.getDebugFPS(), (float)this.getX(), (float)this.getY(), -1);
            this.fr.drawStringWithShadow("§dFPS§f " + Minecraft.getDebugFPS(), (float)this.getX(), (float)this.getY(), -1);
        }
        else if (ColorList.getFPSgold().isEnabled()) {
            this.fr.drawStringWithShadow("§6FPS§f " + Minecraft.getDebugFPS(), (float)this.getX(), (float)this.getY(), -1);
            this.fr.drawStringWithShadow("§6FPS§f " + Minecraft.getDebugFPS(), (float)this.getX(), (float)this.getY(), -1);
        }
        else if (ColorList.getFPSyellow().isEnabled()) {
            this.fr.drawStringWithShadow("§eFPS§f " + Minecraft.getDebugFPS(), (float)this.getX(), (float)this.getY(), -1);
            this.fr.drawStringWithShadow("§eFPS§f " + Minecraft.getDebugFPS(), (float)this.getX(), (float)this.getY(), -1);
        }
        else {
            this.fr.drawStringWithShadow("FPS " + Minecraft.getDebugFPS(), (float)this.getX(), (float)this.getY(), -1);
            this.fr.drawStringWithShadow("FPS " + Minecraft.getDebugFPS(), (float)this.getX(), (float)this.getY(), -1);
        }
    }
    
    private void drawFps3() {
        this.drawbg();
        if (ColorList.getFPSaqua().isEnabled()) {
            this.fr.drawStringWithShadow(String.valueOf(Minecraft.getDebugFPS()) + "§3 : FPS§f", (float)this.getX(), (float)this.getY(), -1);
            this.fr.drawStringWithShadow(String.valueOf(Minecraft.getDebugFPS()) + "§f" + "§3 : FPS§f", (float)this.getX(), (float)this.getY(), -1);
        }
        else if (ColorList.getFPSblue().isEnabled()) {
            this.fr.drawStringWithShadow(String.valueOf(Minecraft.getDebugFPS()) + "§9 : FPS§f", (float)this.getX(), (float)this.getY(), -1);
            this.fr.drawStringWithShadow(String.valueOf(Minecraft.getDebugFPS()) + "§f" + "§9 : FPS§f", (float)this.getX(), (float)this.getY(), -1);
        }
        else if (ColorList.getFPSgreen().isEnabled()) {
            this.fr.drawStringWithShadow(String.valueOf(Minecraft.getDebugFPS()) + "§a : FPS§f", (float)this.getX(), (float)this.getY(), -1);
            this.fr.drawStringWithShadow(String.valueOf(Minecraft.getDebugFPS()) + "§f" + "§a : FPS§f", (float)this.getX(), (float)this.getY(), -1);
        }
        else if (ColorList.getFPSpink().isEnabled()) {
            this.fr.drawStringWithShadow(String.valueOf(Minecraft.getDebugFPS()) + "§d : FPS§f", (float)this.getX(), (float)this.getY(), -1);
            this.fr.drawStringWithShadow(String.valueOf(Minecraft.getDebugFPS()) + "§f" + "§d : FPS§f", (float)this.getX(), (float)this.getY(), -1);
        }
        else if (ColorList.getFPSgold().isEnabled()) {
            this.fr.drawStringWithShadow(String.valueOf(Minecraft.getDebugFPS()) + "§6 : FPS§f", (float)this.getX(), (float)this.getY(), -1);
            this.fr.drawStringWithShadow(String.valueOf(Minecraft.getDebugFPS()) + "§f" + "§6 : FPS§f", (float)this.getX(), (float)this.getY(), -1);
        }
        else if (ColorList.getFPSyellow().isEnabled()) {
            this.fr.drawStringWithShadow(String.valueOf(Minecraft.getDebugFPS()) + "§e : FPS§f", (float)this.getX(), (float)this.getY(), -1);
            this.fr.drawStringWithShadow(String.valueOf(Minecraft.getDebugFPS()) + "§f" + "§e : FPS§f", (float)this.getX(), (float)this.getY(), -1);
        }
        else {
            this.fr.drawStringWithShadow(String.valueOf(Minecraft.getDebugFPS()) + " : FPS", (float)this.getX(), (float)this.getY(), -1);
            this.fr.drawStringWithShadow(String.valueOf(Minecraft.getDebugFPS()) + " : FPS", (float)this.getX(), (float)this.getY(), -1);
        }
    }
    
    private void drawFps() {
        this.drawbg();
        if (ColorList.getFPSaqua().isEnabled()) {
            this.fr.drawStringWithShadow("§3[§bFPS §f" + Minecraft.getDebugFPS() + "§3]§f", (float)this.getX(), (float)this.getY(), -1);
            this.fr.drawStringWithShadow("§3[§bFPS §f" + Minecraft.getDebugFPS() + "§f" + "§3]§f", (float)this.getX(), (float)this.getY(), -1);
        }
        else if (ColorList.getFPSblue().isEnabled()) {
            this.fr.drawStringWithShadow("§9[§1FPS §f" + Minecraft.getDebugFPS() + "§9]§f", (float)this.getX(), (float)this.getY(), -1);
            this.fr.drawStringWithShadow("§9[§1FPS §f" + Minecraft.getDebugFPS() + "§f" + "§9]§f", (float)this.getX(), (float)this.getY(), -1);
        }
        else if (ColorList.getFPSgreen().isEnabled()) {
            this.fr.drawStringWithShadow("§a[§2FPS §f" + Minecraft.getDebugFPS() + "§a]§f", (float)this.getX(), (float)this.getY(), -1);
            this.fr.drawStringWithShadow("§a[§2FPS §f" + Minecraft.getDebugFPS() + "§f" + "§a]§f", (float)this.getX(), (float)this.getY(), -1);
        }
        else if (ColorList.getFPSpink().isEnabled()) {
            this.fr.drawStringWithShadow("§d[§5FPS §f" + Minecraft.getDebugFPS() + "§d]§f", (float)this.getX(), (float)this.getY(), -1);
            this.fr.drawStringWithShadow("§d[§5FPS §f" + Minecraft.getDebugFPS() + "§f" + "§d]§f", (float)this.getX(), (float)this.getY(), -1);
        }
        else if (ColorList.getFPSgold().isEnabled()) {
            this.fr.drawStringWithShadow("§6[§6FPS §f" + Minecraft.getDebugFPS() + "§6]§f", (float)this.getX(), (float)this.getY(), -1);
            this.fr.drawStringWithShadow("§6[§6FPS §f" + Minecraft.getDebugFPS() + "§f" + "§6]§f", (float)this.getX(), (float)this.getY(), -1);
        }
        else if (ColorList.getFPSyellow().isEnabled()) {
            this.fr.drawStringWithShadow("§e[§eFPS §f" + Minecraft.getDebugFPS() + "§e]§f", (float)this.getX(), (float)this.getY(), -1);
            this.fr.drawStringWithShadow("§e[§eFPS §f" + Minecraft.getDebugFPS() + "§f" + "§e]§f", (float)this.getX(), (float)this.getY(), -1);
        }
        else {
            this.fr.drawStringWithShadow("[FPS " + Minecraft.getDebugFPS() + "]", (float)this.getX(), (float)this.getY(), -1);
            this.fr.drawStringWithShadow("[FPS " + Minecraft.getDebugFPS() + "]", (float)this.getX(), (float)this.getY(), -1);
        }
    }
    
    private void drawbg() {
        if (Settings.getNobg().isEnabled()) {
            Gui.drawRect(this.getX() - 2, this.getY() - 2, this.getX() + this.getWidth(), this.getY() + this.getHieght(), new Color(0, 0, 0, 170).getRGB());
        }
    }
    
    @Override
    public void renderDummy(final int mouseX, final int mouseY) {
        if (Settings.getFps1().isEnabled()) {
            this.drawFps1();
            Settings.fps2.setEnabled(false);
            Settings.fps3.setEnabled(false);
        }
        else if (Settings.getFps2().isEnabled()) {
            this.drawFps2();
            Settings.fps3.setEnabled(false);
            Settings.fps1.setEnabled(false);
        }
        else if (Settings.getFps3().isEnabled()) {
            this.drawFps3();
            Settings.fps2.setEnabled(false);
            Settings.fps1.setEnabled(false);
        }
        else {
            this.drawFps();
            Settings.fps2.setEnabled(false);
            Settings.fps3.setEnabled(false);
            Settings.fps1.setEnabled(false);
        }
        super.renderDummy(mouseX, mouseY);
    }
    
    @Override
    public int getWidth() {
        return this.fr.getStringWidth("Fps§b[ §f" + Minecraft.getDebugFPS() + "§b]§f");
    }
    
    @Override
    public int getHieght() {
        return this.fr.FONT_HEIGHT;
    }
}
