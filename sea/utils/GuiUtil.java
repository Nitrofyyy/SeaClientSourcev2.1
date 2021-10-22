// 
// Decompiled by Procyon v0.5.36
// 

package sea.utils;

import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.Tessellator;
import java.awt.Color;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

public class GuiUtil extends Gui
{
    public static GuiUtil instance;
    private static final Minecraft mc;
    
    static {
        mc = Minecraft.getMinecraft();
    }
    
    public GuiUtil() {
        GuiUtil.instance = this;
    }
    
    public static int drawString(final String text, final float x, final float y, final int color, final boolean shadow) {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        return Minecraft.fontRendererObj.drawString(text, x, y, color, shadow);
    }
    
    public static int drawString(final String text, final int x, final int y) {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        return drawString(text, x, y, 16777215);
    }
    
    public static int drawString(final String text, final int x, final int y, final boolean shadow) {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        return drawString(text, x, y, 16777215, shadow);
    }
    
    public static int drawString(final String text, final int x, final int y, final int color) {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        return drawString(text, x, y, color, false);
    }
    
    public static int drawString(final String text, final int x, final int y, final int color, final boolean shadow) {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        final String[] lines = text.split("\n");
        if (lines.length > 1) {
            int j = 0;
            for (int i = 0; i < lines.length; ++i) {
                j += Minecraft.fontRendererObj.drawString(lines[i], (float)x, (float)(y + i * (Minecraft.fontRendererObj.FONT_HEIGHT + 2)), color, shadow);
            }
            return j;
        }
        return Minecraft.fontRendererObj.drawString(text, (float)x, (float)y, color, shadow);
    }
    
    public static int drawScaledString(final String text, final int x, final int y, final boolean shadow, final float scale) {
        GlStateManager.pushMatrix();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.scale(scale, scale, 1.0f);
        final int i = drawString(text, (int)(x / scale), (int)(y / scale), shadow);
        GlStateManager.scale(Math.pow(scale, -1.0), Math.pow(scale, -1.0), 1.0);
        GlStateManager.popMatrix();
        return i;
    }
    
    public static void drawChromaString(final String textIn, final int xIn, final int y, final boolean shadow) {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        for (int j = 0; j < textIn.split("\n").length; ++j) {
            int x = xIn;
            char[] charArray;
            for (int length = (charArray = textIn.split("\n")[j].toCharArray()).length, i = 0; i < length; ++i) {
                final char c = charArray[i];
                final long l = System.currentTimeMillis() - x * 10L - (y - j * 10L) * 10L;
                final float speed = 2000.0f;
                final int color = Color.HSBtoRGB(l % (int)speed / speed, 0.8f, 0.8f);
                drawString(String.valueOf(c), x, y + j * (Minecraft.fontRendererObj.FONT_HEIGHT + 2), color, shadow);
                x += Minecraft.fontRendererObj.getStringWidth(String.valueOf(c));
            }
        }
    }
    
    public static int drawCenteredString(final String text, final int x, final int y) {
        return drawString(text, x - Minecraft.fontRendererObj.getStringWidth(text) / 2, y);
    }
    
    public static int drawCenteredString(final String text, final int x, final int y, final int color) {
        return drawString(text, x - Minecraft.fontRendererObj.getStringWidth(text) / 2, y, color);
    }
    
    public static int drawCenteredString(final String text, final int x, final int y, final int color, final boolean shadow) {
        return drawString(text, x - Minecraft.fontRendererObj.getStringWidth(text) / 2, y, color, shadow);
    }
    
    public static int drawScaledCenteredString(final String text, final int x, final int y, final boolean shadow, final float scale) {
        return drawScaledString(text, x - Minecraft.fontRendererObj.getStringWidth(text) / 2, y, shadow, scale);
    }
    
    public static void drawRectOutline(final int left, final int top, final int right, final int bottom, final int color) {
        Gui.drawRect(left - 1, top - 1, right + 1, top, color);
        Gui.drawRect(right, top, right + 1, bottom, color);
        Gui.drawRect(left - 1, bottom, right + 1, bottom + 1, color);
        Gui.drawRect(left - 1, top, left, bottom, color);
    }
    
