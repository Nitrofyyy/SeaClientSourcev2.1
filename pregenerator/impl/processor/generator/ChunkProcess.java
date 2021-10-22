// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.processor.generator;

import java.util.LinkedHashMap;
import java.util.Comparator;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.storage.ThreadedFileIOBase;
import pregenerator.impl.misc.TrackedRegionFile;
import pregenerator.impl.misc.ProcessResult;
import net.minecraft.world.World;
import com.google.common.collect.UnmodifiableIterator;
import net.minecraft.world.ChunkCoordIntPair;
import java.util.Iterator;
import pregenerator.impl.processor.PrepaireProgress;
import pregenerator.impl.misc.FilePos;
import java.util.BitSet;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraft.world.chunk.storage.RegionFileCache;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import pregenerator.impl.storage.TaskStorage;
import pregenerator.impl.storage.PregenTask;
import net.minecraft.world.gen.ChunkProviderServer;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.storage.RegionFile;
import java.io.File;
import java.util.Map;

public class ChunkProcess
{
    Map<File, RegionFile> fileSystem;
    WorldServer world;
    ChunkProviderServer chunkProvider;
    ChunkHelper helper;
    File worldFile;
    PregenTask task;
    TaskStorage.PregenMemory memory;
    Set<Long> forcedChunks;
    List<ChunkEntry> toLight;
    List<ChunkEntry> entries;
    int totalWorkList;
    long startMemory;
    int startingChunkCount;
    long lastPos;
    
    public ChunkProcess(final WorldServer server, final PregenTask theTask) {
        this.forcedChunks = new HashSet<Long>();
        this.toLight = new ArrayList<ChunkEntry>();
        this.entries = new ArrayList<ChunkEntry>();
        this.totalWorkList = 0;
        this.startingChunkCount = 0;
        this.world = server;
        this.worldFile = new File(server.getChunkSaveLocation(), "region");
        this.chunkProvider = (ChunkProviderServer)server.func_72863_F();
        this.helper = new ChunkHelper(this.chunkProvider, server);
        this.startingChunkCount = this.chunkProvider.func_73152_e();
        this.task = theTask;
        this.fileSystem = (Map<File, RegionFile>)ReflectionHelper.getPrivateValue((Class)RegionFileCache.class, (Object)null, 0);
        if (this.fileSystem == null) {
            throw new IllegalStateException("FileSystem couldn't be found");
        }
    }
    
    public void addTaskList(final Map<Long, BitSet> positions, final FilePos center, final PrepaireProgress progress) {
        this.memory = TaskStorage.getStorage().getOrCreateMemory(this.task);
        progress.addMax(positions.size());
        ((ArrayList)this.entries).ensureCapacity(positions.size());
        final GenerationType type = this.task.getPostType();
        final RegionProvider provider = new RegionProvider(this);
        for (final Map.Entry<Long, BitSet> entry : positions.entrySet()) {
            final ChunkEntry chunk = new ChunkEntry(this, entry.getKey(), entry.getValue(), type);
            chunk.init(provider);
            final int size = chunk.getMaxSize();
            if (size > 0) {
                this.totalWorkList += size;
                if (chunk.isLighting()) {
                    this.toLight.add(chunk);
                }
                else {
                    this.entries.add(chunk);
                }
            }
            progress.growValue(1);
        }
        this.entries.sort(new Sorter(center).reversed());
        System.gc();
    }
    
    public void onTickStart() {
        this.forcedChunks.clear();
        for (final ChunkCoordIntPair pos : this.world.getPersistentChunks().keys()) {
            this.forcedChunks.add(FilePos.asLong(pos.field_77276_a, pos.field_77275_b));
        }
        this.helper.cleanUp();
    }
    
    public void setStartMemory() {
        this.startMemory = this.getCurrentMemoryUsage();
    }
    
    public boolean memoryToBig() {
        long currentUsage = this.getCurrentMemoryUsage() - this.startMemory;
        currentUsage = currentUsage / 1024L / 1024L;
        return currentUsage > 2048L || (this.fileSystem != null && this.fileSystem.size() > 120);
    }
    
    private long getCurrentMemoryUsage() {
        return Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
    }
    
    public void onRemove() {
        this.save();
        this.task.stopTask(this.world);
        this.fileSystem = null;
        this.world = null;
        this.chunkProvider = null;
        this.helper = null;
        this.worldFile = null;
        this.task = null;
        this.forcedChunks = null;
    }
    
    public boolean hasWork() {
        return this.entries.size() > 0;
    }
    
    public ProcessResult tick() {
        final ChunkEntry entry = this.entries.get(this.entries.size() - 1);
        this.lastPos = entry.getPos();
        final ProcessResult result = entry.tick();
        if (entry.isDone()) {
            this.toLight.add(this.entries.remove(this.entries.size() - 1));
            entry.cleanup(false);
        }
        return result;
    }
    
