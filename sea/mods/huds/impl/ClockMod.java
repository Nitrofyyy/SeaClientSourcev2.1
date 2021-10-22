// 
// Decompiled by Procyon v0.5.36
// 

package sea.mods.huds.impl;

import java.time.temporal.TemporalAccessor;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import net.minecraft.client.gui.Gui;
import java.awt.Color;
import sea.SeaClient;
import sea.mods.huds.settings.Settings;
import sea.mods.huds.HudMod;

public class ClockMod extends HudMod
{
    public ClockMod() {
        super("TimeMod", 70, 70, "Time");
    }
    
    @Override
    public int getWidth() {
        return this.fr.getStringWidth(this.getTime());
    }
    
    @Override
    public int getHieght() {
        return this.fr.FONT_HEIGHT;
    }
    
    @Override
    public void renderDummy(final int mouseX, final int mouseY) {
        this.renderTime();
        super.renderDummy(mouseX, mouseY);
    }
    
    @Override
    public void draw() {
        this.renderTime();
        super.draw();
    }
    
    private void renderTime() {
        try {
            if (Settings.getClock1().isEnabled() && SeaClient.getINSTANCE().hudList.ClockMod.isEnabled()) {
                if (Settings.getTimenobg().isEnabled()) {
                    Gui.drawRect(this.getX() - 2, this.getY() - 2, this.getX() + this.getWidth() + 7, this.getY() + this.getHieght(), new Color(0, 0, 0, 170).getRGB());
                }
                this.fr.drawStringWithShadow("§3:§f" + this.getTime() + "§3:§f", (float)(this.getX() + 1), (float)(this.getY() + 1), -1);
                final Settings settings = SeaClient.INSTANCE.settings;
                Settings.clock2.setEnabled(false);
                final Settings settings2 = SeaClient.INSTANCE.settings;
                Settings.clock3.setEnabled(false);
            }
            else if (Settings.getClock2().isEnabled() && SeaClient.getINSTANCE().hudList.ClockMod.isEnabled()) {
                if (Settings.getTimenobg().isEnabled()) {
                    Gui.drawRect(this.getX() - 2, this.getY() - 2, this.getX() + this.getWidth() + 7, this.getY() + this.getHieght(), new Color(0, 0, 0, 170).getRGB());
                }
                this.fr.drawStringWithShadow("§3Time : §f" + this.getTime(), (float)(this.getX() + 1), (float)(this.getY() + 1), -1);
                final Settings settings3 = SeaClient.INSTANCE.settings;
                Settings.clock1.setEnabled(false);
                final Settings settings4 = SeaClient.INSTANCE.settings;
                Settings.clock3.setEnabled(false);
            }
            else if (Settings.getClock3().isEnabled() && this.isEnabled()) {
                if (Settings.getTimenobg().isEnabled()) {
                    Gui.drawRect(this.getX() - 2, this.getY() - 2, this.getX() + this.getWidth() + 7, this.getY() + this.getHieght(), new Color(0, 0, 0, 170).getRGB());
                }
                this.fr.drawStringWithShadow(this.getTime(), (float)(this.getX() + 1), (float)(this.getY() + 1), -1);
            }
            else {
                if (Settings.getTimenobg().isEnabled()) {
                    Gui.drawRect(this.getX() - 2, this.getY() - 2, this.getX() + this.getWidth() + 7, this.getY() + this.getHieght(), new Color(0, 0, 0, 170).getRGB());
                }
                this.fr.drawStringWithShadow("§3[§f" + this.getTime() + "§3]§f", (float)(this.getX() + 1), (float)(this.getY() + 1), -1);
            }
        }
        catch (NullPointerException ex) {}
    }
    
    private String getTime() {
        final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("h:mm a");
        final LocalDateTime localtime = LocalDateTime.now();
        return dtf.format(localtime);
    }
}
