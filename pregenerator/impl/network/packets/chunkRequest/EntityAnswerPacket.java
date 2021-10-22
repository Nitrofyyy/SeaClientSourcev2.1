// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.network.packets.chunkRequest;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pregenerator.impl.client.gui.GuiWorldView;
import net.minecraft.entity.player.EntityPlayer;
import java.util.Iterator;
import pregenerator.base.api.network.IWriteableBuffer;
import pregenerator.impl.tracking.ChunkEntry;
import pregenerator.base.api.network.IReadableBuffer;
import java.util.HashMap;
import java.util.Map;
import pregenerator.base.api.network.PregenPacket;

public class EntityAnswerPacket extends PregenPacket
{
    int dataType;
    boolean target;
    Map<String, Integer> counts;
    
    public EntityAnswerPacket() {
        this.counts = new HashMap<String, Integer>();
    }
    
    public EntityAnswerPacket(final boolean target) {
        this.counts = new HashMap<String, Integer>();
        this.target = target;
    }
    
    public void addEntry(final String s, final int type) {
        this.counts.put(s, type);
    }
    
    public void setType(final int type) {
        this.dataType = type;
    }
    
    public boolean hasData() {
        return this.counts.size() > 0;
    }
    
    @Override
    public void read(final IReadableBuffer buffer) {
        this.target = buffer.readBoolean();
        this.dataType = buffer.readInt();
        if (this.dataType == -1) {
            return;
        }
        for (int expected = buffer.readInt(), i = 0; i < expected; ++i) {
            final String s = ChunkEntry.readString(buffer);
            final int data = buffer.readInt();
            this.counts.put(s, data);
        }
    }
    
    @Override
    public void write(final IWriteableBuffer buffer) {
        buffer.writeBoolean(this.target);
        buffer.writeInt(this.dataType);
        if (this.dataType == -1) {
            return;
        }
        buffer.writeInt(this.counts.size());
        for (final Map.Entry<String, Integer> entry : this.counts.entrySet()) {
            ChunkEntry.writeString(entry.getKey(), buffer);
            buffer.writeInt(entry.getValue());
        }
    }
    
    @Override
    public void handle(final EntityPlayer player) {
        this.onClient();
    }
    
    @SideOnly(Side.CLIENT)
    public void onClient() {
        final GuiWorldView view = this.target ? GuiWorldView.TILE_ENTITIES : GuiWorldView.ENTITIES;
        if (this.dataType == -1) {
            view.noDataFound();
        }
        else {
            view.addStuff(this.counts, this.dataType);
        }
    }
}
