// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.command.delete;

import java.util.ArrayList;
import pregenerator.impl.command.CompleterHelper;
import java.util.List;
import pregenerator.impl.misc.FilePos;
import net.minecraft.world.chunk.storage.RegionFileCache;
import java.io.File;
import pregenerator.impl.command.base.PregenCommand;
import pregenerator.impl.command.base.CommandContainer;
import pregenerator.impl.command.base.BasePregenCommand;

public class DeleteRegionSubCommand extends BasePregenCommand
{
    public DeleteRegionSubCommand() {
        super(3);
        this.addDescription(0, "X Position: The ChunkX Position of the File that should be deleted. (if 'b' infront of the number or after ~ means block position)");
        this.addDescription(1, "Z Position: The ChunkZ Position of the File that should be deleted. (if 'b' infront of the number or after ~ means block position)");
        this.addDescription(2, "(Optional) Dimension: The Dimension the Deletion should be happening in");
        this.addSuggestion("deleteRegion 100 200", "Deletes the Region File that is at Chunk Position X100, Z200");
    }
    
    @Override
    public String getName() {
        return "deleteRegion";
    }
    
    @Override
    public String getDescription() {
        return "Deletes a full 1024 Chunk Set out of the World Directory";
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
            if (!BasePregenCommand.isDimensionValid(dimension)) {
                container.sendChatMessage("Dimension " + dimension + " is not Registered!");
                return;
            }
            final File file = new File(container.getWorld(dimension).getChunkSaveLocation(), "region");
            if (!file.exists()) {
                container.sendChatMessage("RegionFolder doesn't exist");
                return;
            }
            final File chunk = new File(file, "r." + position.x + "." + position.z + ".mca");
            if (!chunk.exists()) {
                container.sendChatMessage("Region File: [" + position + "] doesn't exist");
                return;
            }
            try {
                RegionFileCache.func_76551_a();
                if (chunk.delete()) {
                    container.sendChatMessage("Successfully deleted Region File");
                }
                else {
                    container.sendChatMessage("Couldn't delete the Region File");
                }
            }
            catch (Exception e) {
                container.sendChatMessage("Deletion Region File Failed. Reason: " + e.getMessage());
            }
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
            final CompleterHelper helper = DeleteRegionSubCommand.helper;
            return PregenCommand.getBestMatch(args, CompleterHelper.DIMENSION);
        }
        return new ArrayList<String>();
    }
}
