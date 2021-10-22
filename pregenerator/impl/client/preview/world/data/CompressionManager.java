// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.client.preview.world.data;

import java.awt.Color;
import java.util.Iterator;
import net.minecraft.util.BlockPos;
import pregenerator.impl.client.preview.texture.RenderShader;
import pregenerator.impl.client.preview.world.WorldSeed;
import java.util.HashMap;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.block.material.MapColor;
import java.util.Map;

public class CompressionManager
{
    public static CompressionManager INSTANCE;
    boolean isInit;
    Map<MapColor, Integer> colors;
    Map<BiomeGenBase, Integer> biomeColors;
    Map<Integer, BiomeGenBase> data;
    
    public CompressionManager() {
        this.isInit = false;
        this.colors = new HashMap<MapColor, Integer>();
        this.biomeColors = new HashMap<BiomeGenBase, Integer>();
        this.data = new HashMap<Integer, BiomeGenBase>();
    }
    
    public static boolean isInit() {
        CompressionManager.INSTANCE.init();
        return CompressionManager.INSTANCE.isInit;
    }
    
    public void init() {
        if (this.isInit) {
            return;
        }
        this.isInit = true;
        System.out.println("Loading Compression");
        if (!WorldSeed.canUseBetterCompression()) {
            System.out.println("Compression Not Possible");
            return;
        }
        int index = 0;
        final RenderShader shader = RenderShader.SHADER;
        shader.startShader();
        for (int i = 0; i < MapColor.field_76281_a.length; ++i) {
            final MapColor color = MapColor.field_76281_a[i];
            if (color != null) {
                this.colors.put(color, index);
                this.loadIntoShader("blockColors", index, color.func_151643_b(0));
                this.loadIntoShader("blockColors", index + 84, BaseChunkData.darker(color.func_151643_b(0), 0.8f));
                this.loadIntoShader("blockColors", index + 167, BaseChunkData.brighter(color.func_151643_b(0), 0.8f));
                ++index;
            }
        }
        System.out.println("Stored Block Colors");
        final BlockPos pos = new BlockPos(0, 64, 0);
        index = 0;
        for (final BiomeGenBase biome : BiomeGenBase.field_180278_o.values()) {
            this.biomeColors.put(biome, index);
            this.data.put(index, biome);
            ++index;
        }
        System.out.println("Stored Biome Colors");
        shader.stopShader();
        System.out.println("Compression Finished");
    }
    
    public int getIndexForBiome(final BiomeGenBase biome) {
        final Integer value = this.biomeColors.get(biome);
        return (value == null) ? 0 : value;
    }
    
    public int getIndexForBlock(final MapColor color, final BlockColorType type) {
        final Integer value = this.colors.get(color);
        return (value == null) ? 0 : (value + type.getOffset());
    }
    
    public BiomeGenBase getBiome(final int index) {
        final BiomeGenBase biome = this.data.get(index);
        return (biome == null) ? BiomeGenBase.field_76778_j : biome;
    }
    
    private void loadIntoShader(final String name, final int index, final int color) {
        final float[] data = new Color(index).getColorComponents(new float[3]);
        RenderShader.SHADER.loadVec3Array(name, index, data[0], data[1], data[2]);
    }
    
    static {
        CompressionManager.INSTANCE = new CompressionManager();
    }
    
    public enum BlockColorType
    {
        NORMAL(0), 
        DARKER(84), 
        BRIGTHER(167);
        
        int offset;
        
        private BlockColorType(final int offset) {
            this.offset = offset;
        }
        
        public int getOffset() {
            return this.offset;
        }
    }
}
