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
import pregenerator.impl.client.infos.InfoEntry;
import java.util.List;
import pregenerator.base.api.network.PregenPacket;

public class RequestPacket extends PregenPacket
{
    List<InfoEntry> entries;
    
    public RequestPacket() {
        this.entries = new ArrayList<InfoEntry>();
    }
    
    public RequestPacket(final List<InfoEntry> list) {
        (this.entries = new ArrayList<InfoEntry>()).addAll(list);
    }
    
    @Override
    public void read(final IReadableBuffer buffer) {
        for (int amount = buffer.readByte(), i = 0; i < amount; ++i) {
            final InfoEntry entry = InfoEntry.getByID(buffer.readByte());
            if (entry != null) {
                this.entries.add(entry);
            }
        }
    }
    
    @Override
    public void write(final IWriteableBuffer buffer) {
        buffer.writeByte(this.entries.size());
        for (final InfoEntry entry : this.entries) {
            buffer.writeByte(entry.getID());
        }
    }
    
    @Override
    public void handle(final EntityPlayer player) {
        ChunkPregenerator.networking.sendPacketToPlayer(new AnswerPacket(this.entries), player);
    }
}
