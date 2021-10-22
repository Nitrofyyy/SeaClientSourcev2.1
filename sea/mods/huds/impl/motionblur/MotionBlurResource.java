// 
// Decompiled by Procyon v0.5.36
// 

package sea.mods.huds.impl.motionblur;

import net.minecraft.client.resources.data.IMetadataSection;
import org.apache.commons.io.IOUtils;
import java.nio.charset.Charset;
import java.util.Locale;
import sea.mods.huds.HudList;
import java.io.InputStream;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.resources.IResource;

public class MotionBlurResource implements IResource
{
    private static final String JSON = "{\"targets\":[\"swap\",\"previous\"],\"passes\":[{\"name\":\"phosphor\",\"intarget\":\"minecraft:main\",\"outtarget\":\"swap\",\"auxtargets\":[{\"name\":\"PrevSampler\",\"id\":\"previous\"}],\"uniforms\":[{\"name\":\"Phosphor\",\"values\":[%.2f, %.2f, %.2f]}]},{\"name\":\"blit\",\"intarget\":\"swap\",\"outtarget\":\"previous\"},{\"name\":\"blit\",\"intarget\":\"swap\",\"outtarget\":\"minecraft:main\"}]}";
    
    @Override
    public ResourceLocation getResourceLocation() {
        return null;
    }
    
    @Override
    public InputStream getInputStream() {
        final double amount = 0.7 + HudList.getMotion().blurAmount / 100.0 * 3.0 - 0.01;
        return IOUtils.toInputStream(String.format(Locale.ENGLISH, "{\"targets\":[\"swap\",\"previous\"],\"passes\":[{\"name\":\"phosphor\",\"intarget\":\"minecraft:main\",\"outtarget\":\"swap\",\"auxtargets\":[{\"name\":\"PrevSampler\",\"id\":\"previous\"}],\"uniforms\":[{\"name\":\"Phosphor\",\"values\":[%.2f, %.2f, %.2f]}]},{\"name\":\"blit\",\"intarget\":\"swap\",\"outtarget\":\"previous\"},{\"name\":\"blit\",\"intarget\":\"swap\",\"outtarget\":\"minecraft:main\"}]}", amount, amount, amount), Charset.defaultCharset());
    }
    
    @Override
    public boolean hasMetadata() {
        return false;
    }
    
    @Override
    public IMetadataSection getMetadata(final String p_110526_1_) {
        return null;
    }
    
    @Override
    public String getResourcePackName() {
        return null;
    }
}
