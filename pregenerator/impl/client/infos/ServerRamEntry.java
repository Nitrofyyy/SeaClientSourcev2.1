// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.client.infos;

public class ServerRamEntry extends BarEntry
{
    public ServerRamEntry() {
        this.register();
    }
    
    @Override
    public String getText(final int current, final int max) {
        return "ServerRam: " + current + "/" + max + " MB";
    }
    
    @Override
    public int getMaxServer() {
        return this.toMB(Runtime.getRuntime().maxMemory());
    }
    
    @Override
    public int getCurrentServer() {
        final Runtime time = Runtime.getRuntime();
        return this.toMB(time.totalMemory() - time.freeMemory());
    }
    
    @Override
    public String getName() {
        return "Ram Info";
    }
    
    private int toMB(final long input) {
        return (int)(input / 1024L / 1024L);
    }
}
