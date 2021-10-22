// 
// Decompiled by Procyon v0.5.36
// 

package sea.mods;

import sea.event.EventManager;
import sea.SeaClient;
import net.minecraft.client.Minecraft;

public class Module
{
    public String name;
    public String description;
    public Minecraft mc;
    public static boolean enabled;
    
    public Module(final String name, final String description) {
        this.mc = Minecraft.getMinecraft();
        this.name = name;
        this.description = description;
        Module.enabled = true;
    }
    
    public void onEnable() {
        final EventManager eventManager = SeaClient.eventManager;
        EventManager.register(this);
    }
    
    public void onDisable() {
        final EventManager eventManager = SeaClient.eventManager;
        EventManager.unregister(this);
    }
    
    public void toggle() {
        this.setEnabled(!Module.enabled);
    }
    
    public void setEnabled(final boolean enabled) {
        Module.enabled = enabled;
        if (enabled) {
            this.onEnable();
        }
        else {
            this.onDisable();
        }
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public static boolean isEnabled() {
        return Module.enabled;
    }
}
