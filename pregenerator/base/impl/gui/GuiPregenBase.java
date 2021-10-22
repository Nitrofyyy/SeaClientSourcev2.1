// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.base.impl.gui;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.Tessellator;
import java.util.List;
import net.minecraftforge.fml.client.config.GuiButtonExt;
import java.util.HashMap;
import net.minecraft.client.gui.GuiButton;
import java.util.Map;
import net.minecraft.client.gui.GuiScreen;

public class GuiPregenBase extends GuiScreen
{
    public int centerX;
    public int centerY;
    Map<Integer, GuiButton> buttonMap;
    
    public GuiPregenBase() {
        this.buttonMap = new HashMap<Integer, GuiButton>();
    }
    
    public void func_73866_w_() {
        super.func_73866_w_();
        this.field_146292_n.clear();
        this.buttonMap.clear();
        this.centerX = this.field_146294_l / 2;
        this.centerY = this.field_146295_m / 2;
    }
    
    public GuiButton registerButton(final int id, final int x, final int y, final int width, final int height, final String name) {
        final GuiButton button = (GuiButton)new GuiButtonExt(id, this.centerX + x, this.centerY + y, width, height, name);
        this.field_146292_n.add(button);
        this.buttonMap.put(id, button);
        return button;
    }
    
    public GuiButton registerUnmovedButton(final int id, final int x, final int y, final int width, final int height, final String name) {
        final GuiButton button = (GuiButton)new GuiButtonExt(id, x, y, width, height, name);
        this.field_146292_n.add(button);
        this.buttonMap.put(id, button);
        return button;
    }
    
    public <T extends GuiButton> T registerButton(final T button) {
        this.field_146292_n.add(button);
        this.buttonMap.put(button.field_146127_k, button);
        return button;
    }
    
    public GuiButton getIDButton(final int id) {
        return this.buttonMap.get(id);
    }
    
    public boolean isInsideBox(final int mouseX, final int mouseY, final int minX, final int minY, final int maxX, final int maxY) {
        return mouseX >= this.centerX + minX && mouseX < this.centerX + maxX && mouseY >= this.centerY + minY && mouseY <= this.centerY + maxY;
    }
    
    public void drawListText(final List<String> lines, final int x, final int y) {
        this.func_146283_a((List)lines, x, y);
    }
    
    public void drawSimpleText(final String s, final int x, final int y, final int color) {
        this.field_146289_q.func_78276_b(s, x, y, color);
    }
    
    public void drawText(final String s, final int x, final int y, final int color) {
        this.field_146289_q.func_78276_b(s, this.centerX + x, this.centerY + y, color);
    }
    
    public void drawSplitText(final String s, final int x, final int y, final int limit, final int color) {
        this.field_146289_q.func_78279_b(s, this.centerX + x, this.centerY + y, limit, color);
    }
    
    public void drawCenterText(final String s, final int x, final int y, final int color) {
        this.field_146289_q.func_78276_b(s, this.centerX + x - this.field_146289_q.func_78256_a(s) / 2, this.centerY + y, color);
    }
    
    public void drawLeftText(final String s, final int x, final int y, final int color) {
        this.field_146289_q.func_78276_b(s, this.centerX + x - this.field_146289_q.func_78256_a(s), this.centerY + y, color);
    }
    
    public void drawSimpleRect(final int minX, final int minY, final int maxX, final int maxY, final int color, final boolean drop) {
        if (drop) {
            func_73734_a(minX - 1, minY - 1, maxX, maxY, -13158601);
            func_73734_a(minX, minY, maxX + 1, maxY + 1, -1);
        }
        else {
            func_73734_a(minX, minY, maxX + 1, maxY + 1, -13158601);
            func_73734_a(minX - 1, minY - 1, maxX, maxY, -1);
        }
        func_73734_a(minX, minY, maxX, maxY, color);
    }
    
