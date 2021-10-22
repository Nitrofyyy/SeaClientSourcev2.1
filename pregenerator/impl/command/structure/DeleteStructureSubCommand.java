// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.command.structure;

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
import java.util.Collection;
import pregenerator.impl.command.base.PregenCommand;
import pregenerator.impl.command.CompleterHelper;
import java.util.List;
import pregenerator.impl.misc.Tuple;
import pregenerator.impl.structure.MapGenStructureDataPregen;
import pregenerator.impl.processor.deleter.IDeletionTask;
import pregenerator.impl.misc.FilePos;
import pregenerator.impl.structure.StructureManager;
import pregenerator.impl.command.base.CommandContainer;
import pregenerator.impl.command.base.BasePregenCommand;

public class DeleteStructureSubCommand extends BasePregenCommand
{
    public DeleteStructureSubCommand() {
        super(6);
        this.addDescription(0, "Dimension: The Dimension the Structure is in (if not listed its not deleteable)");
        this.addDescription(1, "Type: The Structure that should be deleted (if not listed its not deleteable)");
        this.addDescription(2, "X Block Position: The Ruff X Block Position of the Structure");
        this.addDescription(3, "Z Block Position: The Ruff Z Block Position of the Structure");
        this.addDescription(4, "(Optional) Delete: If the Structure Should be removed from the World");
        this.addSuggestion("deleteStructure 0 Village 0 0", "Deletes the closest Village to 0 0 from the Files but leaves it in the World");
        this.addSuggestion("deleteStructure 0 Village 0 0 delete", "Deletes the closest Village to 0 0 and removes it from the World");
    }
    
    @Override
    public String getName() {
        return "deleteStructure";
    }
    
    @Override
    public String getDescription() {
        return "Deletes a Structure and optionally regenerates the Chunks for Complete removal";
    }
    
    @Override
    public int getRequiredParameterCount() {
        return 4;
    }
    
    @Override
    public void execute(final CommandContainer container, final String[] args) {
        if (args.length >= 4) {
            final int dimension = BasePregenCommand.getDimension(container, args[2]);
            final String type = args[3];
            final FilePos position = BasePregenCommand.getBlockPos(args[0], args[1], container.getPlayerPos());
            final boolean doRemove = args.length >= 5 && args[4].equalsIgnoreCase("delete");
            if (!BasePregenCommand.isDimensionValid(dimension)) {
                container.sendChatMessage("Dimension " + dimension + " is not Registered!");
                return;
            }
            if (!StructureManager.instance.validateType(dimension, type)) {
                container.sendChatMessage("Type: " + type + " doesn't exist in Dimension " + dimension);
                return;
            }
            final MapGenStructureDataPregen structure = StructureManager.instance.getStructure(dimension, type);
            if (structure == null) {
                container.sendChatMessage("Structure Manager for " + type + " not Found!");
                return;
            }
            final Tuple<FilePos, FilePos> result = structure.deleteStructure(position);
            if (result != null) {
                final FilePos resultPos = result.getFirst();
                container.sendChatMessage("Deleted Structure at: [" + resultPos + "]");
                if (doRemove) {
                    final FilePos resultEnd = result.getSecond();
                    container.onProcessStarted();
                    container.getDeleter().startTask(new RemoveStructure(dimension, resultPos, resultEnd));
                    container.sendChatMessage("Structure Deletion Process starts in 5 seconds.");
                }
            }
            else {
                container.sendChatMessage("Structure not found nearby: [" + position + "]");
            }
        }
        else {
            this.throwErrors(container, args.length);
        }
    }
    
    @Override
    public List<String> getAutoCompleteOption(final String[] args, final int argLayer, final int commandIndex) {
        if (commandIndex == 0) {
            final CompleterHelper helper = DeleteStructureSubCommand.helper;
            return PregenCommand.getBestMatch(args, CompleterHelper.STRUCTURE_DIMENSION);
        }
        if (commandIndex == 1) {
            final CompleterHelper helper2 = DeleteStructureSubCommand.helper;
            return PregenCommand.getBestMatch(args, CompleterHelper.getStructures(BasePregenCommand.parseNumber(args[argLayer - 1], 0), false));
        }
        if (commandIndex == 2) {
            return PregenCommand.getBestMatch(args, StructureManager.instance.getXPositions(BasePregenCommand.parseNumber(args[argLayer - 2], 0), args[argLayer - 1]));
        }
        if (commandIndex == 3) {
            return PregenCommand.getBestMatch(args, StructureManager.instance.getZPositions(BasePregenCommand.parseNumber(args[argLayer - 3], 0), args[argLayer - 2], BasePregenCommand.parseNumber(args[argLayer - 1], 0)));
        }
        if (commandIndex == 4) {
            return PregenCommand.getBestMatch(args, "delete");
        }
        return new ArrayList<String>();
    }
    
    public static class RemoveStructure extends IDeletionTask
    {
        int dim;
        FilePos start;
        FilePos end;
        
        public RemoveStructure(final int dim, final FilePos start, final FilePos end) {
            this.dim = dim;
            this.start = start;
            this.end = end;
        }
        
        @Override
        public Future<DeleteProcess> createTask(final PrepaireProgress progress) {
            final long x = this.end.x - this.start.x;
            final long z = this.end.z - this.start.z;
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
                    final DeleteProcess process = new DeleteProcess(file, ChunkCalculator.createArea(RemoveStructure.this.start.x, RemoveStructure.this.start.z, RemoveStructure.this.end.x, RemoveStructure.this.end.z, progress));
                    if (loaded) {
                        process.setChunkHost(world);
                    }
                    return process;
                }
            });
        }
    }
}
