// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world;

import pregenerator.impl.misc.FilePos;
import pregenerator.impl.client.preview.data.Tasks;
import pregenerator.impl.client.preview.world.data.IChunkData;
import net.minecraft.block.material.Material;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.init.Blocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import java.util.Arrays;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.TimeUnit;
import com.google.common.cache.CacheBuilder;
import net.minecraft.util.BlockPos;
import java.io.File;
import java.io.RandomAccessFile;
import pregenerator.impl.client.preview.data.ITask;
import java.util.Deque;
import com.google.common.cache.Cache;
import net.minecraft.world.chunk.Chunk;
import pregenerator.impl.client.preview.world.IHeightMap;
import pregenerator.impl.client.preview.data.IFileProvider;

public class ChunkCache implements IBlockAccess, IFileProvider, IHeightMap
{
    protected int chunkX;
    protected int chunkZ;
    protected Chunk[][] chunkArray;
    protected boolean hasExtendedLevels;
    protected World worldObj;
    Cache<Integer, int[]> cachedHeights;
    Deque<ITask> tasks;
    int[] dataIndexes;
    int currentIndex;
    RandomAccessFile chunkData;
    RandomAccessFile heightData;
    File chunkFile;
    File heightFile;
    
    public ChunkCache(final World worldIn, final BlockPos posFromIn, final BlockPos posToIn, final int subIn) {
        this.worldObj = worldIn;
        this.chunkX = posFromIn.getX() - subIn >> 4;
        this.chunkZ = posFromIn.getZ() - subIn >> 4;
        final int i = posToIn.getX() + subIn >> 4;
        final int j = posToIn.getZ() + subIn >> 4;
        this.chunkArray = new Chunk[i - this.chunkX + 1][j - this.chunkZ + 1];
        this.hasExtendedLevels = true;
        for (int k = this.chunkX; k <= i; ++k) {
            for (int l = this.chunkZ; l <= j; ++l) {
                this.chunkArray[k - this.chunkX][l - this.chunkZ] = worldIn.getChunkFromChunkCoords(k, l);
            }
        }
        for (int i2 = posFromIn.getX() >> 4; i2 <= posToIn.getX() >> 4; ++i2) {
            for (int j2 = posFromIn.getZ() >> 4; j2 <= posToIn.getZ() >> 4; ++j2) {
                final Chunk chunk = this.chunkArray[i2 - this.chunkX][j2 - this.chunkZ];
                if (chunk != null && !chunk.getAreLevelsEmpty(posFromIn.getY(), posToIn.getY())) {
                    this.hasExtendedLevels = false;
                }
            }
        }
    }
    
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
    
    @Override
    public boolean extendedLevelsInChunkCache() {
        return this.hasExtendedLevels;
    }
    
    @Override
    public TileEntity getTileEntity(final BlockPos pos) {
        final int i = (pos.getX() >> 4) - this.chunkX;
        final int j = (pos.getZ() >> 4) - this.chunkZ;
        return this.chunkArray[i][j].getTileEntity(pos, Chunk.EnumCreateEntityType.IMMEDIATE);
    }
    
    @Override
    public int getCombinedLight(final BlockPos pos, final int lightValue) {
        final int i = this.getLightForExt(EnumSkyBlock.SKY, pos);
        int j = this.getLightForExt(EnumSkyBlock.BLOCK, pos);
        if (j < lightValue) {
            j = lightValue;
        }
        return i << 20 | j << 4;
    }
    
    @Override
    public IBlockState getBlockState(final BlockPos pos) {
        if (pos.getY() >= 0 && pos.getY() < 256) {
            final int i = (pos.getX() >> 4) - this.chunkX;
            final int j = (pos.getZ() >> 4) - this.chunkZ;
            if (i >= 0 && i < this.chunkArray.length && j >= 0 && j < this.chunkArray[i].length) {
                final Chunk chunk = this.chunkArray[i][j];
                if (chunk != null) {
                    return chunk.getBlockState(pos);
                }
            }
        }
        return Blocks.air.getDefaultState();
    }
    
    @Override
    public BiomeGenBase getBiomeGenForCoords(final BlockPos pos) {
        return this.worldObj.getBiomeGenForCoords(pos);
    }
    
    private int getLightForExt(final EnumSkyBlock p_175629_1_, final BlockPos pos) {
        if (p_175629_1_ == EnumSkyBlock.SKY && this.worldObj.provider.getHasNoSky()) {
            return 0;
        }
        if (pos.getY() < 0 || pos.getY() >= 256) {
            return p_175629_1_.defaultLightValue;
        }
        if (this.getBlockState(pos).getBlock().getUseNeighborBrightness()) {
            int l = 0;
            EnumFacing[] values;
            for (int length = (values = EnumFacing.values()).length, n = 0; n < length; ++n) {
                final EnumFacing enumfacing = values[n];
                final int k = this.getLightFor(p_175629_1_, pos.offset(enumfacing));
                if (k > l) {
                    l = k;
                }
                if (l >= 15) {
                    return l;
                }
            }
            return l;
        }
        final int i = (pos.getX() >> 4) - this.chunkX;
        final int j = (pos.getZ() >> 4) - this.chunkZ;
        return this.chunkArray[i][j].getLightFor(p_175629_1_, pos);
    }
    
    @Override
    public boolean isAirBlock(final BlockPos pos) {
        return this.getBlockState(pos).getBlock().getMaterial() == Material.air;
    }
    
    public int getLightFor(final EnumSkyBlock p_175628_1_, final BlockPos pos) {
        if (pos.getY() >= 0 && pos.getY() < 256) {
            final int i = (pos.getX() >> 4) - this.chunkX;
            final int j = (pos.getZ() >> 4) - this.chunkZ;
            return this.chunkArray[i][j].getLightFor(p_175628_1_, pos);
        }
        return p_175628_1_.defaultLightValue;
    }
    
    @Override
    public int getStrongPower(final BlockPos pos, final EnumFacing direction) {
        final IBlockState iblockstate = this.getBlockState(pos);
        return iblockstate.getBlock().getStrongPower(this, pos, iblockstate, direction);
    }
    
    @Override
    public WorldType getWorldType() {
        return this.worldObj.getWorldType();
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
        catch (Exception exception1) {
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
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }
    
    public void addTask(final ITask task) {
        this.tasks.add(task);
    }
    
    public IChunkData getChunk(final int x, final int z, final Cache cache) {
        final Tasks.FetchChunkTask data = new Tasks.FetchChunkTask(x, z, (Cache<FilePos, IChunkData>)cache);
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
        return (index >= 4000000) ? -1L : (this.dataIndexes[index] * type.getOffset());
    }
    
    @Override
    public long getOrCreateIndex(final int x, final int y, final FileType type) {
        final int index = this.index(x, y);
        if (index >= 4000000) {
            return -1L;
        }
        if (this.dataIndexes[index] == -1) {
            this.dataIndexes[index] = this.currentIndex++;
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
        return (y + 1000) * 2000 + x + 1000;
    }
    
    @Override
    public void storeHeightData(final int x, final int y, final int[] heights) {
        final int index = this.index(x, y);
        if (index < 4000000) {
            this.cachedHeights.put((Object)index, (Object)heights);
        }
    }
    
    @Override
    public int getHeight(final int x, final int y) {
        return this.getHeight(x, y, 0);
    }
    
    @Override
    public int getHeight(final int x, final int y, final int defaultValue) {
        final int[] height = this.getHeightData(x >> 4, y >> 4);
        return (height.length <= 0) ? defaultValue : height[(y & 0xF) * 16 + (x & 0xF)];
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
            catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        return data.getHeightData();
    }
}
