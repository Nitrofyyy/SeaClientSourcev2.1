// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.command.delete;

import pregenerator.impl.processor.ChunkCalculator;
import net.minecraft.world.WorldServer;
import java.io.File;
import java.util.concurrent.Callable;
import pregenerator.ChunkPregenerator;
import net.minecraftforge.common.DimensionManager;
import pregenerator.impl.processor.deleter.DeleteProcess;
import java.util.concurrent.Future;
import pregenerator.impl.processor.PrepaireProgress;
import pregenerator.impl.command.CompleterHelper;
import java.util.Iterator;
import java.util.Comparator;
import com.google.common.math.DoubleMath;
import java.math.RoundingMode;
import java.util.ArrayList;
import pregenerator.impl.processor.deleter.IDeletionTask;
import java.util.List;
import pregenerator.impl.misc.FilePos;
import pregenerator.impl.command.base.PregenCommand;
import pregenerator.impl.command.base.CommandContainer;
import pregenerator.impl.command.base.BasePregenCommand;

public class DeleteMassSubCommand extends BasePregenCommand
{
    public DeleteMassSubCommand() {
        super(6);
        this.addDescription(0, "Deletion Type: Which shape the Generation should have");
        this.addDescription(1, "X Center: Which Chunk the center should be in. (if 'b' infront of the number or after ~ means block position)");
        this.addDescription(2, "Z Center: Which Chunk the center should be in. (if 'b' infront of the number or after ~ means block position)");
        this.addDescription(3, "Radius: How big the Radius in Chunks should be. (if 'b' infront of the number or after ~ means block distance)");
        this.addDescription(4, "(Optional) Dimension: The Dimension the Deletion should be happening in");
        this.addDescription(5, "(Optional) Delay: How long the Deleter should wait until it should start (in ticks)");
        this.addSuggestion("deleteMassRadius square 0 0 100 -1 200", "Deletes a 100 Chunk Area around the World Center in the nether and it waits 10 seconds before starting");
    }
    
    @Override
    public String getName() {
        return "deleteMassRadius";
    }
    
    @Override
    public String getDescription() {
        return "Deletes a Massive Area";
    }
    
    @Override
    public int getRequiredParameterCount() {
        return 4;
    }
    
    @Override
    public void execute(final CommandContainer container, final String[] args) {
        if (container.processorRunning()) {
            container.sendChatMessage("While the Pregenerator or Deleter is running you are not allowed to start Deletion Task!");
            return;
        }
        if (args.length >= 4) {
            final int type = BasePregenCommand.getGenType(args[0]);
            final FilePos center = BasePregenCommand.getChunkPos(args[1], args[2], container.getPlayerPos());
            final int radius = BasePregenCommand.getNumber(PregenCommand.getArg(args, 3), 0);
            final int dimension = BasePregenCommand.getDimension(container, PregenCommand.getArg(args, 4));
            if (!BasePregenCommand.isDimensionValid(dimension)) {
                container.sendChatMessage("Dimension " + dimension + " is not Registered!");
                return;
            }
            final List<IDeletionTask> deletion = createTaskList(type, center, radius, dimension);
            if (deletion.isEmpty()) {
                container.sendChatMessage("No Tasks found");
                return;
            }
            container.sendChatMessage("Created " + deletion.size() + " Tasks");
            container.onProcessStarted();
            container.getDeleter().startTasks(deletion);
        }
        else {
            this.throwErrors(container, args.length);
        }
    }
    
    public static List<IDeletionTask> createTaskList(final int type, final FilePos center, final int radius, final int dimension) {
        if (radius <= 25000) {
            final List<IDeletionTask> tasks = new ArrayList<IDeletionTask>();
            tasks.add(new DeleteRadiusSubCommand.RadiusTask(type, dimension, center, radius));
            return tasks;
        }
        final int range = DoubleMath.roundToInt(radius / 1000.0, RoundingMode.UP);
        final List<FilePos> workList = new ArrayList<FilePos>();
        for (int x = -range; x < range; ++x) {
            for (int z = -range; z < range; ++z) {
                workList.add(new FilePos(x, z));
            }
        }
        workList.sort(new Sorter(center));
        final List<IDeletionTask> task = new ArrayList<IDeletionTask>();
        for (final FilePos pos : workList) {
            final int xMin = BasePregenCommand.clamp((pos.x - 1) * 1000, -radius, radius);
            final int zMin = BasePregenCommand.clamp((pos.z - 1) * 1000, -radius, radius);
            final int xMax = BasePregenCommand.clamp((pos.x + 1) * 1000, -radius, radius);
            final int zMax = BasePregenCommand.clamp((pos.z + 1) * 1000, -radius, radius);
            if (type == 0) {
                task.add(new MassDeletionSquare(dimension, new FilePos(xMin, zMin), new FilePos(xMax, zMax)));
            }
            else {
                if (type != 1) {
                    continue;
                }
                task.add(new MassDeletionCycle(dimension, new FilePos(xMin, zMin), new FilePos(xMax, zMax), center, radius));
            }
        }
        return task;
    }
    
