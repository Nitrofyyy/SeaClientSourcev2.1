// 
// Decompiled by Procyon v0.5.36
// 

package sea.mods;

import sea.mods.impl.AutoGG;
import sea.mods.impl.Animtaions;
import java.util.ArrayList;
import sea.mods.impl.ToggleSprint;
import sea.mods.impl.FullBright;
import sea.mods.impl.ModPerspective;

public class ModList
{
    public static ModPerspective perspective;
    public FullBright full;
    public ToggleSprint sprint;
    public ArrayList<Module> mods;
    public Animtaions oldanimations;
    public AutoGG auto;
    
    public ModList() {
        (this.mods = new ArrayList<Module>()).add(this.sprint = new ToggleSprint());
        this.mods.add(ModList.perspective = new ModPerspective());
        this.mods.add(this.full = new FullBright());
        this.mods.add(this.oldanimations = new Animtaions());
        this.mods.add(this.auto = new AutoGG());
    }
    
    public static ModPerspective getPerspective() {
        return ModList.perspective;
    }
}
