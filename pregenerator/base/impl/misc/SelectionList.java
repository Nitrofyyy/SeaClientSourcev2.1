// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.base.impl.misc;

import java.util.Collection;
import java.util.ArrayList;
import java.util.List;

public class SelectionList<V>
{
    List<V> values;
    int index;
    boolean noLoop;
    
    public SelectionList() {
        this.values = new ArrayList<V>();
        this.index = 0;
        this.noLoop = false;
    }
    
    public SelectionList(final List<V> values) {
        this.values = new ArrayList<V>();
        this.index = 0;
        this.noLoop = false;
        this.setValues(values);
    }
    
    public SelectionList setNoLoop() {
        this.noLoop = true;
        return this;
    }
    
    public void setValues(final List<V> value) {
        if (this.values.isEmpty()) {
            this.index = 0;
            this.values.addAll((Collection<? extends V>)value);
            return;
        }
        final V last = this.values.get(this.index);
        this.values.clear();
        this.values.addAll((Collection<? extends V>)value);
        this.index = Math.max(0, this.values.indexOf(last));
    }
    
    public void addValues(final List<V> list) {
        this.values.addAll((Collection<? extends V>)list);
    }
    
    public void addValue(final V value) {
        this.values.add(value);
    }
    
    public int getIndex() {
        return this.index;
    }
    
    public int size() {
        return this.values.size();
    }
    
    public List<V> getValues() {
        return this.values;
    }
    
    public V getValue(final int index) {
        return (index < 0 || index >= this.values.size()) ? null : this.values.get(index);
    }
    
    public V getValue() {
        return this.values.isEmpty() ? null : this.values.get(this.index);
    }
    
    public V removeIndex(final int index) {
        final V value = this.values.remove(index);
        this.setIndex(this.index);
        return value;
    }
    
    public void setIndex(final int index) {
        this.index = Math.max(0, Math.min(index, this.values.size() - 1));
    }
    
    public void setIndexFromValue(final V value) {
        this.index = Math.max(0, this.values.indexOf(value));
    }
    
    public void next() {
        ++this.index;
        if (this.index >= this.values.size()) {
            this.index = (this.noLoop ? (this.values.size() - 1) : 0);
        }
    }
    
    public void prev() {
        --this.index;
        if (this.index < 0) {
            this.index = Math.max(0, this.noLoop ? 0 : (this.values.size() - 1));
        }
    }
    
    public void clear() {
        this.index = 0;
        this.values.clear();
    }
}
