// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.base.api.network;

import net.minecraft.entity.player.EntityPlayer;

public abstract class PregenPacket
{
    public abstract void read(final IReadableBuffer p0);
    
    public abstract void write(final IWriteableBuffer p0);
    
    public abstract void handle(final EntityPlayer p0);
}
