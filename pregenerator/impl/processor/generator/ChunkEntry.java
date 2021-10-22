// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.processor.generator;

import pregenerator.impl.misc.TrackedRegionFile;
import pregenerator.ChunkPregenerator;
import net.minecraft.world.chunk.IChunkProvider;
import pregenerator.impl.retrogen.RetrogenHandler;
import pregenerator.impl.structure.StructureManager;
import pregenerator.ConfigManager;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.common.eventhandler.Event;
import pregenerator.base.impl.misc.PregenEvent;
import net.minecraftforge.common.MinecraftForge;
import pregenerator.impl.misc.ProcessResult;
import java.util.BitSet;
import pregenerator.impl.misc.FilePos;

public class ChunkEntry
{
    static final int[][] DATA3x3;
    static final int[][] DATA2x2;
    GenerationType type;
    FilePos position;
    BitSet tasks;
    ChunkProcess process;
    int maxSize;
    long[] cache;
    int index;
    int state;
    
    public ChunkEntry(final ChunkProcess process, final Long pos, final BitSet tasks, final GenerationType type) {
        this.cache = null;
        this.index = 0;
        this.state = 0;
        this.process = process;
        this.position = new FilePos(pos);
        this.tasks = tasks;
        this.type = type;
        this.state = process.memory.getState(pos);
    }
    
    public void init(final ChunkProcess.RegionProvider provider) {
        final BitSet file = provider.get(this.position.x << 5, this.position.z << 5);
        for (int i = 0; i < 1024; ++i) {
            if (this.tasks.get(i)) {
                if (this.isChunkValid(i % 32, i / 32, file, false, provider) || this.state > 0) {
                    ++this.maxSize;
                }
                else {
                    this.tasks.clear(i);
                }
            }
        }
    }
    
    public boolean isLighting() {
        return this.state >= 2;
    }
    
    public int getMaxSize() {
        return this.maxSize;
    }
    
    public FilePos getPosition() {
        return this.position;
    }
    
    public long getPos() {
        this.generateCache();
        return this.cache[this.index];
    }
    
    public ProcessResult tick() {
        if (this.index == 0) {
            this.process.memory.setState(this.position.asLong(), (byte)1);
        }
        this.generateCache();
        return this.process(this.cache[this.index++]);
    }
    
    public void checkLight() {
        this.generateCache();
        for (int max = this.process.hasWork() ? 2 : 10, i = 0; i < max && !this.isDone(); ++i) {
            final long value = this.cache[this.index++];
            if (this.prepaireLight(value)) {
                final Chunk chunk = this.process.getHelper().loadChunk(FilePos.getX(value), FilePos.getZ(value));
                if (chunk != null) {
                    chunk.func_150809_p();
                    chunk.func_76613_n();
                    MinecraftForge.EVENT_BUS.post((Event)new PregenEvent(chunk));
                }
            }
        }
    }
    
    public boolean isDone() {
        return this.index >= this.maxSize;
    }
    
    private boolean prepaireLight(final long position) {
        for (int i = 0; i < ChunkEntry.DATA3x3.length; ++i) {
            if (!this.prepaireChild(FilePos.add(position, ChunkEntry.DATA3x3[i][0], ChunkEntry.DATA3x3[i][1]))) {
                return false;
            }
        }
        return true;
    }
    
    public void cleanup(final boolean lighting) {
        this.index = 0;
        this.cache = null;
        this.process.unloadChunks();
        this.process.removeFiles();
        if (ConfigManager.autoClearMineshaft) {
            final StructureManager manager = StructureManager.instance;
            manager.createSaveZone((this.position.x << 5) + 16, (this.position.z << 5) + 16, 16, "Mineshaft", this.process.world.field_73011_w.func_177502_q());
            manager.clearZoneLast(this.process.world.field_73011_w.func_177502_q(), "Mineshaft");
        }
        this.process.memory.setState(this.position.asLong(), (byte)(lighting ? 0 : 2));
    }
    
    private ProcessResult process(final long position) {
        if (!this.isChunkCreated(position)) {
            if (!this.type.requiresChunkCreation()) {
                return ProcessResult.MISSING;
            }
            final Chunk chunk = this.process.getHelper().createChunk(FilePos.getX(position), FilePos.getZ(position), this.type == GenerationType.BLOCK_POST, this.type.isPostGen());
            if (!this.type.isPostGen()) {
                this.unloadChunk(chunk);
                return ProcessResult.SUCCESS;
            }
            if (!this.prepaireChildren(position)) {
                this.unloadChunk(chunk);
                return ProcessResult.MISSING;
            }
            if (!this.populate(chunk)) {
                chunk.func_177446_d(false);
                this.unloadChunk(chunk);
                return ProcessResult.CRASH;
            }
            this.unloadChunk(chunk);
            return chunk.func_177419_t() ? ProcessResult.SUCCESS : ProcessResult.MISSING;
        }
        else {
            if (!this.type.requiresChunkLoading() && !this.type.isPostGen()) {
                return ProcessResult.CRASH;
            }
            if (!this.prepaireChildren(position)) {
                return ProcessResult.MISSING;
            }
            final Chunk chunk = this.process.getHelper().loadChunk(FilePos.getX(position), FilePos.getZ(position));
            if (chunk == null) {
                return ProcessResult.CRASH;
            }
            if (chunk.func_177419_t()) {
                if (this.type == GenerationType.RETROGEN) {
                    final boolean result = this.retrogen(chunk);
                    this.unloadChunk(chunk);
                    return result ? ProcessResult.SUCCESS : ProcessResult.MISSING;
                }
                this.unloadChunk(chunk);
                return (this.type == GenerationType.POST_GEN) ? ProcessResult.SUCCESS : ProcessResult.MISSING;
            }
            else {
                if (!this.populate(chunk)) {
                    chunk.func_177446_d(false);
                    this.unloadChunk(chunk);
                    return ProcessResult.CRASH;
                }
                this.unloadChunk(chunk);
                return chunk.func_177419_t() ? ProcessResult.SUCCESS : ProcessResult.MISSING;
            }
        }
    }
    
