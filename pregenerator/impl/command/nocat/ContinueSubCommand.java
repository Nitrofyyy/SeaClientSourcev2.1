// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.command.nocat;

import java.util.ArrayList;
import java.util.List;
import pregenerator.impl.storage.PregenTask;
import pregenerator.impl.storage.TaskStorage;
import pregenerator.impl.command.base.CommandContainer;
import pregenerator.impl.command.base.BasePregenCommand;

public class ContinueSubCommand extends BasePregenCommand
{
    public ContinueSubCommand() {
        super(0);
        this.addSuggestion("continue", "Continues the First Task in the TaskList");
    }
    
    @Override
    public String getName() {
        return "continue";
    }
    
    @Override
    public String getDescription() {
        return "Starts the Pregenerator with a task out of the TaskList";
    }
    
    @Override
    public int getRequiredParameterCount() {
        return 0;
    }
    
    @Override
    public void execute(final CommandContainer container, final String[] args) {
        if (container.processorRunning()) {
            container.sendChatMessage("Pregenerator/Deleter is already running");
        }
        else {
            final TaskStorage storage = container.getStorage();
            if (storage.hasTasks()) {
                final PregenTask task = storage.getNextTask();
                container.sendChatMessage("Starting Tasks: " + task.toString());
                container.onProcessStarted(task);
                container.getProcessor().startTask(task);
            }
            else {
                container.sendChatMessage("No Tasks in TaskList");
            }
        }
    }
    
    @Override
    public List<String> getAutoCompleteOption(final String[] args, final int argLayer, final int commandIndex) {
        return new ArrayList<String>();
    }
}
