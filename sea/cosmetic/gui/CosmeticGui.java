// 
// Decompiled by Procyon v0.5.36
// 

package sea.cosmetic.gui;

import java.io.IOException;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import java.awt.Color;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.GuiScreen;

public class CosmeticGui extends GuiScreen
{
    AbstractClientPlayer entity;
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        final ScaledResolution sr = new ScaledResolution(this.mc);
        Gui.drawRect(0, 0, CosmeticGui.width - 0, sr.getScaledHeight() - 0, new Color(0, 0, 0, 170).getRGB());
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    @Override
    public void initGui() {
        this.buttonList.add(new GuiButton(19, 50, 54, this.fontRendererObj.getStringWidth("Cape") + 10, Minecraft.fontRendererObj.FONT_HEIGHT + 9, "Cape"));
        this.buttonList.add(new GuiButton(30, 50, 74, this.fontRendererObj.getStringWidth("Wings") + 4, 20, "Wings"));
        this.buttonList.add(new GuiButton(20, 50, 94, this.fontRendererObj.getStringWidth("Halo") + 4, 20, "Halo"));
        super.initGui();
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        if (button.id == 19) {
            this.mc.displayGuiScreen(new CapeGui());
        }
        if (button.id == 30) {
            this.mc.displayGuiScreen(new WingsGui());
        }
        if (button.id == 20) {
            this.mc.displayGuiScreen(new HaloGui());
        }
        super.actionPerformed(button);
    }
}