    public void drawGradientRect(final int left, final int top, final int right, final int bottom, final int startColor, final int endColor) {
        final float f = (startColor >> 24 & 0xFF) / 255.0f;
        final float f2 = (startColor >> 16 & 0xFF) / 255.0f;
        final float f3 = (startColor >> 8 & 0xFF) / 255.0f;
        final float f4 = (startColor & 0xFF) / 255.0f;
        final float f5 = (endColor >> 24 & 0xFF) / 255.0f;
        final float f6 = (endColor >> 16 & 0xFF) / 255.0f;
        final float f7 = (endColor >> 8 & 0xFF) / 255.0f;
        final float f8 = (endColor & 0xFF) / 255.0f;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.shadeModel(7425);
        final Tessellator tessellator = Tessellator.getInstance();
        final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldrenderer.pos(right, top, this.zLevel).color(f2, f3, f4, f).reset();
        worldrenderer.pos(left, top, this.zLevel).color(f2, f3, f4, f).reset();
        worldrenderer.pos(left, bottom, this.zLevel).color(f6, f7, f8, f5).reset();
        worldrenderer.pos(right, bottom, this.zLevel).color(f6, f7, f8, f5).reset();
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }
    
    public static void drawRoundedRect(final int paramInt1, final int paramInt2, final int paramInt3, final int paramInt4, final float radius, final int color) {
        final float f1 = (color >> 24 & 0xFF) / 255.0f;
        final float f2 = (color >> 16 & 0xFF) / 255.0f;
        final float f3 = (color >> 8 & 0xFF) / 255.0f;
        final float f4 = (color & 0xFF) / 255.0f;
        GlStateManager.color(f2, f3, f4, f1);
        drawRoundedRect((float)paramInt1, (float)paramInt2, (float)paramInt3, (float)paramInt4, radius);
    }
    
    public static void drawRoundedRect(final float paramInt1, final float paramInt2, final float paramInt3, final float paramInt4, final float radius, final int color) {
        final float f1 = (color >> 24 & 0xFF) / 255.0f;
        final float f2 = (color >> 16 & 0xFF) / 255.0f;
        final float f3 = (color >> 8 & 0xFF) / 255.0f;
        final float f4 = (color & 0xFF) / 255.0f;
        GlStateManager.color(f2, f3, f4, f1);
        drawRoundedRect(paramInt1, paramInt2, paramInt3, paramInt4, radius);
    }
    
    public static void drawRoundedRect(final float paramFloat1, final float paramFloat2, final float paramFloat3, final float paramFloat4, final float paramFloat5) {
        final int i = 18;
        final float f1 = 90.0f / i;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableCull();
        GlStateManager.enableColorMaterial();
        GlStateManager.blendFunc(770, 771);
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GL11.glBegin(5);
        GL11.glVertex2f(paramFloat1 + paramFloat5, paramFloat2);
        GL11.glVertex2f(paramFloat1 + paramFloat5, paramFloat4);
        GL11.glVertex2f(paramFloat3 - paramFloat5, paramFloat2);
        GL11.glVertex2f(paramFloat3 - paramFloat5, paramFloat4);
        GL11.glEnd();
        GL11.glBegin(5);
        GL11.glVertex2f(paramFloat1, paramFloat2 + paramFloat5);
        GL11.glVertex2f(paramFloat1 + paramFloat5, paramFloat2 + paramFloat5);
        GL11.glVertex2f(paramFloat1, paramFloat4 - paramFloat5);
        GL11.glVertex2f(paramFloat1 + paramFloat5, paramFloat4 - paramFloat5);
        GL11.glEnd();
        GL11.glBegin(5);
        GL11.glVertex2f(paramFloat3, paramFloat2 + paramFloat5);
        GL11.glVertex2f(paramFloat3 - paramFloat5, paramFloat2 + paramFloat5);
        GL11.glVertex2f(paramFloat3, paramFloat4 - paramFloat5);
        GL11.glVertex2f(paramFloat3 - paramFloat5, paramFloat4 - paramFloat5);
        GL11.glEnd();
        GL11.glBegin(6);
        float f2 = paramFloat3 - paramFloat5;
        float f3 = paramFloat2 + paramFloat5;
        GL11.glVertex2f(f2, f3);
        for (int j = 0; j <= i; ++j) {
            final float f4 = j * f1;
            GL11.glVertex2f((float)(f2 + paramFloat5 * Math.cos(Math.toRadians(f4))), (float)(f3 - paramFloat5 * Math.sin(Math.toRadians(f4))));
        }
        GL11.glEnd();
        GL11.glBegin(6);
        f2 = paramFloat1 + paramFloat5;
        f3 = paramFloat2 + paramFloat5;
        GL11.glVertex2f(f2, f3);
        for (int j = 0; j <= i; ++j) {
            final float f4 = j * f1;
            GL11.glVertex2f((float)(f2 - paramFloat5 * Math.cos(Math.toRadians(f4))), (float)(f3 - paramFloat5 * Math.sin(Math.toRadians(f4))));
        }
        GL11.glEnd();
        GL11.glBegin(6);
        f2 = paramFloat1 + paramFloat5;
        f3 = paramFloat4 - paramFloat5;
        GL11.glVertex2f(f2, f3);
        for (int j = 0; j <= i; ++j) {
            final float f4 = j * f1;
            GL11.glVertex2f((float)(f2 - paramFloat5 * Math.cos(Math.toRadians(f4))), (float)(f3 + paramFloat5 * Math.sin(Math.toRadians(f4))));
        }
        GL11.glEnd();
        GL11.glBegin(6);
        f2 = paramFloat3 - paramFloat5;
        f3 = paramFloat4 - paramFloat5;
        GL11.glVertex2f(f2, f3);
        for (int j = 0; j <= i; ++j) {
            final float f4 = j * f1;
            GL11.glVertex2f((float)(f2 + paramFloat5 * Math.cos(Math.toRadians(f4))), (float)(f3 + paramFloat5 * Math.sin(Math.toRadians(f4))));
        }
        GL11.glEnd();
        GlStateManager.enableCull();
        GlStateManager.disableBlend();
        GlStateManager.disableColorMaterial();
        GlStateManager.enableTexture2D();
    }
    
