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

public class DeleteExpansionSubCommand extends BasePregenCommand
{
    public DeleteExpansionSubCommand() {
        super(7);
        this.addDescription(0, "Deletion Type: Which shape the Generation should have");
        this.addDescription(1, "X Center: Which Chunk the center should be in. (if 'b' infront of the number or after ~ means block position)");
        this.addDescription(2, "Z Center: Which Chunk the center should be in. (if 'b' infront of the number or after ~ means block position)");
        this.addDescription(3, "Min Radius: The Radius the Deletion should start at. (if 'b' infront of the number or after ~ means block distance)");
        this.addDescription(4, "Max Radius: The Radius the Deletion should end at. (if 'b' infront of the number or after ~ means block distance)");
        this.addDescription(5, "(Optional) Dimension: The Dimension the Deletion should be happening in");
        this.addSuggestion("deleteExpansion square 0 0 100 200 -1", "Deletes a area from 100 Chunks to 200 Chunks in the Nether");
    }
    
    @Override
    public String getName() {
        return "deleteExpansion";
    }
    
    @Override
    public String getDescription() {
        return "Deletes a Area of Unloaded Chunks around an Existing Area";
    }
    
    @Override
    public int getRequiredParameterCount() {
        return 5;
    }
    
    @Override
    public void execute(final CommandContainer container, final String[] args) {
        if (container.processorRunning()) {
            container.sendChatMessage("While the Pregenerator or Deleter is running you are not allowed to start Deletion Task!");
            return;
        }
        if (args.length >= 5) {
            final int type = BasePregenCommand.getGenType(args[0]);
            final FilePos center = BasePregenCommand.getChunkPos(args[1], args[2], container.getPlayerPos());
            final int minRadius = BasePregenCommand.getNumber(PregenCommand.getArg(args, 3), 0);
            final int maxRadius = BasePregenCommand.getNumber(PregenCommand.getArg(args, 4), 0);
            final int dimension = BasePregenCommand.getDimension(container, PregenCommand.getArg(args, 5));
            if (!BasePregenCommand.isDimensionValid(dimension)) {
                container.sendChatMessage("Dimension " + dimension + " is not Registered!");
                return;
            }
            final long ringCount = BasePregenCommand.getRingCount(minRadius, maxRadius, type == 1);
            if (ringCount > 2500000000L) {
                container.sendChatMessage("Expansion uses more then 2.5 Billion Chunks. That is to big. Please make it smaller. (Your Expansion Amount: " + ringCount + ")");
                return;
            }
            container.onProcessStarted();
            container.getDeleter().startTask(new ExpansionTask(type, dimension, center, minRadius, maxRadius));
        }
        else {
            this.throwErrors(container, args.length);
        }
    }
    
    @Override
    public List<String> getAutoCompleteOption(final String[] args, final int argLayer, final int commandIndex) {
        if (commandIndex == 0) {
            final CompleterHelper helper = DeleteExpansionSubCommand.helper;
            return PregenCommand.getBestMatch(args, CompleterHelper.GEN_TYPE);
        }
        if (commandIndex == 1 || commandIndex == 2) {
            return PregenCommand.getBestMatch(args, "0", "~");
        }
        if (commandIndex == 3 || commandIndex == 4) {
            if (args[argLayer].startsWith("b")) {
                final CompleterHelper helper2 = DeleteExpansionSubCommand.helper;
                return PregenCommand.getBestMatch(args, CompleterHelper.GEN_RADIUS_BLOCK);
            }
            final CompleterHelper helper3 = DeleteExpansionSubCommand.helper;
            return PregenCommand.getBestMatch(args, CompleterHelper.GEN_RADIUS_CHUNK);
        }
        else {
            if (commandIndex == 5) {
                final CompleterHelper helper4 = DeleteExpansionSubCommand.helper;
                return PregenCommand.getBestMatch(args, CompleterHelper.DIMENSION);
            }
            return new ArrayList<String>();
        }
    }
    
    public static class ExpansionTask extends IDeletionTask
    {
        int type;
        int dim;
        FilePos center;
        int minRadius;
        int maxRadius;
        
        public ExpansionTask(final int type, final int dim, final FilePos center, final int minRadius, final int maxRadius) {
            this.type = type;
            this.dim = dim;
            this.center = center;
            this.minRadius = minRadius;
            this.maxRadius = maxRadius;
        }
        
        @Override
        public Future<DeleteProcess> createTask(final PrepaireProgress progress) {
            final long max = this.maxRadius * 2;
            final long min = this.minRadius * 2;
            progress.setMax(max * max - min * min);
            final boolean loaded = DimensionManager.getWorld(this.dim) != null;
            final WorldServer world = this.getWorld(this.dim);
            final File file = world.getChunkSaveLocation();
            if (!loaded) {
                DimensionManager.unloadWorld(this.dim);
            }
            return ChunkPregenerator.SERVICE.submit((Callable<DeleteProcess>)new Callable<DeleteProcess>() {
                @Override
                public DeleteProcess call() throws Exception {
                    final DeleteProcess process = new DeleteProcess(file, (ExpansionTask.this.type == 1) ? ChunkCalculator.createCircleExt(ExpansionTask.this.center.x, ExpansionTask.this.center.z, ExpansionTask.this.minRadius, ExpansionTask.this.maxRadius, progress) : ChunkCalculator.createSquareExt(ExpansionTask.this.center.x, ExpansionTask.this.center.z, ExpansionTask.this.minRadius, ExpansionTask.this.maxRadius, progress));
                    if (loaded) {
                        process.setChunkHost(world);
                    }
                    return process;
                }
            });
        }
    }
}
