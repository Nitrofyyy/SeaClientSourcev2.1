// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.network.packets;

import pregenerator.ChunkPregenerator;
import net.minecraft.entity.player.EntityPlayer;
import pregenerator.base.api.network.IWriteableBuffer;
import pregenerator.base.api.network.IReadableBuffer;
import pregenerator.base.api.network.PregenPacket;

public class PermissionRequestPacket extends PregenPacket
{
    boolean value;
    
    @Override
    public void read(final IReadableBuffer buffer) {
        this.value = buffer.readBoolean();
    }
    
    @Override
    public void write(final IWriteableBuffer buffer) {
        buffer.writeBoolean(this.value);
    }
    
    @Override
    public void handle(final EntityPlayer player) {
        ChunkPregenerator.networking.sendPacketToPlayer(new PermissionAnswerPacket(ChunkPregenerator.pregenBase.hasPermission(player, "pregen-chunkview")), player);
    }
}
