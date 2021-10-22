// 
// Decompiled by Procyon v0.5.36
// 

package pl.asie.foamfix.client;

import java.util.Collections;
import javax.vecmath.Matrix4f;
import org.apache.commons.lang3.tuple.Pair;
import java.lang.ref.SoftReference;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import java.lang.invoke.MethodHandles;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import com.google.common.collect.UnmodifiableIterator;
import net.minecraftforge.client.model.TRSRTransformation;
import java.util.List;
import net.minecraftforge.client.model.IPerspectiveAwareModel;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.IModelPart;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import net.minecraftforge.client.model.IFlexibleBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import com.google.common.base.Function;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraftforge.client.model.IModelState;
import java.util.Collection;
import net.minecraftforge.client.model.IModel;
import com.google.common.collect.ImmutableMap;
import java.lang.invoke.MethodHandle;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ItemLayerModel;
import net.minecraftforge.client.model.IRetexturableModel;

public class FoamyItemLayerModel implements IRetexturableModel<ItemLayerModel>
{
    private static final ResourceLocation MISSINGNO;
    private static final MethodHandle BUILD_QUAD;
    private static final MethodHandle TEXTURES_GET;
    private final ItemLayerModel parent;
    
    public FoamyItemLayerModel(final ItemLayerModel parent) {
        this.parent = parent;
    }
    
    public IModel retexture(final ImmutableMap<String, String> textures) {
        return (IModel)new FoamyItemLayerModel((ItemLayerModel)this.parent.retexture((ImmutableMap)textures));
    }
    
    public Collection<ResourceLocation> getDependencies() {
        return (Collection<ResourceLocation>)this.parent.getDependencies();
    }
    
    public Collection<ResourceLocation> getTextures() {
        return (Collection<ResourceLocation>)this.parent.getTextures();
    }
    
    public IFlexibleBakedModel bake(final IModelState state, final VertexFormat format, final Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
        return bake(this.parent, state, format, bakedTextureGetter);
    }
    
    public static IFlexibleBakedModel bake(final ItemLayerModel parent, final IModelState state, final VertexFormat format, final Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
        final ImmutableList.Builder<BakedQuad> builder = (ImmutableList.Builder<BakedQuad>)ImmutableList.builder();
        final Optional<TRSRTransformation> transform = state.apply((Optional<? extends IModelPart>)Optional.absent());
        List<ResourceLocation> textures;
        try {
            textures = (List<ResourceLocation>)FoamyItemLayerModel.TEXTURES_GET.invoke(parent);
        }
        catch (Throwable t2) {
            return ItemLayerModel.instance.bake(state, format, (Function)bakedTextureGetter);
        }
        final ImmutableList.Builder<TextureAtlasSprite> textureAtlas = (ImmutableList.Builder<TextureAtlasSprite>)new ImmutableList.Builder();
        if (FoamyItemLayerModel.BUILD_QUAD != null) {
            for (int i = 0; i < textures.size(); ++i) {
                final TextureAtlasSprite sprite = (TextureAtlasSprite)bakedTextureGetter.apply((Object)textures.get(i));
                textureAtlas.add((Object)sprite);
                try {
                    builder.add((Object)FoamyItemLayerModel.BUILD_QUAD.invokeExact(format, (Optional)transform, EnumFacing.SOUTH, i, 0.0f, 0.0f, 0.53125f, sprite.func_94209_e(), sprite.func_94210_h(), 1.0f, 0.0f, 0.53125f, sprite.func_94212_f(), sprite.func_94210_h(), 1.0f, 1.0f, 0.53125f, sprite.func_94212_f(), sprite.func_94206_g(), 0.0f, 1.0f, 0.53125f, sprite.func_94209_e(), sprite.func_94206_g()));
                }
                catch (Throwable t) {
                    throw new RuntimeException(t);
                }
            }
        }
        else {
            for (int i = 0; i < textures.size(); ++i) {
                final TextureAtlasSprite sprite = (TextureAtlasSprite)bakedTextureGetter.apply((Object)textures.get(i));
                for (final BakedQuad quad : ItemLayerModel.instance.getQuadsForSprite(i, sprite, format, (Optional)transform)) {
                    if (quad.func_178210_d() == EnumFacing.SOUTH) {
                        builder.add((Object)quad);
                    }
                }
            }
        }
        final TextureAtlasSprite particle = (TextureAtlasSprite)bakedTextureGetter.apply((Object)(textures.isEmpty() ? FoamyItemLayerModel.MISSINGNO : textures.get(0)));
        final ImmutableMap<ItemCameraTransforms.TransformType, TRSRTransformation> map = (ImmutableMap<ItemCameraTransforms.TransformType, TRSRTransformation>)IPerspectiveAwareModel.MapWrapper.getTransforms(state);
        return new DynamicItemModel((ImmutableList<BakedQuad>)builder.build(), particle, map, (List<TextureAtlasSprite>)textureAtlas.build(), format, transform).otherModel;
    }
    
    public IModelState getDefaultState() {
        return (IModelState)TRSRTransformation.identity();
    }
    
