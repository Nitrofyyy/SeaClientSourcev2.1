// 
// Decompiled by Procyon v0.5.36
// 

package sea.mods.huds.impl;

import java.util.Iterator;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.ResourcePackRepository;
import java.util.List;
import com.google.common.collect.Lists;
import net.minecraft.client.gui.FontRenderer;
import sea.mods.huds.HudMod;

public class PackDisplay extends HudMod
{
    private static final FontRenderer Gapple;
    int width;
    
    static {
        Gapple = null;
    }
    
    public PackDisplay() {
        super("PackDisplay", 5, 5, "Displays the current Pack");
    }
    
    @Override
    public int getWidth() {
        return this.width;
    }
    
    @Override
    public int getHieght() {
        return this.mc.getResourcePackRepository().getRepositoryEntries().size() * 32 + (this.mc.getResourcePackRepository().getRepositoryEntries().size() - 1) * 2;
    }
    
    @Override
    public void draw() {
        this.width = 0;
        final byte offset = 0;
        for (final ResourcePackRepository.Entry entry : Lists.reverse((List)this.mc.getResourcePackRepository().getRepositoryEntries())) {
            this.width = Math.max(this.width, Minecraft.fontRendererObj.getStringWidth(entry.getResourcePackName()) + 38);
            final List list = Minecraft.fontRendererObj.listFormattedStringToWidth(entry.getTexturePackDescription(), 157);
            for (int font = 0; font < 2 && font < list.size(); ++font) {
                this.width = Math.max(this.width, Minecraft.fontRendererObj.getStringWidth(list.get(font)) + 38);
            }
        }
        for (final ResourcePackRepository.Entry entry : Lists.reverse((List)this.mc.getResourcePackRepository().getRepositoryEntries())) {
            this.drawRect(this.getX(), this.getY() + offset, this.getX() + this.width, this.getX() + offset + 32, new Color(0, 0, 0, 100).getRGB());
            final List list = Minecraft.fontRendererObj.listFormattedStringToWidth(entry.getTexturePackDescription(), 157);
            final FontRenderer fontrenderer = this.fr;
            fontrenderer.drawString(entry.getResourcePackName(), (float)(this.getX() + 36), this.getY() + 0 + offset + 1.0f, 16777215, true);
            for (int l = 0; l < 2 && l < list.size(); ++l) {
                this.fr.drawString(list.get(l), (float)(this.getX() + 36), this.getY() + 11.0f + 10 * l, 8421504, true);
                entry.bindTexturePackIcon(this.mc.getTextureManager());
                GlStateManager.pushMatrix();
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                Gui.drawModalRectWithCustomSizedTexture(this.getX(), this.getY(), 0.0f, 0.0f, 32, 32, 32.0f, 32.0f);
                GlStateManager.enableTexture2D();
                GlStateManager.popMatrix();
            }
        }
        super.draw();
    }
    
    private void drawRect(final int getxPosition, final int i, final int j, final int k, final int rgb) {
    }
    
    @Override
    public void renderDummy(final int mouseX, final int mouseY) {
        this.width = 0;
        final byte offset = 0;
        for (final ResourcePackRepository.Entry entry : Lists.reverse((List)this.mc.getResourcePackRepository().getRepositoryEntries())) {
            this.width = Math.max(this.width, Minecraft.fontRendererObj.getStringWidth(entry.getResourcePackName()) + 38);
            final List list = Minecraft.fontRendererObj.listFormattedStringToWidth(entry.getTexturePackDescription(), 157);
            for (int font = 0; font < 2 && font < list.size(); ++font) {
                this.width = Math.max(this.width, Minecraft.fontRendererObj.getStringWidth(list.get(font)) + 38);
            }
        }
        for (final ResourcePackRepository.Entry entry : Lists.reverse((List)this.mc.getResourcePackRepository().getRepositoryEntries())) {
            this.drawRect(this.getX(), this.getY() + offset, this.getX() + this.width, this.getX() + 32, new Color(0, 0, 0, 100).getRGB());
            final List list = Minecraft.fontRendererObj.listFormattedStringToWidth(entry.getTexturePackDescription(), 157);
            final FontRenderer fontrenderer = this.fr;
            fontrenderer.drawString(entry.getResourcePackName(), (float)(this.getX() + 36), this.getY() + 0 + 1.0f, 16777215, true);
            for (int l = 0; l < 2 && l < list.size(); ++l) {
                this.fr.drawString(list.get(l), (float)(this.getX() + 36), this.getY() + 11.0f + 10 * l, 8421504, true);
                entry.bindTexturePackIcon(this.mc.getTextureManager());
                GlStateManager.pushMatrix();
                GlStateManager.enableBlend();
                GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                Gui.drawModalRectWithCustomSizedTexture(this.getX(), this.getY(), 0.0f, 0.0f, 32, 32, 32.0f, 32.0f);
                GlStateManager.enableTexture2D();
                GlStateManager.popMatrix();
            }
        }
        super.renderDummy(mouseX, mouseY);
    }
}
