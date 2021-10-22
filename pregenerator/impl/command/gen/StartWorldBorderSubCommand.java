// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.command.gen;

import java.util.ArrayList;
import pregenerator.impl.command.CompleterHelper;
import java.util.List;
import net.minecraft.world.border.WorldBorder;
import pregenerator.impl.storage.PregenTask;
import pregenerator.impl.misc.FilePos;
import pregenerator.impl.command.base.PregenCommand;
import pregenerator.impl.command.base.CommandContainer;
import pregenerator.impl.command.base.BasePregenCommand;

public class StartWorldBorderSubCommand extends BasePregenCommand
{
    public StartWorldBorderSubCommand() {
        super(3);
        this.addDescription(0, "(Optional)Dimension: The Dimension it should generate in");
        this.addDescription(1, "(Optional) SplitRadius: In How much radius the Calculator should split the tasks. Default: 1000 Chunks, Max: 1000 Chunks, Min: 100 Chunks");
        this.addDescription(2, "(Optional) Processing Rule: Which type of Generation it should use.");
        this.addSuggestion("startWorldBorder", "Generates everything inside the Worldborder in the dimension the player is in");
        this.addSuggestion("startWorldBorder -1", "Generates everything inside the Worldborder in the Nether");
    }
    
    @Override
    public String getName() {
        return "startWorldBorder";
    }
    
    @Override
    public String getDescription() {
        return "Generates everything inside of a worldBorder";
    }
    
    @Override
    public int getRequiredParameterCount() {
        return 0;
    }
    
    @Override
    public void execute(final CommandContainer container, final String[] args) {
        final int dimensionid = BasePregenCommand.getDimension(container, PregenCommand.getArg(args, 0));
        final int split = BasePregenCommand.clamp(BasePregenCommand.parseNumber(PregenCommand.getArg(args, 1), 1000), 100, 1000);
        final int rule = BasePregenCommand.getProcessRule(PregenCommand.getArg(args, 2));
        if (!BasePregenCommand.isDimensionValid(dimensionid)) {
            container.sendChatMessage("Dimension " + dimensionid + " is not Registered!");
            return;
        }
        final WorldBorder border = container.getWorld(dimensionid).func_175723_af();
        int radius = (int)(border.func_177741_h() / 2.0 / 16.0);
        radius += 10;
        final FilePos center = new FilePos((int)border.func_177731_f(), (int)border.func_177721_g()).toChunkPos();
        final List<PregenTask> pregenTasks = StartMassRadiusSubCommand.createTaskList(0, center, radius, dimensionid, rule, split);
        if (pregenTasks.isEmpty()) {
            container.sendChatMessage("No tasks could be created.");
            return;
        }
        container.sendChatMessage("Created " + pregenTasks.size() + " Tasks");
        if (container.onProcessStarted(pregenTasks.get(0))) {
            container.sendChatMessage("Pregenerator already running. Adding Task(s) to the TaskStorage");
        }
        else {
            container.getProcessor().startTask(pregenTasks.get(0));
        }
        container.getStorage().savePregenTasks(pregenTasks);
    }
    
    @Override
    public List<String> getAutoCompleteOption(final String[] args, final int argLayer, final int commandIndex) {
        if (commandIndex == 0) {
            final CompleterHelper helper = StartWorldBorderSubCommand.helper;
            return PregenCommand.getBestMatch(args, CompleterHelper.DIMENSION);
        }
        if (commandIndex == 1) {
            return PregenCommand.getBestMatch(args, "100", "200", "300", "400", "500", "600", "700", "800", "900", "1000");
        }
        if (commandIndex == 2) {
            final CompleterHelper helper2 = StartWorldBorderSubCommand.helper;
            return PregenCommand.getBestMatch(args, CompleterHelper.GEN_PROCESS);
        }
        return new ArrayList<String>();
    }
}
