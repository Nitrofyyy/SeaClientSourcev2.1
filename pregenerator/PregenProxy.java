// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator;

import pregenerator.impl.client.trackerInfo.TrackerEntry;
import pregenerator.impl.client.infos.InfoEntry;
import net.minecraft.entity.player.EntityPlayer;

public class PregenProxy
{
    public EntityPlayer getClientPlayer() {
        return null;
    }
    
    public void init() {
        InfoEntry.init();
        TrackerEntry.init();
    }
    
    public boolean shouldLog() {
        return true;
    }
}
