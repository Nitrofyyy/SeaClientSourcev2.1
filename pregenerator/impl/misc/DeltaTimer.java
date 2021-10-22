// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.misc;

import java.util.LinkedList;

public class DeltaTimer
{
    LinkedList<Long> lastTimes;
    long lastTime;
    long averageTime;
    
    public DeltaTimer() {
        this.lastTimes = new LinkedList<Long>();
        this.lastTime = -1L;
        this.averageTime = 0L;
    }
    
    public void start() {
        this.lastTime = System.currentTimeMillis();
    }
    
    public long averageDelta() {
        final long newTime = System.currentTimeMillis();
        final long delta = newTime - this.lastTime;
        this.lastTimes.add(delta);
        this.lastTime = newTime;
        this.averageTime += delta;
        if (this.lastTimes.size() > 20) {
            this.averageTime -= this.lastTimes.removeFirst();
        }
        return this.averageTime / this.lastTimes.size();
    }
    
    public boolean hasValues() {
        return this.lastTimes.size() > 0;
    }
    
    public long getAverageDelta() {
        return this.averageTime / this.lastTimes.size();
    }
    
    public void finishDeltaTime() {
        long time = this.lastTimes.removeLast();
        this.averageTime -= time;
        time += this.getDeltaTime();
        this.lastTimes.addLast(time);
        this.averageTime += time;
    }
    
    public long getDeltaTime() {
        return System.currentTimeMillis() - this.lastTime;
    }
    
    public void reset() {
        this.lastTime = -1L;
        this.averageTime = 0L;
        this.lastTimes.clear();
    }
}
