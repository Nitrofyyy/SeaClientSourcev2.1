// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.network.packets.chunkRequest;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pregenerator.impl.client.gui.GuiChunkInfo;
import net.minecraft.entity.player.EntityPlayer;
import pregenerator.base.api.network.IReadableBuffer;
import java.util.Iterator;
import pregenerator.base.api.network.IWriteableBuffer;
import java.util.LinkedList;
import pregenerator.impl.tracking.ChunkEntry;
import java.util.List;
import pregenerator.base.api.network.PregenPacket;

public class ChunkAnswerPacket extends PregenPacket
{
    List<ChunkEntry> chunks;
    int dataType;
    
    public ChunkAnswerPacket() {
        this.chunks = new LinkedList<ChunkEntry>();
    }
    
    public void addChunkEntry(final ChunkEntry entry) {
        this.chunks.add(entry);
    }
    
    public void setType(final int type) {
        this.dataType = type;
    }
    
    public boolean hasData() {
        return this.chunks.size() > 0;
    }
    
    @Override
    public void write(final IWriteableBuffer buffer) {
        buffer.writeInt(this.dataType);
        if (this.dataType == -1) {
            return;
        }
        buffer.writeInt(this.chunks.size());
        for (final ChunkEntry entry : this.chunks) {
            entry.writeToBuffer(buffer);
        }
    }
    
    @Override
    public void read(final IReadableBuffer buffer) {
        this.dataType = buffer.readInt();
        if (this.dataType == -1) {
            return;
        }
        for (int size = buffer.readInt(), i = 0; i < size; ++i) {
            this.chunks.add(ChunkEntry.fromBuffer(buffer));
        }
    }
    
    @Override
    public void handle(final EntityPlayer player) {
        this.handleClient();
    }
    
    @SideOnly(Side.CLIENT)
    public void handleClient() {
        if (this.dataType == -1) {
            GuiChunkInfo.INSTANCE.noDataFound();
        }
        else {
            GuiChunkInfo.INSTANCE.addChunks(this.chunks, this.dataType);
        }
    }
}
