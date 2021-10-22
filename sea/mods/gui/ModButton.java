// 
// Decompiled by Procyon v0.5.36
// 

package sea.mods.gui;

import net.minecraft.util.ResourceLocation;
import java.awt.Color;
import sea.mods.huds.HudMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

public class ModButton
{
    public int x;
    public int y;
    public int w;
    public int h;
    public static int x2;
    Gui gui;
    public static int y2;
    public static int w2;
    public static int h2;
    public int outlineColor;
    public static Minecraft mc;
    public HudMod m;
    
    static {
        ModButton.mc = Minecraft.getMinecraft();
    }
    
    public ModButton(final int x, final int y, final int w, final int h, final HudMod m) {
        this.gui = new Gui();
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        ModButton.x2 = x;
        ModButton.y2 = y;
        ModButton.w2 = w;
        ModButton.h2 = h;
        this.m = m;
    }
    
    public void draw() {
        Gui.drawRect(this.x, this.y, this.x + this.w, this.y + this.h, new Color(0, 0, 0, 170).getRGB());
        Gui.drawRect(this.x, this.y, this.x + this.w, this.y + this.h, new Color(0, 0, 0, 170).getRGB());
        Gui.drawRect(this.x, this.y, this.x + this.w, this.y + this.h, new Color(0, 0, 0, 170).getRGB());
        this.gui.drawHorizontalLine(this.x, this.x + this.w, this.y, this.getColor());
        final int i = this.x + 2;
        final int j = this.y + 2;
        Minecraft.fontRendererObj.drawString(String.valueOf(this.m.name) + " || §c" + this.m.description + "§f", i, j, -1);
    }
    
    private void drawBackGround(final int x, final int y, final int w, final int h, final ResourceLocation resource) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }
    
    private void drawVerticalLine(final int x2, final int i, final int y2, final int Color) {
    }
    
    private void drawHorizontalLine(final int x2, final int i, final int j, final int Color) {
    }
    
    public int getColor() {
        return this.m.isEnabled() ? new Color(0, 255, 0, 255).getRGB() : new Color(255, 0, 0, 255).getRGB();
    }
    
    public void onClick(final int mouseX, final int mouseY, final int button) {
        if (mouseX >= this.x && mouseX <= this.x + this.w && mouseY >= this.y && mouseY <= this.y + this.h) {
            if (this.m.isEnabled()) {
                this.m.setEnabled(false);
            }
            else {
                this.m.setEnabled(true);
            }
            System.out.println(String.valueOf(String.valueOf(this.m.name)) + " " + this.m.enabled + " " + this.getColor());
        }
    }
    
    public boolean isHovered(final int mouseX, final int mouseY) {
        return this.m.isHovered(mouseX, mouseY);
    }
}
