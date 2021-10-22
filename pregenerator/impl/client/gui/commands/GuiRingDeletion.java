// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.client.gui.commands;

import pregenerator.base.impl.misc.DimensionLister;
import pregenerator.impl.command.base.BasePregenCommand;
import java.awt.Color;
import pregenerator.base.api.network.PregenPacket;
import pregenerator.impl.network.packets.gui.DeletionTaskPacket;
import pregenerator.ChunkPregenerator;
import net.minecraft.client.gui.GuiButton;
import pregenerator.base.impl.gui.GuiSlider;
import net.minecraftforge.common.DimensionManager;
import java.util.Arrays;
import pregenerator.base.impl.misc.SelectionList;
import net.minecraft.client.gui.GuiScreen;
import pregenerator.base.impl.gui.GuiPregenBase;

public class GuiRingDeletion extends GuiPregenBase
{
    GuiScreen prev;
    SelectionList<String> shapes;
    SelectionList<Integer> dimensions;
    
    public GuiRingDeletion(final GuiScreen prev) {
        this.shapes = new SelectionList<String>(Arrays.asList("Square", "Circle"));
        this.dimensions = new SelectionList<Integer>();
        this.prev = prev;
        this.dimensions.addValues(Arrays.asList(DimensionManager.getStaticDimensionIDs()));
        this.dimensions.setIndexFromValue(0);
    }
    
    @Override
    public void func_73866_w_() {
        super.func_73866_w_();
        this.registerButton(0, -40, 90, 80, 20, "Back");
        this.registerButton(1, -70, -60, 160, 20, "Deletion Shape: " + this.shapes.getValue());
        this.registerButton(new GuiSlider(2, this.centerX - 160, this.centerY - 30, 160, 20, "Min Radius: ", " Chunks", 1.0, 4000.0, 100.0, false, true).setScrollEffect(1.0));
        this.registerButton(new GuiSlider(3, this.centerX + 10, this.centerY - 30, 160, 20, "Max Radius: ", " Chunks", 1.0, 4000.0, 100.0, false, true).setScrollEffect(1.0));
        this.registerButton(new GuiSlider(4, this.centerX - 160, this.centerY, 160, 20, "Gen Center X: ", " Chunk", -100.0, 100.0, 0.0, false, true).setScrollEffect(1.0));
        this.registerButton(new GuiSlider(5, this.centerX + 10, this.centerY, 160, 20, "Gen Center Z: ", " Chunk", -100.0, 100.0, 0.0, false, true).setScrollEffect(1.0));
        this.registerButton(6, -75, 30, 160, 20, "Dimension: " + this.getName());
        this.registerButton(7, -40, 60, 80, 20, "Delete Chunks!");
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
                this.shapes.prev();
            }
            else {
                this.shapes.next();
            }
            button.field_146126_j = "Deletion Shape: " + this.shapes.getValue();
        }
        else if (id == 6) {
            if (GuiScreen.func_146272_n()) {
                this.dimensions.prev();
            }
            else {
                this.dimensions.next();
            }
            button.field_146126_j = "Dimension: " + this.getName();
        }
        else if (id == 7) {
            final int minRadius = ((GuiSlider)this.getIDButton(2)).getValueInt();
            final int maxRadius = ((GuiSlider)this.getIDButton(2)).getValueInt();
            final int centerX = ((GuiSlider)this.getIDButton(4)).getValueInt();
            final int centerZ = ((GuiSlider)this.getIDButton(5)).getValueInt();
            ChunkPregenerator.networking.sendPacketToServer(new DeletionTaskPacket(false, this.shapes.getIndex(), this.dimensions.getIndex(), centerX, centerZ, minRadius, maxRadius));
            this.field_146297_k.func_147108_a(this.prev);
        }
    }
    
    public void func_73863_a(final int mouseX, final int mouseY, final float partialTicks) {
        this.func_146276_q_();
        super.func_73863_a(mouseX, mouseY, partialTicks);
        this.drawCenterText("Ring Deletion", 0, -100, Color.WHITE.getRGB());
        this.drawCenterText("This Mode can not Exeed 4 Million Chunks of Deletion", 0, -80, Color.WHITE.getRGB());
        this.handleTooltip(mouseX, mouseY);
    }
    
    public String getName() {
        final int dim = this.dimensions.getValue();
        return dim + " (" + getDimensionName(dim) + ")";
    }
    
    public void handleTooltip(final int mouseX, final int mouseY) {
        final int min = ((GuiSlider)this.getIDButton(2)).getValueInt() * 16;
        final int max = ((GuiSlider)this.getIDButton(3)).getValueInt() * 16;
        if (this.getIDButton(1).func_146115_a()) {
            this.drawListText(Arrays.asList("The Shape that the World be Deletion in"), mouseX, mouseY - 15);
        }
        if (this.getIDButton(2).func_146115_a()) {
            this.drawListText(Arrays.asList("The Minimum Radius that the Deletion starts at", "This can not Exceed or Equal the Maximum Radius", "Minimum Size: " + min + " x " + min + " Blocks", "Expected Amount of Chunks to Delete: " + this.getTotalGenerationCount()), mouseX, mouseY + 30);
        }
        if (this.getIDButton(3).func_146115_a()) {
            this.drawListText(Arrays.asList("The Maximum Radius that the Deletion will stop At", "This can not be smaller or Equal to the Minimum Radius", "Maximum Size: " + max + " x " + max + " Blocks", "Expected Amount of Chunks to Delete: " + this.getTotalGenerationCount()), mouseX, mouseY + 30);
        }
        else if (this.getIDButton(4).func_146115_a() || this.getIDButton(5).func_146115_a()) {
            final int x = ((GuiSlider)this.getIDButton(4)).getValueInt() * 16;
            final int z = ((GuiSlider)this.getIDButton(5)).getValueInt() * 16;
            this.drawListText(Arrays.asList("Defines where the Center of the Deletion is", "Center: X=" + x + ", Z=" + z), mouseX, mouseY - 15);
        }
        else if (this.getIDButton(6).func_146115_a()) {
            this.drawListText(Arrays.asList("Defines in which dimension the Deletion happens"), mouseX, mouseY - 15);
        }
        this.getIDButton(7).field_146124_l = (this.getTotalGenerationCount() <= 4000000 && min < max);
    }
    
    private int getTotalGenerationCount() {
        return BasePregenCommand.getRingCount(((GuiSlider)this.getIDButton(2)).getValueInt(), ((GuiSlider)this.getIDButton(3)).getValueInt(), this.shapes.getIndex() == 1);
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
