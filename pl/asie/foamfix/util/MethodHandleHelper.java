// 
// Decompiled by Procyon v0.5.36
// 

package pl.asie.foamfix.util;

import net.minecraftforge.fml.relauncher.ReflectionHelper;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandle;

public final class MethodHandleHelper
{
    private MethodHandleHelper() {
    }
    
    public static MethodHandle findFieldGetter(final Class c, final String... names) {
        try {
            return MethodHandles.lookup().unreflectGetter(ReflectionHelper.findField(c, names));
        }
        catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static MethodHandle findFieldSetter(final Class c, final String... names) {
        try {
            return MethodHandles.lookup().unreflectSetter(ReflectionHelper.findField(c, names));
        }
        catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static MethodHandle findFieldGetter(final String s, final String... names) {
        try {
            return findFieldGetter(Class.forName(s), names);
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static MethodHandle findFieldSetter(final String s, final String... names) {
        try {
            return findFieldSetter(Class.forName(s), names);
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
