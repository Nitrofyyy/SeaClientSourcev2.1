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

public class StartExpansionSubCommand extends BasePregenCommand
{
    public StartExpansionSubCommand() {
        super(7);
        this.addDescription(0, "Type: Which Shape the generation should have");
        this.addDescription(1, "X Center: Which Chunk the center should be in. (if 'b' infront of the number or after ~ means block position)");
        this.addDescription(2, "Z Center: Which Chunk the center should be in. (if 'b' infront of the number or after ~ means block position)");
        this.addDescription(3, "Min Radius: The Radius the Generation should start at. (if 'b' infront of the number or after ~ means block radius");
        this.addDescription(4, "Max Radius: The Radius the Generation should end at. (if 'b' infront of the number or after ~ means block radius");
        this.addDescription(5, "(Optional) Dimension: The Dimension  the Generation should happen in. (It Autoloads dimension if they are unloaded)");
        this.addDescription(6, "(Optional) Processing Rule: Which type of Generation it should use.");
        this.addSuggestion("startexpansion square 0 0 100 200", "Generates everything within a area from 100 Chunks to 200 Chunks in every direction");
        this.addSuggestion("startexpansion square 0 0 100 200 0 TerrainOnly", "Generates only Terrain within a area from 100 Chunks to 200 Chunks in every direction in the Overworld");
    }
    
    @Override
    public String getName() {
        return "startexpansion";
    }
    
    @Override
    public String getDescription() {
        return "Generates a area around a already generated area";
    }
    
    @Override
    public int getRequiredParameterCount() {
        return 5;
    }
    
    @Override
    public void execute(final CommandContainer container, final String[] args) {
        if (args.length >= 5) {
            final int type = BasePregenCommand.getGenType(args[0]);
            FilePos center = BasePregenCommand.getChunkPos(args[1], args[2], container.getPlayerPos());
            final int minRadius = BasePregenCommand.getNumber(args[3], 0);
            final int maxRadius = BasePregenCommand.getNumber(args[4], 0);
            final int dimension = BasePregenCommand.getDimension(container, PregenCommand.getArg(args, 5));
            final int postRule = BasePregenCommand.getProcessRule(PregenCommand.getArg(args, 6));
            if (!BasePregenCommand.isDimensionValid(dimension)) {
                container.sendChatMessage("Dimension " + dimension + " is not Registered!");
                return;
            }
            final long ringCount = BasePregenCommand.getRingCount(minRadius, maxRadius, type == 1);
            if (ringCount > 2500000000L) {
                container.sendChatMessage("Expansion uses more then 2.5 Billion Chunks. That is to big. Please make it smaller. (Your Expansion Amount: " + ringCount + ")");
                return;
            }
            center = BasePregenCommand.applySpawn(args[1], args[2], center, container.getWorldSpawn(dimension));
            final PregenTask task = new PregenTask(4 + type, dimension, center.x, center.z, minRadius, maxRadius, postRule);
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
            final CompleterHelper helper = StartExpansionSubCommand.helper;
            return PregenCommand.getBestMatch(args, CompleterHelper.GEN_TYPE);
        }
        if (commandIndex == 1 || commandIndex == 2) {
            return PregenCommand.getBestMatch(args, "0", "~");
        }
        if (commandIndex == 3 || commandIndex == 4) {
            if (args[argLayer].startsWith("b")) {
                final CompleterHelper helper2 = StartExpansionSubCommand.helper;
                return PregenCommand.getBestMatch(args, CompleterHelper.GEN_RADIUS_BLOCK);
            }
            final CompleterHelper helper3 = StartExpansionSubCommand.helper;
            return PregenCommand.getBestMatch(args, CompleterHelper.GEN_RADIUS_CHUNK);
        }
        else {
            if (commandIndex == 5) {
                final CompleterHelper helper4 = StartExpansionSubCommand.helper;
                return PregenCommand.getBestMatch(args, CompleterHelper.DIMENSION);
            }
            if (commandIndex == 6) {
                final CompleterHelper helper5 = StartExpansionSubCommand.helper;
                return PregenCommand.getBestMatch(args, CompleterHelper.GEN_PROCESS);
            }
            return new ArrayList<String>();
        }
    }
}
