// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.misc;

import net.minecraft.nbt.NBTTagIntArray;

public class BoundingBox
{
    int xMin;
    int zMin;
    int xMax;
    int zMax;
    
    public BoundingBox(final int[] data) {
        this.xMin = data[0];
        this.zMin = data[1];
        this.xMax = data[2];
        this.zMax = data[3];
    }
    
    public BoundingBox(final int x, final int z, final int width, final int height) {
        this.xMin = x - width / 2;
        this.zMin = z - height / 2;
        this.xMax = x + width / 2;
        this.zMax = z + height / 2;
    }
    
    public boolean isInsideBox(final int xPos, final int zPos) {
        return xPos >= this.xMin && xPos <= this.xMax && zPos >= this.zMin && zPos <= this.zMax;
    }
    
    public NBTTagIntArray write() {
        return new NBTTagIntArray(new int[] { this.xMin, this.zMin, this.xMax, this.zMax });
    }
    
    public int getCenterX() {
        return this.xMin + (this.xMax - this.xMin) / 2;
    }
    
    public int getCenterZ() {
        return this.zMin + (this.zMax - this.zMin) / 2;
    }
    
    public int getRadius() {
        return (this.xMax - this.xMin) / 2;
    }
}
