// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.command.gen;

import pregenerator.impl.command.CompleterHelper;
import java.util.ArrayList;
import pregenerator.impl.command.base.PregenCommand;
import java.util.List;
import pregenerator.impl.retrogen.RetrogenHandler;
import pregenerator.impl.command.base.CommandContainer;
import pregenerator.impl.command.base.BasePregenCommand;

public class SelectRetrogenSubCommand extends BasePregenCommand
{
    public SelectRetrogenSubCommand() {
        super(2);
    }
    
    @Override
    public String getName() {
        return "selectRetrogenerator";
    }
    
    @Override
    public String getDescription() {
        return "Adds/Removes the Retrogenerators for the RetroPregenerator";
    }
    
    @Override
    public int getRequiredParameterCount() {
        return 2;
    }
    
    @Override
    public void execute(final CommandContainer container, final String[] args) {
        if (container.processorRunning()) {
            container.sendChatMessage("You can not change the Retrogenerator while the Pregenerator is running!");
        }
        else if (args.length >= 2) {
            final boolean add = args[0].equalsIgnoreCase("add");
            final boolean remove = args[0].equalsIgnoreCase("remove");
            if (!add && !remove) {
                container.sendChatMessage("Invalid Action. It has to be either Add or remove");
                return;
            }
            final String generator = args[1];
            final RetrogenHandler handler = RetrogenHandler.INSTANCE;
            if (!handler.isValidGenerator(generator)) {
                container.sendChatMessage("Generator is not Valid!");
                return;
            }
            if (add) {
                if (handler.isGeneratorActive(generator)) {
                    container.sendChatMessage("Generator is already in the Retrogen Handler");
                    return;
                }
                handler.enableGenerator(generator);
                container.sendChatMessage("Added Retrogen Generator [" + generator + "] to the Retrogen Handler");
            }
            else if (remove) {
                if (!handler.isGeneratorActive(generator)) {
                    container.sendChatMessage("Generator is already not in the Retrogen Handler");
                    return;
                }
                handler.disableGenerator(generator);
                container.sendChatMessage("Removed Retrogen Generator [" + generator + "] from the Retrogen Handler");
            }
        }
        else {
            this.throwErrors(container, args.length);
        }
    }
    
    @Override
    public List<String> getAutoCompleteOption(final String[] args, final int argLayer, final int commandIndex) {
        if (commandIndex == 0) {
            return PregenCommand.getBestMatch(args, "Add", "Remove");
        }
        if (commandIndex != 1) {
            return new ArrayList<String>();
        }
        if (args[0].equalsIgnoreCase("Add")) {
            final CompleterHelper helper = SelectRetrogenSubCommand.helper;
            return PregenCommand.getBestMatch(args, CompleterHelper.RETROGEN_ADD);
        }
        final CompleterHelper helper2 = SelectRetrogenSubCommand.helper;
        return PregenCommand.getBestMatch(args, CompleterHelper.RETROGEN_REMOVE);
    }
}
