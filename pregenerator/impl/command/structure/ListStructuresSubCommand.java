// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.command.structure;

import java.util.ArrayList;
import pregenerator.impl.command.CompleterHelper;
import java.util.List;
import java.util.Iterator;
import pregenerator.impl.structure.MapGenStructureDataPregen;
import pregenerator.impl.misc.FilePos;
import net.minecraft.world.gen.structure.StructureStart;
import pregenerator.impl.structure.StructureManager;
import pregenerator.impl.command.base.PregenCommand;
import pregenerator.impl.command.base.CommandContainer;
import pregenerator.impl.command.base.BasePregenCommand;

public class ListStructuresSubCommand extends BasePregenCommand
{
    public ListStructuresSubCommand() {
        super(3);
        this.addDescription(0, "Dimension: The Dimension the Structure should be found at. (if unlisted");
        this.addDescription(1, "Type: The Structure that should be listed");
        this.addDescription(2, "(Optional) ShowType: If it should list failed structures or only Failed Structures");
        this.addSuggestion("listStructures 0 Village", "Shows all Villages on the Overworld");
        this.addSuggestion("listStructures -1 Fortress OnlyNoneGenerating", "Shows only all to Small Fortresses that wouldn't generate");
    }
    
    @Override
    public String getName() {
        return "listStructures";
    }
    
    @Override
    public String getDescription() {
        return "Lists the Structures that are planned or already generated";
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
            final int printType = this.getType(PregenCommand.getArg(args, 2));
            if (!BasePregenCommand.isDimensionValid(dimension)) {
                container.sendChatMessage("Dimension " + dimension + " is not Registered!");
                return;
            }
            if (type.equalsIgnoreCase("All")) {
                container.sendChatMessage("The Type All is not supported!");
                return;
            }
            if (!StructureManager.instance.validateType(dimension, type)) {
                container.sendChatMessage("Type: " + type + " doesn't exist in Dimension " + dimension);
                return;
            }
            final MapGenStructureDataPregen structure = StructureManager.instance.getStructure(dimension, type);
            if (structure == null) {
                container.sendChatMessage("Structure Manager for " + type + " not Found!");
                return;
            }
            final FilePos playerPosition = container.getPlayerPos();
            container.sendChatMessage("Listing All " + type + "Structures, Count: " + structure.getStarts().size());
            for (final StructureStart start : structure.getStarts()) {
                if (!start.func_75069_d()) {
                    if (printType < 1) {
                        continue;
                    }
                    final FilePos startPos = new FilePos(start.func_143019_e() * 16, start.func_143018_f() * 16);
                    container.sendChatMessage("Not Generateable " + type + " At: [" + start + "], Distance: " + playerPosition.getSquDistane(startPos.toChunkPos()) + " Blocks, Parts: " + start.func_75073_b().size());
                }
                else {
                    if (printType == 2) {
                        continue;
                    }
                    final FilePos startPos = new FilePos(start.func_143019_e() * 16, start.func_143018_f() * 16);
                    container.sendChatMessage(type + " At: [" + start + "] Distance: " + playerPosition.getSquDistane(startPos.toChunkPos()) + " Blocks, Parts: " + start.func_75073_b().size());
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
            final CompleterHelper helper = ListStructuresSubCommand.helper;
            return PregenCommand.getBestMatch(args, CompleterHelper.STRUCTURE_DIMENSION);
        }
        if (commandIndex == 1) {
            final CompleterHelper helper2 = ListStructuresSubCommand.helper;
            return PregenCommand.getBestMatch(args, CompleterHelper.getStructures(BasePregenCommand.parseNumber(args[argLayer - 1], 0), false));
        }
        if (commandIndex == 2) {
            return PregenCommand.getBestMatch(args, "NoneGenerating", "OnlyNoneGenerating");
        }
        return new ArrayList<String>();
    }
    
    public int getType(final String arg) {
        if (arg == null || arg.isEmpty()) {
            return 0;
        }
        if (arg.equalsIgnoreCase("NoneGenerating")) {
            return 1;
        }
        if (arg.equalsIgnoreCase("OnlyNoneGenerating")) {
            return 2;
        }
        return 0;
    }
}
