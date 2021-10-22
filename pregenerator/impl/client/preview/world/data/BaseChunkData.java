// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.client.preview.world.data;

import pregenerator.impl.client.preview.world.IHeightMap;
import java.awt.Color;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.IBlockAccess;
import net.minecraft.util.BlockPos;
import net.minecraft.world.chunk.Chunk;

public abstract class BaseChunkData implements IChunkData
{
    public int chunkX;
    public int chunkZ;
    public boolean slimeChunk;
    protected int[] heightMap;
    
    protected BaseChunkData() {
        this.heightMap = new int[256];
    }
    
    public BaseChunkData(final Chunk chunk) {
        this.heightMap = new int[256];
        this.chunkX = chunk.field_76635_g;
        this.chunkZ = chunk.field_76647_h;
        this.slimeChunk = (chunk.func_76617_a(987234911L).nextInt(10) == 0);
    }
    
    @Override
    public int getX() {
        return this.chunkX;
    }
    
    @Override
    public int getZ() {
        return this.chunkZ;
    }
    
    @Override
    public int getHeight(final int x, final int z) {
        if (this.heightMap.length <= 0) {
            return -1;
        }
        return this.heightMap[z * 16 + x];
    }
    
    @Override
    public boolean isSlimeChunk() {
        return this.slimeChunk;
    }
    
    protected void getFirstBlock(final Chunk chunk, final BlockPos.MutableBlockPos pos) {
        for (IBlockState state = chunk.func_177435_g((BlockPos)pos); state.func_177230_c().isAir((IBlockAccess)chunk.func_177412_p(), (BlockPos)pos) && pos.func_177956_o() > 0; state = chunk.func_177435_g((BlockPos)pos)) {
            pos.func_181079_c(pos.func_177958_n(), pos.func_177956_o() - 1, pos.func_177952_p());
        }
    }
    
    protected int getHeightMapHeight(final Chunk chunk, final BlockPos.MutableBlockPos pos) {
        for (IBlockState state = chunk.func_177435_g((BlockPos)pos); state.func_177230_c().isAir((IBlockAccess)chunk.func_177412_p(), (BlockPos)pos) || !state.func_177230_c().func_149688_o().func_76230_c(); state = chunk.func_177435_g((BlockPos)pos)) {
            pos.func_181079_c(pos.func_177958_n(), pos.func_177956_o() - 1, pos.func_177952_p());
            if (pos.func_177956_o() < 0) {
                return 0;
            }
        }
        return pos.func_177956_o();
    }
    
    public static int darker(final int color, final float factor) {
        return darker(new Color(color), factor);
    }
    
    public static int darker(final Color color, final float factor) {
        return new Color(Math.max((int)(color.getRed() * factor), 0), Math.max((int)(color.getGreen() * factor), 0), Math.max((int)(color.getBlue() * factor), 0), color.getAlpha()).getRGB();
    }
    
    public static int brighter(final int color, final float factor) {
        return brighter(new Color(color), factor);
    }
    
    public static int brighter(final Color color, final float factor) {
        int r = color.getRed();
        int g = color.getGreen();
        int b = color.getBlue();
        final int alpha = color.getAlpha();
        final int i = (int)(1.0 / (1.0 - factor));
        if (r == 0 && g == 0 && b == 0) {
            return new Color(i, i, i, alpha).getRGB();
        }
        if (r > 0 && r < i) {
            r = i;
        }
        if (g > 0 && g < i) {
            g = i;
        }
        if (b > 0 && b < i) {
            b = i;
        }
        return new Color(Math.min((int)(r / factor), 255), Math.min((int)(g / factor), 255), Math.min((int)(b / factor), 255), alpha).getRGB();
    }
    
    protected boolean isDarker(final int x, final int z, final IHeightMap map) {
        final int base = map.getHeight(x, z);
        return this.isEnoughSmaller(base, map.getHeight(x, z + 1, base)) || this.isEnoughSmaller(base, map.getHeight(x + 1, z, base));
    }
    
    protected boolean isBrigther(final int x, final int z, final IHeightMap map) {
        final int base = map.getHeight(x, z);
        return this.isEnoughBigger(base, map.getHeight(x + 1, z, base)) || this.isEnoughBigger(base, map.getHeight(x, z + 1, base));
    }
    
    protected boolean isEnoughBigger(final int base, final int other) {
        return base < other && other - base <= 3;
    }
    
    protected boolean isEnoughSmaller(final int base, final int other) {
        return base > other && base - other <= 3;
    }
}
