// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.misc;

import java.util.LinkedList;

public class ChunkTimer
{
    LinkedList<Long> timers;
    long totalDelta;
    long startTime;
    
    public ChunkTimer() {
        this.timers = new LinkedList<Long>();
    }
    
    public void startTime() {
        this.startTime = System.nanoTime();
    }
    
    public void onChunkFinished() {
        final long newTime = System.nanoTime();
        final long delta = newTime - this.startTime;
        this.timers.add(delta);
        this.totalDelta += delta;
        if (this.timers.size() > 200) {
            this.totalDelta -= this.timers.removeFirst();
        }
        this.startTime = newTime;
    }
    
    public long getAverage() {
        return this.totalDelta / this.timers.size();
    }
    
    public boolean hasValues() {
        return this.timers.size() > 0;
    }
    
    public void cleanUp() {
        this.startTime = 0L;
        this.totalDelta = 0L;
        this.timers.clear();
    }
}
