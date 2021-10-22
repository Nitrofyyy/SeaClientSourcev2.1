// 
// Decompiled by Procyon v0.5.36
// 

package sea;

import sea.mods.gui.ModToggleGui;
import sea.mods.gui.ModToggleGui3;
import sea.mods.gui.ModToggleGui4;
import sea.mods.gui.ModToggleGui2;
import sea.cosmetic.gui.CosmeticGui;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSelectWorld;
import net.minecraft.client.gui.Gui;
import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class ImageButton
{
    protected ResourceLocation image;
    public int x;
    public int y;
    public int w;
    public int h;
    public int nani;
    public int target;
    protected String description;
    boolean skided;
    protected Minecraft mc;
    public static int bgid;
    
    static {
        ImageButton.bgid = 0;
    }
    
    public ImageButton(final ResourceLocation image, final int x, final int y, final int widht, final int height, final String descrip, final int target) {
        this.nani = 0;
        this.skided = false;
        this.image = image;
        this.x = x;
        this.y = y;
        this.w = widht;
        this.h = height;
        this.description = descrip;
        this.target = target;
        this.mc = Minecraft.getMinecraft();
    }
    
    public void draw(final int mouseX, final int mouseY, final Color c) {
        this.HoveredAnimation(mouseX, mouseY);
        if (this.nani < 0) {
            RenderUtil1.insatance.draw2DImage(this.image, this.x - this.nani, this.y - this.nani, this.w + this.nani * 2, this.h + this.nani * 2, c);
            final double descWidth = Minecraft.fontRendererObj.getStringWidth(this.description);
            Gui.drawRect(this.x - (int)descWidth / 2, this.y + this.h + 10, this.x + this.w + (int)descWidth / 2, this.y + this.h + 10 + Minecraft.fontRendererObj.FONT_HEIGHT, Integer.MIN_VALUE);
            Minecraft.fontRendererObj.drawString(this.description, this.x + this.w / 2 - Minecraft.fontRendererObj.getStringWidth(this.description) / 2, this.y + this.h + 11, Color.WHITE.getRGB());
        }
        else {
            RenderUtil1.insatance.draw2DImage(this.image, this.x, this.y, this.w, this.h, c);
        }
    }
    
    public void onClick(final int mouseX, final int mouseY) {
        if (this.isHovered(mouseX, mouseY)) {
            if (this.target == 0) {
                this.mc.displayGuiScreen(new GuiSelectWorld(null));
            }
            if (this.target == 1) {
                this.mc.displayGuiScreen(new GuiMultiplayer(null));
            }
            if (this.target == 3) {
                this.mc.displayGuiScreen(new GuiOptions(null, this.mc.gameSettings));
            }
            if (this.target == 4) {
                this.mc.shutdown();
            }
            if (this.target == 100) {
                this.mc.displayGuiScreen(new CosmeticGui());
            }
            if (this.target == 15) {
                this.mc.displayGuiScreen(new ModToggleGui2());
            }
            if (this.target == 21831) {
                this.mc.displayGuiScreen(new ModToggleGui4());
            }
            if (this.target == 9546524) {
                this.mc.displayGuiScreen(new ModToggleGui3());
            }
            if (this.target == 123123) {
                this.mc.displayGuiScreen(new ModToggleGui2());
            }
            if (this.target == 1234) {
                this.mc.displayGuiScreen(new ModToggleGui3());
            }
            if (this.target == 1694) {
                this.mc.displayGuiScreen(new ModToggleGui());
            }
            switch (this.target) {
                case 110: {
                    ++ImageButton.bgid;
                    if (ImageButton.bgid == 3) {
                        ImageButton.bgid = 0;
                        break;
                    }
                    break;
                }
            }
        }
    }
    
    protected void HoveredAnimation(final int mouseX, final int mouseY) {
        if (this.isHovered(mouseX, mouseY)) {
            if (this.nani < 5) {
                ++this.nani;
            }
        }
        else if (this.nani > 0) {
            --this.nani;
        }
    }
    
    public boolean isHovered(final int mouseX, final int mouseY) {
        return RenderUtil1.insatance.isHoverd(this.x, this.y, this.x + this.w, this.y + this.h, mouseX, mouseY);
    }
}