    @Override
    public List<String> getAutoCompleteOption(final String[] args, final int argLayer, final int commandIndex) {
        if (commandIndex == 0) {
            final CompleterHelper helper = DeleteMassSubCommand.helper;
            return PregenCommand.getBestMatch(args, CompleterHelper.GEN_TYPE);
        }
        if (commandIndex == 1 || commandIndex == 2) {
            return PregenCommand.getBestMatch(args, "0", "~");
        }
        if (commandIndex == 3) {
            if (args[argLayer].startsWith("b")) {
                final CompleterHelper helper2 = DeleteMassSubCommand.helper;
                return PregenCommand.getBestMatch(args, CompleterHelper.GEN_RADIUS_BLOCK);
            }
            final CompleterHelper helper3 = DeleteMassSubCommand.helper;
            return PregenCommand.getBestMatch(args, CompleterHelper.GEN_RADIUS_CHUNK);
        }
        else {
            if (commandIndex == 4) {
                final CompleterHelper helper4 = DeleteMassSubCommand.helper;
                return PregenCommand.getBestMatch(args, CompleterHelper.DIMENSION);
            }
            if (commandIndex == 5) {
                final CompleterHelper helper5 = DeleteMassSubCommand.helper;
                return PregenCommand.getBestMatch(args, CompleterHelper.GEN_DELAY);
            }
            return new ArrayList<String>();
        }
    }
    
    public static class Sorter implements Comparator<FilePos>
    {
        FilePos pos;
        
        public Sorter(final FilePos pos) {
            this.pos = pos.toChunkFile();
        }
        
        @Override
        public int compare(final FilePos o1, final FilePos o2) {
            final int value = o1.getDistance(this.pos.x, this.pos.z) - o2.getDistance(this.pos.x, this.pos.z);
            return (value > 0) ? 1 : ((value < 0) ? -1 : 0);
        }
    }
    
    public static class MassDeletionSquare extends IDeletionTask
    {
        int dim;
        FilePos min;
        FilePos max;
        
        public MassDeletionSquare(final int dim, final FilePos min, final FilePos max) {
            this.dim = dim;
            this.min = min;
            this.max = max;
        }
        
        @Override
        public Future<DeleteProcess> createTask(final PrepaireProgress progress) {
            final long x = this.max.x - this.min.x;
            final long z = this.max.z - this.min.z;
            progress.setMax(x * z);
            final boolean loaded = DimensionManager.getWorld(this.dim) != null;
            final WorldServer world = this.getWorld(this.dim);
            final File file = world.getChunkSaveLocation();
            if (!loaded) {
                DimensionManager.unloadWorld(this.dim);
            }
            return ChunkPregenerator.SERVICE.submit((Callable<DeleteProcess>)new Callable<DeleteProcess>() {
                @Override
                public DeleteProcess call() throws Exception {
                    final DeleteProcess process = new DeleteProcess(file, ChunkCalculator.createArea(MassDeletionSquare.this.min.x, MassDeletionSquare.this.min.z, MassDeletionSquare.this.max.x, MassDeletionSquare.this.max.z, progress));
                    if (loaded) {
                        process.setChunkHost(world);
                    }
                    return process;
                }
            });
        }
    }
    
    public static class MassDeletionCycle extends IDeletionTask
    {
        int dim;
        FilePos min;
        FilePos max;
        FilePos center;
        int radius;
        
        public MassDeletionCycle(final int dim, final FilePos min, final FilePos max, final FilePos center, final int radius) {
            this.dim = dim;
            this.min = min;
            this.max = max;
            this.center = center;
            this.radius = radius;
        }
        
        @Override
        public Future<DeleteProcess> createTask(final PrepaireProgress progress) {
            progress.setMax((long)(this.radius * 2L * 3.141592653589793));
            final boolean loaded = DimensionManager.getWorld(this.dim) != null;
            final WorldServer world = this.getWorld(this.dim);
            final File file = world.getChunkSaveLocation();
            if (!loaded) {
                DimensionManager.unloadWorld(this.dim);
            }
            return ChunkPregenerator.SERVICE.submit((Callable<DeleteProcess>)new Callable<DeleteProcess>() {
                @Override
                public DeleteProcess call() throws Exception {
                    final DeleteProcess process = new DeleteProcess(file, ChunkCalculator.createCircleArea(MassDeletionCycle.this.min.x, MassDeletionCycle.this.min.z, MassDeletionCycle.this.max.x, MassDeletionCycle.this.max.z, MassDeletionCycle.this.center.x, MassDeletionCycle.this.center.z, MassDeletionCycle.this.radius, progress));
                    if (loaded) {
                        process.setChunkHost(world);
                    }
                    return process;
                }
            });
        }
    }
}
