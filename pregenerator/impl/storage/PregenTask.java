// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.storage;

import pregenerator.impl.processor.ChunkCalculator;
import java.util.BitSet;
import java.util.Map;
import pregenerator.impl.misc.FilePos;
import java.util.concurrent.Callable;
import pregenerator.impl.processor.generator.ChunkProcess;
import java.util.concurrent.Future;
import pregenerator.impl.processor.PrepaireProgress;
import pregenerator.impl.processor.generator.GenerationType;
import pregenerator.ChunkPregenerator;
import net.minecraftforge.common.DimensionManager;
import net.minecraft.world.WorldServer;
import net.minecraft.world.World;
import net.minecraft.server.MinecraftServer;
import net.minecraft.nbt.NBTTagIntArray;
import com.google.common.base.Objects;

public class PregenTask
{
    protected int type;
    protected int dimension;
    protected int middleX;
    protected int middleZ;
    protected int radiusX;
    protected int radiusZ;
    protected int postProcessing;
    final int hashCode;
    boolean prevState;
    int prevUpdate;
    boolean preview;
    
    public PregenTask(final int... data) {
        this(data[0], data[1], data[2], data[3], data[4], data[5], data[6]);
    }
    
    public PregenTask(final int type, final int dimension, final int middleX, final int middleZ, final int radiusX, final int radiusZ, final int post) {
        this.preview = false;
        this.type = type;
        this.dimension = dimension;
        this.middleX = middleX;
        this.middleZ = middleZ;
        this.radiusX = radiusX;
        this.radiusZ = radiusZ;
        this.postProcessing = post;
        this.hashCode = Objects.hashCode(new Object[] { type, dimension, middleX, middleZ, radiusX, radiusZ });
    }
    
    public PregenTask setPreview() {
        this.preview = true;
        return this;
    }
    
    public NBTTagIntArray save() {
        return new NBTTagIntArray(new int[] { this.type, this.dimension, this.middleX, this.middleZ, this.radiusX, this.radiusZ, this.postProcessing });
    }
    
    public boolean isPreview() {
        return this.preview;
    }
    
    public int startTask(final MinecraftServer server) {
        final WorldServer world = server.func_71218_a(this.dimension);
        this.prevState = this.isKeepingLoaded(world);
        this.changeDimension(world, true);
        return 1;
    }
    
    public boolean isKeepingLoaded(final World world) {
        return DimensionManager.shouldLoadSpawn(world.field_73011_w.func_177502_q());
    }
    
    private void changeDimension(final World world, final boolean state) {
        ChunkPregenerator.pregenBase.setState(world, state);
    }
    
    public void stopTask(final World world) {
        this.changeDimension(world, this.prevState);
    }
    
    public byte getState() {
        return (byte)this.postProcessing;
    }
    
    public boolean isPostProcessingTask() {
        return this.postProcessing == 1;
    }
    
    public boolean isForcedPostProcess() {
        return this.postProcessing == 2;
    }
    
    public boolean isBenchmarkTask() {
        return this.type == 6 || this.type == 7;
    }
    
    public boolean isLargeTask() {
        return this.type == 7;
    }
    
    public long getTaskSize() {
        return this.isBenchmarkTask() ? ((this.type == 6) ? 40000 : 250000) : 0L;
    }
    
    @Override
    public int hashCode() {
        return this.hashCode;
    }
    
    public int getCenterX() {
        return this.middleX;
    }
    
    public int getCenterZ() {
        return this.middleZ;
    }
    
    public int getXRadius() {
        return this.radiusX;
    }
    
    public int getZRadius() {
        return this.middleZ;
    }
    
    public int getType() {
        return this.type;
    }
    
