// 
// Decompiled by Procyon v0.5.36
// 

package io.prplz.memoryfix;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.fml.common.Mod;

@Mod(modid = "memoryfix", useMetadata = true, acceptedMinecraftVersions = "[1.8.9]")
public class MemoryFix
{
    private int messageDelay;
    private IChatComponent updateMessage;
    
    public MemoryFix() {
        this.messageDelay = 0;
    }
    
    @Mod.EventHandler
    public void init(final FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register((Object)this);
        final String updateUrl = System.getProperty("memoryfix.updateurl", "https://mods.purple.services/update/check/MemoryFix/0.3");
        final UpdateChecker updater = new UpdateChecker(updateUrl, res -> this.updateMessage = res.getUpdateMessage());
        updater.start();
    }
    
    @SubscribeEvent
    public void onTick(final TickEvent.ClientTickEvent event) {
        if (this.updateMessage != null && Minecraft.func_71410_x().field_71439_g != null && ++this.messageDelay == 80) {
            Minecraft.func_71410_x().field_71439_g.func_145747_a(this.updateMessage);
            this.updateMessage = null;
        }
    }
}
