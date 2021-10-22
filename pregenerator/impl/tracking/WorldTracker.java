// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.tracking;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.entity.Entity;
import net.minecraft.world.gen.ChunkProviderServer;
import java.util.Collection;
import java.util.ArrayList;
import net.minecraft.world.chunk.Chunk;
import java.util.List;
import net.minecraft.util.BlockPos;
import java.util.LinkedHashSet;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraft.world.IWorldAccess;
import net.minecraft.world.NextTickListEntry;
import java.util.Set;
import net.minecraft.world.WorldServer;

public class WorldTracker
{
    public WorldServer world;
    AverageCounter blockUpdates;
    AverageCounter blockTicks;
    TimeTracker tracker;
    public Set<NextTickListEntry> tickUpdates;
    WorldListener listener;
    
    public WorldTracker(final WorldServer world, final boolean baseTracking) {
        this.blockUpdates = new AverageCounter(40);
        this.blockTicks = new AverageCounter(40);
        this.tracker = new TimeTracker(40);
        this.world = world;
        this.listener = new WorldListener(this);
        if (baseTracking) {
            world.func_72954_a((IWorldAccess)this.listener);
        }
        try {
            this.tickUpdates = (Set<NextTickListEntry>)ReflectionHelper.getPrivateValue((Class)WorldServer.class, (Object)world, 4);
        }
        catch (Exception e) {
            this.tickUpdates = new LinkedHashSet<NextTickListEntry>();
            e.printStackTrace();
        }
    }
    
    public void setState(final boolean tracking) {
        if (tracking) {
            this.world.func_72954_a((IWorldAccess)this.listener);
        }
        else {
            this.world.func_72848_b((IWorldAccess)this.listener);
        }
    }
    
    public void onBlockUpdate(final BlockPos pos) {
        this.blockUpdates.addOne();
    }
    
    public void onTickStart() {
        this.tracker.setStart();
        this.blockTicks.addMore(this.tickUpdates.size());
        this.blockTicks.onFinished();
    }
    
    public void onTickEnd() {
        this.blockUpdates.onFinished();
        this.tracker.onFinished();
    }
    
    public int getAverageBlockChanges() {
        return this.blockUpdates.getAverage();
    }
    
    public int getBlockChanges() {
        return this.blockUpdates.getLast();
    }
    
    public int getAverageBlockTicks() {
        return this.blockTicks.getAverage();
    }
    
    public int getBlockTicks() {
        return this.blockTicks.getLast();
    }
    
    public int getLoadedChunks() {
        return this.world.func_72863_F().func_73152_e();
    }
    
    public int getLoadedEntities() {
        return this.world.field_72996_f.size();
    }
    
    public int getLoadedTileEntities() {
        return this.world.field_147482_g.size();
    }
    
    public int getTickingTileEntities() {
        return this.world.field_175730_i.size();
    }
    
    public int getDimID() {
        return this.world.field_73011_w.func_177502_q();
    }
    
    public long getAverage() {
        return this.tracker.getAverage();
    }
    
    public long getCurrent() {
        return this.tracker.getLastValue();
    }
    
    public List<Chunk> getChunks() {
        return new ArrayList<Chunk>(this.getProvider().func_152380_a());
    }
    
    private ChunkProviderServer getProvider() {
        return (ChunkProviderServer)this.world.func_72863_F();
    }
    
    public List<Entity> getEntityList() {
        return new ArrayList<Entity>(this.world.field_72996_f);
    }
    
    public List<TileEntity> getTileList() {
        return new ArrayList<TileEntity>(this.world.field_147482_g);
    }
}
