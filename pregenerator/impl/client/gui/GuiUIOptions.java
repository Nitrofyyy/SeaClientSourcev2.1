// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.client.gui;

import java.io.IOException;
import net.minecraft.client.gui.GuiButton;
import java.util.Iterator;
import pregenerator.impl.client.PregenInfo;
import pregenerator.impl.client.infos.InfoEntry;
import pregenerator.impl.client.ClientHandler;
import net.minecraft.client.gui.GuiScreen;
import pregenerator.base.impl.gui.GuiSlider;
import pregenerator.base.impl.gui.GuiPregenBase;

public class GuiUIOptions extends GuiPregenBase implements GuiSlider.ISlider
{
    GuiScreen lastScreen;
    
    public GuiUIOptions(final GuiScreen parent) {
        this.lastScreen = parent;
    }
    
    public boolean func_73868_f() {
        return false;
    }
    
    @Override
    public void func_73866_w_() {
        super.func_73866_w_();
        final PregenInfo handler = ClientHandler.INSTANCE.info;
        this.registerButton(0, -40, 95, 80, 20, "Back");
        this.registerButton(new GuiSlider(1, this.centerX - 165, this.centerY - 110, 150, 20, "Update Rate: ", "x sec", 1.0, 20.0, handler.updateFrequency, false, true, this).setScrollEffect(1.0));
        this.registerButton(2, -165, -89, 150, 20, "UI XPosition: " + handler.xPos.getXName());
        this.registerButton(3, 20, -110, 150, 20, "UI YPosition: " + handler.yPos.getYName());
        this.registerButton(4, 20, -89, 150, 20, "Scale: " + (handler.big ? "Big" : "Small"));
        int yOffset = -60;
        int i = 10;
        for (final InfoEntry entry : InfoEntry.getRegistry()) {
            final int xOffset = (i % 2 == 0) ? -165 : 20;
            this.registerButton(i, xOffset, yOffset, 150, 20, (entry.isActive() ? "Enabled" : "Disabled") + ": " + entry.getName());
            if (i % 2 == 1) {
                yOffset += 21;
            }
            ++i;
        }
    }
    
    @Override
    public void onChangeSliderValue(final GuiSlider slider) {
        ClientHandler.INSTANCE.info.setUpdateFrequency(slider.getValueInt());
    }
    
    protected void func_146284_a(final GuiButton button) {
        final PregenInfo client = ClientHandler.INSTANCE.info;
        int id = button.field_146127_k;
        if (id == 0) {
            this.field_146297_k.func_147108_a(this.lastScreen);
        }
        else if (id == 2) {
            client.setXPos(client.xPos.getNext());
            button.field_146126_j = "UI XPosition: " + client.xPos.getXName();
        }
        else if (id == 3) {
            client.setYPos(client.yPos.getNext());
            button.field_146126_j = "UI YPosition: " + client.yPos.getXName();
        }
        else if (id == 4) {
            client.setBig(!client.big);
            button.field_146126_j = "Scale: " + (client.big ? "Big" : "Small");
        }
        else if (id >= 10) {
            id -= 10;
            final InfoEntry entry = InfoEntry.getByID(id);
            entry.setActive(!entry.isActive());
            button.field_146126_j = (entry.isActive() ? "Disable" : "Enable") + ": " + entry.getName();
            client.updateList();
        }
    }
    
    public void func_73863_a(final int mouseX, final int mouseY, final float partialTicks) {
        this.func_146276_q_();
        super.func_73863_a(mouseX, mouseY, partialTicks);
    }
    
    protected void func_73869_a(final char typedChar, final int keyCode) throws IOException {
        if (keyCode == 18) {
            this.field_146297_k.func_147108_a((GuiScreen)null);
            return;
        }
        if (keyCode == 14) {
            this.field_146297_k.func_147108_a(this.lastScreen);
            return;
        }
        super.func_73869_a(typedChar, keyCode);
    }
}
