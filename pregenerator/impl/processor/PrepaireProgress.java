// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.processor;

public class PrepaireProgress
{
    long current;
    long max;
    
    public void growValue(final int add) {
        this.current += add;
    }
    
    public void setMax(final long max) {
        this.max = max;
    }
    
    public void addMax(final long max) {
        this.max += max;
    }
    
    public void reset() {
        this.current = 0L;
        this.max = 0L;
    }
    
    public long getCurrent() {
        return this.current;
    }
    
    public long getMax() {
        return this.max;
    }
}