    public static void drawRoundedOutline(final int x, final int y, final int x2, final int y2, final float radius, final float width, final int color) {
        final float f1 = (color >> 24 & 0xFF) / 255.0f;
        final float f2 = (color >> 16 & 0xFF) / 255.0f;
        final float f3 = (color >> 8 & 0xFF) / 255.0f;
        final float f4 = (color & 0xFF) / 255.0f;
        GlStateManager.color(f2, f3, f4, f1);
        drawRoundedOutline((float)x, (float)y, (float)x2, (float)y2, radius, width);
    }
    
    private static void drawRoundedOutline(final float x, final float y, final float x2, final float y2, final float radius, final float width) {
        final int i = 18;
        final int j = 90 / i;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableCull();
        GlStateManager.enableColorMaterial();
        GlStateManager.blendFunc(770, 771);
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        if (width != 1.0f) {
            GL11.glLineWidth(width);
        }
        GL11.glBegin(3);
        GL11.glVertex2f(x + radius, y);
        GL11.glVertex2f(x2 - radius, y);
        GL11.glEnd();
        GL11.glBegin(3);
        GL11.glVertex2f(x2, y + radius);
        GL11.glVertex2f(x2, y2 - radius);
        GL11.glEnd();
        GL11.glBegin(3);
        GL11.glVertex2f(x2 - radius, y2 - 0.1f);
        GL11.glVertex2f(x + radius, y2 - 0.1f);
        GL11.glEnd();
        GL11.glBegin(3);
        GL11.glVertex2f(x + 0.1f, y2 - radius);
        GL11.glVertex2f(x + 0.1f, y + radius);
        GL11.glEnd();
        float f1 = x2 - radius;
        float f2 = y + radius;
        GL11.glBegin(3);
        for (int k = 0; k <= i; ++k) {
            final int m = 90 - k * j;
            GL11.glVertex2f((float)(f1 + radius * MathUtils.getRightAngle(m)), (float)(f2 - radius * MathUtils.getAngle(m)));
        }
        GL11.glEnd();
        f1 = x2 - radius;
        f2 = y2 - radius;
        GL11.glBegin(3);
        for (int k = 0; k <= i; ++k) {
            final int m = k * j + 270;
            GL11.glVertex2f((float)(f1 + radius * MathUtils.getRightAngle(m)), (float)(f2 - radius * MathUtils.getAngle(m)));
        }
        GL11.glEnd();
        GL11.glBegin(3);
        f1 = x + radius;
        f2 = y2 - radius;
        for (int k = 0; k <= i; ++k) {
            final int m = k * j + 90;
            GL11.glVertex2f((float)(f1 + radius * MathUtils.getRightAngle(m)), (float)(f2 + radius * MathUtils.getAngle(m)));
        }
        GL11.glEnd();
        GL11.glBegin(3);
        f1 = x + radius;
        f2 = y + radius;
        for (int k = 0; k <= i; ++k) {
            final int m = 270 - k * j;
            GL11.glVertex2f((float)(f1 + radius * MathUtils.getRightAngle(m)), (float)(f2 + radius * MathUtils.getAngle(m)));
        }
        GL11.glEnd();
        if (width != 1.0f) {
            GL11.glLineWidth(1.0f);
        }
        GlStateManager.enableCull();
        GlStateManager.disableBlend();
        GlStateManager.disableColorMaterial();
        GlStateManager.enableTexture2D();
    }
    
