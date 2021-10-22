// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.client.preview.world;

import pregenerator.impl.client.preview.data.Tasks;
import pregenerator.impl.client.preview.world.data.IChunkData;
import pregenerator.impl.misc.FilePos;
import java.util.Arrays;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.TimeUnit;
import com.google.common.cache.CacheBuilder;
import java.io.File;
import java.io.RandomAccessFile;
import pregenerator.impl.client.preview.data.ITask;
import java.util.Deque;
import com.google.common.cache.Cache;
import pregenerator.impl.client.preview.data.IFileProvider;

public class ChunkCache implements IFileProvider, IHeightMap
{
    Cache<Integer, int[]> cachedHeights;
    Deque<ITask> tasks;
    int[] dataIndexes;
    int currentIndex;
    RandomAccessFile chunkData;
    RandomAccessFile heightData;
    File chunkFile;
    File heightFile;
    
    public ChunkCache(final File chunk, final File height) throws Exception {
        this.cachedHeights = (Cache<Integer, int[]>)CacheBuilder.newBuilder().expireAfterAccess(1L, TimeUnit.MINUTES).build();
        this.tasks = new ConcurrentLinkedDeque<ITask>();
        this.dataIndexes = new int[4000000];
        this.currentIndex = 0;
        this.chunkFile = chunk;
        this.heightFile = height;
        this.chunkData = new RandomAccessFile(chunk, "rw");
        this.heightData = new RandomAccessFile(height, "rw");
        Arrays.fill(this.dataIndexes, -1);
    }
    
    public boolean reset() {
        while (!this.tasks.isEmpty()) {
            try {
                Thread.sleep(1L);
            }
            catch (Exception ex) {}
        }
        this.close();
        try {
            this.deleteSave(this.chunkFile);
            this.deleteSave(this.heightFile);
            this.chunkData = new RandomAccessFile(this.chunkFile, "rw");
            this.heightData = new RandomAccessFile(this.heightFile, "rw");
            Arrays.fill(this.dataIndexes, -1);
            return true;
        }
        catch (Exception ex2) {
            return false;
        }
    }
    
    private void deleteSave(final File file) {
        try {
            file.delete();
        }
        catch (Exception ex) {}
    }
    
    public void close() {
        try {
            if (this.chunkData != null) {
                this.chunkData.close();
                this.chunkData = null;
            }
            if (this.heightData != null) {
                this.heightData.close();
                this.heightData = null;
            }
        }
        catch (Exception ex) {}
    }
    
    public void update() {
        try {
            while (!this.tasks.isEmpty()) {
                this.tasks.removeFirst().handleTask(this.chunkData, this.heightData, this);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void addTask(final ITask task) {
        this.tasks.add(task);
    }
    
    public IChunkData getChunk(final int x, final int z, final Cache<FilePos, IChunkData> cache) {
        final Tasks.FetchChunkTask data = new Tasks.FetchChunkTask(x, z, cache);
        this.addTask(data);
        int tries = 0;
        while (!data.isDone()) {
            if (++tries >= 5) {
                return null;
            }
            try {
                Thread.sleep(1L);
            }
            catch (Exception ex) {}
        }
        return data.getData();
    }
    
    @Override
    public boolean hasIndex(final int x, final int y) {
        final int index = this.index(x, y);
        return index < 4000000 && this.dataIndexes[index] != -1;
    }
    
    @Override
    public long getIndex(final int x, final int y, final FileType type) {
        final int index = this.index(x, y);
        if (index >= 4000000) {
            return -1L;
        }
        return this.dataIndexes[index] * type.getOffset();
    }
    
    @Override
    public long getOrCreateIndex(final int x, final int y, final FileType type) {
        final int index = this.index(x, y);
        if (index >= 4000000) {
            return -1L;
        }
        if (this.dataIndexes[index] == -1) {
            this.dataIndexes[index] = this.currentIndex;
            ++this.currentIndex;
        }
        return this.dataIndexes[index] * type.getOffset();
    }
    
    @Override
    public long getTotalOffset(final FileType type) {
        return this.currentIndex * type.getOffset();
    }
    
    @Override
    public int getStored() {
        return this.currentIndex;
    }
    
    private int index(final int x, final int y) {
        return (y + 1000) * 2000 + (x + 1000);
    }
    
    @Override
    public void storeHeightData(final int x, final int y, final int[] heights) {
        final int index = this.index(x, y);
        if (index >= 4000000) {
            return;
        }
        this.cachedHeights.put((Object)index, (Object)heights);
    }
    
    @Override
    public int getHeight(final int x, final int y) {
        return this.getHeight(x, y, 0);
    }
    
    @Override
    public int getHeight(final int x, final int y, final int defaultValue) {
        final int[] height = this.getHeightData(x >> 4, y >> 4);
        if (height.length <= 0) {
            return defaultValue;
        }
        return height[(y & 0xF) * 16 + (x & 0xF)];
    }
    
    @Override
    public int[] getHeightData(final int x, final int y) {
        if (!this.hasHeightsStored(x, y)) {
            return new int[0];
        }
        final int index = this.index(x, y);
        int[] data = (int[])this.cachedHeights.getIfPresent((Object)index);
        if (data == null) {
            data = this.loadDataFromDisk(x, y);
            this.cachedHeights.put((Object)index, (Object)data);
        }
        return data;
    }
    
    @Override
    public boolean hasHeightsStored(final int x, final int y) {
        return this.getIndex(x, y, FileType.HeightData) >= 0L;
    }
    
    private int[] loadDataFromDisk(final int x, final int y) {
        final Tasks.FetchHeightData data = new Tasks.FetchHeightData(x, y);
        this.addTask(data);
        while (!data.isDone()) {
            try {
                Thread.sleep(1L);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        return data.getHeightData();
    }
}
