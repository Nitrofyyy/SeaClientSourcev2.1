// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.processor.deleter;

import pregenerator.ChunkPregenerator;
import net.minecraft.world.WorldServer;
import java.util.concurrent.Future;
import pregenerator.impl.processor.PrepaireProgress;

public abstract class IDeletionTask
{
    public abstract Future<DeleteProcess> createTask(final PrepaireProgress p0);
    
    protected final WorldServer getWorld(final int dim) {
        return ChunkPregenerator.getServer().func_71218_a(dim);
    }
}
