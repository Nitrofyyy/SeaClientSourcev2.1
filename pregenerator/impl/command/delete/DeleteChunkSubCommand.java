// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.command.delete;

import java.util.BitSet;
import net.minecraft.world.WorldServer;
import java.io.File;
import java.util.concurrent.Callable;
import pregenerator.ChunkPregenerator;
import net.minecraftforge.common.DimensionManager;
import pregenerator.impl.processor.deleter.DeleteProcess;
import java.util.concurrent.Future;
import pregenerator.impl.processor.PrepaireProgress;
import java.util.LinkedHashSet;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.Map;
import java.util.ArrayList;
import pregenerator.impl.command.CompleterHelper;
import java.util.List;
import pregenerator.impl.misc.FilePos;
import pregenerator.impl.processor.deleter.IDeletionTask;
import pregenerator.impl.command.base.PregenCommand;
import pregenerator.impl.command.base.CommandContainer;
import pregenerator.impl.command.base.BasePregenCommand;

public class DeleteChunkSubCommand extends BasePregenCommand
{
    public DeleteChunkSubCommand() {
        super(4);
        this.addDescription(0, "X Position: The X Chunk Position of the Deletion. (if 'b' infront of the number or after ~ means block position)");
        this.addDescription(1, "Z Position: The Z Chunk Position of the Deletion. (if 'b' infront of the number or after ~ means block position)");
        this.addDescription(2, "(Optional) Dimension: The Dimension the Deletion should be happening in");
        this.addDescription(3, "(Optional) Delay: How long the Deleter should wait until it should start (in ticks)");
        this.addSuggestion("deleteChunk 100 200", "Deletes a Unloaded Chunk at X100 Z200 at the Dimension that the sender is at");
    }
    
    @Override
    public String getName() {
        return "deleteChunk";
    }
    
    @Override
    public String getDescription() {
        return "Deletes a Single Unloaded Chunk";
    }
    
    @Override
    public int getRequiredParameterCount() {
        return 2;
    }
    
    @Override
    public void execute(final CommandContainer container, final String[] args) {
        if (args.length >= 2) {
            final FilePos position = BasePregenCommand.getChunkPos(args[0], args[1], container.getPlayerPos());
            final int dimension = BasePregenCommand.getDimension(container, PregenCommand.getArg(args, 2));
            final int delay = BasePregenCommand.parseNumber(PregenCommand.getArg(args, 3), 0);
            if (!BasePregenCommand.isDimensionValid(dimension)) {
                container.sendChatMessage("Dimension " + dimension + " is not Registered!");
                return;
            }
            container.onProcessStarted();
            container.getDeleter().startTask(new SingleDeletion(dimension, position, delay));
        }
        else {
            this.throwErrors(container, args.length);
        }
    }
    
    @Override
    public List<String> getAutoCompleteOption(final String[] args, final int argLayer, final int commandIndex) {
        if (commandIndex == 0 || commandIndex == 1) {
            return PregenCommand.getBestMatch(args, "0", "~");
        }
        if (commandIndex == 2) {
            final CompleterHelper helper = DeleteChunkSubCommand.helper;
            return PregenCommand.getBestMatch(args, CompleterHelper.DIMENSION);
        }
        if (commandIndex == 3) {
            final CompleterHelper helper2 = DeleteChunkSubCommand.helper;
            return PregenCommand.getBestMatch(args, CompleterHelper.GEN_DELAY);
        }
        return new ArrayList<String>();
    }
    
    private Map<FilePos, Set<FilePos>> createTask(final FilePos pos) {
        final Map<FilePos, Set<FilePos>> map = new LinkedHashMap<FilePos, Set<FilePos>>();
        final LinkedHashSet<FilePos> file = new LinkedHashSet<FilePos>();
        file.add(pos);
        map.put(pos.toChunkFile(), file);
        return map;
    }
    
    public static class SingleDeletion extends IDeletionTask
    {
        int dim;
        FilePos position;
        
        public SingleDeletion(final int dimension, final FilePos pos, final int delay) {
            this.dim = dimension;
            this.position = pos;
        }
        
        @Override
        public Future<DeleteProcess> createTask(final PrepaireProgress progress) {
            progress.setMax(1L);
            final boolean loaded = DimensionManager.getWorld(this.dim) != null;
            final WorldServer world = this.getWorld(this.dim);
            final File file = world.getChunkSaveLocation();
            if (!loaded) {
                DimensionManager.unloadWorld(this.dim);
            }
            return ChunkPregenerator.SERVICE.submit((Callable<DeleteProcess>)new Callable<DeleteProcess>() {
                @Override
                public DeleteProcess call() throws Exception {
                    final DeleteProcess process = new DeleteProcess(file, SingleDeletion.this.createTask(SingleDeletion.this.position));
                    progress.growValue(1);
                    if (loaded) {
                        process.setChunkHost(world);
                    }
                    return process;
                }
            });
        }
        
        private Map<Long, BitSet> createTask(final FilePos pos) {
            final Map<Long, BitSet> map = new LinkedHashMap<Long, BitSet>();
            final BitSet set = new BitSet();
            set.set((pos.z & 0x1F) * 32 + (pos.x & 0x1F));
            map.put(pos.toChunkFile().asLong(), set);
            return map;
        }
    }
}
