// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.command.nocat;

import java.util.ArrayList;
import pregenerator.impl.command.base.PregenCommand;
import pregenerator.impl.command.CompleterHelper;
import java.util.List;
import pregenerator.impl.storage.TaskStorage;
import pregenerator.impl.command.base.CommandContainer;
import pregenerator.impl.command.base.BasePregenCommand;

public class ClearSubCommand extends BasePregenCommand
{
    public ClearSubCommand() {
        super(1);
        this.addDescription(0, "(Optional) Index: Which index of the TaskList should be cleared");
        this.addSuggestion("clear", "Clears all the Tasks in the Task Storage and stops the Pregenerator");
        this.addSuggestion("clear first", "Clears the First index of the TaskStorage and replaces the current PregenTask");
        this.addSuggestion("clear last", "Clears the last index of the TaskStorage");
        this.addSuggestion("clear 5", "Clears the 5th index out of the TaskStorage");
    }
    
    @Override
    public String getName() {
        return "clear";
    }
    
    @Override
    public String getDescription() {
        return "Deletes the TaskList";
    }
    
    @Override
    public int getRequiredParameterCount() {
        return 0;
    }
    
    @Override
    public void execute(final CommandContainer container, final String[] args) {
        if (args.length >= 1) {
            final TaskStorage storage = container.getStorage();
            if (args[0].equalsIgnoreCase("first")) {
                if (this.sendReturnMessage(storage.clearIndex(0), "First", container)) {
                    final boolean hasStorage = storage.hasTasks();
                    container.getProcessor().interruptTask(true, !hasStorage);
                    if (hasStorage) {
                        container.getProcessor().startTask(storage.getNextTask());
                    }
                    else {
                        container.sendChatMessage("No Tasks Left");
                    }
                }
            }
            else if (args[0].equalsIgnoreCase("last")) {
                if (this.sendReturnMessage(storage.clearLast(), "Last", container)) {
                    final boolean hasStorage = storage.hasTasks();
                    container.getProcessor().interruptTask(true, !hasStorage);
                    if (hasStorage) {
                        container.getProcessor().startTask(storage.getNextTask());
                    }
                    else {
                        container.sendChatMessage("No Tasks Left");
                    }
                }
            }
            else {
                final int number = BasePregenCommand.parseNumber(args[0], 0);
                if (this.sendReturnMessage(storage.clearIndex(number), number + "", container)) {
                    final boolean hasStorage2 = storage.hasTasks();
                    container.getProcessor().interruptTask(true, !hasStorage2);
                    if (hasStorage2) {
                        container.getProcessor().startTask(storage.getNextTask());
                    }
                    else {
                        container.sendChatMessage("No Tasks Left");
                    }
                }
            }
        }
        else {
            container.getStorage().clearAll();
            container.sendChatMessage("Cleared all Tasks");
            container.getProcessor().interruptTask(true);
        }
    }
    
    public boolean sendReturnMessage(final int result, final String index, final CommandContainer container) {
        if (result == 0) {
            container.sendChatMessage("No Task Found to be Deleted");
            return false;
        }
        container.sendChatMessage("Deleted Task at " + index + " Index");
        return result == 2;
    }
    
    @Override
    public List<String> getAutoCompleteOption(final String[] args, final int argLayer, final int commandIndex) {
        if (commandIndex == 0) {
            final CompleterHelper helper = ClearSubCommand.helper;
            return PregenCommand.getBestMatch(args, CompleterHelper.TASKLIST);
        }
        return new ArrayList<String>();
    }
}
