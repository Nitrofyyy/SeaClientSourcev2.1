// 
// Decompiled by Procyon v0.5.36
// 

package sea.event.entity;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import sea.event.Cancelable;

@Cancelable
public class EntityJoinWorldEvent extends EntityEvent
{
    public final World world;
    
    public EntityJoinWorldEvent(final Entity entity, final World world) {
        super(entity);
        this.world = world;
    }
}
