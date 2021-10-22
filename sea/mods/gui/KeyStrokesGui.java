// 
// Decompiled by Procyon v0.5.36
// 

package sea.mods.gui;

import java.io.IOException;
import sea.mods.huds.HudMod;
import sea.mods.huds.settings.Settings;
import sea.SeaClient;
import java.util.Iterator;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import sea.utils.font.FontUtils;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Mouse;
import net.minecraft.client.gui.Gui;
import java.awt.Color;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import java.util.ArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

public class KeyStrokesGui extends GuiScreen
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
    
    public KeyStrokesGui() {
        this.mc = Minecraft.getMinecraft();
        this.modButtons = new ArrayList();
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        final ScaledResolution sr = new ScaledResolution(this.mc);
        final FontRenderer fr = Minecraft.fontRendererObj;
        Gui.drawRect(34, 65, KeyStrokesGui.width - 23, sr.getScaledHeight() - 0, new Color(0, 0, 204, 100).getRGB());
        this.mc.getTextureManager().bindTexture(KeyStrokesGui.CLICK_GUI);
        Gui.drawModalRectWithCustomSizedTexture(-8, 0, 0.0f, 0.0f, KeyStrokesGui.width + 20, KeyStrokesGui.height + 20, (float)(KeyStrokesGui.width + 20), (float)(KeyStrokesGui.height + 20));
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
        GlStateManager.pushMatrix();
        GlStateManager.translate(0.0f, 0.0f, 0.0f);
        GlStateManager.scale(2.0f, 2.0f, 1.0f);
        GlStateManager.translate(-14.0f, 0.0f, 0.0f);
        FontUtils.normal.drawCenteredString("KeyStokes", 118.0f, 13.0f, -1);
        GlStateManager.popMatrix();
        for (final ModButton m : this.modButtons) {
            m.draw();
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.buttonList.add(new GuiButton(19, 0, 5, this.fontRendererObj.getStringWidth("<-") + 10, 20, "<-"));
    }
    
    @Override
    public void initGui() {
        final ArrayList modButtons = this.modButtons;
        final int x = 65;
        final int y = 91;
        final int w = 40;
        final int h = 13;
        final Settings settings = SeaClient.INSTANCE.settings;
        modButtons.add(new ModButton(x, y, w, h, Settings.key1));
        final ArrayList modButtons2 = this.modButtons;
        final int x2 = 65;
        final int y2 = 112;
        final int w2 = 100;
        final int h2 = 13;
        final Settings settings2 = SeaClient.INSTANCE.settings;
        modButtons2.add(new ModButton(x2, y2, w2, h2, Settings.key2));
        final ArrayList modButtons3 = this.modButtons;
        final int x3 = 65;
        final int y3 = 133;
        final int w3 = 110;
        final int h3 = 13;
        final Settings settings3 = SeaClient.INSTANCE.settings;
        modButtons3.add(new ModButton(x3, y3, w3, h3, Settings.key3));
        super.initGui();
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        super.actionPerformed(button);
        if (button.id == 19) {
            this.mc.displayGuiScreen(new ModToggleGui());
        }
    }
    
    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        for (final ModButton m : this.modButtons) {
            m.onClick(mouseX, mouseY, mouseButton);
        }
    }
}
