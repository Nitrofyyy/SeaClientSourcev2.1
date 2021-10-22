// 
// Decompiled by Procyon v0.5.36
// 

package sea.mods.huds.impl;

import net.minecraft.world.WorldProvider;
import sea.mods.huds.HudMod;

public class FullBright extends HudMod
{
    public static WorldProvider world;
    protected final float[] lightBrightnessTable;
    
    public FullBright() {
        super("FullBright", 0, 0, "Will bright ur wolrd");
        this.lightBrightnessTable = new float[16];
    }
    
    @Override
    public int getHieght() {
        return 100;
    }
    
    @Override
    public int getWidth() {
        return 100;
    }
    
    @Override
    public void draw() {
        this.mc.gameSettings.gammaSetting = 100.0f;
        super.draw();
    }
}