    static {
        MISSINGNO = new ResourceLocation("missingno");
        MethodHandle handle = null;
        try {
            handle = MethodHandles.lookup().unreflect(ReflectionHelper.findMethod((Class)ItemLayerModel.class, (Object)null, new String[] { "buildQuad" }, new Class[] { VertexFormat.class, Optional.class, EnumFacing.class, Integer.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE, Float.TYPE }));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        BUILD_QUAD = handle;
        handle = null;
        try {
            handle = MethodHandles.lookup().unreflectGetter(ReflectionHelper.findField((Class)ItemLayerModel.class, new String[] { "textures" }));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        TEXTURES_GET = handle;
    }
    
    public static class Dynamic3DItemModel implements IPerspectiveAwareModel, IFlexibleBakedModel
    {
        private final DynamicItemModel parent;
        private SoftReference<List<BakedQuad>> quadsSoft;
        
        public Dynamic3DItemModel(final DynamicItemModel parent) {
            this.quadsSoft = null;
            this.parent = parent;
        }
        
        public Pair<? extends IFlexibleBakedModel, Matrix4f> handlePerspective(final ItemCameraTransforms.TransformType type) {
            final Pair<? extends IFlexibleBakedModel, Matrix4f> pair = (Pair<? extends IFlexibleBakedModel, Matrix4f>)IPerspectiveAwareModel.MapWrapper.handlePerspective((IFlexibleBakedModel)this, this.parent.transforms, type);
            if (type == ItemCameraTransforms.TransformType.GUI && pair.getRight() == null) {
                return (Pair<? extends IFlexibleBakedModel, Matrix4f>)Pair.of((Object)this.parent, (Object)null);
            }
            return pair;
        }
        
        public List<BakedQuad> func_177551_a(final EnumFacing facing) {
            return (List<BakedQuad>)Collections.EMPTY_LIST;
        }
        
        public List<BakedQuad> func_177550_a() {
            if (this.quadsSoft == null || this.quadsSoft.get() == null) {
                final ImmutableList.Builder<BakedQuad> builder = (ImmutableList.Builder<BakedQuad>)new ImmutableList.Builder();
                for (int i = 0; i < this.parent.textures.size(); ++i) {
                    final TextureAtlasSprite sprite = this.parent.textures.get(i);
                    builder.addAll((Iterable)ItemLayerModel.instance.getQuadsForSprite(i, sprite, this.parent.format, this.parent.transform));
                }
                this.quadsSoft = new SoftReference<List<BakedQuad>>((List<BakedQuad>)builder.build());
            }
            return this.quadsSoft.get();
        }
        
        public boolean func_177555_b() {
            return true;
        }
        
        public boolean func_177556_c() {
            return false;
        }
        
        public boolean func_177553_d() {
            return false;
        }
        
        public TextureAtlasSprite func_177554_e() {
            return this.parent.particle;
        }
        
        public ItemCameraTransforms func_177552_f() {
            return ItemCameraTransforms.field_178357_a;
        }
        
        public VertexFormat getFormat() {
            return this.parent.getFormat();
        }
    }
    
    public static class DynamicItemModel implements IPerspectiveAwareModel, IFlexibleBakedModel
    {
        private final List<TextureAtlasSprite> textures;
        private final TextureAtlasSprite particle;
        private final ImmutableList<BakedQuad> fastQuads;
        private final ImmutableMap<ItemCameraTransforms.TransformType, TRSRTransformation> transforms;
        private final VertexFormat format;
        private final Optional<TRSRTransformation> transform;
        private final IFlexibleBakedModel otherModel;
        
        public DynamicItemModel(final ImmutableList<BakedQuad> quads, final TextureAtlasSprite particle, final ImmutableMap<ItemCameraTransforms.TransformType, TRSRTransformation> transforms, final List<TextureAtlasSprite> textures, final VertexFormat format, final Optional<TRSRTransformation> transform) {
            this.fastQuads = quads;
            this.particle = particle;
            this.transforms = transforms;
            this.textures = textures;
            this.format = format;
            this.transform = transform;
            this.otherModel = (IFlexibleBakedModel)new Dynamic3DItemModel(this);
        }
        
        public List<BakedQuad> func_177551_a(final EnumFacing facing) {
            return Collections.emptyList();
        }
        
        public List<BakedQuad> func_177550_a() {
            return (List<BakedQuad>)this.fastQuads;
        }
        
        public boolean func_177555_b() {
            return true;
        }
        
        public boolean func_177556_c() {
            return false;
        }
        
        public boolean func_177553_d() {
            return false;
        }
        
        public TextureAtlasSprite func_177554_e() {
            return this.particle;
        }
        
        public ItemCameraTransforms func_177552_f() {
            return ItemCameraTransforms.field_178357_a;
        }
        
        public Pair<? extends IFlexibleBakedModel, Matrix4f> handlePerspective(final ItemCameraTransforms.TransformType type) {
            final Pair<? extends IFlexibleBakedModel, Matrix4f> pair = (Pair<? extends IFlexibleBakedModel, Matrix4f>)IPerspectiveAwareModel.MapWrapper.handlePerspective((IFlexibleBakedModel)this, (ImmutableMap)this.transforms, type);
            if (type != ItemCameraTransforms.TransformType.GUI) {
                return (Pair<? extends IFlexibleBakedModel, Matrix4f>)Pair.of((Object)this.otherModel, pair.getRight());
            }
            return pair;
        }
        
        public VertexFormat getFormat() {
            return this.format;
        }
    }
}
