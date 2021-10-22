// 
// Decompiled by Procyon v0.5.36
// 

package sea.mods.gui;

import java.io.IOException;
import sea.cosmetic.gui.CosmeticGui;
import net.minecraft.client.gui.GuiMainMenu;
import sea.mods.huds.HudMod;
import sea.SeaClient;
import java.util.Iterator;
import net.minecraft.client.gui.FontRenderer;
import sea.ImageButton;
import net.minecraft.client.gui.GuiButton;
import org.lwjgl.input.Mouse;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.gui.Gui;
import java.awt.Color;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import java.util.ArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

public class ModToggleGui extends GuiScreen
{
    Minecraft mc;
    ArrayList modButtons;
    int x;
    int y;
    int offset;
    public static final ResourceLocation CLICK_GUI;
    
    static {
        CLICK_GUI = new ResourceLocation("textures/seaclient/ClickGui.png");
    }
    
    public ModToggleGui() {
        this.mc = Minecraft.getMinecraft();
        this.modButtons = new ArrayList();
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        final ScaledResolution sr = new ScaledResolution(this.mc);
        final FontRenderer fr = Minecraft.fontRendererObj;
        Gui.drawRect(34, 65, ModToggleGui.width - 23, sr.getScaledHeight() - 0, new Color(0, 0, 204, 100).getRGB());
        this.mc.getTextureManager().bindTexture(ModToggleGui.CLICK_GUI);
        Gui.drawModalRectWithCustomSizedTexture(-8, 0, 0.0f, 0.0f, ModToggleGui.width + 20, ModToggleGui.height + 20, (float)(ModToggleGui.width + 20), (float)(ModToggleGui.height + 20));
        GlStateManager.pushMatrix();
        GlStateManager.translate(0.0f, 0.0f, 0.0f);
        GlStateManager.scale(2.0f, 2.0f, 1.0f);
        GlStateManager.translate(-14.0f, 0.0f, 0.0f);
        GlStateManager.popMatrix();
        for (final ModButton m : this.modButtons) {
            m.draw();
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
        if (Mouse.hasWheel()) {
            final int wheel = Mouse.getDWheel();
            if (wheel < 0) {
                this.offset += 26;
                if (this.offset < 0) {
                    this.offset = 0;
                }
            }
            else if (wheel > 0) {
                this.offset -= 26;
                if (this.offset < 0) {
                    this.offset = 0;
                }
            }
        }
        this.buttonList.add(new GuiButton(16, 340, 70, fr.getStringWidth(" \u2699"), Minecraft.fontRendererObj.FONT_HEIGHT + 4, " \u2699"));
        this.buttonList.add(new GuiButton(14, 340, 91, fr.getStringWidth(" \u2699"), Minecraft.fontRendererObj.FONT_HEIGHT + 4, " \u2699"));
        this.buttonList.add(new GuiButton(13, 340, 112, fr.getStringWidth(" \u2699"), Minecraft.fontRendererObj.FONT_HEIGHT + 4, " \u2699"));
        this.buttonList.add(new GuiButton(13, 340, 133, fr.getStringWidth(" \u2699"), Minecraft.fontRendererObj.FONT_HEIGHT + 4, " \u2699"));
        this.buttonList.add(new GuiButton(12, 340, 154, fr.getStringWidth(" \u2699"), Minecraft.fontRendererObj.FONT_HEIGHT + 4, " \u2699"));
        this.buttonList.add(new GuiButton(11, 340, 175, fr.getStringWidth(" \u2699"), Minecraft.fontRendererObj.FONT_HEIGHT + 4, " \u2699"));
        this.buttonList.add(new GuiButton(10, 340, 196, fr.getStringWidth(" \u2699"), Minecraft.fontRendererObj.FONT_HEIGHT + 4, " \u2699"));
        this.buttonList.add(new GuiButton(9, 340, 217, fr.getStringWidth(" \u2699"), Minecraft.fontRendererObj.FONT_HEIGHT + 4, " \u2699"));
        this.buttonList.add(new GuiButton(8, 340, 238, fr.getStringWidth(" \u2699"), Minecraft.fontRendererObj.FONT_HEIGHT + 4, " \u2699"));
        for (final ImageButton imagebutton : this.imagebuttons) {
            imagebutton.draw(mouseX, mouseY, Color.WHITE);
        }
    }
    
    @Override
    public void initGui() {
        this.modButtons.add(new ModButton(65, 70, 280, 13, SeaClient.INSTANCE.hudList.FPSMod));
        this.modButtons.add(new ModButton(65, 91, 280, 13, SeaClient.INSTANCE.hudList.ClockMod));
        this.modButtons.add(new ModButton(65, 112, 280, 13, SeaClient.INSTANCE.hudList.DirectionMod));
        this.modButtons.add(new ModButton(65, 133, 280, 13, SeaClient.INSTANCE.hudList.pack));
        this.modButtons.add(new ModButton(65, 154, 280, 13, SeaClient.INSTANCE.hudList.CPSMod));
        this.modButtons.add(new ModButton(65, 175, 280, 13, SeaClient.INSTANCE.hudList.armor));
        this.modButtons.add(new ModButton(65, 196, 280, 13, SeaClient.INSTANCE.hudList.ping));
        this.modButtons.add(new ModButton(65, 217, 280, 13, SeaClient.INSTANCE.hudList.pot));
        this.modButtons.add(new ModButton(65, 238, 280, 13, SeaClient.INSTANCE.hudList.key));
        this.modButtons.add(new ModButton(65, 259, 280, 13, SeaClient.INSTANCE.hudList.mini));
        this.modButtons.add(new ModButton(65, 280, 280, 13, SeaClient.INSTANCE.hudList.fullbright));
        final int i = 24;
        final int j = ModToggleGui.height / 4 + 48;
        this.imagebuttons.clear();
        this.imagebuttons.add(new ImageButton(new ResourceLocation("textures/seaclient/button/cape_icon.png"), GuiMainMenu.width / 20 - 20, j - 35, 33, 33, "Cosmetic", 100));
        this.imagebuttons.add(new ImageButton(new ResourceLocation("textures/seaclient/button/down.png"), GuiMainMenu.width / 20 - 20, j + 105, 25, 25, "Down", 15));
        super.initGui();
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        switch (button.id) {
            case 19: {
                this.mc.displayGuiScreen(new CosmeticGui());
                break;
            }
        }
        super.actionPerformed(button);
        if (button.id == 18) {
            this.mc.displayGuiScreen(new BlockOverlayGui());
        }
        if (button.id == 8) {
            this.mc.displayGuiScreen(new KeyStrokesGui());
        }
        if (button.id == 10) {
            this.mc.displayGuiScreen(new PingGui());
        }
        if (button.id == 17) {
            this.mc.displayGuiScreen(new TimeChangetGui());
        }
        if (button.id == 16) {
            this.mc.displayGuiScreen(new FpsGui());
        }
        if (button.id == 12) {
            this.mc.displayGuiScreen(new CpsGui());
        }
        if (button.id == 15) {
            this.mc.displayGuiScreen(new ModToggleGui());
        }
        if (button.id == 14) {
            this.mc.displayGuiScreen(new ClockGui());
        }
    }
    
    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        for (final ImageButton imagebutton : this.imagebuttons) {
            imagebutton.onClick(mouseX, mouseY);
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
        for (final ModButton m : this.modButtons) {
            m.onClick(mouseX, mouseY, mouseButton);
        }
    }
}
