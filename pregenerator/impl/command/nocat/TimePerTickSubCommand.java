// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.command.nocat;

import java.util.ArrayList;
import pregenerator.impl.command.base.PregenCommand;
import java.util.List;
import pregenerator.ConfigManager;
import pregenerator.impl.command.base.CommandContainer;
import pregenerator.impl.command.base.BasePregenCommand;

public class TimePerTickSubCommand extends BasePregenCommand
{
    public TimePerTickSubCommand() {
        super(1);
        this.addDescription(0, "(Optional) Time: The Time each per tick should have (in ms)");
        this.addSuggestion("timepertick", "reads the current TimePerTick");
        this.addSuggestion("timepertick default", "Resets the timepertick to teh default value (40)");
        this.addSuggestion("timepertick 250", "Sets the time per tick to the most optimal speed that you can gain. At least on the Devs testing it apeared to be");
    }
    
    @Override
    public String getName() {
        return "timepertick";
    }
    
    @Override
    public String getDescription() {
        return "Sets the amount of Time per each Tick has";
    }
    
    @Override
    public int getRequiredParameterCount() {
        return 0;
    }
    
    @Override
    public void execute(final CommandContainer container, final String[] args) {
        if (args.length >= 1) {
            final int time = Math.max(BasePregenCommand.parseNumber(args[0], 40), 10);
            ConfigManager.saveTime(time);
            container.getProcessor().setMaxTime(time);
            container.sendChatMessage("Set TimePerTick to: " + time + " ms");
        }
        else {
            container.sendChatMessage("TimePerTick: " + container.getProcessor().getMaxTime());
        }
    }
    
    @Override
    public List<String> getAutoCompleteOption(final String[] args, final int argLayer, final int commandIndex) {
        if (commandIndex == 0) {
            return PregenCommand.getBestMatch(args, "default", "10", "20", "30", "40", "50", "75", "100", "200", "250", "400", "500", "600", "750", "900", "1000");
        }
        return new ArrayList<String>();
    }
}
