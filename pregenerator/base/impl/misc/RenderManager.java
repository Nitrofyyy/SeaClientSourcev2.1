// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.base.impl.misc;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.gui.FontRenderer;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pregenerator.base.api.misc.IRenderHelper;

@SideOnly(Side.CLIENT)
public class RenderManager implements IRenderHelper
{
    Minecraft mc;
    
    @Override
    public IRenderHelper init() {
        this.mc = Minecraft.func_71410_x();
        return this;
    }
    
    @Override
    public void renderText(final int x, final int y, final int width, final String text) {
        final FontRenderer font = this.mc.field_71466_p;
        GL11.glPushMatrix();
        GL11.glScalef(0.5f, 0.5f, 1.0f);
        GL11.glTranslatef((float)(x + width / 2), (float)(y + 3), 0.0f);
        font.func_175063_a(text, (float)x, (float)(y - 2), 16777215);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glPopMatrix();
    }
    
    @Override
    public void renderCenterText(final int x, final int y, final int width, final String text) {
        final FontRenderer font = this.mc.field_71466_p;
        GL11.glPushMatrix();
        GL11.glScalef(0.5f, 0.5f, 1.0f);
        GL11.glTranslatef((float)(x + width / 2), (float)(y + 3), 0.0f);
        font.func_175063_a(text, (float)(x + width / 2 - font.func_78256_a(text) / 2), (float)(y - 2), 16777215);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glPopMatrix();
    }
    
    @Override
    public void renderArea(final int x, final int y, final int width, final int height) {
        GL11.glPushMatrix();
        GlStateManager.func_179101_C();
        RenderHelper.func_74518_a();
        GlStateManager.func_179140_f();
        GlStateManager.func_179097_i();
        final int i = 10 + width;
        final int l1 = x + 12;
        final int i2 = y - 12;
        final int k = 10 + height;
        final int j = -267386864;
        this.drawGradientRect(l1 - 3, i2 - 4, l1 + i + 3, i2 - 3, -267386864, -267386864);
        this.drawGradientRect(l1 - 3, i2 + k + 3, l1 + i + 3, i2 + k + 4, -267386864, -267386864);
        this.drawGradientRect(l1 - 3, i2 - 3, l1 + i + 3, i2 + k + 3, -267386864, -267386864);
        this.drawGradientRect(l1 - 4, i2 - 3, l1 - 3, i2 + k + 3, -267386864, -267386864);
        this.drawGradientRect(l1 + i + 3, i2 - 3, l1 + i + 4, i2 + k + 3, -267386864, -267386864);
        final int j2 = 1344798847;
        this.drawGradientRect(l1 - 3, i2 - 3 + 1, l1 - 3 + 1, i2 + k + 3 - 1, 1347420415, 1344798847);
        this.drawGradientRect(l1 + i + 2, i2 - 3 + 1, l1 + i + 3, i2 + k + 3 - 1, 1347420415, 1344798847);
        this.drawGradientRect(l1 - 3, i2 - 3, l1 + i + 3, i2 - 3 + 1, 1347420415, 1347420415);
        this.drawGradientRect(l1 - 3, i2 + k + 2, l1 + i + 3, i2 + k + 3, 1344798847, 1344798847);
        GL11.glPopMatrix();
    }
    
    protected void drawGradientRect(final int left, final int top, final int right, final int bottom, final int startColor, final int endColor) {
        final float f = (startColor >> 24 & 0xFF) / 255.0f;
        final float f2 = (startColor >> 16 & 0xFF) / 255.0f;
        final float f3 = (startColor >> 8 & 0xFF) / 255.0f;
        final float f4 = (startColor & 0xFF) / 255.0f;
        final float f5 = (endColor >> 24 & 0xFF) / 255.0f;
        final float f6 = (endColor >> 16 & 0xFF) / 255.0f;
        final float f7 = (endColor >> 8 & 0xFF) / 255.0f;
        final float f8 = (endColor & 0xFF) / 255.0f;
        GlStateManager.func_179090_x();
        GlStateManager.func_179147_l();
        GlStateManager.func_179118_c();
        GlStateManager.func_179120_a(770, 771, 1, 0);
        GlStateManager.func_179103_j(7425);
        final Tessellator tessellator = Tessellator.func_178181_a();
        final WorldRenderer vertexbuffer = tessellator.func_178180_c();
        vertexbuffer.func_181668_a(7, DefaultVertexFormats.field_181706_f);
        vertexbuffer.func_181662_b((double)right, (double)top, -10.0).func_181666_a(f2, f3, f4, f).func_181675_d();
        vertexbuffer.func_181662_b((double)left, (double)top, -10.0).func_181666_a(f2, f3, f4, f).func_181675_d();
        vertexbuffer.func_181662_b((double)left, (double)bottom, -10.0).func_181666_a(f6, f7, f8, f5).func_181675_d();
        vertexbuffer.func_181662_b((double)right, (double)bottom, -10.0).func_181666_a(f6, f7, f8, f5).func_181675_d();
        tessellator.func_78381_a();
        GlStateManager.func_179103_j(7424);
        GlStateManager.func_179084_k();
        GlStateManager.func_179141_d();
        GlStateManager.func_179098_w();
    }
    
    @Override
    public void renderBar(final int x, final int y, final int width, final int progress, final String text) {
        final FontRenderer font = this.mc.field_71466_p;
        this.mc.func_110434_K().func_110577_a(Gui.field_110324_m);
        this.drawTexturedModalRect(x, y, 0, 74, width, 182, 5, 0);
        this.drawTexturedModalRect(x, y, 0, 74, width, 182, 5, 0);
        if (progress > 0) {
            this.drawTexturedModalRect(x, y, 0, 79, progress, 182, 5, 0);
        }
        GL11.glPushMatrix();
        GL11.glScalef(0.5f, 0.5f, 1.0f);
        GL11.glTranslatef((float)(x + width / 2), (float)(y + 3), 0.0f);
        font.func_175063_a(text, (float)(x + width / 2 - font.func_78256_a(text) / 2), (float)(y - 2), 16777215);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glPopMatrix();
    }
    
    private void drawTexturedModalRect(final int x, final int y, final int textureX, final int textureY, final int width, final int texWidth, final int height, final int zLevel) {
        final float f = 0.00390625f;
        final float f2 = 0.00390625f;
        final Tessellator tessellator = Tessellator.func_178181_a();
        final WorldRenderer vertexbuffer = tessellator.func_178180_c();
        vertexbuffer.func_181668_a(7, DefaultVertexFormats.field_181707_g);
        vertexbuffer.func_181662_b((double)(x + 0), (double)(y + height), (double)zLevel).func_181673_a((double)((textureX + 0) * f), (double)((textureY + height) * f2)).func_181675_d();
        vertexbuffer.func_181662_b((double)(x + width), (double)(y + height), (double)zLevel).func_181673_a((double)((textureX + texWidth) * f), (double)((textureY + height) * f2)).func_181675_d();
        vertexbuffer.func_181662_b((double)(x + width), (double)(y + 0), (double)zLevel).func_181673_a((double)((textureX + texWidth) * f), (double)((textureY + 0) * f2)).func_181675_d();
        vertexbuffer.func_181662_b((double)(x + 0), (double)(y + 0), (double)zLevel).func_181673_a((double)((textureX + 0) * f), (double)((textureY + 0) * f2)).func_181675_d();
        tessellator.func_78381_a();
    }
}
