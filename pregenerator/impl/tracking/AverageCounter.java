// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.tracking;

import java.util.LinkedList;

public class AverageCounter
{
    LinkedList<Integer> allValues;
    int limit;
    int count;
    int average;
    int last;
    
    public AverageCounter(final int limit) {
        this.allValues = new LinkedList<Integer>();
        this.count = 0;
        this.average = 0;
        this.limit = limit;
    }
    
    public void addOne() {
        ++this.count;
    }
    
    public void addMore(final int amount) {
        this.count += amount;
    }
    
    public void onFinished() {
        this.allValues.add(this.count);
        this.average += this.count;
        this.last = this.count;
        this.count = 0;
        if (this.allValues.size() > this.limit) {
            this.average -= this.allValues.removeFirst();
        }
    }
    
    public int getAverage() {
        return this.allValues.isEmpty() ? 0 : (this.average / this.allValues.size());
    }
    
    public int getLast() {
        return this.last;
    }
    
    public void clear() {
        this.allValues.clear();
        this.count = 0;
        this.average = 0;
        this.last = 0;
    }
}
