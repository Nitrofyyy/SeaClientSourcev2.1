// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.misc;

public class FilePos
{
    public final int x;
    public final int z;
    
    public FilePos(final long longIn) {
        this.x = (int)longIn;
        this.z = (int)(longIn >> 32);
    }
    
    public FilePos(final int xPos, final int zPos) {
        this.x = xPos;
        this.z = zPos;
    }
    
    public FilePos offset(final int xOff, final int zOff) {
        return new FilePos(this.x + xOff, this.z + zOff);
    }
    
    public static long add(final long pos, final int x, final int z) {
        return asLong(x + getX(pos), z + getZ(pos));
    }
    
    public static long asLong(final int x, final int z) {
        return ((long)x & 0xFFFFFFFFL) | ((long)z & 0xFFFFFFFFL) << 32;
    }
    
    public static int getX(final long pos) {
        return (int)pos;
    }
    
    public static int getZ(final long pos) {
        return (int)(pos >> 32);
    }
    
    @Override
    public int hashCode() {
        final int i = 1664525 * this.x + 1013904223;
        final int j = 1664525 * (this.z ^ 0xDEADBEEF) + 1013904223;
        return i ^ j;
    }
    
    @Override
    public boolean equals(final Object arg) {
        if (arg instanceof FilePos) {
            final FilePos pos = (FilePos)arg;
            return pos.x == this.x && pos.z == this.z;
        }
        return false;
    }
    
    public long asLong() {
        return asLong(this.x, this.z);
    }
    
    @Override
    public String toString() {
        return "X: " + this.x + " Z: " + this.z;
    }
    
    public FilePos toChunkFile() {
        return new FilePos(this.x >> 5, this.z >> 5);
    }
    
    public FilePos toChunkPos() {
        return new FilePos(this.x >> 4, this.z >> 4);
    }
    
    public int getDistance(final int xPos, final int zPos) {
        final int xDis = this.x - xPos;
        final int zDis = this.z - zPos;
        return xDis * xDis + zDis * zDis;
    }
    
    public double getSqDistance(final int xPos, final int zPos) {
        return Math.sqrt(this.getDistance(xPos, zPos));
    }
    
    public int getSquDistane(final FilePos pos) {
        return (int)this.getSqDistance(pos.x, pos.z);
    }
}
