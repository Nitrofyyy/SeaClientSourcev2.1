// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.base.impl.misc;

import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.common.eventhandler.Event;

public class PregenEvent extends Event
{
    final Chunk chunk;
    
    public PregenEvent(final Chunk chunk) {
        this.chunk = chunk;
    }
    
    public Chunk getChunk() {
        return this.chunk;
    }
}
