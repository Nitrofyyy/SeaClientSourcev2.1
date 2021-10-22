// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.command.info;

import java.util.ArrayList;
import pregenerator.impl.command.CompleterHelper;
import pregenerator.impl.command.base.PregenCommand;
import java.util.List;
import java.util.EnumSet;
import pregenerator.impl.processor.generator.ChunkProcessor;
import pregenerator.impl.processor.generator.ChunkLogger;
import pregenerator.impl.command.base.CommandContainer;
import pregenerator.impl.command.base.BasePregenCommand;

public class EditInfoSubCommand extends BasePregenCommand
{
    public EditInfoSubCommand() {
        super(2);
        this.addDescription(0, "Add/Remove: If you want to Add/Remove the Index out of the Logger List");
        this.addDescription(1, "Type: The Info you want to Add/Remove");
        this.addSuggestion("EditInfo add CPU-Usage", "Add the CPU Usage information ");
    }
    
    @Override
    public String getName() {
        return "EditInfo";
    }
    
    @Override
    public String getDescription() {
        return "Changes the Information you get from the Processor Logger";
    }
    
    @Override
    public int getRequiredParameterCount() {
        return 2;
    }
    
    @Override
    public void execute(final CommandContainer container, final String[] args) {
        if (args.length >= 2) {
            final ChunkLogger logger = ChunkLogger.byID(args[1]);
            if (logger == null) {
                container.sendChatMessage("Type " + args[1] + " is not Valid");
                return;
            }
            final EnumSet<ChunkLogger> already = ChunkProcessor.INSTANCE.getLoggerInfo();
            if (args[0].equalsIgnoreCase("add")) {
                if (already.add(logger)) {
                    container.sendChatMessage("Added " + logger.getName() + " to the logger");
                    return;
                }
                container.sendChatMessage("Type " + logger.getName() + " is already in the logger");
            }
            else if (args[0].equalsIgnoreCase("remove")) {
                if (already.remove(logger)) {
                    container.sendChatMessage("Removed " + logger.getName() + " from the logger");
                    return;
                }
                container.sendChatMessage("Type " + logger.getName() + " is already not in the logger");
            }
            else {
                container.sendChatMessage("Instruction " + args[0] + " is not valid!");
            }
        }
        else {
            this.throwErrors(container, args.length);
        }
    }
    
    @Override
    public List<String> getAutoCompleteOption(final String[] args, final int argLayer, final int commandIndex) {
        if (commandIndex == 0) {
            return PregenCommand.getBestMatch(args, "add", "remove");
        }
        if (commandIndex == 1) {
            if (args[argLayer - 1].equalsIgnoreCase("add")) {
                final CompleterHelper helper = EditInfoSubCommand.helper;
                return PregenCommand.getBestMatch(args, CompleterHelper.INFO_ADD);
            }
            if (args[argLayer - 1].equalsIgnoreCase("remove")) {
                final CompleterHelper helper2 = EditInfoSubCommand.helper;
                return PregenCommand.getBestMatch(args, CompleterHelper.INFO_REMOVE);
            }
        }
        return new ArrayList<String>();
    }
}
