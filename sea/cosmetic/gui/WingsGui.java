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

public class WingsGui extends GuiScreen
{
    AbstractClientPlayer entity;
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        final ScaledResolution sr = new ScaledResolution(this.mc);
        Gui.drawRect(18, 18, WingsGui.width - 12, sr.getScaledHeight() - 9, new Color(0, 0, 0, 170).getRGB());
        this.drawHorizontalLine(17, 200 + WingsGui.width / 2, 16, new Color(51, 204, 255).getRGB());
        this.drawHorizontalLine(17, 201 + WingsGui.width / 2, 230, new Color(51, 204, 255).getRGB());
        this.drawVerticalLine(17, 17 + WingsGui.width / 2, 16, new Color(51, 204, 255).getRGB());
        this.drawVerticalLine(414, 17 + WingsGui.width / 2, 15, new Color(51, 204, 255).getRGB());
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    @Override
    public void initGui() {
        this.buttonList.add(new GuiButton(30, 50, 54, this.fontRendererObj.getStringWidth("Wings Balck & Grey") + 4, 20, "Wings Balck & Grey"));
        this.buttonList.add(new GuiButton(20, 50, 74, this.fontRendererObj.getStringWidth("Wings Blue & Green") + 4, 20, "Wings Blue & Green"));
        this.buttonList.add(new GuiButton(10, 50, 94, this.fontRendererObj.getStringWidth("Wings Green & Yellow") + 4, 20, "Wings Green & Yellow"));
        this.buttonList.add(new GuiButton(50, 50, 114, this.fontRendererObj.getStringWidth("Wings Red & Blue") + 4, 20, "Wings Red & Blue"));
        this.buttonList.add(new GuiButton(60, 50, 134, this.fontRendererObj.getStringWidth("Wings Peach & Blue") + 4, 20, "Wings Peach & Blue"));
        this.buttonList.add(new GuiButton(70, 50, 154, this.fontRendererObj.getStringWidth("Wings Forest Green & Moss Green") + 4, 20, "Wings Forest Green & Moss Green"));
        this.buttonList.add(new GuiButton(40, 50, 24, this.fontRendererObj.getStringWidth("§cAll wings off§f") + 10, 20, "§cAll wings off§f"));
        super.initGui();
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        if (button.id == 30) {
            CosmeticBoolean.dragonWings3 = false;
            CosmeticBoolean.dragonWings2 = false;
            CosmeticBoolean.dragonWings = true;
            System.out.println(CosmeticBoolean.dragonWings);
        }
        else if (button.id == 40) {
            CosmeticBoolean.dragonWings = false;
            System.out.println(CosmeticBoolean.dragonWings);
        }
        if (button.id == 20) {
            CosmeticBoolean.dragonWings3 = false;
            CosmeticBoolean.dragonWings4 = false;
            CosmeticBoolean.dragonWings5 = false;
            CosmeticBoolean.dragonWings6 = false;
            CosmeticBoolean.dragonWings = false;
            CosmeticBoolean.dragonWings2 = true;
            System.out.println(CosmeticBoolean.dragonWings2);
        }
        else if (button.id == 30) {
            CosmeticBoolean.dragonWings3 = false;
            CosmeticBoolean.dragonWings2 = false;
            CosmeticBoolean.dragonWings4 = false;
            CosmeticBoolean.dragonWings5 = false;
            CosmeticBoolean.dragonWings6 = false;
            CosmeticBoolean.dragonWings = true;
            System.out.println(CosmeticBoolean.dragonWings);
        }
        else if (button.id == 40) {
            CosmeticBoolean.dragonWings2 = false;
            System.out.println(CosmeticBoolean.dragonWings2);
        }
        if (button.id == 10) {
            CosmeticBoolean.dragonWings3 = true;
            CosmeticBoolean.dragonWings2 = false;
            CosmeticBoolean.dragonWings4 = false;
            CosmeticBoolean.dragonWings5 = false;
            CosmeticBoolean.dragonWings6 = false;
            CosmeticBoolean.dragonWings = false;
        }
        else if (button.id == 40) {
            CosmeticBoolean.dragonWings3 = false;
            System.out.println(CosmeticBoolean.dragonWings3);
        }
        if (button.id == 50) {
            CosmeticBoolean.dragonWings3 = false;
            CosmeticBoolean.dragonWings2 = false;
            CosmeticBoolean.dragonWings4 = true;
            CosmeticBoolean.dragonWings5 = false;
            CosmeticBoolean.dragonWings6 = false;
            CosmeticBoolean.dragonWings = false;
        }
        else if (button.id == 40) {
            CosmeticBoolean.dragonWings4 = false;
            System.out.println(CosmeticBoolean.dragonWings4);
        }
        if (button.id == 60) {
            CosmeticBoolean.dragonWings3 = false;
            CosmeticBoolean.dragonWings2 = false;
            CosmeticBoolean.dragonWings4 = false;
            CosmeticBoolean.dragonWings5 = true;
            CosmeticBoolean.dragonWings6 = false;
            CosmeticBoolean.dragonWings = false;
        }
        else if (button.id == 40) {
            CosmeticBoolean.dragonWings5 = false;
            System.out.println(CosmeticBoolean.dragonWings5);
        }
        if (button.id == 70) {
            CosmeticBoolean.dragonWings3 = false;
            CosmeticBoolean.dragonWings2 = false;
            CosmeticBoolean.dragonWings4 = false;
            CosmeticBoolean.dragonWings5 = false;
            CosmeticBoolean.dragonWings6 = true;
            CosmeticBoolean.dragonWings = false;
        }
        else if (button.id == 40) {
            CosmeticBoolean.dragonWings6 = false;
            System.out.println(CosmeticBoolean.dragonWings4);
        }
        super.actionPerformed(button);
    }
}
