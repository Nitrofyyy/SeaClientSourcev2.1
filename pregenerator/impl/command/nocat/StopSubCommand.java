// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.command.nocat;

import java.util.ArrayList;
import pregenerator.impl.command.base.PregenCommand;
import java.util.List;
import pregenerator.impl.storage.PregenTask;
import pregenerator.impl.command.base.CommandContainer;
import pregenerator.impl.command.base.BasePregenCommand;

public class StopSubCommand extends BasePregenCommand
{
    public StopSubCommand() {
        super(1);
        this.addDescription(0, "(Optional) Deletion: If it should clear the entire tasklist or just the current task");
        this.addSuggestion("stop", "Stops the Running Processor");
        this.addSuggestion("stop delete", "Stops the Processor and Deletes the Running Pregen Task if there was one");
        this.addSuggestion("stop deleteAll", "Stops the Processor and Deletes the Pregen TaskList");
    }
    
    @Override
    public String getName() {
        return "stop";
    }
    
    @Override
    public String getDescription() {
        return "Stops the current task and optionally deletes also the task";
    }
    
    @Override
    public int getRequiredParameterCount() {
        return 0;
    }
    
    @Override
    public void execute(final CommandContainer container, final String[] args) {
        if (!container.processorRunning()) {
            container.sendChatMessage("Nothing is running");
            return;
        }
        if (container.getDeleter().isRunning()) {
            container.getDeleter().interruptTask();
            container.sendChatMessage("Interrupted Chunk Deleter");
        }
        else {
            final PregenTask current = container.getProcessor().getTask();
            container.getProcessor().interruptTask(false);
            if (args.length < 1) {
                container.sendChatMessage("Pregenerator Stopped, TaskList Remains");
                return;
            }
            if (args[0].equalsIgnoreCase("deleteAll")) {
                container.getStorage().clearAll();
                container.sendChatMessage("Pregenerator Stopped, All Tasks removed");
            }
            else if (args[0].equalsIgnoreCase("delete")) {
                container.getStorage().finishTask(current);
                container.sendChatMessage("Pregenerator Stopped, Current Task Deleted");
            }
            else {
                container.sendChatMessage("Pregenerator Stopped, TaskList Remains");
            }
        }
    }
    
    @Override
    public List<String> getAutoCompleteOption(final String[] args, final int argLayer, final int commandIndex) {
        if (commandIndex == 0) {
            return PregenCommand.getBestMatch(args, "delete", "deleteAll");
        }
        return new ArrayList<String>();
    }
}
