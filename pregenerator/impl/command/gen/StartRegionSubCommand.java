// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.command.gen;

import java.util.ArrayList;
import pregenerator.impl.command.CompleterHelper;
import java.util.List;
import pregenerator.impl.misc.FilePos;
import pregenerator.impl.storage.PregenTask;
import pregenerator.impl.command.base.PregenCommand;
import pregenerator.impl.command.base.CommandContainer;
import pregenerator.impl.command.base.BasePregenCommand;

public class StartRegionSubCommand extends BasePregenCommand
{
    public StartRegionSubCommand() {
        super(4);
        this.addDescription(0, "Region File X: The X Position in Chunks of the Region File (if 'b' infront of the number or after ~ means block position)");
        this.addDescription(1, "Region File Z: The Z Position in Chunks of the Region File (if 'b' infront of the number or after ~ means block position)");
        this.addDescription(2, "(Optional) Dimension: The Dimension the Generation should happen in. (It Autoloads dimension if they are unloaded)");
        this.addDescription(3, "(Optional) Processing Rule: Which type of Generation it should use.");
        this.addSuggestion("startregion 20 20", "Generates everything at the Region File 0 0 at the dimension the sender is in");
    }
    
    @Override
    public String getName() {
        return "startregion";
    }
    
    @Override
    public String getDescription() {
        return "Generates a full RegionFile";
    }
    
    @Override
    public int getRequiredParameterCount() {
        return 2;
    }
    
    @Override
    public void execute(final CommandContainer container, final String[] args) {
        if (args.length >= 2) {
            final FilePos center = BasePregenCommand.getChunkPos(args[0], args[1], container.getPlayerPos()).toChunkFile();
            final int dimension = BasePregenCommand.getDimension(container, PregenCommand.getArg(args, 2));
            final int postRule = BasePregenCommand.getProcessRule(PregenCommand.getArg(args, 3));
            if (!BasePregenCommand.isDimensionValid(dimension)) {
                container.sendChatMessage("Dimension " + dimension + " is not Registered!");
                return;
            }
            final PregenTask task = new PregenTask(2, dimension, center.x * 32, center.z * 32, center.x * 32 + 32, center.z * 32 + 32, postRule);
            if (container.onProcessStarted(task)) {
                container.sendChatMessage("Pregenerator already running. Adding Task to the TaskStorage");
                return;
            }
            container.getProcessor().startTask(task);
        }
        else {
            this.throwErrors(container, args.length);
        }
    }
    
    @Override
    public List<String> getAutoCompleteOption(final String[] args, final int argLayer, final int commandIndex) {
        if (commandIndex == 0 || commandIndex == 1) {
            return PregenCommand.getBestMatch(args, "0", "~", "b0");
        }
        if (commandIndex == 2) {
            final CompleterHelper helper = StartRegionSubCommand.helper;
            return PregenCommand.getBestMatch(args, CompleterHelper.DIMENSION);
        }
        if (commandIndex == 3) {
            final CompleterHelper helper2 = StartRegionSubCommand.helper;
            return PregenCommand.getBestMatch(args, CompleterHelper.GEN_PROCESS);
        }
        return new ArrayList<String>();
    }
}
