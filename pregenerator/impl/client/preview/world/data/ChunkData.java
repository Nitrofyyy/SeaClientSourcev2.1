// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.client.preview.world.data;

import pregenerator.impl.client.preview.data.IFileProvider;
import java.io.RandomAccessFile;
import pregenerator.impl.client.preview.world.IHeightMap;
import pregenerator.impl.client.preview.texture.MoveableTexture;
import java.nio.ByteBuffer;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.World;
import net.minecraft.util.BlockPos;
import net.minecraft.world.chunk.Chunk;

public class ChunkData extends BaseChunkData
{
    int[] originalBlockColors;
    int[] blockColors;
    int[] biomeIDs;
    int[] biomeGrassColor;
    int[] biomeFoliageColor;
    
    protected ChunkData() {
        this.originalBlockColors = new int[0];
        this.blockColors = new int[256];
        this.biomeIDs = new int[256];
        this.biomeGrassColor = new int[256];
        this.biomeFoliageColor = new int[256];
    }
    
    public ChunkData(final Chunk chunk) {
        super(chunk);
        this.originalBlockColors = new int[0];
        this.blockColors = new int[256];
        this.biomeIDs = new int[256];
        this.biomeGrassColor = new int[256];
        this.biomeFoliageColor = new int[256];
        this.originalBlockColors = new int[256];
        final World world = chunk.func_177412_p();
        final int xPos = chunk.field_76635_g * 16;
        final int zPos = chunk.field_76647_h * 16;
        int heighest = chunk.func_76625_h() + 16;
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
                this.blockColors[index] = chunk.func_177435_g((BlockPos)pos).func_177230_c().func_149688_o().func_151565_r().func_151643_b(0);
                this.originalBlockColors[index] = this.blockColors[index];
                this.biomeFoliageColor[index] = foliage;
                this.biomeGrassColor[index] = grass;
                this.biomeIDs[index] = biome.field_76756_M;
                pos.func_181079_c(xPos + x, heighest, zPos + z);
                this.heightMap[index] = this.getHeightMapHeight(chunk, pos);
            }
        }
    }
    
    public static ChunkData createDataFromBuffer(final ByteBuffer chunkData, final ByteBuffer heightData) throws Exception {
        final ChunkData data = new ChunkData();
        data.chunkX = chunkData.getInt();
        data.chunkZ = chunkData.getInt();
        data.slimeChunk = (chunkData.get() == 1);
        for (int i = 0; i < 256; ++i) {
            data.blockColors[i] = chunkData.getInt();
        }
        for (int i = 0; i < 256; ++i) {
            data.biomeIDs[i] = chunkData.getShort();
        }
        final BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        for (int x = 0; x < 16; ++x) {
            for (int z = 0; z < 16; ++z) {
                pos.func_181079_c(data.chunkX * 16 + x, 64, data.chunkZ * 16 + z);
                final BiomeGenBase subBiome = data.getBiome(data.biomeIDs[z << 4 | x]);
                data.biomeFoliageColor[z << 4 | x] = subBiome.func_180625_c((BlockPos)pos);
                data.biomeGrassColor[z << 4 | x] = subBiome.func_180627_b((BlockPos)pos);
            }
        }
        if (heightData.get() <= 0) {
            data.heightMap = new int[0];
        }
        else {
            for (int j = 0; j < data.heightMap.length; ++j) {
                data.heightMap[j] = heightData.get() + 128;
            }
        }
        return data;
    }
    
    public int[] getData(final int view) {
        switch (view) {
            case 1: {
                return this.biomeFoliageColor;
            }
            case 2: {
                return this.biomeGrassColor;
            }
            default: {
                return this.blockColors;
            }
        }
    }
    
    @Override
    public int getBiome(final int x, final int z) {
        return this.biomeIDs[z * 16 + x];
    }
    
    private BiomeGenBase getBiome(final int id) {
        return BiomeGenBase.func_180276_a(id, BiomeGenBase.field_76772_c);
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
                    this.blockColors[index] = BaseChunkData.darker(this.originalBlockColors[index], 0.8f);
                }
                else if (this.isBrigther(xPos, zPos, map)) {
                    this.blockColors[index] = BaseChunkData.brighter(this.originalBlockColors[index], 0.8f);
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
        ByteBuffer buffer = ByteBuffer.allocate((int)IFileProvider.FileType.Chunk_Data.getOffset());
        buffer.put((byte)1);
        buffer.putInt(this.chunkX).putInt(this.chunkZ);
        buffer.put((byte)(this.slimeChunk ? 1 : 0));
        for (int i = 0; i < 256; ++i) {
            buffer.putInt(this.blockColors[i]);
        }
        for (int i = 0; i < 256; ++i) {
            buffer.putShort((short)this.biomeIDs[i]);
        }
        chunkData.seek(provider.getOrCreateIndex(this.chunkX, this.chunkZ, IFileProvider.FileType.Chunk_Data));
        chunkData.write(buffer.array());
        buffer = ByteBuffer.allocate((int)IFileProvider.FileType.HeightData.getOffset());
        buffer.put((byte)1);
        for (int i = 0; i < this.heightMap.length; ++i) {
            buffer.put((byte)(this.heightMap[i] - 128));
        }
        heightData.seek(provider.getOrCreateIndex(this.chunkX, this.chunkZ, IFileProvider.FileType.HeightData));
        heightData.write(buffer.array());
    }
}
