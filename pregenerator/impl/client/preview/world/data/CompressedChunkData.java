// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.client.preview.world.data;

import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import pregenerator.impl.client.preview.data.IFileProvider;
import java.io.RandomAccessFile;
import pregenerator.impl.client.preview.world.IHeightMap;
import pregenerator.impl.client.preview.texture.MoveableTexture;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.World;
import net.minecraft.util.BlockPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.block.material.MapColor;

public class CompressedChunkData extends BaseChunkData
{
    int[] blockColors;
    MapColor[] originals;
    int[] biomeIDs;
    
    protected CompressedChunkData() {
        this.blockColors = new int[256];
        this.originals = new MapColor[0];
        this.biomeIDs = new int[256];
    }
    
    public CompressedChunkData(final Chunk chunk) {
        super(chunk);
        this.blockColors = new int[256];
        this.originals = new MapColor[0];
        this.biomeIDs = new int[256];
        this.originals = new MapColor[256];
        final World world = chunk.func_177412_p();
        final int xPos = chunk.field_76635_g * 16;
        final int zPos = chunk.field_76647_h * 16;
        int heighest = chunk.func_76625_h() + 16;
        final int left = Math.abs(heighest - chunk.func_177412_p().field_73011_w.getActualHeight());
        if (heighest <= 5 || chunk.func_177412_p().field_73011_w.func_177495_o()) {
            heighest = chunk.func_177412_p().func_181545_F() + 1;
        }
        final BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        for (int x = 0; x < 16; ++x) {
            for (int z = 0; z < 16; ++z) {
                pos.func_181079_c(xPos + x, 64, zPos + z);
                final BiomeGenBase biome = chunk.func_177411_a((BlockPos)pos, world.func_72959_q());
                final int grass = biome.func_180627_b((BlockPos)pos);
                final int foliage = biome.func_180625_c((BlockPos)pos);
                pos.func_181079_c(xPos + x, heighest, zPos + z);
                this.getFirstBlock(chunk, pos);
                final int index = z * 16 + x;
                final IBlockState state = chunk.func_177435_g((BlockPos)pos);
                final MapColor map = state.func_177230_c().func_180659_g(state);
                this.blockColors[index] = CompressionManager.INSTANCE.getIndexForBlock(map, CompressionManager.BlockColorType.NORMAL);
                this.originals[index] = map;
                this.biomeIDs[index] = CompressionManager.INSTANCE.getIndexForBiome(biome);
                pos.func_181079_c(xPos + x, heighest, zPos + z);
                this.heightMap[index] = this.getHeightMapHeight(chunk, pos);
            }
        }
    }
    
    public int[] getData(final boolean type) {
        return type ? this.biomeIDs : this.blockColors;
    }
    
    @Override
    public int getBiome(final int x, final int z) {
        return 0;
    }
    
    @Override
    public void addToTexture(final MoveableTexture texture, final int view, final int size) {
        texture.addChunkData(this, size, view);
    }
    
    @Override
    public void updateHeightMap(final IHeightMap map) {
        for (int x = 0; x < 16; ++x) {
            for (int z = 0; z < 16; ++z) {
                final int xPos = this.chunkX * 16 + x;
                final int zPos = this.chunkZ * 16 + z;
                final int index = z * 16 + x;
                if (this.isDarker(xPos, zPos, map)) {
                    this.blockColors[index] = CompressionManager.INSTANCE.getIndexForBlock(this.originals[index], CompressionManager.BlockColorType.DARKER);
                }
                else if (this.isBrigther(xPos, zPos, map)) {
                    this.blockColors[index] = CompressionManager.INSTANCE.getIndexForBlock(this.originals[index], CompressionManager.BlockColorType.BRIGTHER);
                }
            }
        }
    }
    
    @Override
    public void storeInHeightMap(final IHeightMap map) {
        map.storeHeightData(this.chunkX, this.chunkZ, this.heightMap);
    }
    
    @Override
    public void handleTask(final RandomAccessFile chunkData, final RandomAccessFile heightData, final IFileProvider provider) throws Exception {
        MappedByteBuffer buffer = chunkData.getChannel().map(FileChannel.MapMode.READ_WRITE, provider.getOrCreateIndex(this.chunkX, this.chunkZ, IFileProvider.FileType.Compressed_Chunk_Data), IFileProvider.FileType.Compressed_Chunk_Data.getOffset());
        buffer.put((byte)1);
        buffer.putInt(this.chunkX).putInt(this.chunkZ);
        buffer.put((byte)(this.slimeChunk ? 1 : 0));
        for (int i = 0; i < 256; ++i) {
            buffer.put((byte)(this.blockColors[i] - 128));
        }
        for (int i = 0; i < 256; ++i) {
            buffer.put((byte)(this.biomeIDs[i] - 128));
        }
        buffer = heightData.getChannel().map(FileChannel.MapMode.READ_WRITE, provider.getIndex(this.chunkX, this.chunkZ, IFileProvider.FileType.HeightData), IFileProvider.FileType.HeightData.getOffset());
        buffer.put((byte)1);
        for (int i = 0; i < this.heightMap.length; ++i) {
            buffer.put((byte)(this.heightMap[i] - 128));
        }
    }
    
    public static CompressedChunkData createDataFromBuffer(final ByteBuffer chunkData, final ByteBuffer heightData) throws Exception {
        final CompressedChunkData data = new CompressedChunkData();
        data.chunkX = chunkData.getInt();
        data.chunkZ = chunkData.getInt();
        data.slimeChunk = (chunkData.get() == 1);
        for (int i = 0; i < 256; ++i) {
            data.blockColors[i] = chunkData.get() + 128;
        }
        for (int i = 0; i < 256; ++i) {
            data.biomeIDs[i] = chunkData.get() + 128;
        }
        if (heightData.get() <= 0) {
            data.heightMap = new int[0];
        }
        else {
            for (int i = 0; i < data.heightMap.length; ++i) {
                data.heightMap[i] = heightData.get() + 128;
            }
        }
        return data;
    }
}
