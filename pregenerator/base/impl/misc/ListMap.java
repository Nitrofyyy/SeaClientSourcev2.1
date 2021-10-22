// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.base.impl.misc;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.Map;
import java.util.List;

public class ListMap<L, V>
{
    List<L> listOptions;
    Map<L, SelectionList<V>> values;
    int index;
    
    public ListMap() {
        this.listOptions = new ArrayList<L>();
        this.values = new LinkedHashMap<L, SelectionList<V>>();
        this.index = 0;
    }
    
    public void fillIndexes(final List<L> list) {
        if (this.values.isEmpty()) {
            this.index = 0;
            this.listOptions.addAll((Collection<? extends L>)list);
            return;
        }
        final L last = this.listOptions.get(this.index);
        this.listOptions.clear();
        this.listOptions.addAll((Collection<? extends L>)list);
        this.index = Math.max(0, this.listOptions.indexOf(last));
    }
    
    public void putData(final List<V> data) {
        final L key = this.getCurrentKey();
        if (key == null) {
            return;
        }
        this.values.put(key, new SelectionList<V>(data));
    }
    
    public void putData(final L value, final List<V> data) {
        this.values.put(value, new SelectionList<V>(data));
    }
    
    public void setIndex(final int index) {
        this.index = Math.max(0, Math.min(index, this.listOptions.size() - 1));
    }
    
    public void next() {
        ++this.index;
        if (this.index >= this.listOptions.size()) {
            this.index = 0;
        }
    }
    
    public void prev() {
        --this.index;
        if (this.index < 0) {
            this.index = Math.max(0, this.listOptions.size() - 1);
        }
    }
    
    public int size() {
        return this.listOptions.size();
    }
    
    public int getCurrentIndex() {
        return this.index;
    }
    
    public L getCurrentKey() {
        return this.listOptions.isEmpty() ? null : this.listOptions.get(this.index);
    }
    
    public L getKey(final int index) {
        return (index < 0 || index >= this.listOptions.size()) ? null : this.listOptions.get(index);
    }
    
    public SelectionList<V> getCurrentValues() {
        if (this.listOptions.isEmpty()) {
            return null;
        }
        return this.values.get(this.listOptions.get(this.index));
    }
    
    public SelectionList<V> getForIndex(final int index) {
        return (index < 0 || index >= this.listOptions.size()) ? null : this.values.get(this.listOptions.get(index));
    }
    
    public SelectionList<V> getOrCreate(final L value) {
        SelectionList<V> result = this.values.get(value);
        if (result == null) {
            result = new SelectionList<V>();
            this.values.put(value, result);
            this.listOptions.add(value);
        }
        return result;
    }
    
    public List<L> getKeys() {
        return this.listOptions;
    }
    
    public void clear() {
        this.index = 0;
        this.listOptions.clear();
        this.values.clear();
    }
}
