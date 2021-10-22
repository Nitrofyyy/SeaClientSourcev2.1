// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.client.preview.world;

import pregenerator.impl.structure.MapGenStructureDataPregen;
import pregenerator.impl.structure.StructureManager;
import java.util.Collection;
import pregenerator.impl.storage.PregenTask;
import net.minecraft.world.chunk.Chunk;
import pregenerator.impl.client.preview.texture.MoveableTexture;
import java.util.Iterator;
import pregenerator.impl.processor.generator.ChunkProcessor;
import pregenerator.impl.client.preview.data.SubProcessor;
import pregenerator.impl.client.preview.data.ITask;
import pregenerator.impl.client.preview.data.Tasks;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.LinkedHashSet;
import java.util.LinkedHashMap;
import java.util.concurrent.TimeUnit;
import com.google.common.cache.CacheBuilder;
import pregenerator.impl.client.preview.data.IFileProvider;
import pregenerator.impl.client.preview.world.data.Evaluator;
import pregenerator.impl.misc.Tuple;
import java.util.Deque;
import net.minecraft.world.gen.structure.StructureStart;
import java.util.Set;
import java.util.Map;
import pregenerator.impl.client.preview.world.data.IChunkData;
import com.google.common.cache.Cache;
import pregenerator.impl.misc.FilePos;

public class WorldInstance
{
    static final FilePos CENTER;
    Cache<FilePos, IChunkData> cachedChunks;
    Map<String, Set<StructureStart>> structureCache;
    Set<FilePos> slimeChunks;
    Deque<IChunkData> toSave;
    Deque<Tuple<IChunkData, Integer>> toRender;
    Evaluator evaluator;
    final int dimension;
    int radius;
    ChunkCache cache;
    boolean isFocus;
    IFileProvider.FileType type;
    boolean square;
    int lastState;
    
    public WorldInstance(final int dimension, final boolean shape, final ChunkCache cache) {
        this.cachedChunks = (Cache<FilePos, IChunkData>)CacheBuilder.newBuilder().maximumSize(64L).expireAfterAccess(30L, TimeUnit.SECONDS).build();
        this.structureCache = new LinkedHashMap<String, Set<StructureStart>>();
        this.slimeChunks = new LinkedHashSet<FilePos>();
        this.toSave = new ConcurrentLinkedDeque<IChunkData>();
        this.toRender = new ConcurrentLinkedDeque<Tuple<IChunkData, Integer>>();
        this.evaluator = new Evaluator();
        this.isFocus = false;
        this.square = true;
        this.lastState = -1;
        this.radius = 100;
        this.square = shape;
        this.dimension = dimension;
        this.cache = cache;
        this.isFocus = true;
        this.type = (WorldSeed.isUsingCompression() ? IFileProvider.FileType.Compressed_Chunk_Data : IFileProvider.FileType.Chunk_Data);
    }
    
    public void setRadius(final int radius) {
        this.radius = radius;
    }
    
    public void toggleShape() {
        this.square = !this.square;
    }
    
    public boolean isSquare() {
        return this.square;
    }
    
    public boolean isSaveToUse() {
        return this.lastState != 0;
    }
    
    public void setFocus(final int focusDim, final int view) {
        this.isFocus = (focusDim == this.dimension);
        if (this.isFocus) {
            this.reload(view);
        }
        else {
            this.toRender.clear();
        }
    }
    
    public int getRadius() {
        return this.radius;
    }
    
    public IChunkData getChunk(final int x, final int z) {
        final IChunkData data = (IChunkData)this.cachedChunks.getIfPresent((Object)new FilePos(x, z));
        if (data != null) {
            return data;
        }
        return this.cache.getChunk(x, z, this.cachedChunks);
    }
    
    public boolean isFocus() {
        return this.isFocus;
    }
    
    public void reload(final int view) {
        this.cache.addTask(new Tasks.MassFetchTask(this.toRender, view));
    }
    
    public void update(final int view, final SubProcessor process) {
        for (int i = 0; i < 100 && !this.toSave.isEmpty(); ++i) {
            final IChunkData data = this.toSave.removeFirst();
            data.storeInHeightMap(this.cache);
            if (data.isSlimeChunk()) {
                this.slimeChunks.add(new FilePos(data.getX(), data.getZ()));
            }
            this.cache.addTask(data);
            this.evaluator.addChunk(data);
            if (this.isFocus) {
                this.toRender.addLast(new Tuple<IChunkData, Integer>(data, view));
            }
        }
        process.addTask(new Runnable() {
            @Override
            public void run() {
                WorldInstance.this.process(view);
            }
        });
    }
    
    private void process(final int view) {
        for (final IChunkData data : this.evaluator.tick(ChunkProcessor.INSTANCE.isStopped(), this.cache)) {
            this.cache.addTask(data);
            if (this.isFocus) {
                this.toRender.addLast(new Tuple<IChunkData, Integer>(data, view));
            }
        }
    }
    
    public void render(final MoveableTexture texture) {
        for (int toUpdate = Math.max(1000, this.toRender.size() / 10), i = 0; i < toUpdate && !this.toRender.isEmpty(); ++i) {
            final Tuple<IChunkData, Integer> value = this.toRender.removeFirst();
            final IChunkData data = value.getFirst();
            if (data.getX() >= -this.radius && data.getX() < this.radius && data.getZ() >= -this.radius) {
                if (data.getZ() < this.radius) {
                    data.addToTexture(texture, value.getSecond(), this.radius * 16);
                }
            }
        }
    }
    
    public void addChunk(final Chunk data) {
        if (this.square) {
            if (data.field_76635_g < -this.radius || data.field_76635_g >= this.radius || data.field_76647_h < -this.radius || data.field_76647_h >= this.radius) {
                return;
            }
        }
        else if (WorldInstance.CENTER.getSqDistance(data.field_76635_g, data.field_76647_h) > this.radius) {
            return;
        }
        this.toSave.add(this.type.createData(data));
    }
    
    public PregenTask createTask(final int state) {
        this.lastState = Math.max(this.lastState, state);
        return new PregenTask(this.square ? 0 : 1, this.dimension, 0, 0, this.radius, 0, state);
    }
    
    public Collection<FilePos> getSlimeChunks() {
        return this.slimeChunks;
    }
    
    public Map<String, Set<StructureStart>> getStructures() {
        final Map<String, Set<StructureStart>> results = new LinkedHashMap<String, Set<StructureStart>>();
        for (final MapGenStructureDataPregen structure : StructureManager.instance.getStructureHolders(this.dimension)) {
            try {
                results.put(structure.field_76190_i, new LinkedHashSet<StructureStart>(structure.getStarts()));
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        for (final Map.Entry<String, Set<StructureStart>> entry : this.structureCache.entrySet()) {
            Set<StructureStart> resultEntry = results.get(entry.getKey());
            if (resultEntry == null) {
                resultEntry = new LinkedHashSet<StructureStart>();
                results.put(entry.getKey(), resultEntry);
            }
            resultEntry.addAll(entry.getValue());
        }
        return this.structureCache = results;
    }
    
    static {
        CENTER = new FilePos(0, 0);
    }
}
