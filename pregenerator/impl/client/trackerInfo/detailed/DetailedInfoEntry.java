// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.client.trackerInfo.detailed;

import pregenerator.base.api.misc.IRenderHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pregenerator.impl.client.ClientHandler;
import pregenerator.base.api.network.IReadableBuffer;
import pregenerator.base.api.network.IWriteableBuffer;
import pregenerator.impl.client.trackerInfo.TrackerEntry;

public class DetailedInfoEntry extends TrackerEntry
{
    public DetailedInfoEntry() {
        this.register();
    }
    
    @Override
    public String getName() {
        return "Detailed Info";
    }
    
    @Override
    public boolean hasConfig() {
        return false;
    }
    
    @Override
    public void writeServer(final IWriteableBuffer buf) {
    }
    
    @Override
    public void readClient(final IReadableBuffer buf) {
    }
    
    @Override
    public int currentValue() {
        return 0;
    }
    
    @Override
    public int maxValue() {
        return 0;
    }
    
    @Override
    public int getYOffset() {
        return 12;
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public boolean shouldRender() {
        return ClientHandler.INSTANCE.tracker.showDetailed;
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public void render(final int x, int y, final float progress, final int width, final IRenderHelper helper) {
        y += 6;
        helper.renderCenterText(x - width / 2, y, width, "Detailed-Dim-Info: " + ClientHandler.INSTANCE.tracker.targetDim);
    }
}
