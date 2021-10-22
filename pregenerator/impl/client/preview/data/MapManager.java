// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.client.preview.data;

import net.minecraft.world.World;
import java.io.File;
import pregenerator.impl.client.preview.world.WorldSeed;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import pregenerator.impl.client.preview.world.ChunkCache;
import java.util.Map;

public class MapManager implements Runnable
{
    Thread thread;
    boolean running;
    Map<Integer, ChunkCache> dimensions;
    
    public MapManager() {
        this.running = true;
        this.dimensions = new ConcurrentHashMap<Integer, ChunkCache>();
        (this.thread = new Thread(this, "FileManager")).setDaemon(true);
        this.thread.start();
    }
    
    public void shutdown() {
        if (!this.running) {
            return;
        }
        this.running = false;
        try {
            this.thread.interrupt();
            while (this.thread.isAlive()) {
                try {
                    Thread.sleep(1L);
                }
                catch (Exception ex) {}
            }
            this.thread = null;
            for (final ChunkCache dim : this.dimensions.values()) {
                dim.close();
            }
            this.dimensions.clear();
            System.gc();
        }
        catch (Exception e) {
            if (!this.running) {
                return;
            }
            e.printStackTrace();
        }
    }
    
    @Override
    public void run() {
        while (this.running) {
            try {
                for (final ChunkCache dim : this.dimensions.values()) {
                    dim.update();
                }
                Thread.sleep(1L);
            }
            catch (Exception e) {
                if (!this.running) {
                    return;
                }
                e.printStackTrace();
            }
        }
    }
    
    public ChunkCache createDimension(final int dim) {
        ChunkCache cache = this.dimensions.get(dim);
        if (cache == null) {
            try {
                WorldSeed.getMapFolder().mkdirs();
                final File chunk = new File(WorldSeed.getMapFolder(), "Chunk_DIM_" + dim);
                final File height = new File(WorldSeed.getMapFolder(), "Height_DIM_" + dim);
                cache = new ChunkCache(chunk, height);
                this.dimensions.put(dim, cache);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        return cache;
    }
    
    public void removeDimension(final int dimension) {
        final ChunkCache cache = this.dimensions.remove(dimension);
        if (cache == null) {
            return;
        }
        cache.close();
    }
    
    public void addTask(final World world, final ITask task) {
        this.addTask(world.field_73011_w.func_177502_q(), task);
    }
    
    public void addTask(final int dim, final ITask task) {
        final ChunkCache data = this.dimensions.get(dim);
        if (data == null) {
            return;
        }
        data.addTask(task);
    }
}
