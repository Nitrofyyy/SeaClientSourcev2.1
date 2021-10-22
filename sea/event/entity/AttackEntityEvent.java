// 
// Decompiled by Procyon v0.5.36
// 

package sea.event.entity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.Entity;
import sea.event.Cancelable;

@Cancelable
public class AttackEntityEvent extends PlayerEvent
{
    public final Entity target;
    
    public AttackEntityEvent(final EntityPlayer player, final Entity target) {
        super(player);
        this.target = target;
    }
    
    public Entity getTarget() {
        return this.target;
    }
}
