// 
// Decompiled by Procyon v0.5.36
// 

package sea.event;

public class EventCancelable extends Event
{
    private boolean cancelled;
    
    public EventCancelable() {
        this.cancelled = false;
    }
    
    public boolean isCancelled() {
        return this.cancelled;
    }
    
    @Override
    public void setCancelled(final boolean cancelled) {
        this.cancelled = cancelled;
    }
}
