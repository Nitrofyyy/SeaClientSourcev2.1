// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.command.utils;

import java.util.ArrayList;
import pregenerator.impl.command.base.PregenCommand;
import java.util.List;
import pregenerator.ConfigManager;
import pregenerator.impl.command.base.CommandContainer;
import pregenerator.impl.command.base.BasePregenCommand;

public class PlayerLimitSubCommand extends BasePregenCommand
{
    public PlayerLimitSubCommand() {
        super(1);
        this.addDescription(0, "Amount: How many players should be online to Pause the Pregenerator");
        this.addSuggestion("setPlayerLimit disable", "Resets the PlayerLimit to ignoring");
        this.addSuggestion("setPlayerLimit 10", "Pauses the Pregenerator when 10 Players are online");
    }
    
    @Override
    public String getName() {
        return "setPlayerLimit";
    }
    
    @Override
    public String getDescription() {
        return "Pauses the Pregenerator when PlayerLimit is reached";
    }
    
    @Override
    public int getRequiredParameterCount() {
        return 0;
    }
    
    @Override
    public void execute(final CommandContainer container, final String[] args) {
        if (args.length >= 1) {
            final int level = Math.max(-1, args[0].equalsIgnoreCase("disable") ? -1 : BasePregenCommand.parseNumber(args[0], -1));
            ConfigManager.setPlayerCount(level);
            container.sendChatMessage("Set Player-Limit to: " + level);
        }
        else {
            container.sendChatMessage("Player-Limit: " + ConfigManager.playerDeactivation);
        }
    }
    
    @Override
    public List<String> getAutoCompleteOption(final String[] args, final int argLayer, final int commandIndex) {
        if (commandIndex == 0) {
            return PregenCommand.getBestMatch(args, "disable", "-1", "0", "1", "2", "4", "8", "16");
        }
        return new ArrayList<String>();
    }
}
