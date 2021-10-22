// 
// Decompiled by Procyon v0.5.36
// 

package sea.mods.huds.impl;

import net.minecraft.client.gui.Gui;
import java.awt.Color;
import sea.mods.huds.HudMod;

public class ToggleSprintHud extends HudMod
{
    public ToggleSprintHud() {
        super("ToggleSprint", 70, 20, "Render if ur sprinting");
    }
    
    @Override
    public void draw() {
        if (this.mc.thePlayer.isSprinting()) {
            Gui.drawRect(this.getX() - 2, this.getY() - 2, this.getX() + this.getWidth(), this.getY() + this.getHieght(), new Color(0, 0, 0, 170).getRGB());
            this.fr.drawString("§3[§fSprinting§b§3]§f", this.getX(), this.getY(), -1);
        }
        else {
            Gui.drawRect(this.getX() - 2, this.getY() - 2, this.getX() + this.getWidth() + 20, this.getY() + this.getHieght(), new Color(0, 0, 0, 170).getRGB());
            this.fr.drawString("§3[§fNot Sprinting§b§3]§f", this.getX(), this.getY(), -1);
        }
        super.draw();
    }
    
    @Override
    public void renderDummy(final int mouseX, final int mouseY) {
        Gui.drawRect(this.getX() - 2, this.getY() - 2, this.getX() + this.getWidth(), this.getY() + this.getHieght(), new Color(0, 0, 0, 170).getRGB());
        this.fr.drawStringWithShadow("ToggleSprint", (float)this.getX(), (float)this.getY(), -1);
        super.renderDummy(mouseX, mouseY);
    }
    
    @Override
    public int getWidth() {
        return this.fr.getStringWidth("§3[§fSprinting§b§3]§f");
    }
    
    @Override
    public int getHieght() {
        return this.fr.FONT_HEIGHT + 1;
    }
}
