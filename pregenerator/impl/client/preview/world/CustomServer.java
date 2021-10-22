// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.client.preview.world;

import net.minecraft.world.storage.WorldInfo;
import net.minecraft.world.storage.SaveHandler;
import net.minecraft.world.WorldType;
import net.minecraft.client.Minecraft;
import net.minecraft.world.WorldSettings;
import net.minecraft.server.integrated.IntegratedServer;

public class CustomServer extends IntegratedServer
{
    boolean forcedShutdown;
    WorldSettings settings;
    
    public CustomServer(final Minecraft mcIn, final String folderName, final String worldName, final WorldSettings settings) {
        super(mcIn, folderName, worldName, settings);
        this.forcedShutdown = false;
        this.settings = settings;
    }
    
    public void func_71222_d() {
    }
    
    public void func_71247_a(final String saveName, final String worldNameIn, final long seed, final WorldType type, final String generatorOptions) {
        final SaveHandler handler = (SaveHandler)this.func_71254_M().func_75804_a(saveName, true);
        if (handler.func_75757_d() == null) {
            handler.func_75761_a(new WorldInfo(this.settings, worldNameIn));
        }
        super.func_71247_a(saveName, worldNameIn, seed, type, generatorOptions);
    }
    
    public void func_71263_m() {
        if (this.func_71200_ad()) {
            super.func_71263_m();
        }
        else {
            this.forcedShutdown = true;
        }
    }
    
    public boolean func_71241_aa() {
        return super.func_71241_aa() || this.forcedShutdown;
    }
}
