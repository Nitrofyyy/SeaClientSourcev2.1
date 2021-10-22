// 
// Decompiled by Procyon v0.5.36
// 

package sea.mods.impl;

import sea.SeaClient;
import sea.mods.Module;

public class Animtaions extends Module
{
    public Animtaions() {
        super("", "");
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
        SeaClient.getINSTANCE().animations = true;
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
        SeaClient.getINSTANCE().animations = false;
    }
}
