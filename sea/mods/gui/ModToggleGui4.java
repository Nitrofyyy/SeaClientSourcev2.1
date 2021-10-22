// 
// Decompiled by Procyon v0.5.36
// 

package sea.mods.gui;

import java.io.IOException;
import sea.cosmetic.gui.CosmeticGui;
import net.minecraft.client.gui.GuiMainMenu;
import sea.mods.huds.HudMod;
import sea.mods.huds.HudList;
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

public class ModToggleGui4 extends GuiScreen
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
    
    public ModToggleGui4() {
        this.mc = Minecraft.getMinecraft();
        this.modButtons = new ArrayList();
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        final ScaledResolution sr = new ScaledResolution(this.mc);
        final FontRenderer fr = Minecraft.fontRendererObj;
        Gui.drawRect(34, 65, ModToggleGui4.width - 23, sr.getScaledHeight() - 0, new Color(0, 0, 204, 100).getRGB());
        this.mc.getTextureManager().bindTexture(ModToggleGui4.CLICK_GUI);
        Gui.drawModalRectWithCustomSizedTexture(-8, 0, 0.0f, 0.0f, ModToggleGui4.width + 20, ModToggleGui4.height + 20, (float)(ModToggleGui4.width + 20), (float)(ModToggleGui4.height + 20));
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
        this.buttonList.add(new GuiButton(14, 340, 81, fr.getStringWidth(" \u2699"), Minecraft.fontRendererObj.FONT_HEIGHT + 4, " \u2699"));
        this.buttonList.add(new GuiButton(13, 340, 102, fr.getStringWidth(" \u2699"), Minecraft.fontRendererObj.FONT_HEIGHT + 4, " \u2699"));
        for (final ImageButton imagebutton : this.imagebuttons) {
            imagebutton.draw(mouseX, mouseY, Color.WHITE);
        }
    }
    
    @Override
    public void initGui() {
        final int i = 24;
        final int j = ModToggleGui4.height / 4 + 48;
        final ArrayList modButtons = this.modButtons;
        final int x = 65;
        final int y = 81;
        final int w = 280;
        final int h = 13;
        final HudList hudList = SeaClient.INSTANCE.hudList;
        modButtons.add(new ModButton(x, y, w, h, HudList.score));
        final ArrayList modButtons2 = this.modButtons;
        final int x2 = 65;
        final int y2 = 102;
        final int w2 = 280;
        final int h2 = 13;
        final HudList hudList2 = SeaClient.INSTANCE.hudList;
        modButtons2.add(new ModButton(x2, y2, w2, h2, HudList.motion));
        final ArrayList modButtons3 = this.modButtons;
        final int x3 = 65;
        final int y3 = 123;
        final int w3 = 280;
        final int h3 = 13;
        final HudList hudList3 = SeaClient.INSTANCE.hudList;
        modButtons3.add(new ModButton(x3, y3, w3, h3, HudList.crosshair));
        this.imagebuttons.clear();
        this.imagebuttons.add(new ImageButton(new ResourceLocation("textures/seaclient/button/up.png"), GuiMainMenu.width / 20 - 20, j - 35, 25, 25, "UP", 9546524));
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
        if (button.id == 13) {
            this.mc.displayGuiScreen(new MotionBlurGui());
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
