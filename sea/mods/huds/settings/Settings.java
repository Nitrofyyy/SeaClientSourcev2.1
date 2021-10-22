// 
// Decompiled by Procyon v0.5.36
// 

package sea.mods.huds.settings;

import sea.event.impl.EventUpdate;
import java.util.Iterator;
import sea.mods.huds.settings.impl.CpsNoBG2;
import sea.mods.huds.settings.impl.PingNoBG;
import sea.mods.huds.settings.impl.TimeNoBg;
import sea.mods.huds.settings.impl.FpsNoBG;
import sea.mods.huds.settings.impl.KeyStoke3;
import sea.mods.huds.settings.impl.KeyStoke2;
import sea.mods.huds.settings.impl.KeyStroke1;
import sea.mods.huds.settings.impl.Ping3;
import sea.mods.huds.settings.impl.Ping2;
import sea.mods.huds.settings.impl.Ping1;
import sea.mods.huds.settings.impl.Cps3;
import sea.mods.huds.settings.impl.Cps2;
import sea.mods.huds.settings.impl.Cps;
import sea.mods.huds.settings.impl.Clock3;
import sea.mods.huds.settings.impl.Clock2;
import sea.mods.huds.settings.impl.Clock1;
import sea.mods.huds.settings.impl.FpsSettings3;
import sea.mods.huds.settings.impl.FpsSettings2;
import sea.mods.huds.settings.impl.FpsSettings1;
import sea.mods.huds.settings.impl.Time3;
import sea.mods.huds.settings.impl.Time2;
import sea.mods.huds.settings.impl.Time1;
import sea.mods.huds.HudMod;
import java.util.ArrayList;

public class Settings
{
    public ArrayList<HudMod> hudMods;
    public static Settings INSTANCE;
    public static Time1 time1;
    public static Time2 time2;
    public static Time3 time3;
    public static FpsSettings1 fps1;
    public static FpsSettings2 fps2;
    public static FpsSettings3 fps3;
    public static Clock1 clock1;
    public static Clock2 clock2;
    public static Clock3 clock3;
    public static Cps cps1;
    public static Cps2 cps2;
    public static Cps3 cps3;
    public static Ping1 ping1;
    public static Ping2 ping2;
    public static Ping3 ping3;
    public static KeyStroke1 key1;
    public static KeyStoke2 key2;
    public static KeyStoke3 key3;
    public static FpsNoBG nobg;
    public static TimeNoBg timenobg;
    public static PingNoBG pingnobg;
    public static CpsNoBG2 cpsnobg;
    
    public Settings() {
        (this.hudMods = new ArrayList<HudMod>()).add(Settings.time1 = new Time1());
        this.hudMods.add(Settings.time2 = new Time2());
        this.hudMods.add(Settings.time3 = new Time3());
        this.hudMods.add(Settings.fps1 = new FpsSettings1());
        this.hudMods.add(Settings.fps2 = new FpsSettings2());
        this.hudMods.add(Settings.fps3 = new FpsSettings3());
        this.hudMods.add(Settings.clock1 = new Clock1());
        this.hudMods.add(Settings.clock2 = new Clock2());
        this.hudMods.add(Settings.clock3 = new Clock3());
        this.hudMods.add(Settings.cps1 = new Cps());
        this.hudMods.add(Settings.cps2 = new Cps2());
        this.hudMods.add(Settings.cps3 = new Cps3());
        this.hudMods.add(Settings.ping1 = new Ping1());
        this.hudMods.add(Settings.ping2 = new Ping2());
        this.hudMods.add(Settings.ping3 = new Ping3());
        this.hudMods.add(Settings.key1 = new KeyStroke1());
        this.hudMods.add(Settings.key2 = new KeyStoke2());
        this.hudMods.add(Settings.key3 = new KeyStoke3());
        this.hudMods.add(Settings.timenobg = new TimeNoBg());
        this.hudMods.add(Settings.nobg = new FpsNoBG());
        this.hudMods.add(Settings.cpsnobg = new CpsNoBG2());
        this.hudMods.add(Settings.pingnobg = new PingNoBG());
    }
    
    public void renderMod() {
        for (final HudMod hm : this.hudMods) {
            if (hm.isEnabled()) {
                hm.draw();
            }
        }
    }
    
    public static KeyStroke1 getKey1() {
        return Settings.key1;
    }
    
    public static KeyStoke2 getKey2() {
        return Settings.key2;
    }
    
    public static KeyStoke3 getKey3() {
        return Settings.key3;
    }
    
    public static PingNoBG getPingnobg() {
        return Settings.pingnobg;
    }
    
    public static Clock1 getClock1() {
        return Settings.clock1;
    }
    
    public static Ping1 getPing1() {
        return Settings.ping1;
    }
    
    public static Ping2 getPing2() {
        return Settings.ping2;
    }
    
    public static Ping3 getPing3() {
        return Settings.ping3;
    }
    
    public static Clock2 getClock2() {
        return Settings.clock2;
    }
    
    public static Clock3 getClock3() {
        return Settings.clock3;
    }
    
    public static TimeNoBg getTimenobg() {
        return Settings.timenobg;
    }
    
    public static Cps getCps1() {
        return Settings.cps1;
    }
    
    public static Cps2 getCps2() {
        return Settings.cps2;
    }
    
    public static Cps3 getCps3() {
        return Settings.cps3;
    }
    
    public static CpsNoBG2 getCpsnobg() {
        return Settings.cpsnobg;
    }
    
    public static FpsNoBG getNobg() {
        return Settings.nobg;
    }
    
    public static FpsSettings1 getFps1() {
        return Settings.fps1;
    }
    
    public static FpsSettings2 getFps2() {
        return Settings.fps2;
    }
    
    public static FpsSettings3 getFps3() {
        return Settings.fps3;
    }
    
    public static Time1 getTime1() {
        return Settings.time1;
    }
    
    public static Time2 getTime2() {
        return Settings.time2;
    }
    
    public static Time3 getTime3() {
        return Settings.time3;
    }
    
    public static Settings getInstance() {
        return Settings.INSTANCE;
    }
    
    public EventUpdate[] getRegisteredRenderers() {
        return null;
    }
}