    public void drawRectangle(final int xSize, final int ySize, final int xOffset, final int yOffset, final int color, final boolean drop) {
        final int minX = this.centerX - xSize + xOffset;
        final int minY = this.centerY - ySize + yOffset;
        final int maxX = this.centerX + xSize + xOffset;
        final int maxY = this.centerY + ySize + yOffset;
        if (drop) {
            func_73734_a(minX - 1, minY - 1, maxX, maxY, -13158601);
            func_73734_a(minX, minY, maxX + 1, maxY + 1, -1);
        }
        else {
            func_73734_a(minX, minY, maxX + 1, maxY + 1, -13158601);
            func_73734_a(minX - 1, minY - 1, maxX, maxY, -1);
        }
        func_73734_a(minX, minY, maxX, maxY, color);
    }
    
    public void renderTextureWithOffset(final float x, final float y, final float width, final float height, final float zLevel) {
        this.renderTexture(x + this.centerX, y + this.centerY, width, height, zLevel);
    }
    
    public void renderTexture(final float x, final float y, final float width, final float height, final float zLevel) {
        final Tessellator tessellator = Tessellator.func_178181_a();
        final WorldRenderer worldrenderer = tessellator.func_178180_c();
        worldrenderer.func_181668_a(7, DefaultVertexFormats.field_181707_g);
        worldrenderer.func_181662_b((double)x, (double)(y + height), (double)zLevel).func_181673_a(0.0, 1.0).func_181675_d();
        worldrenderer.func_181662_b((double)(x + width), (double)(y + height), (double)zLevel).func_181673_a(1.0, 1.0).func_181675_d();
        worldrenderer.func_181662_b((double)(x + width), (double)y, (double)zLevel).func_181673_a(1.0, 0.0).func_181675_d();
        worldrenderer.func_181662_b((double)x, (double)y, (double)zLevel).func_181673_a(0.0, 0.0).func_181675_d();
        tessellator.func_78381_a();
    }
    
    public void drawArea(final float left, final float top, final float right, final float bottom, final int color) {
        final float f3 = (color >> 24 & 0xFF) / 255.0f;
        final float f4 = (color >> 16 & 0xFF) / 255.0f;
        final float f5 = (color >> 8 & 0xFF) / 255.0f;
        final float f6 = (color & 0xFF) / 255.0f;
        final Tessellator tessellator = Tessellator.func_178181_a();
        final WorldRenderer worldrenderer = tessellator.func_178180_c();
        GlStateManager.func_179147_l();
        GlStateManager.func_179090_x();
        GlStateManager.func_179120_a(770, 771, 1, 0);
        GlStateManager.func_179131_c(f4, f5, f6, f3);
        worldrenderer.func_181668_a(7, DefaultVertexFormats.field_181705_e);
        worldrenderer.func_181662_b((double)left, (double)bottom, 0.0).func_181675_d();
        worldrenderer.func_181662_b((double)right, (double)bottom, 0.0).func_181675_d();
        worldrenderer.func_181662_b((double)right, (double)top, 0.0).func_181675_d();
        worldrenderer.func_181662_b((double)left, (double)top, 0.0).func_181675_d();
        tessellator.func_78381_a();
        GlStateManager.func_179098_w();
        GlStateManager.func_179084_k();
    }
    
    public void drawArea(float left, float top, float right, float bottom, final int color, final WorldRenderer builder) {
        if (left < right) {
            final float i = left;
            left = right;
            right = i;
        }
        if (top < bottom) {
            final float j = top;
            top = bottom;
            bottom = j;
        }
        builder.func_181662_b((double)left, (double)bottom, 0.0).func_181675_d();
        builder.func_181662_b((double)right, (double)bottom, 0.0).func_181675_d();
        builder.func_181662_b((double)right, (double)top, 0.0).func_181675_d();
        builder.func_181662_b((double)left, (double)top, 0.0).func_181675_d();
    }
}
