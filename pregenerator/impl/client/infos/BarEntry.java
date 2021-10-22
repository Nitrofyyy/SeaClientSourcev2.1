// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.client.infos;

import pregenerator.base.api.network.IReadableBuffer;
import pregenerator.base.api.network.IWriteableBuffer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import pregenerator.base.api.misc.IRenderHelper;

public abstract class BarEntry extends InfoEntry
{
    int max;
    int current;
    
    public abstract String getText(final int p0, final int p1);
    
    @SideOnly(Side.CLIENT)
    @Override
    public void render(final int x, final int y, final float progress, final int width, final IRenderHelper helper) {
        final int progresBar = (int)(progress * width);
        helper.renderBar(x - width / 2, y, width, progresBar, this.getText(this.currentValue(), this.maxValue()));
    }
    
    @Override
    public void write(final IWriteableBuffer buf) {
        buf.writeInt(this.getCurrentServer());
        buf.writeInt(this.getMaxServer());
    }
    
    @Override
    public void read(final IReadableBuffer buf) {
        this.current = buf.readInt();
        this.max = buf.readInt();
    }
    
    @Override
    public int currentValue() {
        return this.current;
    }
    
    @Override
    public int maxValue() {
        return this.max;
    }
    
    public abstract int getMaxServer();
    
    public abstract int getCurrentServer();
}
