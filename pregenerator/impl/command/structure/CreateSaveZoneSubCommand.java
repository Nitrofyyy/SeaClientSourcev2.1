// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.command.structure;

import java.util.ArrayList;
import pregenerator.impl.command.base.PregenCommand;
import pregenerator.impl.command.CompleterHelper;
import java.util.List;
import pregenerator.impl.misc.FilePos;
import pregenerator.impl.structure.StructureManager;
import pregenerator.impl.command.base.CommandContainer;
import pregenerator.impl.command.base.BasePregenCommand;

public class CreateSaveZoneSubCommand extends BasePregenCommand
{
    public CreateSaveZoneSubCommand() {
        super(5);
        this.addDescription(0, "Dimension: The Dimension the Savezone should be in");
        this.addDescription(1, "Type: Which Structures the SaveZone should effect");
        this.addDescription(2, "X Center: The Center X (in Chunks) of the SaveZone. (if 'b' infront of the number or after ~ means block position)");
        this.addDescription(3, "Z Center: The Center Z (in Chunks) of the SaveZone. (if 'b' infront of the number or after ~ means block position)");
        this.addDescription(4, "Radius: The Radius in Chunks of the SaveZone (if 'b' infront of the number means BlockDistance)");
        this.addSuggestion("createSaveZone 0 Village 0 0 100", "Creates a SaveZone where villages can't spawn within 100 Chunks of the Center of the world");
    }
    
    @Override
    public String getName() {
        return "createSaveZone";
    }
    
    @Override
    public String getDescription() {
        return "Creates a plane that prevents selected structures not to generate";
    }
    
    @Override
    public int getRequiredParameterCount() {
        return 5;
    }
    
    @Override
    public void execute(final CommandContainer container, final String[] args) {
        if (args.length >= 5) {
            final int dimension = BasePregenCommand.getDimension(container, args[0]);
            final String type = args[1];
            final FilePos position = BasePregenCommand.getChunkPos(args[2], args[3], container.getPlayerPos());
            final int radius = BasePregenCommand.getNumber(args[4], 0);
            if (!BasePregenCommand.isDimensionValid(dimension)) {
                container.sendChatMessage("Dimension " + dimension + " is not Registered!");
                return;
            }
            if (!StructureManager.instance.validateType(dimension, type)) {
                container.sendChatMessage("Type: " + type + " doesn't exist in Dimension " + dimension);
                return;
            }
            StructureManager.instance.createSaveZone(position.x, position.z, radius, type, dimension);
            container.sendChatMessage("Created " + type + " SaveZone At: [" + position + "], Dimension: " + dimension + " with a Radius of " + radius + " Chunks");
        }
        else {
            this.throwErrors(container, args.length);
        }
    }
    
    @Override
    public List<String> getAutoCompleteOption(final String[] args, final int argLayer, final int commandIndex) {
        if (commandIndex == 0) {
            final CompleterHelper helper = CreateSaveZoneSubCommand.helper;
            return PregenCommand.getBestMatch(args, CompleterHelper.STRUCTURE_DIMENSION);
        }
        if (commandIndex == 1) {
            final CompleterHelper helper2 = CreateSaveZoneSubCommand.helper;
            return PregenCommand.getBestMatch(args, CompleterHelper.getStructures(BasePregenCommand.parseNumber(args[argLayer - 1], 0), true));
        }
        if (commandIndex == 2 || commandIndex == 3) {
            return PregenCommand.getBestMatch(args, "0", "~");
        }
        if (commandIndex != 4) {
            return new ArrayList<String>();
        }
        if (args[argLayer].startsWith("b")) {
            final CompleterHelper helper3 = CreateSaveZoneSubCommand.helper;
            return PregenCommand.getBestMatch(args, CompleterHelper.GEN_RADIUS_BLOCK);
        }
        final CompleterHelper helper4 = CreateSaveZoneSubCommand.helper;
        return PregenCommand.getBestMatch(args, CompleterHelper.GEN_RADIUS_CHUNK);
    }
}
