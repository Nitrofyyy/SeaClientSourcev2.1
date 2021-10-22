// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.processor.generator;

public enum GenerationType
{
    TERRAIN_ONLY, 
    FAST_CHECK_GEN, 
    NORMAL_GEN, 
    POST_GEN, 
    BLOCK_POST, 
    RETROGEN;
    
    public boolean isPostGen() {
        return this != GenerationType.TERRAIN_ONLY && this != GenerationType.BLOCK_POST;
    }
    
    public boolean requiresChunkLoading() {
        return this == GenerationType.POST_GEN || this == GenerationType.RETROGEN;
    }
    
    public boolean requiresChunkCreation() {
        return this != GenerationType.POST_GEN && this != GenerationType.RETROGEN;
    }
}
