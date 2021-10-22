// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.base.impl.misc;

import java.util.HashMap;
import net.minecraft.world.WorldProvider;
import net.minecraftforge.common.DimensionManager;
import java.util.Map;

public class DimensionLister
{
    static Map<Integer, String> nameMap;
    
    public static String getDimensionName(final int id) {
        try {
            if (!DimensionManager.isDimensionRegistered(id)) {
                return "Unknown";
            }
            final WorldProvider prov = DimensionManager.createProviderFor(id);
            final String name = prov.func_80007_l();
            DimensionLister.nameMap.put(id, name);
        }
        catch (Exception e) {
            DimensionLister.nameMap.put(id, "Unknown");
        }
        return DimensionLister.nameMap.get(id);
    }
    
    static {
        DimensionLister.nameMap = new HashMap<Integer, String>();
    }
}
