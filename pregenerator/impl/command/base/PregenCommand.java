// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.command.base;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import pregenerator.impl.command.CompleterHelper;

public abstract class PregenCommand
{
    public static final CompleterHelper helper;
    
    public abstract String getName();
    
    public abstract String getDescription();
    
    public abstract String[] getArgumentDescriptions();
    
    public abstract int getRequiredParameterCount();
    
    public abstract Map<String, String> getExamples();
    
    public abstract void execute(final CommandContainer p0, final String[] p1);
    
    public abstract List<String> getAutoCompleteOption(final String[] p0, final int p1, final int p2);
    
    public void throwErrors(final CommandContainer container, final int currentLength) {
        if (currentLength >= this.getRequiredParameterCount()) {
            return;
        }
        final int req = this.getRequiredParameterCount();
        final String[] parameters = this.getArgumentDescriptions();
        container.sendChatMessage("Error Missing following Arugments: ");
        for (int i = currentLength; i < parameters.length; ++i) {
            container.sendChatMessage("[Index: " + i + "]: " + parameters[i]);
        }
    }
    
    public static List<String> getBestMatch(final String[] inputArgs, final CompleterHelper.ICompleter complete) {
        return getBestMatch(inputArgs, complete.getCompleter());
    }
    
    public static List<String> getBestMatch(final String[] inputArgs, final String... possibleCompletions) {
        return getBestMatch(inputArgs, Arrays.asList(possibleCompletions));
    }
    
    public static List<String> getBestMatch(final String[] inputArgs, final Collection<String> possibleCompletions) {
        final String lastArgument = inputArgs[inputArgs.length - 1];
        final List<String> results = new ArrayList<String>();
        for (final String entry : possibleCompletions) {
            if (entry.regionMatches(true, 0, lastArgument, 0, lastArgument.length())) {
                results.add(entry);
            }
        }
        return results;
    }
    
    public static String getArg(final String[] args, final int layer) {
        if (args.length > layer) {
            return args[layer];
        }
        return null;
    }
    
    static {
        helper = new CompleterHelper();
    }
}
