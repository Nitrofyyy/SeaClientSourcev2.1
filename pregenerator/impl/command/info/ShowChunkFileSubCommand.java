// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.command.info;

import java.util.ArrayList;
import pregenerator.impl.command.CompleterHelper;
import java.util.List;
import pregenerator.impl.misc.FilePos;
import pregenerator.impl.misc.RegionFileHelper;
import java.io.File;
import pregenerator.impl.command.base.PregenCommand;
import pregenerator.impl.command.base.CommandContainer;
import pregenerator.impl.command.base.BasePregenCommand;

public class ShowChunkFileSubCommand extends BasePregenCommand
{
    public ShowChunkFileSubCommand() {
        super(3);
        this.addDescription(0, "Chunk X: The X Position of the Chunk that the file you want to check with. (if 'b' infront of the number or after ~ means block position)");
        this.addDescription(1, "Chunk Z: The Z Position of the Chunk that the file you want to check with. (if 'b' infront of the number or after ~ means block position)");
        this.addDescription(2, "(Optional) Dimension: The Dimension the Generation should be in (Auto Loads Dimensions)");
        this.addSuggestion("ShowChunkFile 0 0 1", "Shows the Region File 0.0.mca in the End");
    }
    
    @Override
    public String getName() {
        return "ShowChunkFile";
    }
    
    @Override
    public String getDescription() {
        return "Shows a Detailed info about the ChunkSaveFile";
    }
    
    @Override
    public int getRequiredParameterCount() {
        return 2;
    }
    
    @Override
    public void execute(final CommandContainer container, final String[] args) {
        if (args.length >= 2) {
            final FilePos position = BasePregenCommand.getChunkPos(args[0], args[1], container.getPlayerPos()).toChunkFile();
            final int dimension = BasePregenCommand.getDimension(container, PregenCommand.getArg(args, 2));
            if (!BasePregenCommand.isDimensionValid(dimension)) {
                container.sendChatMessage("Dimension " + dimension + " is not Registered!");
                return;
            }
            final File file = new File(container.getWorld(dimension).getChunkSaveLocation(), "region");
            if (!file.exists()) {
                container.sendChatMessage("Region Folder for Dimension " + dimension + " doesn't exist");
                return;
            }
            final File regionFile = new File(file, "r." + position.x + "." + position.z + ".mca");
            if (!regionFile.exists()) {
                container.sendChatMessage("Region File [" + position + "] doesn't Exist");
                return;
            }
            final RegionFileHelper helper = new RegionFileHelper(regionFile);
            try {
                helper.close();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            final long length = regionFile.length();
            final long kb = length / 1024L;
            final long mb = kb / 1024L;
            container.sendChatMessage("RegionFile [" + position + "] contains " + helper.getInstalledChunks().size() + " / 1024 Chunks and is " + mb + "MB (" + kb + "KB) big");
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
            final CompleterHelper helper = ShowChunkFileSubCommand.helper;
            return PregenCommand.getBestMatch(args, CompleterHelper.DIMENSION);
        }
        return new ArrayList<String>();
    }
}
