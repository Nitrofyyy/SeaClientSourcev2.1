// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity;

import sea.event.Cancelable;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import sea.event.Event;

public abstract class RenderLivingEvent<T extends EntityLivingBase> extends Event
{
    public final EntityLivingBase entity;
    public final RendererLivingEntity<T> renderer;
    public final double x;
    public final double y;
    public final double z;
    
    public RenderLivingEvent(final EntityLivingBase entity, final RendererLivingEntity<T> renderer, final double x, final double y, final double z) {
        this.entity = entity;
        this.renderer = renderer;
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    @Cancelable
    public static class Pre<T extends EntityLivingBase> extends RenderLivingEvent<T>
    {
        public Pre(final EntityLivingBase entity, final RendererLivingEntity<T> renderer, final double x, final double y, final double z) {
            super(entity, renderer, x, y, z);
        }
    }
    
    public static class Post<T extends EntityLivingBase> extends RenderLivingEvent<T>
    {
        public Post(final EntityLivingBase entity, final RendererLivingEntity<T> renderer, final double x, final double y, final double z) {
            super(entity, renderer, x, y, z);
        }
    }
    
    public abstract static class Specials<T extends EntityLivingBase> extends RenderLivingEvent<T>
    {
        public Specials(final EntityLivingBase entity, final RendererLivingEntity<T> renderer, final double x, final double y, final double z) {
            super(entity, renderer, x, y, z);
        }
        
        @Cancelable
        public static class Pre<T extends EntityLivingBase> extends Specials<T>
        {
            public Pre(final EntityLivingBase entity, final RendererLivingEntity<T> renderer, final double x, final double y, final double z) {
                super(entity, renderer, x, y, z);
            }
        }
        
        public static class Post<T extends EntityLivingBase> extends Specials<T>
        {
            public Post(final EntityLivingBase entity, final RendererLivingEntity<T> renderer, final double x, final double y, final double z) {
                super(entity, renderer, x, y, z);
            }
        }
    }
}
