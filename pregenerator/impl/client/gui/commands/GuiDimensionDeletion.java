// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.client.gui.commands;

import pregenerator.base.impl.misc.DimensionLister;
import java.awt.Color;
import pregenerator.base.api.network.PregenPacket;
import pregenerator.impl.network.packets.gui.DimensionTaskPacket;
import pregenerator.ChunkPregenerator;
import net.minecraft.client.gui.GuiButton;
import java.util.Arrays;
import net.minecraftforge.common.DimensionManager;
import pregenerator.base.impl.misc.SelectionList;
import net.minecraft.client.gui.GuiScreen;
import pregenerator.base.impl.gui.GuiPregenBase;

public class GuiDimensionDeletion extends GuiPregenBase
{
    GuiScreen prev;
    SelectionList<Integer> dimensions;
    
    public GuiDimensionDeletion(final GuiScreen prev) {
        this.dimensions = new SelectionList<Integer>();
        this.prev = prev;
        this.dimensions.addValues(Arrays.asList(DimensionManager.getStaticDimensionIDs()));
    }
    
    @Override
    public void func_73866_w_() {
        super.func_73866_w_();
        this.registerButton(0, -40, 90, 80, 20, "Back");
        this.registerButton(1, -80, -50, 160, 20, "Dimension: " + this.getName());
        this.registerButton(2, 10, -20, 100, 20, "Delete Dimension!");
        this.registerButton(3, -100, -20, 100, 20, "Unload Dimension!");
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
            if (GuiScreen.func_146272_n()) {
                this.dimensions.prev();
            }
            else {
                this.dimensions.next();
            }
            button.field_146126_j = "Dimension: " + this.getName();
        }
        else if (id == 2 || id == 3) {
            ChunkPregenerator.networking.sendPacketToServer(new DimensionTaskPacket(id == 3, this.dimensions.getValue()));
        }
    }
    
    public void func_73863_a(final int mouseX, final int mouseY, final float partialTicks) {
        this.func_146276_q_();
        super.func_73863_a(mouseX, mouseY, partialTicks);
        this.drawCenterText("Dimension Deletion", 0, -100, Color.WHITE.getRGB());
        this.drawCenterText("Dimensions have to be Unloaded to be deleted (except for the overworld)", 0, -80, Color.WHITE.getRGB());
        if (this.getIDButton(1).func_146115_a()) {
            this.drawListText(Arrays.asList("The Dimension that needs to be deleted"), mouseX, mouseY - 15);
        }
    }
    
    public String getName() {
        final int dim = this.dimensions.getValue();
        return dim + " (" + getDimensionName(dim) + ")";
    }
    
    public static String getDimensionName(final int id) {
        try {
            return DimensionLister.getDimensionName(id);
        }
        catch (Exception e) {
            return "Unknown";
        }
    }
}
