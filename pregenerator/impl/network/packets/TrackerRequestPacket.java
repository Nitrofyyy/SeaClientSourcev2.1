// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.network.packets;

import pregenerator.ChunkPregenerator;
import net.minecraft.entity.player.EntityPlayer;
import java.util.Iterator;
import pregenerator.base.api.network.IWriteableBuffer;
import pregenerator.base.api.network.IReadableBuffer;
import java.util.Collection;
import java.util.ArrayList;
import pregenerator.impl.client.trackerInfo.TrackerEntry;
import java.util.List;
import pregenerator.base.api.network.PregenPacket;

public class TrackerRequestPacket extends PregenPacket
{
    List<TrackerEntry> entries;
    
    public TrackerRequestPacket() {
        this.entries = new ArrayList<TrackerEntry>();
    }
    
    public TrackerRequestPacket(final List<TrackerEntry> list) {
        (this.entries = new ArrayList<TrackerEntry>()).addAll(list);
    }
    
    @Override
    public void read(final IReadableBuffer buffer) {
        for (int count = buffer.readByte(), i = 0; i < count; ++i) {
            final TrackerEntry entry = TrackerEntry.getByID(buffer.readByte());
            if (entry != null) {
                entry.readServer(buffer);
                this.entries.add(entry);
            }
        }
    }
    
    @Override
    public void write(final IWriteableBuffer buffer) {
        buffer.writeByte(this.entries.size());
        for (final TrackerEntry entry : this.entries) {
            buffer.writeByte(entry.getID());
            entry.writeClient(buffer);
        }
    }
    
    @Override
    public void handle(final EntityPlayer player) {
        ChunkPregenerator.networking.sendPacketToPlayer(new TrackerAnswerPacket(this.entries), player);
    }
}
