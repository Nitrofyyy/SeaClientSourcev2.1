// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.processor;

import pregenerator.impl.misc.FilePos;
import java.util.LinkedHashMap;
import java.util.BitSet;
import java.util.Map;

public class ChunkCalculator
{
    public static Map<Long, BitSet> createArea(final int xStart, final int zStart, final int xEnd, final int zEnd, final PrepaireProgress progress) {
        final Map<Long, BitSet> storage = new LinkedHashMap<Long, BitSet>();
        for (int x = xStart; x < xEnd; ++x) {
            for (int z = zStart; z < zEnd; ++z) {
                final Long value = FilePos.asLong(x >> 5, z >> 5);
                BitSet set = storage.get(value);
                if (set == null) {
                    set = new BitSet(1024);
                    storage.put(value, set);
                }
                set.set((z & 0x1F) * 32 + (x & 0x1F));
                progress.growValue(1);
            }
        }
        return storage;
    }
    
    public static Map<Long, BitSet> createCircleArea(final int xStart, final int zStart, final int xEnd, final int zEnd, final int centerX, final int centerZ, final int radius, final PrepaireProgress progress) {
        final Map<Long, BitSet> storage = new LinkedHashMap<Long, BitSet>();
        for (int x = xStart; x < xEnd; ++x) {
            for (int z = zStart; z < zEnd; ++z) {
                final FilePos pos = new FilePos(x, z);
                if (pos.getSqDistance(centerX, centerZ) <= radius) {
                    final Long value = FilePos.asLong(x >> 5, z >> 5);
                    BitSet set = storage.get(value);
                    if (set == null) {
                        set = new BitSet(1024);
                        storage.put(value, set);
                    }
                    set.set((z & 0x1F) * 32 + (x & 0x1F));
                    progress.growValue(1);
                }
            }
        }
        return storage;
    }
    
    public static Map<Long, BitSet> createSquare(final int xPos, final int zPos, final int radius, final PrepaireProgress progress) {
        final Map<Long, BitSet> result = new LinkedHashMap<Long, BitSet>();
        for (int x = -radius; x < radius; ++x) {
            for (int z = -radius; z < radius; ++z) {
                final Long value = FilePos.asLong(x + xPos >> 5, z + zPos >> 5);
                BitSet set = result.get(value);
                if (set == null) {
                    set = new BitSet(1024);
                    result.put(value, set);
                }
                set.set((zPos + z & 0x1F) * 32 + (xPos + x & 0x1F));
                progress.growValue(1);
            }
        }
        return result;
    }
    
    public static Map<Long, BitSet> createSquareExt(final int xPos, final int zPos, final int minRadius, final int maxRadius, final PrepaireProgress progress) {
        final Map<Long, BitSet> result = new LinkedHashMap<Long, BitSet>();
        for (int x = -maxRadius; x < maxRadius; ++x) {
            for (int z = -maxRadius; z < maxRadius; ++z) {
                progress.growValue(1);
                if (x <= -minRadius || x > minRadius || z <= -minRadius || z > minRadius) {
                    final Long value = FilePos.asLong(x + xPos >> 5, z + zPos >> 5);
                    BitSet set = result.get(value);
                    if (set == null) {
                        set = new BitSet(1024);
                        result.put(value, set);
                    }
                    set.set((zPos + z & 0x1F) * 32 + (xPos + x & 0x1F));
                }
            }
        }
        return result;
    }
    
    public static Map<Long, BitSet> createCircle(final int xPos, final int zPos, final int radius, final PrepaireProgress progress) {
        final Map<Long, BitSet> result = new LinkedHashMap<Long, BitSet>();
        for (int x = -radius; x < radius; ++x) {
            for (int z = -radius; z < radius; ++z) {
                progress.growValue(1);
                if (!isNotInReach(x, z, radius)) {
                    final Long value = FilePos.asLong(x + xPos >> 5, z + zPos >> 5);
                    BitSet set = result.get(value);
                    if (set == null) {
                        set = new BitSet(1024);
                        result.put(value, set);
                    }
                    set.set((zPos + z & 0x1F) * 32 + (xPos + x & 0x1F));
                }
            }
        }
        return result;
    }
    
    public static Map<Long, BitSet> createCircleExt(final int xPos, final int zPos, final int minRadius, final int maxRadius, final PrepaireProgress progress) {
        final Map<Long, BitSet> result = new LinkedHashMap<Long, BitSet>();
        for (int x = -maxRadius; x < maxRadius; ++x) {
            for (int z = -maxRadius; z < maxRadius; ++z) {
                progress.growValue(1);
                if (!isNotInRange(x, z, minRadius, maxRadius)) {
                    final Long value = FilePos.asLong(x + xPos >> 5, z + zPos >> 5);
                    BitSet set = result.get(value);
                    if (set == null) {
                        set = new BitSet(1024);
                        result.put(value, set);
                    }
                    set.set((zPos + z & 0x1F) * 32 + (xPos + x & 0x1F));
                }
            }
        }
        return result;
    }
    
    static boolean isNotInRange(final int x, final int y, final int minRadius, final int maxRadius) {
        final long distance = x * x + y * y;
        return distance >= maxRadius * maxRadius && distance < minRadius * minRadius;
    }
    
    static boolean isNotInReach(final int x, final int y, final int radius) {
        return x * x + y * y >= radius * radius;
    }
}
