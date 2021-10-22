// 
// Decompiled by Procyon v0.5.36
// 

package sea.mods.huds.impl;

import java.util.Iterator;
import java.util.Collection;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.Minecraft;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.Tessellator;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.gui.FontRenderer;
import sea.mods.huds.HudMod;

public class ModPotionStatus extends HudMod
{
    protected FontRenderer fontRendererObj;
    protected float zLevelFloat;
    
    public ModPotionStatus() {
        super("PotionStatus", 40, 50, "Render ur Potion status");
    }
    
    public void drawTexturedModalRect(final int x, final int y, final int textureX, final int textureY, final int width, final int height) {
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        final float f = 0.00390625f;
        final float f2 = 0.00390625f;
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos(x + 0, y + height, this.zLevelFloat).tex((textureX + 0) * 0.00390625f, (textureY + height) * 0.00390625f).endVertex();
        worldrenderer.pos(x + width, y + height, this.zLevelFloat).tex((textureX + width) * 0.00390625f, (textureY + height) * 0.00390625f).endVertex();
        worldrenderer.pos(x + width, y + 0, this.zLevelFloat).tex((textureX + width) * 0.00390625f, (textureY + 0) * 0.00390625f).endVertex();
        worldrenderer.pos(x + 0, y + 0, this.zLevelFloat).tex((textureX + 0) * 0.00390625f, (textureY + 0) * 0.00390625f).endVertex();
        tessellator.draw();
    }
    
    @Override
    public void draw() {
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        final boolean offsetX = true;
        final boolean offsetY = true;
        final boolean i = true;
        int i2 = 16;
        final Collection collection = this.mc.thePlayer.getActivePotionEffects();
        if (!collection.isEmpty()) {
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GlStateManager.disableLighting();
            int l = 20;
            if (collection.size() > 100) {
                l = 150 / (collection.size() - 10);
            }
            for (final PotionEffect potioneffect : this.mc.thePlayer.getActivePotionEffects()) {
                final Potion potion = Potion.potionTypes[potioneffect.getPotionID()];
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                if (potion.hasStatusIcon()) {
                    Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("textures/gui/container/inventory.png"));
                    final int s1 = potion.getStatusIconIndex();
                    this.drawTexturedModalRect(this.getX() + 21 - 20, this.getY() + i2 - 14, 0 + s1 % 8 * 18, 198 + s1 / 8 * 18, 18, 18);
                }
                String s2 = I18n.format(potion.getName(), new Object[0]);
                if (potioneffect.getAmplifier() == 1) {
                    s2 = String.valueOf(String.valueOf(s2)) + " " + I18n.format("enchantment.level.2", new Object[0]);
                }
                else if (potioneffect.getAmplifier() == 2) {
                    s2 = String.valueOf(String.valueOf(s2)) + " " + I18n.format("enchantment.level.3", new Object[0]);
                }
                else if (potioneffect.getAmplifier() == 3) {
                    s2 = String.valueOf(String.valueOf(s2)) + " " + I18n.format("enchantment.level.4", new Object[0]);
                }
                this.fr.drawString(s2, this.getX() + 21, this.getY() + i2 - 14, -1);
                final String s3 = Potion.getDurationString(potioneffect);
                this.fr.drawString(s3, this.getX() + 21, this.getY() + i2 + 10 - 14, -1);
                i2 += l;
            }
        }
    }
    
    @Override
    public void renderDummy(final int mouseX, final int mouseY) {
        this.fr.drawString("Absortion", this.getX(), this.getY() + 10, -1);
        this.fr.drawString("Speed", this.getX(), this.getY() + 28, -1);
        this.fr.drawString("Resistance", this.getX(), this.getY() + 46, -1);
        super.renderDummy(mouseX, mouseY);
    }
}