    private boolean prepaireChildren(final long position) {
        for (int i = 0; i < ChunkEntry.DATA2x2.length; ++i) {
            if (!this.prepaireChild(FilePos.add(position, ChunkEntry.DATA2x2[i][0], ChunkEntry.DATA2x2[i][1]))) {
                return false;
            }
        }
        return true;
    }
    
    private boolean prepaireChild(final long position) {
        if (this.process.getHelper().containsChunk(position)) {
            return true;
        }
        if (this.isChunkCreated(position)) {
            return this.process.getHelper().loadChunk(FilePos.getX(position), FilePos.getZ(position)) != null;
        }
        return this.process.getHelper().createChunk(FilePos.getX(position), FilePos.getZ(position), false, true) != null;
    }
    
    private boolean retrogen(final Chunk chunk) {
        return RetrogenHandler.INSTANCE.retrogenChunk(chunk, this.process.getProvider().field_73246_d, this.process.getProvider());
    }
    
    private boolean populate(final Chunk chunk) {
        try {
            chunk.func_76624_a((IChunkProvider)this.process.getProvider(), this.process.getProvider().field_73246_d, chunk.field_76635_g, chunk.field_76647_h);
            return true;
        }
        catch (Exception e) {
            ChunkPregenerator.LOGGER.info("Chunk Gen crash happend. No worry the Chunk gets skipped!", (Throwable)e);
            return false;
        }
    }
    
    private void unloadChunk(final Chunk chunk) {
        if (this.process.task.isPreview()) {
            MinecraftForge.EVENT_BUS.post((Event)new PregenEvent(chunk));
        }
        this.process.getHelper().unloadChunk(chunk);
    }
    
    protected boolean isChunkValid(final int x, final int z, final BitSet file, final boolean spawn, final ChunkProcess.RegionProvider region) {
        switch (this.type) {
            case TERRAIN_ONLY: {
                return this.isChunkGenerated(x, z, file, false, spawn, region) != ProcessResult.SUCCESS;
            }
            case FAST_CHECK_GEN: {
                return this.isChunkGenerated(x, z, file, false, spawn, region) != ProcessResult.SUCCESS;
            }
            case NORMAL_GEN: {
                return true;
            }
            case POST_GEN: {
                return this.isChunkGenerated(x, z, file, true, spawn, region) == ProcessResult.SUCCESS;
            }
            case BLOCK_POST: {
                return this.isChunkGenerated(x, z, file, false, spawn, region) != ProcessResult.SUCCESS;
            }
            case RETROGEN: {
                return this.isChunkGenerated(x, z, file, true, spawn, region) == ProcessResult.SUCCESS;
            }
            default: {
                return false;
            }
        }
    }
    
    protected ProcessResult isChunkGenerated(final int x, final int z, final BitSet file, final boolean full, final boolean spawn, final ChunkProcess.RegionProvider region) {
        final int regionX = (this.position.x << 5) + x;
        final int regionZ = (this.position.z << 5) + z;
        if (this.process.getProvider().func_73149_a(regionX, regionZ)) {
            return ProcessResult.SUCCESS;
        }
        if (file.isEmpty()) {
            return ProcessResult.CRASH;
        }
        if (!file.get(z * 32 + x)) {
            return ProcessResult.MISSING;
        }
        if (!full && this.isUnfinished(regionX, regionZ, region)) {
            return ProcessResult.MISSING;
        }
        return ProcessResult.SUCCESS;
    }
    
    protected boolean isUnfinished(final int x, final int z, final ChunkProcess.RegionProvider region) {
        for (int i = 1; i < 2; ++i) {
            if (!region.getChunk(x + i, z) || !region.getChunk(x - i, z) || !region.getChunk(x, z + i) || !region.getChunk(x, z - i)) {
                return true;
            }
        }
        return false;
    }
    
    private boolean isChunkCreated(final long pos) {
        final int x = FilePos.getX(pos);
        final int z = FilePos.getZ(pos);
        if (this.process.getProvider().func_73149_a(x, z)) {
            return true;
        }
        final TrackedRegionFile file = this.process.getFile(x, z);
        return file != null && file.func_76709_c(x & 0x1F, z & 0x1F);
    }
    
    private void generateCache() {
        if (this.cache == null) {
            this.cache = new long[this.maxSize];
            int i = 0;
            int index = 0;
            while (i < 1024) {
                if (this.tasks.get(i)) {
                    this.cache[index++] = FilePos.asLong((this.position.x << 5) + i % 32, (this.position.z << 5) + i / 32);
                }
                ++i;
            }
        }
    }
    
    static {
        DATA3x3 = new int[][] { { -1, -1 }, { 0, -1 }, { 1, -1 }, { -1, 0 }, { 1, 0 }, { -1, 1 }, { 0, 1 }, { 1, 1 } };
        DATA2x2 = new int[][] { { 1, 0 }, { 0, 1 }, { 1, 1 } };
    }
}
