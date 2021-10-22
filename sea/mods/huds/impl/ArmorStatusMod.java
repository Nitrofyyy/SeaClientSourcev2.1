// 
// Decompiled by Procyon v0.5.36
// 

package sea.mods.huds.impl;

import net.minecraft.client.renderer.RenderHelper;
import org.lwjgl.opengl.GL11;
import net.minecraft.item.Item;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import sea.mods.huds.HudMod;

public class ArmorStatusMod extends HudMod
{
    public ArmorStatusMod() {
        super("AmorStatusMod", 90, 90, "Render ur armor");
    }
    
    @Override
    public int getWidth() {
        return 64;
    }
    
    @Override
    public int getHieght() {
        return 64;
    }
    
    @Override
    public void draw() {
        for (int i = 0; i < this.mc.thePlayer.inventory.armorInventory.length; ++i) {
            final ItemStack itemStack = this.mc.thePlayer.inventory.armorInventory[i];
            this.renderItemStack(i, itemStack);
        }
        super.draw();
    }
    
    @Override
    public void renderDummy(final int mouseX, final int mouseY) {
        this.renderItemStack(4, new ItemStack(Items.diamond_helmet));
        this.renderItemStack(3, new ItemStack(Items.diamond_chestplate));
        this.renderItemStack(2, new ItemStack(Items.diamond_leggings));
        this.renderItemStack(1, new ItemStack(Items.diamond_boots));
        for (int i = 1; i < this.mc.thePlayer.inventory.armorInventory.length; ++i) {
            final ItemStack itemstack = this.mc.thePlayer.inventory.armorInventory[i];
            super.renderDummy(mouseX, mouseY);
        }
    }
    
    private void renderItemStack(final int i, final ItemStack is) {
        if (is != null) {
            GL11.glPushMatrix();
            final int yAdd = -16 * i + 48;
            if (is.getItem().isDamageable()) {
                final int damage = (is.getMaxDamage() - is.getItemDamage()) / is.getMaxDamage() * 100;
                this.fr.drawString(String.format("%d", damage), this.getX() + 20, this.getY() + yAdd + 5, -1);
                if (this.mc.thePlayer != null && is != null) {
                    if (is.getItem().isDamageable()) {
                        final int damage2 = (is.getMaxDamage() - is.getItemDamage()) / is.getMaxDamage() * 100;
                        this.fr.drawString(String.format("%d", damage), this.getX() + 20, this.getY() + yAdd + 5, -1);
                    }
                    if (is.isStackable() && this.mc.thePlayer.getHeldItem().stackSize != 1) {
                        this.fr.drawString(Integer.toString(this.mc.thePlayer.getHeldItem().stackSize), this.getX() + 20, this.getY() + yAdd + 5, -1);
                    }
                }
                RenderHelper.enableGUIStandardItemLighting();
                this.mc.getRenderItem().renderItemAndEffectIntoGUI(is, this.getX(), this.getY() + yAdd);
                GL11.glPopMatrix();
            }
        }
    }
}
