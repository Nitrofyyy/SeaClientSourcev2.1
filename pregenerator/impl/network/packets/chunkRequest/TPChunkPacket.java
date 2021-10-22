// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.network.packets.chunkRequest;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import pregenerator.ChunkPregenerator;
import net.minecraft.entity.player.EntityPlayer;
import pregenerator.base.api.network.IWriteableBuffer;
import pregenerator.base.api.network.IReadableBuffer;
import pregenerator.impl.misc.FilePos;
import pregenerator.base.api.network.PregenPacket;

public class TPChunkPacket extends PregenPacket
{
    int x;
    int z;
    
    public TPChunkPacket() {
    }
    
    public TPChunkPacket(final FilePos pos) {
        this.x = pos.x * 16 + 8;
        this.z = pos.z * 16 + 8;
    }
    
    @Override
    public void read(final IReadableBuffer buffer) {
        this.x = buffer.readInt();
        this.z = buffer.readInt();
    }
    
    @Override
    public void write(final IWriteableBuffer buffer) {
        buffer.writeInt(this.x);
        buffer.writeInt(this.z);
    }
    
    @Override
    public void handle(final EntityPlayer player) {
        final MinecraftServer server = ChunkPregenerator.getServer();
        if (!server.func_152345_ab()) {
            server.func_152344_a((Runnable)new Runnable() {
                @Override
                public void run() {
                    TPChunkPacket.this.process(player);
                }
            });
            return;
        }
        this.process(player);
    }
    
    public void process(final EntityPlayer player) {
        if (player instanceof EntityPlayerMP) {
            final EntityPlayerMP mp = (EntityPlayerMP)player;
            if (mp.func_70115_ae()) {
                mp.func_70078_a((Entity)null);
            }
            mp.func_70634_a((double)this.x, mp.field_70163_u, (double)this.z);
            mp.field_70143_R = 0.0f;
        }
    }
}
