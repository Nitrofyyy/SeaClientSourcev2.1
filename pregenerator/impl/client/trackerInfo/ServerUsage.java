// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.client.trackerInfo;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pregenerator.base.api.misc.IRenderHelper;
import pregenerator.base.api.network.IReadableBuffer;
import pregenerator.base.api.network.IWriteableBuffer;
import java.text.DecimalFormat;

public class ServerUsage extends TrackerEntry
{
    public static final DecimalFormat DECIMALFORMAT;
    long data;
    
    public ServerUsage() {
        this.register();
    }
    
    @Override
    public String getName() {
        return "Server-Usage";
    }
    
    @Override
    public void writeServer(final IWriteableBuffer buf) {
        buf.writeLong(this.getTracker().getAverage());
    }
    
    @Override
    public void readClient(final IReadableBuffer buf) {
        this.data = buf.readLong();
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
    public void render(final int x, int y, float progress, final int width, final IRenderHelper helper) {
        progress = this.clamp(0.0f, 1.0f, this.getValue() / 50.0f);
        int progresBar = (int)(progress * width);
        final int value = this.getValue();
        helper.renderBar(x - width / 2, y, width, progresBar, "Server: " + value + " / 50 ms (" + this.data / 1000L + " qs)");
        y += 6;
        final float max = value / 50.0f;
        final float tps = Math.min(20.0f, 20.0f / max);
        progress = this.clamp(0.0f, 1.0f, tps / 20.0f);
        progresBar = (int)(progress * width);
        helper.renderBar(x - width / 2, y, width, progresBar, "TPS: " + ServerUsage.DECIMALFORMAT.format(tps) + " / 20");
    }
    
    public int getValue() {
        return (int)(this.data / 1000L / 1000L);
    }
    
    public float clamp(final float min, final float max, final float current) {
        return (current < min) ? min : ((current > max) ? max : current);
    }
    
    static {
        DECIMALFORMAT = new DecimalFormat("#.##");
    }
}
