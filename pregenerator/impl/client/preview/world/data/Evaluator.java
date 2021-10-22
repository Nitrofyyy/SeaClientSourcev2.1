// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.client.preview.world.data;

import java.util.Iterator;
import java.util.Set;
import java.util.LinkedHashSet;
import java.util.ArrayList;
import java.util.List;
import pregenerator.impl.client.preview.world.IHeightMap;
import pregenerator.impl.misc.FilePos;
import java.util.concurrent.ConcurrentHashMap;
import pregenerator.impl.misc.Tuple;
import java.util.Map;

public class Evaluator
{
    Map<Long, Tuple<IChunkData, Timer>> toEvaluate;
    
    public Evaluator() {
        this.toEvaluate = new ConcurrentHashMap<Long, Tuple<IChunkData, Timer>>();
    }
    
    public void addChunk(final IChunkData data) {
        Tuple<IChunkData, Timer> entry = this.toEvaluate.get(FilePos.asLong(data.getX(), data.getZ()));
        if (entry == null) {
            entry = new Tuple<IChunkData, Timer>(data, new Timer());
            this.toEvaluate.put(FilePos.asLong(data.getX(), data.getZ()), entry);
        }
        else {
            entry.setFirst(data);
            entry.getSecond().reset();
        }
    }
    
    public List<IChunkData> tick(final boolean forced, final IHeightMap map) {
        final List<IChunkData> finished = new ArrayList<IChunkData>();
        final Set<Long> toRemove = new LinkedHashSet<Long>();
        for (final Map.Entry<Long, Tuple<IChunkData, Timer>> subEntry : this.toEvaluate.entrySet()) {
            final Tuple<IChunkData, Timer> entry = subEntry.getValue();
            if (this.processChunk(entry, map)) {
                if (!entry.getSecond().isTimeOut(forced)) {
                    continue;
                }
                entry.getFirst().updateHeightMap(map);
                finished.add(entry.getFirst());
                toRemove.add(subEntry.getKey());
            }
            else {
                finished.add(entry.getFirst());
                toRemove.add(subEntry.getKey());
            }
        }
        for (final Long value : toRemove) {
            this.toEvaluate.remove(value);
        }
        return finished;
    }
    
    private boolean processChunk(final Tuple<IChunkData, Timer> entry, final IHeightMap map) {
        final IChunkData data = entry.getFirst();
        if (!this.canGenerate(data.getX(), data.getZ(), map)) {
            return true;
        }
        data.updateHeightMap(map);
        return false;
    }
    
    public boolean canGenerate(final int x, final int z, final IHeightMap map) {
        return map.hasHeightsStored(x + 1, z) && map.hasHeightsStored(x, z + 1);
    }
    
    public void clear() {
        this.toEvaluate.clear();
    }
    
    public static class Timer
    {
        int time;
        
        public boolean isTimeOut(final boolean forced) {
            ++this.time;
            return this.time >= 200 && forced;
        }
        
        public void reset() {
            this.time = 0;
        }
    }
}
