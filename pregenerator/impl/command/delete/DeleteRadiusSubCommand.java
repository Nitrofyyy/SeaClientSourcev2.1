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
import java.util.ArrayList;
import pregenerator.impl.command.CompleterHelper;
import java.util.List;
import pregenerator.impl.misc.FilePos;
import pregenerator.impl.processor.deleter.IDeletionTask;
import pregenerator.impl.command.base.PregenCommand;
import pregenerator.impl.command.base.CommandContainer;
import pregenerator.impl.command.base.BasePregenCommand;

public class DeleteRadiusSubCommand extends BasePregenCommand
{
    public DeleteRadiusSubCommand() {
        super(6);
        this.addDescription(0, "Deletion Type: Which shape the Generation should have");
        this.addDescription(1, "X Center: Which Chunk the center should be in. (if 'b' infront of the number or after ~ means block position)");
        this.addDescription(2, "Z Center: Which Chunk the center should be in. (if 'b' infront of the number or after ~ means block position)");
        this.addDescription(3, "Radius: How big the Radius in Chunks should be. (if 'b' infront of the number or after ~ means block distance)");
        this.addDescription(4, "(Optional) Dimension: The Dimension the Deletion should be happening in");
        this.addDescription(5, "(Optional) Delay: How long the Deleter should wait until it should start (in ticks)");
        this.addSuggestion("deleteRadius square 0 0 100 -1 200", "Deletes a 100 Chunk Area around the World Center in the nether and it waits 10 seconds before starting");
    }
    
    @Override
    public String getName() {
        return "deleteRadius";
    }
    
    @Override
    public String getDescription() {
        return "Deletes a Radius of Unloaded Chunks";
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
            if (radius > 25000) {
                container.sendChatMessage("A Radius over 25000 is to big to Delete at once. If you want to delete more use the dimension delete command");
                return;
            }
            container.onProcessStarted();
            container.getDeleter().startTask(new RadiusTask(type, dimension, center, radius));
        }
        else {
            this.throwErrors(container, args.length);
        }
    }
    
    @Override
    public List<String> getAutoCompleteOption(final String[] args, final int argLayer, final int commandIndex) {
        if (commandIndex == 0) {
            final CompleterHelper helper = DeleteRadiusSubCommand.helper;
            return PregenCommand.getBestMatch(args, CompleterHelper.GEN_TYPE);
        }
        if (commandIndex == 1 || commandIndex == 2) {
            return PregenCommand.getBestMatch(args, "0", "~");
        }
        if (commandIndex == 3) {
            if (args[argLayer].startsWith("b")) {
                final CompleterHelper helper2 = DeleteRadiusSubCommand.helper;
                return PregenCommand.getBestMatch(args, CompleterHelper.GEN_RADIUS_BLOCK);
            }
            final CompleterHelper helper3 = DeleteRadiusSubCommand.helper;
            return PregenCommand.getBestMatch(args, CompleterHelper.GEN_RADIUS_CHUNK);
        }
        else {
            if (commandIndex == 4) {
                final CompleterHelper helper4 = DeleteRadiusSubCommand.helper;
                return PregenCommand.getBestMatch(args, CompleterHelper.DIMENSION);
            }
            if (commandIndex == 5) {
                final CompleterHelper helper5 = DeleteRadiusSubCommand.helper;
                return PregenCommand.getBestMatch(args, CompleterHelper.GEN_DELAY);
            }
            return new ArrayList<String>();
        }
    }
    
    public static class RadiusTask extends IDeletionTask
    {
        int type;
        int dim;
        FilePos center;
        int radius;
        
        public RadiusTask(final int type, final int dim, final FilePos center, final int radius) {
            this.type = type;
            this.dim = dim;
            this.center = center;
            this.radius = radius;
        }
        
        @Override
        public Future<DeleteProcess> createTask(final PrepaireProgress progress) {
            if (this.type == 1) {
                progress.setMax((long)(this.radius * 2L * 3.141592653589793));
            }
            else {
                final long dia = this.radius * 2;
                progress.setMax(dia * dia);
            }
            final boolean loaded = DimensionManager.getWorld(this.dim) != null;
            final WorldServer world = this.getWorld(this.dim);
            final File file = world.getChunkSaveLocation();
            if (!loaded) {
                DimensionManager.unloadWorld(this.dim);
            }
            return ChunkPregenerator.SERVICE.submit((Callable<DeleteProcess>)new Callable<DeleteProcess>() {
                @Override
                public DeleteProcess call() throws Exception {
                    final DeleteProcess process = new DeleteProcess(file, (RadiusTask.this.type == 1) ? ChunkCalculator.createCircle(RadiusTask.this.center.x, RadiusTask.this.center.z, RadiusTask.this.radius, progress) : ChunkCalculator.createSquare(RadiusTask.this.center.x, RadiusTask.this.center.z, RadiusTask.this.radius, progress));
                    if (loaded) {
                        process.setChunkHost(world);
                    }
                    return process;
                }
            });
        }
    }
}
