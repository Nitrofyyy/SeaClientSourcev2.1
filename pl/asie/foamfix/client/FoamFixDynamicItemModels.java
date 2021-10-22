// 
// Decompiled by Procyon v0.5.36
// 

package pl.asie.foamfix.client;

import net.minecraft.client.resources.IResourceManager;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.IFlexibleBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import com.google.common.base.Function;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraftforge.client.model.IModelState;
import java.util.Set;
import java.lang.invoke.MethodHandle;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.ItemLayerModel;
import pl.asie.foamfix.util.MethodHandleHelper;
import net.minecraftforge.client.model.ModelLoaderRegistry;

public class FoamFixDynamicItemModels
{
    public static void register() {
        final MethodHandle LOADERS = MethodHandleHelper.findFieldGetter(ModelLoaderRegistry.class, "loaders");
        try {
            final Set<ICustomModelLoader> loaders = (Set<ICustomModelLoader>)LOADERS.invoke();
            loaders.remove(ItemLayerModel.Loader.instance);
            loaders.add((ICustomModelLoader)Loader.INSTANCE);
        }
        catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }
    
    public IFlexibleBakedModel bake(final IModelState state, final VertexFormat format, final Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
        return FoamyItemLayerModel.bake((ItemLayerModel)this, state, format, bakedTextureGetter);
    }
    
    public enum Loader implements ICustomModelLoader
    {
        INSTANCE;
        
        private static final IModel model;
        
        public void func_110549_a(final IResourceManager resourceManager) {
            ItemLayerModel.Loader.instance.func_110549_a(resourceManager);
        }
        
        public boolean accepts(final ResourceLocation modelLocation) {
            return ItemLayerModel.Loader.instance.accepts(modelLocation);
        }
        
        public IModel loadModel(final ResourceLocation modelLocation) {
            return Loader.model;
        }
        
        static {
            model = (IModel)new FoamyItemLayerModel(ItemLayerModel.instance);
        }
    }
}