    public boolean hasLight() {
        return this.toLight.size() > 0;
    }
    
    public void checkLight() {
        final ChunkEntry entry = this.toLight.get(0);
        entry.checkLight();
        if (entry.isDone()) {
            this.toLight.remove(0);
            entry.cleanup(true);
        }
    }
    
    public FilePos getPosition() {
        return new FilePos(this.lastPos);
    }
    
    public int getTotalWorkList() {
        return this.totalWorkList;
    }
    
    public PregenTask getTask() {
        return this.task;
    }
    
    public boolean containsChunk(final int x, final int z) {
        return this.world.func_73040_p().func_152621_a(x, z) || this.forcedChunks.contains(new FilePos(x, z));
    }
    
    public TrackedRegionFile getFile(final int x, final int z) {
        try {
            final File file = new File(this.worldFile, "r." + (x >> 5) + "." + (z >> 5) + ".mca");
            final RegionFile region = this.fileSystem.get(file);
            if (region instanceof TrackedRegionFile && ((TrackedRegionFile)region).isValid()) {
                return (TrackedRegionFile)region;
            }
            if (region != null) {
                this.fileSystem.remove(file);
                try {
                    ThreadedFileIOBase.func_178779_a().func_75734_a();
                    region.func_76708_c();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (file.exists()) {
                final TrackedRegionFile newRegion = new TrackedRegionFile(file);
                this.fileSystem.put(file, newRegion);
                return newRegion;
            }
        }
        catch (Exception e2) {
            e2.printStackTrace();
        }
        return null;
    }
    
    public void removeFiles() {
        try {
            ChunkThread.clearCache();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void save() {
        try {
            ThreadedFileIOBase.func_178779_a().func_75734_a();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void unloadChunks() {
        for (final Chunk chunk : this.chunkProvider.field_73245_g) {
            if (this.containsChunk(chunk.field_76635_g, chunk.field_76647_h)) {
                continue;
            }
            this.helper.unloadChunk(chunk);
        }
        int amount = this.helper.getChunkCount() - this.startingChunkCount;
        if (amount >= 3000) {
            if (this.helper.getChunkCount() > 5000) {
                amount = this.helper.getChunkCount();
            }
            amount /= 100;
            for (int i = 0; i < amount; ++i) {
                this.chunkProvider.func_73156_b();
            }
        }
    }
    
    public ChunkProviderServer getProvider() {
        return this.chunkProvider;
    }
    
    public void unloadChunk(final Chunk chunk) {
        if (this.containsChunk(chunk.field_76635_g, chunk.field_76647_h)) {
            return;
        }
        this.helper.unloadChunk(chunk);
    }
    
    public ChunkHelper getHelper() {
        return this.helper;
    }
    
    public static class Sorter implements Comparator<ChunkEntry>
    {
        FilePos pos;
        
        public Sorter(final FilePos pos) {
            this.pos = pos.toChunkFile();
        }
        
        @Override
        public int compare(final ChunkEntry o1, final ChunkEntry o2) {
            final int value = o1.getPosition().getDistance(this.pos.x, this.pos.z) - o2.getPosition().getDistance(this.pos.x, this.pos.z);
            return (value > 0) ? 1 : ((value < 0) ? -1 : 0);
        }
    }
    
    static class RegionProvider
    {
        static final BitSet EMPTY;
        static final BitSet FULL;
        Map<Long, BitSet> map;
        ChunkProcess process;
        
        public RegionProvider(final ChunkProcess process) {
            this.map = new LinkedHashMap<Long, BitSet>();
            this.process = process;
        }
        
        public boolean getChunk(final int chunkX, final int chunkZ) {
            return this.get(chunkX, chunkZ).get((chunkZ & 0x1F) * 32 + (chunkX & 0x1F));
        }
        
        public BitSet get(final int chunkX, final int chunkZ) {
            BitSet set = this.map.get(FilePos.asLong(chunkX >> 5, chunkZ >> 5));
            if (set == null) {
                final RegionFile file = this.process.getFile(chunkX, chunkZ);
                if (file != null) {
                    set = new BitSet(1024);
                    set.clear();
                    for (int i = 0; i < 1024; ++i) {
                        if (file.func_76709_c(i % 32, i / 32)) {
                            set.set(i);
                        }
                    }
                    if (set.equals(RegionProvider.FULL)) {
                        set = RegionProvider.FULL;
                    }
                    this.map.put(FilePos.asLong(chunkX >> 5, chunkZ >> 5), set);
                }
                else {
                    this.map.put(FilePos.asLong(chunkX >> 5, chunkZ >> 5), RegionProvider.EMPTY);
                    set = RegionProvider.EMPTY;
                }
            }
            return set;
        }
        
        static BitSet create() {
            final BitSet set = new BitSet(1024);
            set.set(0, 1023);
            return set;
        }
        
        static {
            EMPTY = new BitSet(0);
            FULL = create();
        }
    }
}
