// 
// Decompiled by Procyon v0.5.36
// 

package sea.mods.huds.impl;

import net.minecraft.client.gui.Gui;
import java.awt.Color;
import sea.mods.huds.settings.Settings;
import sea.mods.huds.HudMod;

public class PingMod extends HudMod
{
    public PingMod() {
        super("Ping", 100, 80, "Render ur ping");
    }
    
    private void drawPing1() {
        if (Settings.getPingnobg().isEnabled()) {
            Gui.drawRect(this.getX() - 2, this.getY() - 2, this.getX() + this.getWidth(), this.getY() + this.getHieght(), new Color(0, 0, 0, 170).getRGB());
        }
        if (this.mc.getCurrentServerData() == null && this.mc.isSingleplayer()) {
            Gui.drawCenteredString(this.fr, "ms : -1", (float)(this.getX() + this.getWidth() / 2), (float)(this.getY() + 1), -1);
        }
        else if (this.mc.getCurrentServerData().pingToServer <= 10L) {
            Gui.drawCenteredString(this.fr, "ms : §2" + this.mc.getCurrentServerData().pingToServer, (float)(this.getX() + this.getWidth() / 2), (float)(this.getY() + 1), -1);
        }
        else if (this.mc.getCurrentServerData().pingToServer <= 100L) {
            Gui.drawCenteredString(this.fr, "ms : §a" + this.mc.getCurrentServerData().pingToServer, (float)(this.getX() + this.getWidth() / 2), (float)(this.getY() + 1), -1);
        }
        else if (this.mc.getCurrentServerData().pingToServer >= 200L) {
            Gui.drawCenteredString(this.fr, "ms : §c" + this.mc.getCurrentServerData().pingToServer, (float)(this.getX() + this.getWidth() / 2), (float)(this.getY() + 1), -1);
        }
        else if (this.mc.getCurrentServerData().pingToServer >= 300L) {
            Gui.drawCenteredString(this.fr, "ms : §4" + this.mc.getCurrentServerData().pingToServer, (float)(this.getX() + this.getWidth() / 2), (float)(this.getY() + 1), -1);
        }
    }
    
    private void drawPing2() {
        if (Settings.getPingnobg().isEnabled()) {
            Gui.drawRect(this.getX() - 2, this.getY() - 2, this.getX() + this.getWidth(), this.getY() + this.getHieght(), new Color(0, 0, 0, 170).getRGB());
        }
        if (this.mc.getCurrentServerData() == null && this.mc.isSingleplayer()) {
            Gui.drawCenteredString(this.fr, "ms : -1", (float)(this.getX() + this.getWidth() / 2), (float)(this.getY() + 1), -1);
        }
        else {
            Gui.drawCenteredString(this.fr, "Ping : " + this.mc.getCurrentServerData().pingToServer, (float)(this.getX() + this.getWidth() / 2), (float)(this.getY() + 1), -1);
        }
    }
    
    private void drawPing3() {
        if (Settings.getPingnobg().isEnabled()) {
            Gui.drawRect(this.getX() - 2, this.getY() - 2, this.getX() + this.getWidth(), this.getY() + this.getHieght(), new Color(0, 0, 0, 170).getRGB());
        }
        if (this.mc.getCurrentServerData() == null && this.mc.isSingleplayer()) {
            Gui.drawCenteredString(this.fr, "ms : -1", (float)(this.getX() + this.getWidth() / 2), (float)(this.getY() + 1), -1);
        }
        else {
            Gui.drawCenteredString(this.fr, "Pong : " + this.mc.getCurrentServerData().pingToServer, (float)(this.getX() + this.getWidth() / 2), (float)(this.getY() + 1), -1);
        }
    }
    
    @Override
    public void draw() {
        if (Settings.getPing1().isEnabled()) {
            this.drawPing1();
        }
        else if (Settings.getPing2().isEnabled()) {
            this.drawPing2();
        }
        else if (Settings.getPing3().isEnabled()) {
            this.drawPing3();
        }
        else {
            this.drawPing1();
        }
    }
    
    @Override
    public void renderDummy(final int mouseX, final int mouseY) {
        Gui.drawRect(this.getX() - 2, this.getY() - 2, this.getX() + this.getWidth(), this.getY() + this.getHieght(), new Color(0, 0, 0, 170).getRGB());
        Gui.drawCenteredString(this.fr, "ms -1", (float)(this.getX() + this.getWidth() / 2), (float)(this.getY() + 1), -1);
        super.renderDummy(mouseX, mouseY);
    }
    
    @Override
    public int getWidth() {
        return (this.mc.getCurrentServerData() != null) ? (this.fr.getStringWidth("ms : 999") + 8) : this.fr.getStringWidth("Ping ms");
    }
    
    @Override
    public int getHieght() {
        return 10;
    }
}
