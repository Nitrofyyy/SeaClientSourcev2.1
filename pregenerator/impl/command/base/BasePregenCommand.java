// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.command.base;

import pregenerator.impl.misc.FilePos;
import net.minecraftforge.common.DimensionManager;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class BasePregenCommand extends PregenCommand
{
    Map<String, String> suggestions;
    String[] descriptions;
    
    public BasePregenCommand(final int amounts) {
        this.suggestions = new LinkedHashMap<String, String>();
        this.descriptions = new String[amounts];
    }
    
    public void addSuggestion(final String command, final String description) {
        this.suggestions.put(command, description);
    }
    
    public void addDescription(final int index, final String data) {
        this.descriptions[index] = data;
    }
    
    @Override
    public String[] getArgumentDescriptions() {
        return this.descriptions;
    }
    
    @Override
    public Map<String, String> getExamples() {
        return this.suggestions;
    }
    
    public static boolean isDimensionValid(final int dim) {
        try {
            DimensionManager.getProviderType(dim);
            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public static int getDimension(final CommandContainer container, final String dimension) {
        if (dimension == null || dimension.isEmpty() || dimension.equals("~")) {
            return container.getPlayerDimension();
        }
        return Integer.parseInt(dimension);
    }
    
    public static FilePos getChunkPos(final String xPos, final String zPos, FilePos playerPos) {
        playerPos = playerPos.toChunkPos();
        return new FilePos(getNumber(xPos, playerPos.x), getNumber(zPos, playerPos.z));
    }
    
    public static FilePos getBlockPos(final String xPos, final String zPos, final FilePos playerPos) {
        return new FilePos(getNumber(xPos.replace("b", ""), playerPos.x), getNumber(zPos.replace("b", ""), playerPos.z));
    }
    
    public static FilePos applySpawn(final String xPos, final String zPos, final FilePos original, final FilePos worldSpawn) {
        int x = original.x;
        int z = original.z;
        if (xPos.startsWith("s")) {
            x += worldSpawn.x;
        }
        if (zPos.startsWith("s")) {
            z += worldSpawn.z;
        }
        return new FilePos(x, z);
    }
    
    public static int getNumber(String s, final int offset) {
        if (s.startsWith("s")) {
            s = s.substring(1);
            if (s.isEmpty()) {
                return 0;
            }
        }
        if (!s.startsWith("~")) {
            if (!s.startsWith("b")) {
                return Integer.parseInt(s);
            }
            return Integer.parseInt(s.substring(1)) / 16;
        }
        else {
            if (s.length() <= 1) {
                return offset;
            }
            s = s.substring(1);
            if (s.startsWith("b")) {
                return offset + Integer.parseInt(s.substring(1)) / 16;
            }
            return offset + Integer.parseInt(s);
        }
    }
    
    public static int parseNumber(final String s, final int defaultNumber) {
        if (s == null || s.isEmpty()) {
            return defaultNumber;
        }
        try {
            return Integer.parseInt(s);
        }
        catch (Exception e) {
            return defaultNumber;
        }
    }
    
    public static int clamp(final int current, final int min, final int max) {
        return (current < min) ? min : ((current > max) ? max : current);
    }
    
    public static int getGenType(final String type) {
        if (type == null || type.isEmpty()) {
            return 0;
        }
        if (type.equalsIgnoreCase("square")) {
            return 0;
        }
        if (type.equalsIgnoreCase("circle")) {
            return 1;
        }
        return 0;
    }
    
    public static int getProcessRule(final String rule) {
        if (rule == null || rule.isEmpty()) {
            return 1;
        }
        if (rule.equalsIgnoreCase("TerrainOnly")) {
            return 0;
        }
        if (rule.equalsIgnoreCase("PostProcessingOnly")) {
            return 2;
        }
        if (rule.equalsIgnoreCase("BlockingPostProcessing")) {
            return 3;
        }
        if (rule.equalsIgnoreCase("Retrogen")) {
            return 4;
        }
        return 1;
    }
    
    public static long getFullCount(final int xMin, final int zMin, final int xMax, final int zMax) {
        final int xSize = xMax - xMin;
        final int zSize = zMax - zMin;
        return xSize * xSize + zSize * zSize;
    }
    
    public static int getRingCount(final int min, final int max, final boolean circle) {
        final int minDia = min * 2;
        final int maxDia = max * 2;
        if (circle) {
            return (int)(maxDia * 3.141592653589793 - minDia * 3.141592653589793);
        }
        return maxDia * maxDia - minDia * minDia;
    }
    
    public static int getFullCount(final int radius, final boolean circle) {
        final int dia = radius * 2;
        return circle ? ((int)(dia * 3.141592653589793)) : (dia * dia);
    }
}
