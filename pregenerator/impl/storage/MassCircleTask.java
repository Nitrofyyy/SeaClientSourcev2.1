// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.storage;

import pregenerator.impl.processor.ChunkCalculator;
import java.util.BitSet;
import java.util.Map;
import pregenerator.impl.processor.PrepaireProgress;
import net.minecraft.nbt.NBTTagIntArray;

public class MassCircleTask extends PregenTask
{
    int centerX;
    int centerZ;
    int radius;
    
    public MassCircleTask(final int... data) {
        this(data[0] == 3, data[1], data[2], data[3], data[4], data[5], data[6], data[7], data[8], data[9]);
    }
    
    public MassCircleTask(final boolean type, final int dimID, final int minX, final int minZ, final int maxX, final int maxZ, final int centerX, final int centerZ, final int radius, final int post) {
        super(type ? 3 : 2, dimID, minX, minZ, maxX, maxZ, post);
        this.centerX = centerX;
        this.centerZ = centerZ;
        this.radius = radius;
    }
    
    @Override
    public NBTTagIntArray save() {
        return new NBTTagIntArray(new int[] { this.type, this.dimension, this.middleX, this.middleZ, this.radiusX, this.radiusZ, this.postProcessing, this.centerX, this.centerZ, this.radius });
    }
    
    @Override
    public long getTaskSizes() {
        if (this.type == 2) {
            return (this.radiusX - this.middleX) * (this.radiusZ - this.middleZ);
        }
        return (long)(this.radius * 2L * 3.141592653589793);
    }
    
    @Override
    protected Map<Long, BitSet> makeTask(final PrepaireProgress progress) {
        if (this.type == 2) {
            return ChunkCalculator.createArea(this.middleX, this.middleZ, this.radiusX, this.radiusZ, progress);
        }
        return ChunkCalculator.createCircleArea(this.middleX, this.middleZ, this.radiusX, this.radiusZ, this.centerX, this.centerZ, this.radius, progress);
    }
}
