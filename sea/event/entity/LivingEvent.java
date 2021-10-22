// 
// Decompiled by Procyon v0.5.36
// 

package sea.event.entity;

import sea.event.Cancelable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

public class LivingEvent extends EntityEvent
{
    public final EntityLivingBase entityLiving;
    
    public LivingEvent(final EntityLivingBase entity) {
        super(entity);
        this.entityLiving = entity;
    }
    
    @Cancelable
    public static class LivingUpdateEvent extends LivingEvent
    {
        public LivingUpdateEvent(final EntityLivingBase e) {
            super(e);
        }
    }
    
    public static class LivingJumpEvent extends LivingEvent
    {
        public LivingJumpEvent(final EntityLivingBase e) {
            super(e);
        }
    }
}
