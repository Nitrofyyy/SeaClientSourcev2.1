// 
// Decompiled by Procyon v0.5.36
// 

package sea.cosmetic.gui;

import java.io.IOException;
import sea.cosmetic.CosmeticBoolean;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.Gui;
import java.awt.Color;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.GuiScreen;

public class HaloGui extends GuiScreen
{
    AbstractClientPlayer entity;
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        final ScaledResolution sr = new ScaledResolution(this.mc);
        Gui.drawRect(18, 18, HaloGui.width - 12, sr.getScaledHeight() - 9, new Color(0, 0, 0, 170).getRGB());
        this.drawHorizontalLine(17, 200 + HaloGui.width / 2, 16, new Color(51, 204, 255).getRGB());
        this.drawHorizontalLine(17, 201 + HaloGui.width / 2, 230, new Color(51, 204, 255).getRGB());
        this.drawVerticalLine(17, 17 + HaloGui.width / 2, 16, new Color(51, 204, 255).getRGB());
        this.drawVerticalLine(414, 17 + HaloGui.width / 2, 15, new Color(51, 204, 255).getRGB());
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    @Override
    public void initGui() {
        this.buttonList.add(new GuiButton(30, 50, 54, this.fontRendererObj.getStringWidth("Halo Blue") + 4, 20, "Halo Blue"));
        this.buttonList.add(new GuiButton(20, 50, 84, this.fontRendererObj.getStringWidth("Halo Green") + 4, 20, "Halo Green"));
        this.buttonList.add(new GuiButton(40, 50, 24, this.fontRendererObj.getStringWidth("§cAll halo off§f") + 10, 20, "§cAll halo off§f"));
        super.initGui();
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        if (button.id == 30) {
            CosmeticBoolean.halogreen = false;
            CosmeticBoolean.haloblue = true;
            System.out.println("blue " + CosmeticBoolean.haloblue);
        }
        else if (button.id == 40) {
            CosmeticBoolean.haloblue = false;
            System.out.println("blue " + CosmeticBoolean.haloblue);
        }
        if (button.id == 20) {
            CosmeticBoolean.haloblue = false;
            CosmeticBoolean.halogreen = true;
            System.out.println("green " + CosmeticBoolean.halogreen);
        }
        else if (button.id == 40) {
            CosmeticBoolean.halogreen = false;
            System.out.println("green " + CosmeticBoolean.halogreen);
        }
        super.actionPerformed(button);
    }
}
