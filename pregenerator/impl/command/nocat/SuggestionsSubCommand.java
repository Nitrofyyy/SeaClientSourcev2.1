// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.command.nocat;

import java.util.ArrayList;
import java.util.Collection;
import net.minecraftforge.fml.common.FMLLog;
import java.util.List;
import java.util.Iterator;
import pregenerator.impl.command.base.PregenCommand;
import java.util.Map;
import pregenerator.impl.command.base.CommandContainer;
import pregenerator.impl.command.PregenBaseCommand;
import pregenerator.impl.command.base.BasePregenCommand;

public class SuggestionsSubCommand extends BasePregenCommand
{
    PregenBaseCommand command;
    
    public SuggestionsSubCommand(final PregenBaseCommand theCommand) {
        super(1);
        this.command = theCommand;
        this.addDescription(0, "Command: The Command you want suggestions for");
        this.addSuggestion("suggestion startradius", "Provides suggestions on the startradius command");
    }
    
    @Override
    public String getName() {
        return "suggestion";
    }
    
    @Override
    public String getDescription() {
        return "Prints out Suggestions for the Selected command";
    }
    
    @Override
    public int getRequiredParameterCount() {
        return 1;
    }
    
    @Override
    public void execute(final CommandContainer container, final String[] args) {
        if (args.length >= 1) {
            final PregenCommand sub = this.command.getCommand(args[0]);
            if (sub == null) {
                container.sendChatMessage("Command " + args[0] + " doesn't exist");
                return;
            }
            final String base = this.command.hasCategory(sub) ? ("/pregen " + this.command.getCategoryForCommand(sub) + " ") : "/pregen ";
            final Map<String, String> suggestions = sub.getExamples();
            if (suggestions == null || suggestions.isEmpty()) {
                container.sendChatMessage("Command " + args[0] + " has no Examples.");
                return;
            }
            container.sendChatMessage("Suggestions for " + args[0]);
            container.sendChatMessage("");
            int i = 0;
            for (final Map.Entry<String, String> entry : suggestions.entrySet()) {
                container.sendChatMessage("[" + i + "] " + base + entry.getKey());
                container.sendChatMessage(entry.getValue());
                container.sendChatMessage("");
                ++i;
            }
        }
        else {
            container.sendChatMessage("This command is basically giving you examples / suggestions on other commands. Please type in after this command the command you want a suggestion for!");
        }
    }
    
    @Override
    public List<String> getAutoCompleteOption(final String[] args, final int argLayer, final int commandIndex) {
        FMLLog.getLogger().info("Test: " + commandIndex);
        if (commandIndex == 0) {
            return PregenCommand.getBestMatch(args, this.command.getAllSubCommands());
        }
        return new ArrayList<String>();
    }
}
