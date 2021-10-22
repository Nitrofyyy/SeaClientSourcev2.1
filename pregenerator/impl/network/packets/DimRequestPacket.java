// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.network.packets;

import pregenerator.impl.tracking.ServerTracker;
import pregenerator.ChunkPregenerator;
import net.minecraft.entity.player.EntityPlayer;
import pregenerator.base.api.network.IWriteableBuffer;
import pregenerator.base.api.network.IReadableBuffer;
import pregenerator.base.api.network.PregenPacket;

public class DimRequestPacket extends PregenPacket
{
    int index;
    
    public DimRequestPacket(final int dim) {
        this.index = 0;
        this.index = dim;
    }
    
    public DimRequestPacket() {
        this.index = 0;
    }
    
    @Override
    public void read(final IReadableBuffer buffer) {
        this.index = buffer.readInt();
    }
    
    @Override
    public void write(final IWriteableBuffer buffer) {
        buffer.writeInt(this.index);
    }
    
    @Override
    public void handle(final EntityPlayer player) {
        ChunkPregenerator.networking.sendPacketToPlayer(new DimAnswerPacket(ServerTracker.INSTANCE.getTracker(), this.index), player);
    }
}
