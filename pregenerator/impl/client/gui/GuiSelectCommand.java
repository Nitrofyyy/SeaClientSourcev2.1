// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.client.gui;

import java.util.Arrays;
import java.awt.Color;
import pregenerator.impl.network.packets.gui.ManualTaskPacket;
import pregenerator.impl.client.gui.commands.GuiRingDeletion;
import pregenerator.impl.client.gui.commands.GuiDimensionDeletion;
import pregenerator.impl.client.gui.commands.GuiSimpleDeletion;
import pregenerator.impl.client.gui.commands.GuiMassPregen;
import pregenerator.impl.client.gui.commands.GuiExpansionPregen;
import pregenerator.impl.client.gui.commands.GuiSimplePregen;
import net.minecraft.client.gui.GuiButton;
import pregenerator.base.api.network.PregenPacket;
import pregenerator.impl.network.packets.gui.ProcessRequestPacket;
import pregenerator.ChunkPregenerator;
import net.minecraft.client.gui.GuiScreen;
import pregenerator.base.impl.gui.GuiPregenBase;

public class GuiSelectCommand extends GuiPregenBase
{
    GuiScreen prev;
    public boolean running;
    public boolean hasTask;
    int ticker;
    
    public GuiSelectCommand(final GuiScreen prev) {
        this.running = false;
        this.hasTask = false;
        this.ticker = 0;
        this.prev = prev;
    }
    
    @Override
    public void func_73866_w_() {
        super.func_73866_w_();
        this.registerButton(0, -40, 90, 80, 20, "Back");
        this.registerButton(1, -140, -80, 80, 20, "Simple Pregen");
        this.registerButton(2, -50, -80, 100, 20, "Pregen Expansion");
        this.registerButton(3, 60, -80, 80, 20, "Mass Pregen");
        this.registerButton(10, -140, -50, 80, 20, "Simple Deletion");
        this.registerButton(11, -50, -50, 100, 20, "Dimension Deletion");
        this.registerButton(12, 60, -50, 80, 20, "Ring Deletion");
        this.registerButton(20, -140, -20, 80, 20, "Stop Tasks").field_146124_l = false;
        this.registerButton(21, -50, -20, 100, 20, "Delete Task List").field_146124_l = false;
        this.registerButton(22, 60, -20, 80, 20, "Continue Tasks").field_146124_l = false;
        this.ticker = 0;
        ChunkPregenerator.networking.sendPacketToServer(new ProcessRequestPacket());
    }
    
    public boolean func_73868_f() {
        return false;
    }
    
    protected void func_146284_a(final GuiButton button) {
        final int id = button.field_146127_k;
        if (id == 0) {
            this.field_146297_k.func_147108_a(this.prev);
        }
        else if (id == 1) {
            this.field_146297_k.func_147108_a((GuiScreen)new GuiSimplePregen(this));
        }
        else if (id == 2) {
            this.field_146297_k.func_147108_a((GuiScreen)new GuiExpansionPregen(this));
        }
        else if (id == 3) {
            this.field_146297_k.func_147108_a((GuiScreen)new GuiMassPregen(this));
        }
        else if (id == 10) {
            this.field_146297_k.func_147108_a((GuiScreen)new GuiSimpleDeletion(this));
        }
        else if (id == 11) {
            this.field_146297_k.func_147108_a((GuiScreen)new GuiDimensionDeletion(this));
        }
        else if (id == 12) {
            this.field_146297_k.func_147108_a((GuiScreen)new GuiRingDeletion(this));
        }
        else if (id >= 20 && id <= 22) {
            ChunkPregenerator.networking.sendPacketToServer(new ManualTaskPacket(id - 20));
        }
    }
    
    public void func_73863_a(final int mouseX, final int mouseY, final float partialTicks) {
        ++this.ticker;
        if (this.ticker >= 1200) {
            this.ticker = 0;
            ChunkPregenerator.networking.sendPacketToServer(new ProcessRequestPacket());
        }
        this.getIDButton(20).field_146124_l = this.running;
        this.getIDButton(21).field_146124_l = this.hasTask;
        this.getIDButton(22).field_146124_l = (!this.running && this.hasTask);
        this.func_146276_q_();
        super.func_73863_a(mouseX, mouseY, partialTicks);
        this.drawCenterText("Chunk Pregen Commands", 0, -100, Color.WHITE.getRGB());
        if (this.getIDButton(20).func_146115_a()) {
            this.drawListText(Arrays.asList("Stops the Pregenerator or Deleter", "This does not delete tasks"), mouseX, mouseY - 30);
        }
        else if (this.getIDButton(21).func_146115_a()) {
            this.drawListText(Arrays.asList("Stops the Pregenerator or Deleter and Deletes all the tasks"), mouseX, mouseY - 30);
        }
        else if (this.getIDButton(22).func_146115_a()) {
            this.drawListText(Arrays.asList("Continues the Pregenerator if tasks are present"), mouseX, mouseY - 30);
        }
    }
}
