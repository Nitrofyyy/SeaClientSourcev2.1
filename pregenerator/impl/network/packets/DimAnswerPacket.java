// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.network.packets;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pregenerator.impl.client.gui.GuiStructureView;
import pregenerator.impl.client.gui.GuiWorldView;
import pregenerator.impl.client.gui.GuiChunkInfo;
import java.util.Collection;
import java.util.ArrayList;
import pregenerator.impl.client.gui.GuiTrackerOptions;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import pregenerator.base.api.network.IWriteableBuffer;
import pregenerator.base.api.network.IReadableBuffer;
import java.util.Iterator;
import pregenerator.impl.tracking.WorldTracker;
import java.util.List;
import java.util.HashSet;
import java.util.Set;
import pregenerator.base.api.network.PregenPacket;

public class DimAnswerPacket extends PregenPacket
{
    Set<Integer> set;
    int target;
    
    public DimAnswerPacket() {
        this.set = new HashSet<Integer>();
    }
    
    public DimAnswerPacket(final List<WorldTracker> list, final int answer) {
        this.set = new HashSet<Integer>();
        for (final WorldTracker tracker : list) {
            this.set.add(tracker.getDimID());
        }
        this.target = answer;
    }
    
    @Override
    public void read(final IReadableBuffer buffer) {
        this.target = buffer.readByte();
        for (int expected = buffer.readShort(), i = 0; i < expected; ++i) {
            this.set.add(buffer.readInt());
        }
    }
    
    @Override
    public void write(final IWriteableBuffer buffer) {
        buffer.writeByte(this.target);
        buffer.writeShort(this.set.size());
        for (final Integer entry : this.set) {
            buffer.writeInt(entry);
        }
    }
    
    @Override
    public void handle(final EntityPlayer player) {
        this.handleClient();
    }
    
    @SideOnly(Side.CLIENT)
    public void handleClient() {
        if (this.target == 0) {
            final Minecraft mc = Minecraft.func_71410_x();
            if (mc.field_71462_r instanceof GuiTrackerOptions) {
                ((GuiTrackerOptions)mc.field_71462_r).addDimensions(new ArrayList<Integer>(this.set));
            }
        }
        else if (this.target == 1) {
            GuiChunkInfo.INSTANCE.addDims(new ArrayList<Integer>(this.set));
        }
        else if (this.target == 2) {
            GuiWorldView.ENTITIES.addDims(new ArrayList<Integer>(this.set));
        }
        else if (this.target == 3) {
            GuiWorldView.TILE_ENTITIES.addDims(new ArrayList<Integer>(this.set));
        }
        else if (this.target == 4) {
            GuiStructureView.INSTANCE.addDims(new ArrayList<Integer>(this.set));
        }
    }
}
