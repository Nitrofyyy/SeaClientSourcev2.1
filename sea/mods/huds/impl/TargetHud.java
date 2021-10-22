// 
// Decompiled by Procyon v0.5.36
// 

package sea.mods.huds.impl;

import net.minecraft.client.gui.inventory.GuiInventory;
import java.awt.Color;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import sea.mods.huds.HudMod;

public class TargetHud extends HudMod
{
    EntityLivingBase render;
    Entity renderEnity;
    Gui gui;
    NetworkPlayerInfo networkplayerinfo;
    
    public TargetHud() {
        super("TargetHud", 50, 60, "Render ur Enemy's detail");
        this.gui = new Gui();
    }
    
    @Override
    public int getHieght() {
        return 50;
    }
    
    @Override
    public int getWidth() {
        return 90;
    }
    
    @Override
    public void draw() {
        this.render = (EntityLivingBase)this.mc.pointedEntity;
        if (this.render != null) {
            Gui.drawRect(this.getX() - 49, this.getY() - 39, this.getX() + this.getWidth() - 23, this.getY() - 36 + this.getHieght(), new Color(0, 51, 153, 255).getRGB());
            Gui.drawRect(this.getX() - 39, this.getY() - 39, this.getX() + this.getWidth() - 33, this.getY() - 36 + this.getHieght(), new Color(0, 0, 0, 170).getRGB());
            Gui.drawRect(this.getX() - 39, this.getY() - 39, this.getX() + this.getWidth() - 33, this.getY() - 36 + this.getHieght(), new Color(0, 0, 0, 170).getRGB());
            Gui.drawRect(this.getX() - 39, this.getY() - 39, this.getX() + this.getWidth() - 33, this.getY() - 36 + this.getHieght(), new Color(0, 0, 0, 170).getRGB());
            Gui.drawRect(this.getX() - 39, this.getY() - 39, this.getX() + this.getWidth() - 33, this.getY() - 36 + this.getHieght(), new Color(0, 0, 0, 170).getRGB());
            Gui.drawRect(this.getX() - 39, this.getY() - 39, this.getX() + this.getWidth() - 33, this.getY() - 36 + this.getHieght(), new Color(0, 0, 0, 170).getRGB());
            this.fr.drawStringWithShadow(this.render.getName(), (float)this.getX(), (float)(this.getY() - 10), -1);
            this.fr.drawStringWithShadow("§c" + (int)this.render.getHealth() + "§f", (float)this.getX(), (float)this.getY(), -1);
            GuiInventory.drawEntityOnScreen(this.getX() - 17, this.getY() + 10, 18, 18.0f, 0.0f, this.render);
            super.draw();
        }
    }
    
    @Override
    public void renderDummy(final int mouseX, final int mouseY) {
        Gui.drawRect(this.getX() - 49, this.getY() - 39, this.getX() + this.getWidth() - 23, this.getY() - 36 + this.getHieght(), new Color(0, 51, 153, 255).getRGB());
        Gui.drawRect(this.getX() - 39, this.getY() - 39, this.getX() + this.getWidth() - 33, this.getY() - 36 + this.getHieght(), new Color(0, 0, 0, 170).getRGB());
        Gui.drawRect(this.getX() - 39, this.getY() - 39, this.getX() + this.getWidth() - 33, this.getY() - 36 + this.getHieght(), new Color(0, 0, 0, 170).getRGB());
        Gui.drawRect(this.getX() - 39, this.getY() - 39, this.getX() + this.getWidth() - 33, this.getY() - 36 + this.getHieght(), new Color(0, 0, 0, 170).getRGB());
        Gui.drawRect(this.getX() - 39, this.getY() - 39, this.getX() + this.getWidth() - 33, this.getY() - 36 + this.getHieght(), new Color(0, 0, 0, 170).getRGB());
        Gui.drawRect(this.getX() - 39, this.getY() - 39, this.getX() + this.getWidth() - 33, this.getY() - 36 + this.getHieght(), new Color(0, 0, 0, 170).getRGB());
        this.fr.drawStringWithShadow(this.mc.thePlayer.getName(), (float)this.getX(), (float)(this.getY() - 10), -1);
        this.fr.drawStringWithShadow("§c" + (int)this.mc.thePlayer.getHealth() + "§f", (float)this.getX(), (float)this.getY(), -1);
        GuiInventory.drawEntityOnScreen(this.getX() - 17, this.getY() + 10, 18, 18.0f, 0.0f, this.mc.thePlayer);
        super.renderDummy(mouseX, mouseY);
    }
}
