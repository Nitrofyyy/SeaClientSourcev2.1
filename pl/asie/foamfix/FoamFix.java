// 
// Decompiled by Procyon v0.5.36
// 

package pl.asie.foamfix;

import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import pl.asie.foamfix.shared.FoamFixShared;
import org.apache.logging.log4j.LogManager;
import pl.asie.foamfix.api.FoamFixAPI;
import pl.asie.foamfix.common.FoamFixHelper;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import java.text.DecimalFormat;
import org.apache.logging.log4j.Logger;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.Mod;

@Mod(modid = "foamfix", name = "FoamFix", version = "@VERSION@", acceptableRemoteVersions = "*", acceptedMinecraftVersions = "[1.8.8,1.9)")
public class FoamFix
{
    @SidedProxy(clientSide = "pl.asie.foamfix.ProxyClient", serverSide = "pl.asie.foamfix.ProxyCommon", modId = "foamfix")
    public static ProxyCommon proxy;
    public static Logger logger;
    public static int stage;
    private static final DecimalFormat RAM_SAVED_DF;
    
    @Mod.EventHandler
    public void preInit(final FMLPreInitializationEvent event) {
        FoamFixAPI.HELPER = new FoamFixHelper();
        FoamFix.logger = LogManager.getLogger("foamfix");
        FoamFix.stage = 0;
        FoamFixShared.config.init(event.getSuggestedConfigurationFile(), false);
        FoamFix.proxy.preInit();
    }
    
    @Mod.EventHandler
    public void init(final FMLInitializationEvent event) {
        FoamFix.stage = 1;
        MinecraftForge.EVENT_BUS.register((Object)FoamFix.proxy);
        FoamFix.proxy.init();
    }
    
    @Mod.EventHandler
    public void postInit(final FMLPostInitializationEvent event) {
        FoamFix.stage = 2;
        FoamFix.proxy.postInit();
    }
    
    @Mod.EventHandler
    public void serverStopping(final FMLServerStoppingEvent event) {
    }
    
    public static void updateRamSaved() {
    }
    
    static {
        RAM_SAVED_DF = new DecimalFormat("0.#");
    }
}
