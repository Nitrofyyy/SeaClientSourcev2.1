// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.client.infos;

public class ProgressBarEntry extends BarEntry
{
    public ProgressBarEntry() {
        this.register();
    }
    
    @Override
    public String getText(final int current, final int max) {
        return ProgressBarEntry.FORMAT.format(current) + "/" + ProgressBarEntry.FORMAT.format(max) + " Chunks";
    }
    
    @Override
    public int getMaxServer() {
        return this.getProcessor().getMaxProcess();
    }
    
    @Override
    public int getCurrentServer() {
        return this.getProcessor().getCurrentProcessed();
    }
    
    @Override
    public String getName() {
        return "Progress";
    }
}
