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

public class StartAreaSubCommand extends BasePregenCommand
{
    public StartAreaSubCommand() {
        super(7);
        this.addDescription(0, "Type: Which Shape the generation should have");
        this.addDescription(1, "StartX: The Chunk X Position where the generator should start at. (if 'b' infront of the number or after ~ means block position)");
        this.addDescription(2, "StartZ: The Chunk Z Position where the generator should start at. (if 'b' infront of the number or after ~ means block position)");
        this.addDescription(3, "EndX: The Chunk X Position where the generator should end at. (if 'b' infront of the number or after ~ means block radius");
        this.addDescription(4, "EndZ: The Chunk Z Position where the generator should end at. (if 'b' infront of the number or after ~ means block radius");
        this.addDescription(5, "(Optional) Dimension: The Dimension the Generation should happen in. (It Autoloads dimension if they are unloaded)");
        this.addDescription(6, "(Optional) Processing Rule: Which type of Generation it should use.");
        this.addSuggestion("startarea -b1000 -b1000 b1000 b1000", "Generates 1000 Block radius from the Center of the world");
        this.addSuggestion("startarea ~-100 ~-100 ~100 ~100 -1 TerrainOnly", "Generates a 100 Chunk Radius with only Terrain around the Players Position in the Nether");
    }
    
    @Override
    public String getName() {
        return "startarea";
    }
    
    @Override
    public String getDescription() {
        return "Generates a Plane of Chunks";
    }
    
    @Override
    public int getRequiredParameterCount() {
        return 5;
    }
    
    @Override
    public void execute(final CommandContainer container, final String[] args) {
        if (args.length >= 5) {
            final int type = BasePregenCommand.getGenType(args[0]);
            final FilePos minPos = BasePregenCommand.getChunkPos(args[1], args[2], container.getPlayerPos());
            final FilePos maxPos = BasePregenCommand.getChunkPos(args[3], args[4], container.getPlayerPos());
            final int dimension = BasePregenCommand.getDimension(container, PregenCommand.getArg(args, 5));
            final int postRule = BasePregenCommand.getProcessRule(PregenCommand.getArg(args, 6));
            if (!BasePregenCommand.isDimensionValid(dimension)) {
                container.sendChatMessage("Dimension " + dimension + " is not Registered!");
                return;
            }
            final long count = BasePregenCommand.getFullCount(minPos.x, minPos.z, maxPos.x, maxPos.z);
            if (count > 2500000000L) {
                container.sendChatMessage("Area Generation uses more then 2.5 Billion Chunks. That is to big. Please make it smaller. (Your Area Amount: " + count + ")");
                return;
            }
            final PregenTask task = new PregenTask(2, dimension, minPos.x, minPos.z, maxPos.x, maxPos.z, postRule);
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
        if (commandIndex == 0) {
            final CompleterHelper helper = StartAreaSubCommand.helper;
            return PregenCommand.getBestMatch(args, CompleterHelper.GEN_TYPE);
        }
        if (commandIndex >= 1 && commandIndex <= 4) {
            return PregenCommand.getBestMatch(args, "0", "~", "b0");
        }
        if (commandIndex == 5) {
            final CompleterHelper helper2 = StartAreaSubCommand.helper;
            return PregenCommand.getBestMatch(args, CompleterHelper.DIMENSION);
        }
        if (commandIndex == 6) {
            final CompleterHelper helper3 = StartAreaSubCommand.helper;
            return PregenCommand.getBestMatch(args, CompleterHelper.GEN_PROCESS);
        }
        return new ArrayList<String>();
    }
}
