// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.command.utils;

import java.util.ArrayList;
import pregenerator.impl.command.CompleterHelper;
import java.util.List;
import net.minecraft.util.BlockPos;
import pregenerator.impl.command.base.PregenCommand;
import pregenerator.impl.command.base.CommandContainer;
import pregenerator.impl.command.base.BasePregenCommand;

public class FindSpawnSubCommand extends BasePregenCommand
{
    public FindSpawnSubCommand() {
        super(1);
        this.addDescription(0, "(Optional) Dimension: The Dimension that the Spawnpoint is needed for");
        this.addSuggestion("findSpawn", "Finds the SpawnPoint of the CommandSenders Dimension");
        this.addSuggestion("findSpawn -1", "Finds the SpawnPoint of the Nether");
    }
    
    @Override
    public String getName() {
        return "findSpawn";
    }
    
    @Override
    public String getDescription() {
        return "Finds the Spawn in the Selected Dimension";
    }
    
    @Override
    public int getRequiredParameterCount() {
        return 0;
    }
    
    @Override
    public void execute(final CommandContainer container, final String[] args) {
        final int dimension = BasePregenCommand.getDimension(container, PregenCommand.getArg(args, 0));
        if (!BasePregenCommand.isDimensionValid(dimension)) {
            container.sendChatMessage("Dimension " + dimension + " is not Registered");
            return;
        }
        final BlockPos pos = container.getWorld(dimension).func_175694_M();
        container.sendChatMessage("Spawn for Dimension: " + dimension + ": [X: " + pos.func_177958_n() + ", Y: " + pos.func_177956_o() + ", Z: " + pos.func_177952_p() + "]");
    }
    
    @Override
    public List<String> getAutoCompleteOption(final String[] args, final int argLayer, final int commandIndex) {
        if (commandIndex == 0) {
            final CompleterHelper helper = FindSpawnSubCommand.helper;
            return PregenCommand.getBestMatch(args, CompleterHelper.DIMENSION);
        }
        return new ArrayList<String>();
    }
}