    public static void drawCircle(final float x, final float y, final float radius, final float thickness, final Color color, final boolean smooth) {
        drawPartialCircle(x, y, radius, 0, 360, thickness, color, smooth);
    }
    
    public static void drawPartialCircle(final int x, final int y, final float radius, final int startAngle, final int endAngle, final float thickness, final Color color, final boolean smooth) {
        drawPartialCircle(x, y, radius, startAngle, endAngle, thickness, color, smooth);
    }
    
    public static void drawPartialCircle(final float x, final float y, final float radius, int startAngle, int endAngle, final float thickness, final Color colour, final boolean smooth) {
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        if (startAngle > endAngle) {
            final int temp = startAngle;
            startAngle = endAngle;
            endAngle = temp;
        }
        if (startAngle < 0) {
            startAngle = 0;
        }
        if (endAngle > 360) {
            endAngle = 360;
        }
        if (smooth) {
            GL11.glEnable(2848);
        }
        else {
            GL11.glDisable(2848);
        }
        GL11.glLineWidth(thickness);
        GL11.glColor4f(colour.getRed() / 255.0f, colour.getGreen() / 255.0f, colour.getBlue() / 255.0f, colour.getAlpha() / 255.0f);
        GL11.glBegin(3);
        final float ratio = 0.017453292f;
        for (int i = startAngle; i <= endAngle; ++i) {
            final float radians = (i - 90) * ratio;
            GL11.glVertex2f(x + (float)Math.cos(radians) * radius, y + (float)Math.sin(radians) * radius);
        }
        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
    }
    
    public static void drawFilledRect(final float x1, final float y1, final float x2, final float y2, final int colour, final boolean smooth) {
        drawFilledShape(new float[] { x1, y1, x1, y2, x2, y2, x2, y1 }, new Color(colour, true), smooth);
    }
    
    public static void drawFilledShape(final float[] points, final Color colour, final boolean smooth) {
        GL11.glPushMatrix();
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        if (smooth) {
            GL11.glEnable(2848);
        }
        else {
            GL11.glDisable(2848);
        }
        GL11.glLineWidth(1.0f);
        GL11.glColor4f(colour.getRed() / 255.0f, colour.getGreen() / 255.0f, colour.getBlue() / 255.0f, colour.getAlpha() / 255.0f);
        GL11.glBegin(9);
        for (int i = 0; i < points.length; i += 2) {
            GL11.glVertex2f(points[i], points[i + 1]);
        }
        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glPopMatrix();
    }
    
    public static void drawLine(final float x, final float x1, final float y, final float thickness, final int colour, final boolean smooth) {
        drawLines(new float[] { x, y, x1, y }, thickness, new Color(colour, true), smooth);
    }
    
    public static void drawVerticalLine(final float x, final float y, final float y1, final float thickness, final int colour, final boolean smooth) {
        drawLines(new float[] { x, y, x, y1 }, thickness, new Color(colour, true), smooth);
    }
    
    public static void drawLines(final float[] points, final float thickness, final Color colour, final boolean smooth) {
        GL11.glPushMatrix();
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        if (smooth) {
            GL11.glEnable(2848);
        }
        else {
            GL11.glDisable(2848);
        }
        GL11.glLineWidth(thickness);
        GL11.glColor4f(colour.getRed() / 255.0f, colour.getGreen() / 255.0f, colour.getBlue() / 255.0f, colour.getAlpha() / 255.0f);
        GL11.glBegin(1);
        for (int i = 0; i < points.length; i += 2) {
            GL11.glVertex2f(points[i], points[i + 1]);
        }
        GL11.glEnd();
        GL11.glEnable(2848);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glEnable(3553);
        GL11.glPopMatrix();
    }
}
