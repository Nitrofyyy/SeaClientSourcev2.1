// 
// Decompiled by Procyon v0.5.36
// 

package pl.asie.foamfix.client;

import gnu.trove.strategy.HashingStrategy;
import gnu.trove.set.hash.TCustomHashSet;

public class DeduplicatingStorageTrove<T> extends TCustomHashSet<T> implements IDeduplicatingStorage<T>
{
    public DeduplicatingStorageTrove(final HashingStrategy<T> strategy) {
        super((HashingStrategy)strategy);
    }
    
    public T deduplicate(final T o) {
        final int i = this.index((Object)o);
        if (i >= 0) {
            return (T)this._set[i];
        }
        this.add((Object)o);
        return o;
    }
}
