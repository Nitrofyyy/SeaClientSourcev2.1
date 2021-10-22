// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.client;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import pregenerator.PregenProxy;

public class PregenClientProxy extends PregenProxy
{
    @Override
    public EntityPlayer getClientPlayer() {
        return Minecraft.func_71410_x().field_71439_g;
    }
    
    @Override
    public void init() {
        super.init();
        ClientHandler.INSTANCE.init();
    }
    
    @Override
    public boolean shouldLog() {
        return !ClientHandler.INSTANCE.info.shouldShow;
    }
}
