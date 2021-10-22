// 
// Decompiled by Procyon v0.5.36
// 

package sea.mods.gui;

import java.io.IOException;
import sea.cosmetic.gui.CosmeticGui;
import net.minecraft.client.gui.GuiMainMenu;
import sea.mods.huds.HudList;
import sea.mods.huds.HudMod;
import sea.SeaClient;
import java.util.Iterator;
import net.minecraft.client.gui.FontRenderer;
import sea.ImageButton;
import net.minecraft.client.gui.GuiButton;
import org.lwjgl.input.Mouse;
import net.minecraft.client.gui.Gui;
import java.awt.Color;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import java.util.ArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

public class ModToggleGui2 extends GuiScreen
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
    
    public ModToggleGui2() {
        this.mc = Minecraft.getMinecraft();
        this.modButtons = new ArrayList();
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        final ScaledResolution sr = new ScaledResolution(this.mc);
        final FontRenderer fr = Minecraft.fontRendererObj;
        Gui.drawRect(34, 65, ModToggleGui2.width - 23, sr.getScaledHeight() - 0, new Color(0, 0, 204, 100).getRGB());
        this.mc.getTextureManager().bindTexture(ModToggleGui2.CLICK_GUI);
        Gui.drawModalRectWithCustomSizedTexture(-8, 0, 0.0f, 0.0f, ModToggleGui2.width + 20, ModToggleGui2.height + 20, (float)(ModToggleGui2.width + 20), (float)(ModToggleGui2.height + 20));
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
        this.modButtons.add(new ModButton(65, 70, 280, 13, SeaClient.INSTANCE.hudList.key));
        this.modButtons.add(new ModButton(65, 91, 280, 13, SeaClient.INSTANCE.hudList.mini));
        this.modButtons.add(new ModButton(65, 112, 280, 13, SeaClient.INSTANCE.hudList.fullbright));
        final ArrayList modButtons = this.modButtons;
        final int x = 65;
        final int y = 133;
        final int w = 280;
        final int h = 13;
        final HudList hudList = SeaClient.INSTANCE.hudList;
        modButtons.add(new ModButton(x, y, w, h, HudList.sprint));
        this.modButtons.add(new ModButton(65, 154, 280, 13, SeaClient.INSTANCE.hudList.freelook));
        this.modButtons.add(new ModButton(65, 175, 280, 13, SeaClient.INSTANCE.hudList.serverdisplay));
        this.modButtons.add(new ModButton(65, 196, 280, 13, SeaClient.INSTANCE.hudList.heartDisplay));
        final ArrayList modButtons2 = this.modButtons;
        final int x2 = 65;
        final int y2 = 217;
        final int w2 = 280;
        final int h2 = 13;
        final HudList hudList2 = SeaClient.INSTANCE.hudList;
        modButtons2.add(new ModButton(x2, y2, w2, h2, HudList.tntTimer));
        final ArrayList modButtons3 = this.modButtons;
        final int x3 = 65;
        final int y3 = 238;
        final int w3 = 280;
        final int h3 = 13;
        final HudList hudList3 = SeaClient.INSTANCE.hudList;
        modButtons3.add(new ModButton(x3, y3, w3, h3, HudList.oldanime));
        this.imagebuttons.clear();
        final int i = 24;
        final int j = ModToggleGui2.height / 4 + 48;
        this.imagebuttons.add(new ImageButton(new ResourceLocation("textures/seaclient/button/up.png"), GuiMainMenu.width / 20 - 20, j - 35, 25, 25, "UP", 1694));
        this.imagebuttons.add(new ImageButton(new ResourceLocation("textures/seaclient/button/down.png"), GuiMainMenu.width / 20 - 20, j + 105, 25, 25, "Down", 1234));
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
        if (button.id == 15) {
            this.mc.displayGuiScreen(new ModToggleGui3());
        }
        if (button.id == 16) {
            this.mc.displayGuiScreen(new KeyStrokesGui());
        }
        if (button.id == 18) {
            this.mc.displayGuiScreen(new ModToggleGui());
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
