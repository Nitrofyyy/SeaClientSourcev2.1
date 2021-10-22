// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.tracking;

import java.util.LinkedList;

public class TimeTracker
{
    LinkedList<Long> allValues;
    int limit;
    long start;
    long average;
    long last;
    
    public TimeTracker(final int limit) {
        this.allValues = new LinkedList<Long>();
        this.start = 0L;
        this.average = 0L;
        this.last = 0L;
        this.limit = limit;
    }
    
    public void setStart() {
        this.start = System.nanoTime();
    }
    
    public void onFinished() {
        final long value = System.nanoTime() - this.start;
        this.allValues.add(value);
        this.average += value;
        this.start = 0L;
        this.last = value;
        if (this.allValues.size() > this.limit) {
            this.average -= this.allValues.removeFirst();
        }
    }
    
    public long getLastValue() {
        return this.last;
    }
    
    public long getAverage() {
        return this.allValues.isEmpty() ? 0L : (this.average / this.allValues.size());
    }
    
    public void clear() {
        this.allValues.clear();
        this.average = 0L;
        this.start = 0L;
        this.last = 0L;
    }
}
