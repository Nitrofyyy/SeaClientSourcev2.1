// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.tracking;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.Map;

public class CollectorMap<T>
{
    Map<Class<T>, Set<T>> dataMap;
    List<T> allEntries;
    
    public CollectorMap() {
        this.dataMap = new HashMap<Class<T>, Set<T>>();
        this.allEntries = new LinkedList<T>();
    }
    
    public void addEntry(final T data) {
        this.allEntries.add(data);
        Set<T> set = this.dataMap.get(data.getClass());
        if (set == null) {
            set = new HashSet<T>();
            this.dataMap.put((Class<T>)data.getClass(), set);
        }
        set.add(data);
    }
    
    public void addAll(final Collection<T> data) {
        for (final T entry : data) {
            this.addEntry(entry);
        }
    }
    
    public Set<Map.Entry<Class<T>, Set<T>>> entrySet() {
        return this.dataMap.entrySet();
    }
    
    public void removeAll(final Class<? extends T> type) {
        final Iterator<Map.Entry<Class<T>, Set<T>>> iter = this.dataMap.entrySet().iterator();
        while (iter.hasNext()) {
            final Map.Entry<Class<T>, Set<T>> entry = iter.next();
            if (type.isAssignableFrom(entry.getKey())) {
                this.allEntries.removeAll(entry.getValue());
                iter.remove();
            }
        }
    }
    
    public List<T> getAllOfType(final Class clz) {
        final Set<T> set = this.dataMap.get(clz);
        if (set != null) {
            return new ArrayList<T>((Collection<? extends T>)set);
        }
        return new ArrayList<T>();
    }
    
    public int size() {
        return this.allEntries.size();
    }
    
    public boolean isEmpty() {
        return this.allEntries.isEmpty();
    }
    
    public int getTypeCount() {
        return this.dataMap.size();
    }
}
