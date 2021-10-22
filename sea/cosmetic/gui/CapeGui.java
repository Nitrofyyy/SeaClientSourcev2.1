// 
// Decompiled by Procyon v0.5.36
// 

package sea.cosmetic.gui;

import java.io.IOException;
import sea.WebOpener;
import sea.cosmetic.CosmeticBoolean;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.Gui;
import java.awt.Color;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.GuiScreen;

public class CapeGui extends GuiScreen
{
    AbstractClientPlayer entity;
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        final ScaledResolution sr = new ScaledResolution(this.mc);
        Gui.drawRect(18, 18, CapeGui.width - 12, sr.getScaledHeight() - 9, new Color(0, 0, 0, 170).getRGB());
        this.drawHorizontalLine(17, 200 + CapeGui.width / 2, 16, new Color(0, 0, 0, 170).getRGB());
        this.drawHorizontalLine(17, 201 + CapeGui.width / 2, 230, new Color(0, 0, 0, 170).getRGB());
        this.drawVerticalLine(17, 17 + CapeGui.width / 2, 16, new Color(51, 204, 255).getRGB());
        this.drawVerticalLine(414, 17 + CapeGui.width / 2, 15, new Color(51, 204, 255).getRGB());
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    @Override
    public void initGui() {
        this.buttonList.add(new GuiButton(19, 37, 54, this.fontRendererObj.getStringWidth("SeaCape") + 4, 20, "SeaCape"));
        this.buttonList.add(new GuiButton(190, 84, 54, this.fontRendererObj.getStringWidth("RickCape") + 10, 20, "RickCape"));
        this.buttonList.add(new GuiButton(192, 137, 54, this.fontRendererObj.getStringWidth("SnowCape") + 10, 20, "SnowCape"));
        this.buttonList.add(new GuiButton(199, 194, 54, this.fontRendererObj.getStringWidth("SkyCape") + 10, 20, "SkyCape"));
        this.buttonList.add(new GuiButton(20, 123, 24, this.fontRendererObj.getStringWidth("§4All capes off§f") + 10, 20, "§cAll capes off§f"));
        super.initGui();
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        if (button.id == 19) {
            CosmeticBoolean.cape1 = true;
            CosmeticBoolean.rickcape = false;
            CosmeticBoolean.snowcape = false;
            CosmeticBoolean.skycape = false;
            System.out.println(CosmeticBoolean.cape1);
        }
        if (button.id == 190) {
            CosmeticBoolean.rickcape = true;
            CosmeticBoolean.cape1 = false;
            CosmeticBoolean.skycape = false;
            CosmeticBoolean.snowcape = false;
            System.out.println("RickCape: " + CosmeticBoolean.rickcape);
            WebOpener.openLink("https://www.youtube.com/watch?v=dQw4w9WgXcQ");
        }
        if (button.id == 192) {
            CosmeticBoolean.snowcape = true;
            CosmeticBoolean.skycape = false;
            CosmeticBoolean.rickcape = false;
            CosmeticBoolean.cape1 = false;
            System.out.println("SnowCape: " + CosmeticBoolean.snowcape);
        }
        if (button.id == 199) {
            CosmeticBoolean.skycape = true;
            CosmeticBoolean.snowcape = false;
            CosmeticBoolean.rickcape = false;
            CosmeticBoolean.cape1 = false;
            System.out.println("SkyCape: " + CosmeticBoolean.skycape);
        }
        if (button.id == 20) {
            CosmeticBoolean.cape1 = false;
            CosmeticBoolean.skycape = false;
            CosmeticBoolean.rickcape = false;
            CosmeticBoolean.snowcape = false;
            System.out.println(CosmeticBoolean.cape1);
        }
        super.actionPerformed(button);
    }
}
