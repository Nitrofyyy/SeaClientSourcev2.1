// 
// Decompiled by Procyon v0.5.36
// 

package sea.mods.gui;

import java.io.IOException;
import sea.mods.huds.HudMod;
import sea.mods.huds.settings.color.ColorList;
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

public class BlockOverlayGui extends GuiScreen
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
    
    public BlockOverlayGui() {
        this.mc = Minecraft.getMinecraft();
        this.modButtons = new ArrayList();
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        final ScaledResolution sr = new ScaledResolution(this.mc);
        final FontRenderer fr = Minecraft.fontRendererObj;
        Gui.drawRect(34, 65, BlockOverlayGui.width - 23, sr.getScaledHeight() - 0, new Color(0, 0, 204, 100).getRGB());
        this.mc.getTextureManager().bindTexture(BlockOverlayGui.CLICK_GUI);
        Gui.drawModalRectWithCustomSizedTexture(-8, 0, 0.0f, 0.0f, BlockOverlayGui.width + 20, BlockOverlayGui.height + 20, (float)(BlockOverlayGui.width + 20), (float)(BlockOverlayGui.height + 20));
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
        FontUtils.normal.drawCenteredString("BLOCKSOVERLAYS", 118.0f, 13.0f, -1);
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
        final ColorList colorList = SeaClient.INSTANCE.colorList;
        modButtons.add(new ModButton(x, y, w, h, ColorList.blockgreen));
        final ArrayList modButtons2 = this.modButtons;
        final int x2 = 65;
        final int y2 = 112;
        final int w2 = 43;
        final int h2 = 13;
        final ColorList colorList2 = SeaClient.INSTANCE.colorList;
        modButtons2.add(new ModButton(x2, y2, w2, h2, ColorList.blockblue));
        final ArrayList modButtons3 = this.modButtons;
        final int x3 = 65;
        final int y3 = 133;
        final int w3 = 80;
        final int h3 = 13;
        final ColorList colorList3 = SeaClient.INSTANCE.colorList;
        modButtons3.add(new ModButton(x3, y3, w3, h3, ColorList.blockaqua));
        final ArrayList modButtons4 = this.modButtons;
        final int x4 = 65;
        final int y4 = 154;
        final int w4 = 68;
        final int h4 = 13;
        final ColorList colorList4 = SeaClient.INSTANCE.colorList;
        modButtons4.add(new ModButton(x4, y4, w4, h4, ColorList.blockyellow));
        final ArrayList modButtons5 = this.modButtons;
        final int x5 = 65;
        final int y5 = 175;
        final int w5 = 45;
        final int h5 = 13;
        final ColorList colorList5 = SeaClient.INSTANCE.colorList;
        modButtons5.add(new ModButton(x5, y5, w5, h5, ColorList.blockpink));
        final ArrayList modButtons6 = this.modButtons;
        final int x6 = 65;
        final int y6 = 196;
        final int w6 = 80;
        final int h6 = 13;
        final ColorList colorList6 = SeaClient.INSTANCE.colorList;
        modButtons6.add(new ModButton(x6, y6, w6, h6, ColorList.blockwhite));
        super.initGui();
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        super.actionPerformed(button);
        if (button.id == 19) {
            this.mc.displayGuiScreen(new ModToggleGui3());
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
