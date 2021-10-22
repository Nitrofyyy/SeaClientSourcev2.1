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

public class StartRadiusSubCommand extends BasePregenCommand
{
    public StartRadiusSubCommand() {
        super(6);
        this.addDescription(0, "Generation Type: Which shape the Generation should have");
        this.addDescription(1, "X Center: Which Chunk the center should be in. (if 'b' infront of the number or after ~ means block position)");
        this.addDescription(2, "Z Center: Which Chunk the center should be in. (if 'b' infront of the number or after ~ means block position)");
        this.addDescription(3, "Radius: How big the Radius in Chunks should be. (if 'b' infront of the number or after ~ means block distance)");
        this.addDescription(4, "(Optional) Dimension: The Dimension the Generation should be in (Auto Loads Dimensions)");
        this.addDescription(5, "(Optional) Processing Rule: Which type of Generation it should use.");
        this.addSuggestion("startradius square 0 0 100", "Generates everything within 100 Chunks radius in the Senders Dimension (Server = Overworld, Player = The Dimension the Player is in)");
        this.addSuggestion("startradius square 0 0 50 -1 TerrainOnly", "Generates only Terrain within 50 Chunks radius, in the Nether");
        this.addSuggestion("startradius square 0 0 b1000", "Generates everything within 1000 Blocks radius in the Senders Dimension (Server = Overworld, Player = The Dimension the Player is in)");
        this.addSuggestion("startradius square 0 0 100 0 PostProcessingOnly", "Populates everything within 100 Chunks radius without generating new Chunks in the Overworld");
        this.addSuggestion("startradius square ~ ~ 100", "Generates a 100 Chunk Radius around the Players Position");
        this.addSuggestion("startradius square ~-100 ~ 100", "Generates a 100 Chunk Radius -100 Chunks X Away from the player");
        this.addSuggestion("startradius square s s 100", "Generates a 100 Chunk Radius around the Spawn");
        this.addSuggestion("startradius cicle 0 0 100", "Generates a Circle of a 100 Chunk Radius");
    }
    
    @Override
    public String getName() {
        return "startradius";
    }
    
    @Override
    public String getDescription() {
        return "Generates a Square or Circle Radius";
    }
    
    @Override
    public int getRequiredParameterCount() {
        return 4;
    }
    
    @Override
    public void execute(final CommandContainer container, final String[] args) {
        if (args.length >= 4) {
            final int type = BasePregenCommand.getGenType(args[0]);
            FilePos center = BasePregenCommand.getChunkPos(args[1], args[2], container.getPlayerPos());
            final int radius = BasePregenCommand.getNumber(args[3], 0);
            final int dimension = BasePregenCommand.getDimension(container, PregenCommand.getArg(args, 4));
            final int postRule = BasePregenCommand.getProcessRule(PregenCommand.getArg(args, 5));
            if (!BasePregenCommand.isDimensionValid(dimension)) {
                container.sendChatMessage("Dimension " + dimension + " is not Registered!");
                return;
            }
            if (radius > 25000) {
                container.sendChatMessage("Radius " + radius + " Chunks is to big. Say below 1000 Chunks (16.000 Blocks each direction) or use startmassradius");
                return;
            }
            center = BasePregenCommand.applySpawn(args[1], args[2], center, container.getWorldSpawn(dimension));
            final PregenTask task = new PregenTask(type, dimension, center.x, center.z, radius, 0, postRule);
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
            final CompleterHelper helper = StartRadiusSubCommand.helper;
            return PregenCommand.getBestMatch(args, CompleterHelper.GEN_TYPE);
        }
        if (commandIndex == 1 || commandIndex == 2) {
            return PregenCommand.getBestMatch(args, "0", "~");
        }
        if (commandIndex == 3) {
            if (args[argLayer].startsWith("b")) {
                final CompleterHelper helper2 = StartRadiusSubCommand.helper;
                return PregenCommand.getBestMatch(args, CompleterHelper.GEN_RADIUS_BLOCK);
            }
            final CompleterHelper helper3 = StartRadiusSubCommand.helper;
            return PregenCommand.getBestMatch(args, CompleterHelper.GEN_RADIUS_CHUNK);
        }
        else {
            if (commandIndex == 4) {
                final CompleterHelper helper4 = StartRadiusSubCommand.helper;
                return PregenCommand.getBestMatch(args, CompleterHelper.DIMENSION);
            }
            if (commandIndex == 5) {
                final CompleterHelper helper5 = StartRadiusSubCommand.helper;
                return PregenCommand.getBestMatch(args, CompleterHelper.GEN_PROCESS);
            }
            return new ArrayList<String>();
        }
    }
}
