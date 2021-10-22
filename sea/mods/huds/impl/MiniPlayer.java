// 
// Decompiled by Procyon v0.5.36
// 

package sea.mods.huds.impl;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.entity.AbstractClientPlayer;
import sea.mods.huds.HudMod;

public class MiniPlayer extends HudMod
{
    public AbstractClientPlayer player;
    
    public MiniPlayer() {
        super("Miniplayer", 5, 5, "Like bedrock");
    }
    
    @Override
    public void draw() {
        if (this.mc.thePlayer.isSprinting() || this.mc.thePlayer.isSneaking()) {
            GuiInventory.drawEntityOnScreen(this.getX() + 15, this.getY() + 50, 25, 50.0f, 0.0f, this.mc.thePlayer);
        }
        super.draw();
    }
    
    @Override
    public void renderDummy(final int mouseX, final int mouseY) {
        GuiInventory.drawEntityOnScreen(this.getX() + 15, this.getY() + 50, 25, 50.0f, 0.0f, this.mc.thePlayer);
        super.renderDummy(mouseX, mouseY);
    }
    
    @Override
    public int getHieght() {
        return 20;
    }
    
    @Override
    public int getWidth() {
        return 30;
    }
}
