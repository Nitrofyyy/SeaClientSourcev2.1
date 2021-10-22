// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.command.gen;

import java.util.Iterator;
import pregenerator.impl.storage.MassCircleTask;
import java.util.Comparator;
import pregenerator.impl.command.delete.DeleteMassSubCommand;
import com.google.common.math.DoubleMath;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.ArrayList;
import pregenerator.impl.command.CompleterHelper;
import java.util.List;
import pregenerator.impl.misc.FilePos;
import pregenerator.impl.storage.PregenTask;
import pregenerator.impl.command.base.PregenCommand;
import pregenerator.impl.command.base.CommandContainer;
import pregenerator.impl.command.base.BasePregenCommand;

public class StartMassRadiusSubCommand extends BasePregenCommand
{
    public StartMassRadiusSubCommand() {
        super(7);
        this.addDescription(0, "Generation Type: Which shape the Generation should have");
        this.addDescription(1, "X Center: Which Chunk the center should be in. (if 'b' infront of the number or after ~ means block position)");
        this.addDescription(2, "Z Center: Which Chunk the center should be in. (if 'b' infront of the number or after ~ means block position)");
        this.addDescription(3, "Radius: How big the Radius in Chunks should be. (if 'b' infront of the number or after ~ means block distance)");
        this.addDescription(4, "(Optional) SplitRadius: In How much radius the Calculator should split the tasks. Default: 1000 Chunks, Max: 1000 Chunks, Min: 100 Chunks");
        this.addDescription(5, "(Optional) Dimension: The Dimension the Generation should be in (Auto Loads Dimensions)");
        this.addDescription(6, "(Optional) Processing Rule: Which type of Generation it should use.");
        this.addSuggestion("startmassradius square 0 0 10000", "Generates a 10000 Chunk Radius with multible tasks of 1000 Chunk Radius in the dimension the Sender is in");
        this.addSuggestion("startmassradius square 0 0 10000 100", "Generates a 10000 Chunk Radius with multible tasks of 100 Chunk Radius");
    }
    
    @Override
    public String getName() {
        return "startmassradius";
    }
    
    @Override
    public String getDescription() {
        return "Generates a Massive Amount of Area";
    }
    
    @Override
    public int getRequiredParameterCount() {
        return 4;
    }
    
    @Override
    public void execute(final CommandContainer container, final String[] args) {
        if (args.length >= 4) {
            final int type = BasePregenCommand.getGenType(args[0]);
            FilePos center = BasePregenCommand.getChunkPos(args[1], args[2], container.getPlayerPos());
            final int radius = BasePregenCommand.getNumber(args[3], 0);
            final int splitRadius = BasePregenCommand.clamp(BasePregenCommand.parseNumber(PregenCommand.getArg(args, 4), 25000), 100, 25000);
            final int dimension = BasePregenCommand.getDimension(container, PregenCommand.getArg(args, 5));
            final int postRule = BasePregenCommand.getProcessRule(PregenCommand.getArg(args, 6));
            if (!BasePregenCommand.isDimensionValid(dimension)) {
                container.sendChatMessage("Dimension " + dimension + " is not Registered!");
                return;
            }
            center = BasePregenCommand.applySpawn(args[1], args[2], center, container.getWorldSpawn(dimension));
            final List<PregenTask> list = createTaskList(type, center, radius, dimension, postRule, splitRadius);
            if (list.isEmpty()) {
                container.sendChatMessage("No tasks could be created.");
                return;
            }
            container.sendChatMessage("Created " + list.size() + " Tasks");
            if (container.onProcessStarted(list.get(0))) {
                container.sendChatMessage("Pregenerator already running. Adding Task(s) to the TaskStorage");
            }
            else {
                container.getProcessor().startTask(list.get(0));
            }
            container.getStorage().savePregenTasks(list);
        }
        else {
            this.throwErrors(container, args.length);
        }
    }
    
    @Override
    public List<String> getAutoCompleteOption(final String[] args, final int argLayer, final int commandIndex) {
        if (commandIndex == 0) {
            final CompleterHelper helper = StartMassRadiusSubCommand.helper;
            return PregenCommand.getBestMatch(args, CompleterHelper.GEN_TYPE);
        }
        if (commandIndex == 1 || commandIndex == 2) {
            return PregenCommand.getBestMatch(args, "0", "~");
        }
        if (commandIndex == 3) {
            if (args[argLayer].startsWith("b")) {
                final CompleterHelper helper2 = StartMassRadiusSubCommand.helper;
                return PregenCommand.getBestMatch(args, CompleterHelper.GEN_RADIUS_BLOCK);
            }
            final CompleterHelper helper3 = StartMassRadiusSubCommand.helper;
            return PregenCommand.getBestMatch(args, CompleterHelper.GEN_RADIUS_CHUNK);
        }
        else {
            if (commandIndex == 4) {
                return PregenCommand.getBestMatch(args, "100", "200", "300", "400", "500", "600", "700", "800", "900", "1000", "2000", "3000", "4000", "5000", "10000", "20000", "25000");
            }
            if (commandIndex == 5) {
                final CompleterHelper helper4 = StartMassRadiusSubCommand.helper;
                return PregenCommand.getBestMatch(args, CompleterHelper.DIMENSION);
            }
            if (commandIndex == 6) {
                final CompleterHelper helper5 = StartMassRadiusSubCommand.helper;
                return PregenCommand.getBestMatch(args, CompleterHelper.GEN_PROCESS);
            }
            return new ArrayList<String>();
        }
    }
    
    public static List<PregenTask> createTaskList(final int type, final FilePos center, final int radius, final int dimension, final int postRule, final int splitRadius) {
        if (radius <= splitRadius) {
            return Arrays.asList(new PregenTask(type, dimension, center.x, center.z, radius, 0, postRule));
        }
        final int range = DoubleMath.roundToInt(radius / (double)splitRadius, RoundingMode.UP);
        final List<FilePos> workList = new ArrayList<FilePos>();
        for (int x = -range; x < range; ++x) {
            for (int z = -range; z < range; ++z) {
                workList.add(new FilePos(x, z));
            }
        }
        workList.sort(new DeleteMassSubCommand.Sorter(center));
        final List<PregenTask> task = new ArrayList<PregenTask>();
        for (final FilePos pos : workList) {
            final int xMin = BasePregenCommand.clamp((pos.x - 1) * splitRadius - 1, -radius, radius);
            final int zMin = BasePregenCommand.clamp((pos.z - 1) * splitRadius - 1, -radius, radius);
            final int xMax = BasePregenCommand.clamp((pos.x + 1) * splitRadius, -radius, radius);
            final int zMax = BasePregenCommand.clamp((pos.z + 1) * splitRadius, -radius, radius);
            task.add(new MassCircleTask(type == 1, dimension, xMin, zMin, xMax, zMax, pos.x, pos.z, radius, postRule));
        }
        return task;
    }
}
