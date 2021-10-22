// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.command.nocat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import pregenerator.impl.command.base.PregenCommand;
import pregenerator.impl.command.base.CommandContainer;
import pregenerator.impl.command.PregenBaseCommand;
import pregenerator.impl.command.base.BasePregenCommand;

public class HelpSubCommand extends BasePregenCommand
{
    PregenBaseCommand command;
    
    public HelpSubCommand(final PregenBaseCommand help) {
        super(1);
        this.addDescription(0, "(Optional) Command: The Sub Command you would like to get Explained or the SubCategory that you would like to know explained");
        this.addSuggestion("help startradius", "Explains the Basic Pregen Command for you");
        this.command = help;
    }
    
    @Override
    public String getName() {
        return "help";
    }
    
    @Override
    public String getDescription() {
        return "Explains all the Commands and each command Parameter to the User";
    }
    
    @Override
    public int getRequiredParameterCount() {
        return 0;
    }
    
    @Override
    public void execute(final CommandContainer container, final String[] args) {
        this.command.printHelp(container, PregenCommand.getArg(args, 0));
    }
    
    @Override
    public List<String> getAutoCompleteOption(final String[] args, final int argLayer, final int commandIndex) {
        if (commandIndex == 0) {
            return PregenCommand.getBestMatch(args, this.command.getAllSubCommands());
        }
        return new ArrayList<String>();
    }
}
