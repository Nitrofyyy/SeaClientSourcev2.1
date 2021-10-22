// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.storage;

import java.util.Collection;
import pregenerator.ChunkPregenerator;
import java.util.LinkedHashSet;
import net.minecraft.command.ICommandSender;
import java.util.Set;

public class GlobalListeners
{
    public static GlobalListeners INSTANCE;
    Set<ICommandSender> listeners;
    
    public GlobalListeners() {
        this.listeners = new LinkedHashSet<ICommandSender>();
    }
    
    public void sendChatMessage(final String text) {
        ChunkPregenerator.pregenBase.sendChatMessage(this.listeners, text);
    }
    
    public boolean addListener(final ICommandSender sender) {
        return this.listeners.add(sender);
    }
    
    public boolean removeListener(final ICommandSender sender) {
        return this.listeners.remove(sender);
    }
    
    public boolean containsListener(final ICommandSender sender) {
        return this.listeners.contains(sender);
    }
    
    public void clearListeners() {
        this.listeners.clear();
    }
    
    static {
        GlobalListeners.INSTANCE = new GlobalListeners();
    }
}
