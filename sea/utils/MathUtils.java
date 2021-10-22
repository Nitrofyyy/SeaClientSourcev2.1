// 
// Decompiled by Procyon v0.5.36
// 

package sea.utils;

import net.minecraft.util.MathHelper;

public class MathUtils
{
    private static final double[] a;
    private static final double[] b;
    
    static {
        a = new double[65536];
        b = new double[360];
        for (int i = 0; i < 65536; ++i) {
            MathUtils.a[i] = Math.sin(i * 3.141592653589793 * 2.0 / 65536.0);
        }
        for (int i = 0; i < 360; ++i) {
            MathUtils.b[i] = Math.sin(Math.toRadians(i));
        }
    }
    
    public static double getAngle(int paramInt) {
        paramInt %= 360;
        return MathUtils.b[paramInt];
    }
    
    public static double getRightAngle(int paramInt) {
        paramInt += 90;
        paramInt %= 360;
        return MathUtils.b[paramInt];
    }
    
    private static float snapToStep(float value, final float valueStep) {
        if (valueStep > 0.0f) {
            value = valueStep * Math.round(value / valueStep);
        }
        return value;
    }
    
    public static float normalizeValue(final float p_148266_1_, final float valueMin, final float valueMax, final float valueStep) {
        return MathHelper.clamp_float((snapToStepClamp(p_148266_1_, valueMin, valueMax, valueStep) - valueMin) / (valueMax - valueMin), 0.0f, 1.0f);
    }
    
    private static float snapToStepClamp(float value, final float valueMin, final float valueMax, final float valueStep) {
        value = snapToStep(value, valueStep);
        return MathHelper.clamp_float(value, valueMin, valueMax);
    }
    
    public static float denormalizeValue(final float value, final float valueMin, final float valueMax, final float valueStep) {
        return snapToStepClamp(valueMin + (valueMax - valueMin) * MathHelper.clamp_float(value, 0.0f, 1.0f), valueMin, valueMax, valueStep);
    }
}
