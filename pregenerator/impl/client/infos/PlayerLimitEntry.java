// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.client.infos;

import pregenerator.ConfigManager;

public class PlayerLimitEntry extends BarEntry
{
    public PlayerLimitEntry() {
        this.register();
    }
    
    @Override
    public String getName() {
        return "PlayerLimit Info";
    }
    
    @Override
    public String getText(final int current, final int max) {
        return "PlayerLimit: " + current + "/" + max;
    }
    
    @Override
    public boolean shouldRender() {
        return this.max >= 0;
    }
    
    @Override
    public int getMaxServer() {
        return ConfigManager.playerDeactivation;
    }
    
    @Override
    public int getCurrentServer() {
        return this.getProcessor().getServer().func_71233_x();
    }
}
