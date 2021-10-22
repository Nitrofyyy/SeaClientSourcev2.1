// 
// Decompiled by Procyon v0.5.36
// 

package sea.mods.gui;

import java.io.IOException;
import sea.mods.huds.settings.color.ColorList;
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

public class FpsGui extends GuiScreen
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
    
    public FpsGui() {
        this.mc = Minecraft.getMinecraft();
        this.modButtons = new ArrayList();
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        final ScaledResolution sr = new ScaledResolution(this.mc);
        final FontRenderer fr = Minecraft.fontRendererObj;
        Gui.drawRect(34, 65, FpsGui.width - 23, sr.getScaledHeight() - 0, new Color(0, 0, 204, 100).getRGB());
        this.mc.getTextureManager().bindTexture(FpsGui.CLICK_GUI);
        Gui.drawModalRectWithCustomSizedTexture(-8, 0, 0.0f, 0.0f, FpsGui.width + 20, FpsGui.height + 20, (float)(FpsGui.width + 20), (float)(FpsGui.height + 20));
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
        FontUtils.normal.drawCenteredString("Fps Settings", 118.0f, 13.0f, -1);
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
        final int y = 81;
        final int w = 30;
        final int h = 13;
        final Settings settings = SeaClient.INSTANCE.settings;
        modButtons.add(new ModButton(x, y, w, h, Settings.fps1));
        final ArrayList modButtons2 = this.modButtons;
        final int x2 = 65;
        final int y2 = 102;
        final int w2 = 30;
        final int h2 = 13;
        final Settings settings2 = SeaClient.INSTANCE.settings;
        modButtons2.add(new ModButton(x2, y2, w2, h2, Settings.fps2));
        final ArrayList modButtons3 = this.modButtons;
        final int x3 = 65;
        final int y3 = 123;
        final int w3 = 30;
        final int h3 = 13;
        final Settings settings3 = SeaClient.INSTANCE.settings;
        modButtons3.add(new ModButton(x3, y3, w3, h3, Settings.fps3));
        final ArrayList modButtons4 = this.modButtons;
        final int x4 = 65;
        final int y4 = 144;
        final int w4 = 60;
        final int h4 = 13;
        final Settings settings4 = SeaClient.INSTANCE.settings;
        modButtons4.add(new ModButton(x4, y4, w4, h4, Settings.nobg));
        final ArrayList modButtons5 = this.modButtons;
        final int x5 = 200;
        final int y5 = 81;
        final int w5 = 70;
        final int h5 = 13;
        final ColorList colorList = SeaClient.INSTANCE.colorList;
        modButtons5.add(new ModButton(x5, y5, w5, h5, ColorList.FPSaqua));
        final ArrayList modButtons6 = this.modButtons;
        final int x6 = 200;
        final int y6 = 102;
        final int w6 = 70;
        final int h6 = 13;
        final ColorList colorList2 = SeaClient.INSTANCE.colorList;
        modButtons6.add(new ModButton(x6, y6, w6, h6, ColorList.FPSblue));
        final ArrayList modButtons7 = this.modButtons;
        final int x7 = 200;
        final int y7 = 123;
        final int w7 = 70;
        final int h7 = 13;
        final ColorList colorList3 = SeaClient.INSTANCE.colorList;
        modButtons7.add(new ModButton(x7, y7, w7, h7, ColorList.FPSgreen));
        final ArrayList modButtons8 = this.modButtons;
        final int x8 = 200;
        final int y8 = 144;
        final int w8 = 70;
        final int h8 = 13;
        final ColorList colorList4 = SeaClient.INSTANCE.colorList;
        modButtons8.add(new ModButton(x8, y8, w8, h8, ColorList.FPSgold));
        final ArrayList modButtons9 = this.modButtons;
        final int x9 = 200;
        final int y9 = 165;
        final int w9 = 70;
        final int h9 = 13;
        final ColorList colorList5 = SeaClient.INSTANCE.colorList;
        modButtons9.add(new ModButton(x9, y9, w9, h9, ColorList.FPSpink));
        final ArrayList modButtons10 = this.modButtons;
        final int x10 = 200;
        final int y10 = 186;
        final int w10 = 70;
        final int h10 = 13;
        final ColorList colorList6 = SeaClient.INSTANCE.colorList;
        modButtons10.add(new ModButton(x10, y10, w10, h10, ColorList.FPSyellow));
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
