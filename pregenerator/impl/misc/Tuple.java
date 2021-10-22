// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.misc;

public class Tuple<K, V>
{
    K first;
    V second;
    
    public Tuple(final K key, final V value) {
        this.first = key;
        this.second = value;
    }
    
    public K getFirst() {
        return this.first;
    }
    
    public V getSecond() {
        return this.second;
    }
    
    public void setFirst(final K first) {
        this.first = first;
    }
    
    public void setSecond(final V second) {
        this.second = second;
    }
}
