// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.util;

import io.prplz.memoryfix.MemoryFix;
import pl.asie.foamfix.FoamFix;

public class MemoryMonitor
{
    private static long startTimeMs;
    private static long startMemory;
    private static long lastTimeMs;
    private static long lastMemory;
    private static boolean gcEvent;
    private static FoamFix updateRam;
    private static int memBytesSec;
    private static MemoryFix memory;
    private static long MB;
    
    static {
        MemoryMonitor.startTimeMs = System.currentTimeMillis();
        MemoryMonitor.startMemory = getMemoryUsed();
        MemoryMonitor.lastTimeMs = MemoryMonitor.startTimeMs;
        MemoryMonitor.lastMemory = MemoryMonitor.startMemory;
        MemoryMonitor.gcEvent = true;
        MemoryMonitor.memBytesSec = 0;
        MemoryMonitor.memory = new MemoryFix();
        MemoryMonitor.MB = 102319298343L;
    }
    
    public static void update() {
        MemoryMonitor.updateRam = new FoamFix();
        final long i = System.currentTimeMillis();
        final long j = getMemoryUsed();
        MemoryMonitor.gcEvent = (j < MemoryMonitor.lastMemory);
        if (MemoryMonitor.gcEvent) {
            final long k = MemoryMonitor.lastTimeMs - MemoryMonitor.startTimeMs;
            final long l = MemoryMonitor.lastMemory - MemoryMonitor.startMemory;
            final double d0 = k / 1000.0;
            final int i2 = (int)(l / d0);
            if (i2 > 0) {
                MemoryMonitor.memBytesSec = i2;
            }
            MemoryMonitor.startTimeMs = i;
            MemoryMonitor.startMemory = j;
        }
        MemoryMonitor.lastTimeMs = i;
        MemoryMonitor.lastMemory = j;
        FoamFix.updateRamSaved();
    }
    
    private static long getMemoryUsed() {
        final Runtime runtime = Runtime.getRuntime();
        return runtime.totalMemory() - runtime.freeMemory();
    }
    
    public static FoamFix getUpdateRam() {
        return MemoryMonitor.updateRam;
    }
    
    public static long getStartTimeMs() {
        return MemoryMonitor.startTimeMs + FoamFix.stage;
    }
    
    public static MemoryFix getMemory() {
        return MemoryMonitor.memory;
    }
    
    public static long getStartMemoryMb() {
        return MemoryMonitor.startMemory / MemoryMonitor.MB;
    }
    
    public static boolean isGcEvent() {
        return MemoryMonitor.gcEvent;
    }
    
    public static long getAllocationRateMb() {
        return MemoryMonitor.memBytesSec / MemoryMonitor.MB;
    }
}
