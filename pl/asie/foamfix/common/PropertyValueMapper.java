// 
// Decompiled by Procyon v0.5.36
// 

package pl.asie.foamfix.common;

import net.minecraft.util.MathHelper;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import gnu.trove.map.hash.TObjectIntHashMap;
import java.util.Collection;
import java.util.ArrayList;
import net.minecraft.block.state.IBlockState;
import gnu.trove.map.TObjectIntMap;
import net.minecraft.block.state.BlockState;
import java.util.Map;
import net.minecraft.block.properties.IProperty;
import java.util.Comparator;

public class PropertyValueMapper
{
    private static final Comparator<IProperty> COMPARATOR_BIT_FITNESS;
    private static final Map<IProperty, Entry> entryMap;
    private static final Map<BlockState, PropertyValueMapper> mapperMap;
    private final Entry[] entryList;
    private final TObjectIntMap<IProperty> entryPositionMap;
    private final IBlockState[] stateMap;
    
    public PropertyValueMapper(final BlockState container) {
        final Collection<IProperty> properties = (Collection<IProperty>)container.func_177623_d();
        this.entryList = new Entry[properties.size()];
        final List<IProperty> propertiesSortedFitness = new ArrayList<IProperty>(properties);
        propertiesSortedFitness.sort(PropertyValueMapper.COMPARATOR_BIT_FITNESS);
        int i = 0;
        for (final IProperty p : propertiesSortedFitness) {
            this.entryList[i++] = getPropertyEntry(p);
        }
        this.entryPositionMap = (TObjectIntMap<IProperty>)new TObjectIntHashMap(10, 0.5f, -1);
        int bitPos = 0;
        Entry lastEntry = null;
        for (final Entry ee : this.entryList) {
            this.entryPositionMap.put((Object)ee.property, bitPos);
            bitPos += ee.bits;
            lastEntry = ee;
        }
        if (lastEntry == null) {
            this.stateMap = new IBlockState[1 << bitPos];
        }
        else {
            this.stateMap = new IBlockState[(1 << bitPos - lastEntry.bits) * lastEntry.property.func_177700_c().size()];
        }
    }
    
    public static PropertyValueMapper getOrCreate(final BlockState owner) {
        PropertyValueMapper e = PropertyValueMapper.mapperMap.get(owner);
        if (e == null) {
            e = new PropertyValueMapper(owner);
            PropertyValueMapper.mapperMap.put(owner, e);
        }
        return e;
    }
    
    protected static Entry getPropertyEntry(final IProperty property) {
        Entry e = PropertyValueMapper.entryMap.get(property);
        if (e == null) {
            e = new Entry(property);
            PropertyValueMapper.entryMap.put(property, e);
        }
        return e;
    }
    
    public int generateValue(final IBlockState state) {
        int bitPos = 0;
        int value = 0;
        for (final Entry e : this.entryList) {
            value |= e.get(state.func_177229_b(e.property)) << bitPos;
            bitPos += e.bits;
        }
        this.stateMap[value] = state;
        return value;
    }
    
    public <T extends Comparable<T>, V extends T> IBlockState withProperty(int value, final IProperty<T> property, final V propertyValue) {
        final int bitPos = this.entryPositionMap.get((Object)property);
        if (bitPos >= 0) {
            final Entry e = getPropertyEntry(property);
            if (e != null) {
                final int nv = e.get(propertyValue);
                if (nv < 0) {
                    return null;
                }
                value &= ~(e.bitSize - 1 << bitPos);
                value |= nv << bitPos;
                return this.stateMap[value];
            }
        }
        return null;
    }
    
    public IBlockState getPropertyByValue(final int value) {
        return this.stateMap[value];
    }
    
    public <T extends Comparable<T>, V extends T> int withPropertyValue(int value, final IProperty<T> property, final V propertyValue) {
        final int bitPos = this.entryPositionMap.get((Object)property);
        if (bitPos >= 0) {
            final Entry e = getPropertyEntry(property);
            if (e != null) {
                final int nv = e.get(propertyValue);
                if (nv < 0) {
                    return -1;
                }
                value &= ~(e.bitSize - 1 << bitPos);
                value |= nv << bitPos;
                return value;
            }
        }
        return -1;
    }
    
    static {
        COMPARATOR_BIT_FITNESS = new Comparator<IProperty>() {
            @Override
            public int compare(final IProperty first, final IProperty second) {
                final int diff1 = PropertyValueMapper.getPropertyEntry(first).bitSize - first.func_177700_c().size();
                final int diff2 = PropertyValueMapper.getPropertyEntry(second).bitSize - second.func_177700_c().size();
                return diff1 - diff2;
            }
        };
        entryMap = new IdentityHashMap<IProperty, Entry>();
        mapperMap = new IdentityHashMap<BlockState, PropertyValueMapper>();
    }
    
    public static class Entry
    {
        private final IProperty property;
        private final TObjectIntMap values;
        private final int bitSize;
        private final int bits;
        
        private Entry(final IProperty property) {
            this.property = property;
            this.values = (TObjectIntMap)new TObjectIntHashMap(10, 0.5f, -1);
            this.bitSize = MathHelper.func_151236_b(property.func_177700_c().size());
            int bits = 0;
            for (int b = this.bitSize; b != 0; b >>= 1) {
                ++bits;
            }
            this.bits = bits;
            int i = 0;
            for (final Object o : property.func_177700_c()) {
                this.values.put(o, i++);
            }
        }
        
        public int get(final Object v) {
            return this.values.get(v);
        }
    }
}
