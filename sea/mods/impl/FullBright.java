// 
// Decompiled by Procyon v0.5.36
// 

package sea.mods.impl;

import sea.mods.Module;

public class FullBright extends Module
{
    public FullBright() {
        super("FullBright", "kjasd");
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
        this.mc.gameSettings.gammaSetting = 10.0f;
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
        this.mc.gameSettings.gammaSetting = 1.0f;
    }
}
