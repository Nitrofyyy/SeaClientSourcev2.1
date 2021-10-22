// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.client.gui;

import java.util.List;
import java.io.IOException;
import net.minecraft.client.gui.GuiButton;
import java.util.Iterator;
import pregenerator.impl.client.TrackerInfo;
import pregenerator.impl.client.trackerInfo.TrackerEntry;
import pregenerator.impl.client.ClientHandler;
import pregenerator.base.api.network.PregenPacket;
import pregenerator.impl.network.packets.DimRequestPacket;
import pregenerator.ChunkPregenerator;
import pregenerator.base.impl.misc.SelectionList;
import net.minecraft.client.gui.GuiScreen;
import pregenerator.base.impl.gui.GuiSlider;
import pregenerator.base.impl.gui.GuiPregenBase;

public class GuiTrackerOptions extends GuiPregenBase implements GuiSlider.ISlider
{
    GuiScreen lastScreen;
    SelectionList<Integer> dimensions;
    boolean notified;
    
    public GuiTrackerOptions(final GuiScreen parent) {
        this.dimensions = new SelectionList<Integer>();
        this.notified = false;
        this.lastScreen = parent;
        ChunkPregenerator.networking.sendPacketToServer(new DimRequestPacket(0));
    }
    
    public boolean func_73868_f() {
        return false;
    }
    
    @Override
    public void func_73866_w_() {
        super.func_73866_w_();
        final TrackerInfo handler = ClientHandler.INSTANCE.tracker;
        this.registerButton(0, -40, 95, 80, 20, "Back");
        this.registerButton(new GuiSlider(1, this.centerX - 165, this.centerY - 110, 150, 20, "Update Rate: ", "x sec", 1.0, 20.0, handler.updateFrequency, false, true, this).setScrollEffect(1.0));
        this.registerButton(2, -165, -89, 150, 20, "UI XPosition: " + handler.xPos.getXName());
        this.registerButton(3, 20, -110, 150, 20, "UI YPosition: " + handler.yPos.getYName());
        this.registerButton(4, 20, -89, 150, 20, "Scale: " + (handler.big ? "Big" : "Small"));
        this.registerButton(5, -165, -68, 150, 20, handler.showDetailed ? "Disable Detailed" : "Show Detailed");
        this.registerButton(6, 20, -68, 150, 20, "Detailed Dim: " + handler.targetDim);
        int yOffset = -40;
        int i = 10;
        int skipped = 0;
        for (final TrackerEntry entry : TrackerEntry.getRegistry()) {
            if (!entry.hasConfig()) {
                ++i;
                ++skipped;
            }
            else {
                final int actualIndex = i - skipped;
                final int xOffset = (actualIndex % 2 == 0) ? -165 : 20;
                this.registerButton(i, xOffset, yOffset, 150, 20, (entry.isActive() ? "Enabled" : "Disabled") + ": " + entry.getName());
                if (actualIndex % 2 == 1) {
                    yOffset += 21;
                }
                ++i;
            }
        }
    }
    
    public void func_73863_a(final int mouseX, final int mouseY, final float partialTicks) {
        this.func_73866_w_();
        this.func_146276_q_();
        super.func_73863_a(mouseX, mouseY, partialTicks);
    }
    
    @Override
    public void onChangeSliderValue(final GuiSlider slider) {
        ClientHandler.INSTANCE.tracker.setUpdateFrequency(slider.getValueInt());
    }
    
    protected void func_146284_a(final GuiButton button) {
        final TrackerInfo client = ClientHandler.INSTANCE.tracker;
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
        else if (id == 5) {
            client.setDetailed(!client.showDetailed);
            button.field_146126_j = (client.showDetailed ? "Disable Detailed" : "Show Detailed");
        }
        else if (id == 6) {
            if (this.dimensions.size() <= 0) {
                if (!this.notified) {
                    this.func_175275_f("Waiting for ServerData!");
                    this.notified = true;
                }
                return;
            }
            if (GuiScreen.func_146272_n()) {
                this.dimensions.prev();
            }
            else {
                this.dimensions.next();
            }
            client.targetDim = this.dimensions.getValue();
        }
        else if (id >= 10) {
            id -= 10;
            final TrackerEntry entry = TrackerEntry.getByID(id);
            entry.setActive(!entry.isActive());
            button.field_146126_j = (entry.isActive() ? "Disable" : "Enable") + ": " + entry.getName();
            client.updateList();
        }
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
    
    public void addDimensions(final List<Integer> list) {
        this.dimensions.addValues(list);
        if (this.notified) {
            this.func_175275_f("ServerData Received");
            this.notified = false;
        }
        this.dimensions.setIndexFromValue(ClientHandler.INSTANCE.tracker.targetDim);
    }
}
