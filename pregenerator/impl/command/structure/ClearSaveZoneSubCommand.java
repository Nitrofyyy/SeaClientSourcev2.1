// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.command.structure;

import java.util.ArrayList;
import pregenerator.impl.command.CompleterHelper;
import java.util.List;
import pregenerator.impl.structure.StructureManager;
import pregenerator.impl.command.base.PregenCommand;
import pregenerator.impl.command.base.CommandContainer;
import pregenerator.impl.command.base.BasePregenCommand;

public class ClearSaveZoneSubCommand extends BasePregenCommand
{
    public ClearSaveZoneSubCommand() {
        super(3);
        this.addDescription(0, "Dimension: Which Dimension the SaveZone is at");
        this.addDescription(1, "Type: Which Structure SaveZone should be effected");
        this.addDescription(2, "(Optional) Index: Which index should be deleted");
        this.addSuggestion("ClearSaveZone 0 Village last", "Deletes the last SaveZone for Villages in the Overworld");
        this.addSuggestion("ClearSaveZone 0 Village first", "Deletes the first SaveZone for Villages in the Overworld");
        this.addSuggestion("ClearSaveZone 0 Village 5", "Deletes the 5th SaveZone for Villages in the Overworld");
    }
    
    @Override
    public String getName() {
        return "ClearSaveZone";
    }
    
    @Override
    public String getDescription() {
        return "Clears a Created SaveZone";
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
            String index = PregenCommand.getArg(args, 2);
            if (index == null) {
                index = "All";
            }
            if (!BasePregenCommand.isDimensionValid(dimension)) {
                container.sendChatMessage("Dimension " + dimension + " is not Registered!");
                return;
            }
            if (!StructureManager.instance.validateType(dimension, type)) {
                container.sendChatMessage("Type: " + type + " doesn't exist in Dimension " + dimension);
                return;
            }
            if (index.equalsIgnoreCase("All")) {
                StructureManager.instance.clearAllZones(dimension, type);
                container.sendChatMessage("Cleared All SaveZones in Dimension " + dimension);
            }
            else if (index.equalsIgnoreCase("first")) {
                if (StructureManager.instance.clearZoneAt(0, dimension, type)) {
                    container.sendChatMessage("Cleared First " + type + " SaveZone in Dimension " + dimension);
                }
                else {
                    container.sendChatMessage("No SaveZones found");
                }
            }
            else if (index.equalsIgnoreCase("last")) {
                if (StructureManager.instance.clearZoneLast(dimension, type)) {
                    container.sendChatMessage("Cleared Last " + type + " SaveZone in Dimension " + dimension);
                }
                else {
                    container.sendChatMessage("No SaveZones found");
                }
            }
            else {
                final int number = BasePregenCommand.parseNumber(index, 0);
                if (StructureManager.instance.clearZoneAt(number, dimension, type)) {
                    container.sendChatMessage("Cleared " + number + " " + type + " SaveZone in Dimension " + dimension);
                }
                else {
                    container.sendChatMessage("No SaveZones found");
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
            final CompleterHelper helper = ClearSaveZoneSubCommand.helper;
            return PregenCommand.getBestMatch(args, CompleterHelper.DIMENSION);
        }
        if (commandIndex == 1) {
            final CompleterHelper helper2 = ClearSaveZoneSubCommand.helper;
            return PregenCommand.getBestMatch(args, CompleterHelper.getStructures(BasePregenCommand.parseNumber(args[argLayer - 1], 0), false));
        }
        if (commandIndex == 2) {
            return PregenCommand.getBestMatch(args, "first", "last", "all", "0", "1", "2", "3");
        }
        return new ArrayList<String>();
    }
}
