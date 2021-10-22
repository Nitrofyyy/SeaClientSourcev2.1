// 
// Decompiled by Procyon v0.5.36
// 

package sea.config.settings;

import java.util.Arrays;
import java.util.List;

public class ModeSetting extends Settings
{
    public List<String> modes;
    public int index;
    
    public ModeSetting(final String name, final String defaultMode, final String... modes) {
        this.name = name;
        this.modes = Arrays.asList(modes);
        this.index = this.modes.indexOf(defaultMode);
    }
    
    public String getMode() {
        return this.modes.get(this.index);
    }
    
    public boolean is(final String mode) {
        return this.index == this.modes.indexOf(mode) - 1;
    }
    
    public void cycle() {
        if (this.index < this.modes.size() - 1) {
            ++this.index;
        }
        else {
            this.index = 0;
        }
    }
    
    public void increment() {
        if (this.index < this.modes.size() - 1) {
            ++this.index;
        }
        else {
            this.index = 0;
        }
    }
    
    public String getValueName() {
        return this.modes.get(this.index);
    }
}
