// 
// Decompiled by Procyon v0.5.36
// 

package sea.mods.huds.settings.color;

import sea.event.impl.EventUpdate;
import java.util.Iterator;
import sea.mods.huds.settings.color.impl.FpsGold;
import sea.mods.huds.settings.color.impl.FPSPink;
import sea.mods.huds.settings.color.impl.FPSAqua;
import sea.mods.huds.settings.color.impl.FPSYellow;
import sea.mods.huds.settings.color.impl.FPSBlue;
import sea.mods.huds.settings.color.impl.FPSGreen;
import sea.mods.huds.settings.color.impl.Blockwhite;
import sea.mods.huds.settings.color.impl.Blockpink;
import sea.mods.huds.settings.color.impl.Blockaqua;
import sea.mods.huds.settings.color.impl.Blockyellow;
import sea.mods.huds.settings.color.impl.Blockblue;
import sea.mods.huds.settings.color.impl.Blockgreen;
import sea.mods.huds.HudMod;
import java.util.ArrayList;

public class ColorList
{
    public ArrayList<HudMod> hudMods;
    public static Blockgreen blockgreen;
    public static Blockblue blockblue;
    public static Blockyellow blockyellow;
    public static Blockaqua blockaqua;
    public static Blockpink blockpink;
    public static Blockwhite blockwhite;
    public static FPSGreen FPSgreen;
    public static FPSBlue FPSblue;
    public static FPSYellow FPSyellow;
    public static FPSAqua FPSaqua;
    public static FPSPink FPSpink;
    public static FpsGold FPSgold;
    public static ColorList INSTANCE;
    
    public ColorList() {
        (this.hudMods = new ArrayList<HudMod>()).add(ColorList.blockgreen = new Blockgreen());
        this.hudMods.add(ColorList.blockblue = new Blockblue());
        this.hudMods.add(ColorList.blockyellow = new Blockyellow());
        this.hudMods.add(ColorList.blockaqua = new Blockaqua());
        this.hudMods.add(ColorList.blockpink = new Blockpink());
        this.hudMods.add(ColorList.blockwhite = new Blockwhite());
        this.hudMods.add(ColorList.FPSgreen = new FPSGreen());
        this.hudMods.add(ColorList.FPSblue = new FPSBlue());
        this.hudMods.add(ColorList.FPSyellow = new FPSYellow());
        this.hudMods.add(ColorList.FPSaqua = new FPSAqua());
        this.hudMods.add(ColorList.FPSpink = new FPSPink());
        this.hudMods.add(ColorList.FPSgold = new FpsGold());
    }
    
    public void renderMod() {
        for (final HudMod hm : this.hudMods) {
            if (hm.isEnabled()) {
                hm.draw();
            }
        }
    }
    
    public static FPSAqua getFPSaqua() {
        return ColorList.FPSaqua;
    }
    
    public static FPSBlue getFPSblue() {
        return ColorList.FPSblue;
    }
    
    public static FpsGold getFPSgold() {
        return ColorList.FPSgold;
    }
    
    public static FPSGreen getFPSgreen() {
        return ColorList.FPSgreen;
    }
    
    public static FPSPink getFPSpink() {
        return ColorList.FPSpink;
    }
    
    public static FPSYellow getFPSyellow() {
        return ColorList.FPSyellow;
    }
    
    public static Blockaqua getBlockaqua() {
        return ColorList.blockaqua;
    }
    
    public static Blockblue getBlockblue() {
        return ColorList.blockblue;
    }
    
    public static Blockgreen getBlockgreen() {
        return ColorList.blockgreen;
    }
    
    public static Blockpink getBlockpink() {
        return ColorList.blockpink;
    }
    
    public static Blockwhite getBlockwhite() {
        return ColorList.blockwhite;
    }
    
    public static Blockyellow getBlockyellow() {
        return ColorList.blockyellow;
    }
    
    public static ColorList getInstance() {
        return ColorList.INSTANCE;
    }
    
    public EventUpdate[] getRegisteredRenderers() {
        return null;
    }
}
