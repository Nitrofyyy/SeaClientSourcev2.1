// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.command.structure;

import java.util.ArrayList;
import pregenerator.impl.command.base.PregenCommand;
import pregenerator.impl.command.CompleterHelper;
import java.util.Iterator;
import pregenerator.impl.misc.FilePos;
import pregenerator.impl.misc.BoundingBox;
import java.util.List;
import java.util.Map;
import pregenerator.impl.structure.StructureManager;
import pregenerator.impl.command.base.CommandContainer;
import pregenerator.impl.command.base.BasePregenCommand;

public class ListSaveZonesSubCommand extends BasePregenCommand
{
    public ListSaveZonesSubCommand() {
        super(2);
        this.addDescription(0, "Dimension: The Dimension that needs to be checkt");
        this.addDescription(1, "Type: The Structure that should be checkt for");
        this.addSuggestion("ListSaveZones 0 Village", "Lists all the Villages in the Overworld");
        this.addSuggestion("ListSaveZones -1 Fortress", "Lists all the Fortresses in the Nether");
    }
    
    @Override
    public String getName() {
        return "ListSaveZones";
    }
    
    @Override
    public String getDescription() {
        return "Lists all the SaveZones that exist";
    }
    
    @Override
    public int getRequiredParameterCount() {
        return 2;
    }
    
    @Override
    public void execute(final CommandContainer container, final String[] args) {
        if (args.length >= 2) {
            final int dimension = BasePregenCommand.getDimension(container, args[0]);
            final String type = args[1];
            if (!BasePregenCommand.isDimensionValid(dimension)) {
                container.sendChatMessage("Dimension " + dimension + " is not Registered!");
                return;
            }
            if (type.equalsIgnoreCase("All")) {
                container.sendChatMessage("All SaveZones for Dimension " + dimension);
                for (final Map.Entry<String, List<BoundingBox>> boxes : StructureManager.instance.getBoxes(dimension).entrySet()) {
                    container.sendChatMessage("Structure: " + boxes.getKey());
                    for (final BoundingBox box : boxes.getValue()) {
                        container.sendChatMessage("Box At: [X: " + new FilePos(box.getCenterX(), box.getCenterZ()) + "], with a Chunk Radius of: " + box.getRadius() + " Chunks");
                    }
                }
            }
            else {
                if (!StructureManager.instance.validateType(dimension, type)) {
                    container.sendChatMessage("Type: " + type + " doesn't exist in Dimension " + dimension);
                    return;
                }
                container.sendChatMessage("Structure " + type + " for Dimension " + dimension);
                for (final BoundingBox box2 : StructureManager.instance.getBoxes(dimension, type)) {
                    container.sendChatMessage("Box At: [X: " + new FilePos(box2.getCenterX(), box2.getCenterZ()) + "], with a Chunk Radius of: " + box2.getRadius() + " Chunks");
                }
            }
        }
        else {
            this.throwErrors(container, args.length);
        }
    }
    
    @Override
    public List<String> getAutoCompleteOption(final String[] args, final int argLayer, final int commandIndex) {
        if (commandIndex == 0) {
            final CompleterHelper helper = ListSaveZonesSubCommand.helper;
            return PregenCommand.getBestMatch(args, CompleterHelper.STRUCTURE_DIMENSION);
        }
        if (commandIndex == 1) {
            final CompleterHelper helper2 = ListSaveZonesSubCommand.helper;
            return PregenCommand.getBestMatch(args, CompleterHelper.getStructures(BasePregenCommand.parseNumber(args[argLayer - 1], 0), true));
        }
        return new ArrayList<String>();
    }
}
