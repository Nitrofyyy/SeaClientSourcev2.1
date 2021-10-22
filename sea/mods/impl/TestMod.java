// 
// Decompiled by Procyon v0.5.36
// 

package sea.mods.impl;

import net.minecraft.client.Minecraft;
import sea.mods.Module;

public class TestMod extends Module
{
    public TestMod() {
        super("hi", "");
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
        Minecraft.getMinecraft().gameSettings.gammaSetting = 100.0f;
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
        Minecraft.getMinecraft().gameSettings.gammaSetting = 1.0f;
    }
}
