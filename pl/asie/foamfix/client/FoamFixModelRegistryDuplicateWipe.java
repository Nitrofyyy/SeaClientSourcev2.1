// 
// Decompiled by Procyon v0.5.36
// 

package pl.asie.foamfix.client;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import gnu.trove.map.hash.TIntObjectHashMap;
import net.minecraft.item.Item;
import net.minecraft.block.state.IBlockState;
import java.util.Iterator;
import net.minecraft.client.resources.model.IBakedModel;
import java.lang.reflect.Field;
import net.minecraft.client.renderer.ItemModelMesher;
import java.util.IdentityHashMap;
import net.minecraftforge.client.ItemModelMesherForge;
import java.util.Map;
import net.minecraft.client.renderer.BlockModelShapes;
import pl.asie.foamfix.ProxyClient;
import net.minecraft.client.resources.model.ModelResourceLocation;
import pl.asie.foamfix.FoamFix;
import net.minecraft.util.RegistrySimple;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.TextureStitchEvent;

public class FoamFixModelRegistryDuplicateWipe
{
    @SubscribeEvent
    public void onTextureStitchPost(final TextureStitchEvent.Post event) {
        final ItemModelMesher imm = Minecraft.func_71410_x().func_175599_af().func_175037_a();
        final BlockModelShapes bms = Minecraft.func_71410_x().func_175602_ab().func_175023_a();
        final ModelManager mgr = bms.func_178126_b();
        Field f = ReflectionHelper.findField((Class)ModelManager.class, new String[] { "modelRegistry", "field_174958_a" });
        try {
            final RegistrySimple<ModelResourceLocation, IBakedModel> registry = (RegistrySimple<ModelResourceLocation, IBakedModel>)f.get(mgr);
            FoamFix.logger.info("Clearing unnecessary model registry of size " + registry.func_148742_b().size() + ".");
            for (final ModelResourceLocation l : registry.func_148742_b()) {
                registry.func_82595_a((Object)l, (Object)ProxyClient.DUMMY_MODEL);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        f = ReflectionHelper.findField((Class)BlockModelShapes.class, new String[] { "bakedModelStore", "field_178129_a" });
        try {
            final Map<IBlockState, IBakedModel> modelStore = (Map<IBlockState, IBakedModel>)f.get(bms);
            FoamFix.logger.info("Clearing unnecessary model store of size " + modelStore.size() + ".");
            modelStore.clear();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        if (imm instanceof ItemModelMesherForge) {
            f = ReflectionHelper.findField((Class)ItemModelMesherForge.class, new String[] { "models" });
            try {
                final IdentityHashMap<Item, TIntObjectHashMap<IBakedModel>> modelStore2 = (IdentityHashMap<Item, TIntObjectHashMap<IBakedModel>>)f.get(imm);
                FoamFix.logger.info("Clearing unnecessary item shapes cache of size " + modelStore2.size() + ".");
                modelStore2.clear();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
