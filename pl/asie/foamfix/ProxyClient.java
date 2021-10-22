// 
// Decompiled by Procyon v0.5.36
// 

package pl.asie.foamfix;

import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import java.util.Collections;
import net.minecraft.client.renderer.block.model.BakedQuad;
import java.util.List;
import net.minecraft.util.EnumFacing;
import pl.asie.foamfix.client.FoamFixModelRegistryDuplicateWipe;
import pl.asie.foamfix.client.FoamFixModelDeduplicate;
import net.minecraftforge.common.MinecraftForge;
import pl.asie.foamfix.shared.FoamFixShared;
import net.minecraft.client.resources.model.IBakedModel;
import pl.asie.foamfix.client.Deduplicator;

public class ProxyClient extends ProxyCommon
{
    public static Deduplicator deduplicator;
    public static final IBakedModel DUMMY_MODEL;
    
    @Override
    public void preInit() {
        super.preInit();
        if (!FoamFixShared.config.clDeduplicate) {
            ProxyClient.deduplicator = null;
        }
    }
    
    @Override
    public void init() {
        super.init();
        MinecraftForge.EVENT_BUS.register((Object)new FoamFixModelDeduplicate());
        if (FoamFixShared.config.clCleanRedundantModelRegistry) {
            MinecraftForge.EVENT_BUS.register((Object)new FoamFixModelRegistryDuplicateWipe());
        }
    }
    
    @Override
    public void postInit() {
        super.postInit();
        if (ProxyClient.deduplicator != null) {
            ProxyClient.deduplicator.successfuls = 0;
        }
    }
    
    static {
        ProxyClient.deduplicator = new Deduplicator();
        DUMMY_MODEL = new IBakedModel() {
            public List<BakedQuad> func_177551_a(final EnumFacing facing) {
                return Collections.emptyList();
            }
            
            public List<BakedQuad> func_177550_a() {
                return Collections.emptyList();
            }
            
            public boolean func_177555_b() {
                return false;
            }
            
            public boolean func_177556_c() {
                return false;
            }
            
            public boolean func_177553_d() {
                return false;
            }
            
            public TextureAtlasSprite func_177554_e() {
                return Minecraft.func_71410_x().func_147117_R().getTextureExtry(TextureMap.field_174945_f.toString());
            }
            
            public ItemCameraTransforms func_177552_f() {
                return ItemCameraTransforms.field_178357_a;
            }
        };
    }
}
