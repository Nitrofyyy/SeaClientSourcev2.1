// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui;

import java.util.Iterator;
import java.io.IOException;
import net.minecraft.client.resources.I18n;
import java.util.List;
import net.minecraft.util.IChatComponent;

public class GuiDisconnected extends GuiScreen
{
    private String reason;
    private IChatComponent message;
    private List<String> multilineMessage;
    private final GuiScreen parentScreen;
    private int field_175353_i;
    
    public GuiDisconnected(final GuiScreen screen, final String reasonLocalizationKey, final IChatComponent chatComp) {
        this.parentScreen = screen;
        this.reason = I18n.format(reasonLocalizationKey, new Object[0]);
        this.message = chatComp;
    }
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
    }
    
    @Override
    public void initGui() {
        this.buttonList.clear();
        this.multilineMessage = this.fontRendererObj.listFormattedStringToWidth(this.message.getFormattedText(), GuiDisconnected.width - 50);
        this.field_175353_i = this.multilineMessage.size() * this.fontRendererObj.FONT_HEIGHT;
        this.buttonList.add(new GuiButton(0, GuiDisconnected.width / 2 - 100, GuiDisconnected.height / 2 + this.field_175353_i / 2 + this.fontRendererObj.FONT_HEIGHT, I18n.format("gui.toMenu", new Object[0])));
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        if (button.id == 0) {
            this.mc.displayGuiScreen(this.parentScreen);
        }
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawDefaultBackground();
        Gui.drawCenteredString(this.fontRendererObj, this.reason, (float)(GuiDisconnected.width / 2), (float)(GuiDisconnected.height / 2 - this.field_175353_i / 2 - this.fontRendererObj.FONT_HEIGHT * 2), 11184810);
        int i = GuiDisconnected.height / 2 - this.field_175353_i / 2;
        if (this.multilineMessage != null) {
            for (final String s : this.multilineMessage) {
                Gui.drawCenteredString(this.fontRendererObj, s, (float)(GuiDisconnected.width / 2), (float)i, 16777215);
                i += this.fontRendererObj.FONT_HEIGHT;
            }
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
