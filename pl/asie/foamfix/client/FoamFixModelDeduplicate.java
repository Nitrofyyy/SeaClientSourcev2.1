// 
// Decompiled by Procyon v0.5.36
// 

package pl.asie.foamfix.client;

import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import java.util.Iterator;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import java.util.Collection;
import net.minecraft.block.Block;
import pl.asie.foamfix.FoamFix;
import pl.asie.foamfix.ProxyClient;
import net.minecraftforge.fml.common.ProgressManager;
import net.minecraft.util.RegistrySimple;
import pl.asie.foamfix.shared.FoamFixShared;
import net.minecraftforge.client.event.ModelBakeEvent;

public class FoamFixModelDeduplicate
{
    @SubscribeEvent(priority = EventPriority.LOW)
    public void onModelBake(final ModelBakeEvent event) {
        if (FoamFixShared.config.clDeduplicate) {
            final ProgressManager.ProgressBar bakeBar = ProgressManager.push("FoamFix: deduplicating", ((RegistrySimple)event.modelRegistry).func_148742_b().size());
            if (ProxyClient.deduplicator == null) {
                ProxyClient.deduplicator = new Deduplicator();
            }
            FoamFix.logger.info("Deduplicating models...");
            ProxyClient.deduplicator.maxRecursion = FoamFixShared.config.clDeduplicateRecursionLevel;
            ProxyClient.deduplicator.addObjects(Block.field_149771_c.func_148742_b());
            ProxyClient.deduplicator.addObjects(Item.field_150901_e.func_148742_b());
            for (final ModelResourceLocation loc : ((RegistrySimple)event.modelRegistry).func_148742_b()) {
                final IBakedModel model = (IBakedModel)event.modelRegistry.func_82594_a((Object)loc);
                final String modelName = loc.toString();
                bakeBar.step(String.format("[%s]", modelName));
                try {
                    ProxyClient.deduplicator.addObject(loc);
                    ProxyClient.deduplicator.deduplicateObject(model, 0);
                }
                catch (Exception ex) {}
            }
            ProgressManager.pop(bakeBar);
            FoamFix.logger.info("Deduplicated " + ProxyClient.deduplicator.successfuls + " objects.");
        }
        ProxyClient.deduplicator = null;
        FoamFix.updateRamSaved();
    }
}
