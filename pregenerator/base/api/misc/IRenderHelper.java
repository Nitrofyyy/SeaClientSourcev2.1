// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.base.api.misc;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public interface IRenderHelper
{
    IRenderHelper init();
    
    void renderText(final int p0, final int p1, final int p2, final String p3);
    
    void renderCenterText(final int p0, final int p1, final int p2, final String p3);
    
    void renderArea(final int p0, final int p1, final int p2, final int p3);
    
    void renderBar(final int p0, final int p1, final int p2, final int p3, final String p4);
}
