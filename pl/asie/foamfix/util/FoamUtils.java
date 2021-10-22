// 
// Decompiled by Procyon v0.5.36
// 

package pl.asie.foamfix.util;

import java.lang.reflect.Method;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.statemap.BlockStateMapper;
import java.lang.invoke.MethodHandles;
import java.util.Iterator;
import net.minecraftforge.client.model.IModel;
import java.lang.reflect.Field;
import net.minecraft.util.ResourceLocation;
import com.google.common.collect.Sets;
import pl.asie.foamfix.FoamFix;
import java.util.Map;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import java.lang.invoke.MethodHandle;

public final class FoamUtils
{
    public static final MethodHandle MLR_GET_TEXTURES;
    public static final MethodHandle ML_LOAD_BLOCK;
    
    private FoamUtils() {
    }
    
    public static void wipeModelLoaderRegistryCache() {
        final Field resourceCacheField = ReflectionHelper.findField((Class)ModelLoaderRegistry.class, new String[] { "cache" });
        try {
            final Map<ResourceLocation, IModel> oldResourceCache = (Map<ResourceLocation, IModel>)resourceCacheField.get(null);
            int itemsCleared = 0;
            FoamFix.logger.info("Clearing ModelLoaderRegistry cache (" + oldResourceCache.size() + " items)...");
            for (final ResourceLocation r : Sets.newHashSet((Iterable)oldResourceCache.keySet())) {
                oldResourceCache.remove(r);
                ++itemsCleared;
            }
            FoamFix.logger.info("Cleared " + itemsCleared + " objects.");
        }
        catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
    
    static {
        MethodHandle MLR_GET_TEXTURES_TMP = null;
        try {
            final Class k = Class.forName("net.minecraftforge.client.model.ModelLoaderRegistry");
            final Method m = k.getDeclaredMethod("getTextures", (Class[])new Class[0]);
            m.setAccessible(true);
            MLR_GET_TEXTURES_TMP = MethodHandles.lookup().unreflect(m);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        MLR_GET_TEXTURES = MLR_GET_TEXTURES_TMP;
        MethodHandle ML_LOAD_BLOCK_TMP = null;
        try {
            final Class i = Class.forName("net.minecraft.client.renderer.block.model.ModelBakery");
            final Method j = i.getDeclaredMethod("loadBlock", BlockStateMapper.class, Block.class, ResourceLocation.class);
            j.setAccessible(true);
            ML_LOAD_BLOCK_TMP = MethodHandles.lookup().unreflect(j);
        }
        catch (Exception e2) {
            e2.printStackTrace();
        }
        ML_LOAD_BLOCK = ML_LOAD_BLOCK_TMP;
    }
}
