// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.misc;

import java.util.LinkedList;

public class FileCounter
{
    LinkedList<Integer> averageList;
    int perTick;
    int average;
    
    public FileCounter() {
        this.averageList = new LinkedList<Integer>();
        this.perTick = 0;
        this.average = 0;
    }
    
    public void onChunkProcessed() {
        ++this.perTick;
    }
    
    public void add(final int amount) {
        this.perTick += amount;
        this.onTickEnded();
    }
    
    public void onTickEnded() {
        this.averageList.add(this.perTick);
        this.average += this.perTick;
        if (this.averageList.size() > 200) {
            this.average -= this.averageList.removeFirst();
        }
        this.perTick = 0;
    }
    
    public void reset() {
        this.perTick = 0;
        this.average = 0;
        this.averageList.clear();
    }
    
    public void onTickCut() {
        this.perTick = 0;
    }
    
    public float getAverage() {
        return this.average / (float)this.averageList.size();
    }
    
    public int getIntAverage() {
        return this.average / this.averageList.size();
    }
}
