// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.command.utils;

import java.util.ArrayList;
import pregenerator.impl.command.base.PregenCommand;
import java.util.List;
import pregenerator.impl.command.base.CommandContainer;
import pregenerator.impl.command.base.BasePregenCommand;

public class SkipChunksSubCommand extends BasePregenCommand
{
    public SkipChunksSubCommand() {
        super(1);
        this.addDescription(0, "Amount: How many chunks that should be skipped");
        this.addSuggestion("SkipChunks 1000", "Skips 1000 Chunks of the Current Progress");
    }
    
    @Override
    public String getName() {
        return "SkipChunks";
    }
    
    @Override
    public String getDescription() {
        return "Skips Chunks in a Running Pregeneration";
    }
    
    @Override
    public int getRequiredParameterCount() {
        return 1;
    }
    
    @Override
    public void execute(final CommandContainer container, final String[] args) {
        if (container.getProcessor().isStopped()) {
            container.sendChatMessage("Pregenerator is not running. Nothing to Skip");
            return;
        }
        if (args.length >= 1) {
            final int number = BasePregenCommand.parseNumber(args[0], 0);
            if (number <= 0) {
                container.sendChatMessage("You can not Skip 0 or less Chunks");
                return;
            }
            final boolean added = container.getListener().addListener(container.getSender());
            container.getProcessor().skipChunks(number);
            if (added) {
                container.getListener().removeListener(container.getSender());
            }
        }
        else {
            this.throwErrors(container, args.length);
        }
    }
    
    @Override
    public List<String> getAutoCompleteOption(final String[] args, final int argLayer, final int commandIndex) {
        if (commandIndex == 0) {
            return PregenCommand.getBestMatch(args, "100", "1000", "10000", "100000", "10000000");
        }
        return new ArrayList<String>();
    }
}
