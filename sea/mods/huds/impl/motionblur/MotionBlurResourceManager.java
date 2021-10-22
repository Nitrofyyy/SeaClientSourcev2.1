// 
// Decompiled by Procyon v0.5.36
// 

package sea.mods.huds.impl.motionblur;

import java.util.List;
import net.minecraft.client.resources.IResource;
import net.minecraft.util.ResourceLocation;
import java.util.Set;
import net.minecraft.client.resources.data.IMetadataSerializer;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.FallbackResourceManager;

public class MotionBlurResourceManager extends FallbackResourceManager implements IResourceManager
{
    public MotionBlurResourceManager(final IMetadataSerializer frmMetadataSerializerIn) {
        super(frmMetadataSerializerIn);
    }
    
    @Override
    public Set getResourceDomains() {
        return null;
    }
    
    @Override
    public IResource getResource(final ResourceLocation location) {
        return new MotionBlurResource();
    }
    
    @Override
    public List getAllResources(final ResourceLocation location) {
        return null;
    }
}
