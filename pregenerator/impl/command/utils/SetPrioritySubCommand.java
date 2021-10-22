// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.command.utils;

import java.util.ArrayList;
import pregenerator.impl.command.base.PregenCommand;
import java.util.List;
import pregenerator.impl.command.base.CommandContainer;
import pregenerator.impl.command.base.BasePregenCommand;

public class SetPrioritySubCommand extends BasePregenCommand
{
    public SetPrioritySubCommand() {
        super(1);
        this.addDescription(0, "(Optional) Type: If the Game or Pregenerator should be prioritized");
        this.addSuggestion("setPriority", "Prints out the Current Priority");
        this.addSuggestion("setPriority game", "Set the CPU Priority to the Game");
        this.addSuggestion("setPriority pregenerator", "Set the CPU Priority to the Pregenerator");
    }
    
    @Override
    public String getName() {
        return "setPriority";
    }
    
    @Override
    public String getDescription() {
        return "Changes if the Pregenerator is taking over CPU priority or not";
    }
    
    @Override
    public int getRequiredParameterCount() {
        return 0;
    }
    
    @Override
    public void execute(final CommandContainer container, final String[] args) {
        if (args.length >= 1) {
            if (args[0].equalsIgnoreCase("pregenerator")) {
                container.getProcessor().setPriority(true);
                container.sendChatMessage("Setting Priority to Pregenerator");
            }
            else if (args[0].equalsIgnoreCase("game")) {
                container.getProcessor().setPriority(false);
                container.sendChatMessage("Setting Priority to Game");
            }
            else {
                container.sendChatMessage(args[0] + " isn't a valid option");
            }
        }
        else {
            container.sendChatMessage((container.getProcessor().isPriority() ? "Pregenerator" : "Game") + " is Priority");
        }
    }
    
    @Override
    public List<String> getAutoCompleteOption(final String[] args, final int argLayer, final int commandIndex) {
        if (commandIndex == 0) {
            return PregenCommand.getBestMatch(args, "game", "pregenerator");
        }
        return new ArrayList<String>();
    }
}
