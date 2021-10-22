// 
// Decompiled by Procyon v0.5.36
// 

package sea.mods.huds.impl;

import org.lwjgl.input.Mouse;
import net.minecraft.client.gui.Gui;
import java.awt.Color;
import sea.mods.huds.settings.Settings;
import java.util.ArrayList;
import java.util.List;
import sea.mods.huds.HudMod;

public class CPSMod extends HudMod
{
    private List<Long> clicks;
    private boolean wasPressed;
    private long lastPressed;
    private List<Long> clicks2;
    private boolean wasPressed2;
    private long lastPressed2;
    
    public CPSMod() {
        super("CPSMod", 50, 50, "Render ur Clicks");
        this.clicks = new ArrayList<Long>();
        this.clicks2 = new ArrayList<Long>();
    }
    
    private void drawCps1() {
        if (Settings.getCpsnobg().isEnabled()) {
            Gui.drawRect(this.getX() - 2, this.getY() - 2, this.getX() + this.getWidth(), this.getY() + this.getHieght(), new Color(0, 0, 0, 170).getRGB());
        }
        this.fr.drawStringWithShadow(String.valueOf(this.getCPS()) + " CPS", (float)this.getX(), (float)this.getY(), -1);
    }
    
    private void drawCps2() {
        if (Settings.getCpsnobg().isEnabled()) {
            Gui.drawRect(this.getX() - 2, this.getY() - 2, this.getX() + this.getWidth(), this.getY() + this.getHieght(), new Color(0, 0, 0, 170).getRGB());
        }
        this.fr.drawStringWithShadow("CPS " + this.getCPS2(), (float)this.getX(), (float)this.getY(), -1);
    }
    
    private void drawCps3() {
        if (Settings.getCpsnobg().isEnabled()) {
            Gui.drawRect(this.getX() - 2, this.getY() - 2, this.getX() + this.getWidth(), this.getY() + this.getHieght(), new Color(0, 0, 0, 170).getRGB());
        }
        this.fr.drawStringWithShadow("[" + this.getCPS() + " | " + this.getCPS2() + "]", (float)this.getX(), (float)this.getY(), -1);
    }
    
    @Override
    public void draw() {
        final boolean lpressed = Mouse.isButtonDown(0);
        final boolean rpressed = Mouse.isButtonDown(1);
        if (lpressed != this.wasPressed) {
            this.lastPressed = System.currentTimeMillis();
            this.wasPressed = lpressed;
            if (lpressed) {
                this.clicks.add(this.lastPressed);
            }
        }
        else if (rpressed != this.wasPressed2) {
            this.lastPressed2 = System.currentTimeMillis() + 10L;
            if (this.wasPressed2 = rpressed) {
                this.clicks2.add(this.lastPressed2);
            }
        }
        try {
            if (Settings.getCps1().isEnabled() && this.isEnabled()) {
                this.drawCps1();
            }
            else if (Settings.getCps2().isEnabled() && this.isEnabled()) {
                this.drawCps2();
            }
            else if (Settings.getCps3().isEnabled() && this.isEnabled()) {
                this.drawCps3();
            }
            else {
                this.drawCps1();
            }
        }
        catch (NullPointerException ex) {}
        super.draw();
    }
    
    @Override
    public void renderDummy(final int mouseX, final int mouseY) {
        if (Settings.getCps1().isEnabled() && this.isEnabled()) {
            this.drawCps1();
        }
        else if (Settings.getCps2().isEnabled() && this.isEnabled()) {
            this.drawCps2();
        }
        else if (Settings.getCps3().isEnabled() && this.isEnabled()) {
            this.drawCps3();
        }
        else {
            this.drawCps1();
        }
        super.renderDummy(mouseX, mouseY);
    }
    
    @Override
    public int getWidth() {
        return this.fr.getStringWidth("1 | 1") + 1;
    }
    
    @Override
    public int getHieght() {
        return this.fr.FONT_HEIGHT + 1;
    }
    
    private int getCPS() {
        final long time = System.currentTimeMillis();
        this.clicks.removeIf(aLong -> aLong + 1000L < time);
        return this.clicks.size();
    }
    
    private int getCPS2() {
        final long time2 = System.currentTimeMillis();
        this.clicks2.removeIf(aLong2 -> aLong2 + 1000L < time2);
        return this.clicks2.size();
    }
}