    public GenerationType getPostType() {
        return GenerationType.values()[this.postProcessing];
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof PregenTask) {
            final PregenTask other = (PregenTask)obj;
            return other.type == this.type && this.dimension == other.dimension && this.middleX == other.middleX && this.middleZ == other.middleZ && this.radiusX == other.radiusX && this.radiusZ == other.radiusZ;
        }
        return false;
    }
    
    public Future<ChunkProcess> createTask(final PrepaireProgress progress) {
        final WorldServer world = DimensionManager.getWorld(this.dimension);
        if (world == null) {
            return null;
        }
        progress.reset();
        ChunkPregenerator.LOGGER.info("Test: " + this.getTaskSizes());
        progress.setMax(this.getTaskSizes());
        return ChunkPregenerator.SERVICE.submit((Callable<ChunkProcess>)new Callable<ChunkProcess>() {
            @Override
            public ChunkProcess call() throws Exception {
                final ChunkProcess process = new ChunkProcess(world, PregenTask.this);
                process.addTaskList(PregenTask.this.makeTask(progress), new FilePos(PregenTask.this.middleX, PregenTask.this.middleZ), progress);
                return process;
            }
        });
    }
    
    public long getTaskSizes() {
        switch (this.type) {
            case 0: {
                return this.radiusX * 2L * (this.radiusX * 2L);
            }
            case 1: {
                return (long)(this.radiusX * 2L * 3.141592653589793);
            }
            case 2: {
                return this.radiusX * 2L * (this.radiusZ * 2L);
            }
            case 4: {
                final long minDiameter = this.radiusX * 2L;
                final long maxDiameter = this.radiusZ * 2L;
                return maxDiameter * maxDiameter - minDiameter * minDiameter;
            }
            case 5: {
                return (long)(this.radiusZ * 2L * 3.141592653589793) - (long)(this.radiusX * 2L * 3.141592653589793);
            }
            case 6: {
                return this.radiusX * 2L * (this.radiusX * 2L);
            }
            case 7: {
                return this.radiusX * 2L * (this.radiusX * 2L);
            }
            default: {
                return 0L;
            }
        }
    }
    
    protected Map<Long, BitSet> makeTask(final PrepaireProgress progress) {
        if (this.type == 0) {
            return ChunkCalculator.createSquare(this.middleX, this.middleZ, this.radiusX, progress);
        }
        if (this.type == 1) {
            return ChunkCalculator.createCircle(this.middleX, this.middleZ, this.radiusX, progress);
        }
        if (this.type == 2) {
            return ChunkCalculator.createArea(this.middleX, this.middleZ, this.radiusX, this.radiusZ, progress);
        }
        if (this.type == 3) {
            return null;
        }
        if (this.type == 4) {
            return ChunkCalculator.createSquareExt(this.middleX, this.middleZ, this.radiusX, this.radiusZ, progress);
        }
        if (this.type == 5) {
            return ChunkCalculator.createCircleExt(this.middleX, this.middleZ, this.radiusX, this.radiusZ, progress);
        }
        if (this.type == 6 || this.type == 7) {
            return ChunkCalculator.createSquare(this.middleX, this.middleZ, this.radiusX, progress);
        }
        return null;
    }
    
    @Override
    public String toString() {
        if (this.type == 0) {
            return "Radius Task: Dim: " + this.dimension + ", X: " + this.middleX + ", Z: " + this.middleZ + ", Radius: " + this.radiusX + ", Post Processing: " + this.getPost();
        }
        if (this.type == 1) {
            return "Circle Task: Dim: " + this.dimension + ", X: " + this.middleX + ", Z: " + this.middleZ + ", Radius: " + this.radiusX + ", Post Processing: " + this.postProcessing;
        }
        if (this.type == 2) {
            return "Area Task: Dim: " + this.dimension + ", XStart: " + this.middleX + ", ZStart: " + this.middleZ + ", XEnd: " + this.radiusX + ", ZEnd: " + this.radiusZ + ", Post Processing: " + this.getPost();
        }
        if (this.type == 3) {
            return "Circle Area Task: Dim: " + this.dimension + ", XStart: " + this.middleX + ", ZStart: " + this.middleZ + ", XEnd: " + this.radiusX + ", ZEnd: " + this.radiusZ + ", Post Processing: " + this.getPost();
        }
        if (this.type == 4) {
            return "Radius Extension Task: Dim: " + this.dimension + ", X: " + this.middleX + ", Z: " + this.middleZ + ", Min Radius: " + this.radiusX + ", Max Radius: " + this.radiusZ + " Post Processing: " + this.getPost();
        }
        if (this.type == 5) {
            return "Radius Circle Extension Task: Dim: " + this.dimension + ", X: " + this.middleX + ", Z: " + this.middleZ + ", Min Radius: " + this.radiusX + ", Max Radius: " + this.radiusZ + " Post Processing: " + this.getPost();
        }
        if (this.type == 6) {
            return "Small Benchmark Task: Dim: " + this.dimension + "";
        }
        if (this.type == 7) {
            return "Big Benchmark Task: Dim: " + this.dimension + "";
        }
        return "Plane Task: Dim: " + this.dimension + ", X: " + this.middleX + ", Z: " + this.middleZ + ", X Radius: " + this.radiusX + ", Z Radius: " + this.radiusZ + ", Post Processing: " + this.getPost();
    }
    
    protected String getPost() {
        if (this.postProcessing == 0) {
            return "Terrain Only";
        }
        if (this.postProcessing == 3) {
            return "PostProcessing Only";
        }
        if (this.postProcessing == 4) {
            return "Blocked PostProcessing";
        }
        if (this.postProcessing == 5) {
            return "Retrogen";
        }
        return "Terrain & PostProcessing";
    }
}
