// 
// Decompiled by Procyon v0.5.36
// 

package pl.asie.foamfix;

import java.util.Iterator;
import java.lang.reflect.Method;
import com.google.common.collect.BiMap;
import java.util.BitSet;
import net.minecraftforge.fml.common.registry.FMLControlledNamespacedRegistry;
import java.lang.reflect.Field;
import com.google.common.cache.CacheBuilder;
import java.util.Map;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraft.launchwrapper.LaunchClassLoader;
import pl.asie.foamfix.shared.FoamFixShared;

public class ProxyCommon
{
    private void optimizeLaunchWrapper() {
        if (FoamFixShared.config.lwWeakenResourceCache) {
            FoamFix.logger.info("Weakening LaunchWrapper resource cache...");
            try {
                final LaunchClassLoader loader = (LaunchClassLoader)this.getClass().getClassLoader();
                final Field resourceCacheField = ReflectionHelper.findField((Class)LaunchClassLoader.class, new String[] { "resourceCache" });
                final Map oldResourceCache = (Map)resourceCacheField.get(loader);
                final Map newResourceCache = CacheBuilder.newBuilder().weakValues().build().asMap();
                newResourceCache.putAll(oldResourceCache);
                resourceCacheField.set(loader, newResourceCache);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    private void optimizeForgeRegistries() {
        try {
            int optimizedRegs = 0;
            int optimizedSavings = 0;
            final Class persistentRegistryClass = Class.forName("net.minecraftforge.fml.common.registry.PersistentRegistryManager$PersistentRegistry");
            final Field biMapField = persistentRegistryClass.getDeclaredField("registries");
            final Field availMapField = FMLControlledNamespacedRegistry.class.getDeclaredField("availabilityMap");
            final Field sizeStickyField = BitSet.class.getDeclaredField("sizeIsSticky");
            final Method trimToSizeMethod = BitSet.class.getDeclaredMethod("trimToSize", (Class<?>[])new Class[0]);
            biMapField.setAccessible(true);
            availMapField.setAccessible(true);
            sizeStickyField.setAccessible(true);
            trimToSizeMethod.setAccessible(true);
            for (final Object registryHolder : persistentRegistryClass.getEnumConstants()) {
                final BiMap biMap = (BiMap)biMapField.get(registryHolder);
                for (final FMLControlledNamespacedRegistry registry : biMap.values()) {
                    final BitSet availMap = (BitSet)availMapField.get(registry);
                    final int size = availMap.size();
                    if (size > 65536) {
                        sizeStickyField.set(availMap, false);
                        trimToSizeMethod.invoke(availMap, new Object[0]);
                        ++optimizedRegs;
                        optimizedSavings += size - availMap.size() >> 3;
                    }
                }
            }
            FoamFixShared.ramSaved += optimizedSavings;
            FoamFix.logger.info("Optimized " + optimizedRegs + " FML registries, saving " + optimizedSavings + " bytes.");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void preInit() {
    }
    
    public void init() {
    }
    
    public void postInit() {
        this.optimizeLaunchWrapper();
        if (FoamFixShared.config.geDynamicRegistrySizeScaling) {
            this.optimizeForgeRegistries();
        }
        FoamFix.updateRamSaved();
    }
}
