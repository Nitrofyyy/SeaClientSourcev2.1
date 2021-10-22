// 
// Decompiled by Procyon v0.5.36
// 

package sea.config.settings;

public class BooleanSetting extends Settings
{
    public static boolean enabled;
    
    public BooleanSetting(final String name, final boolean enabled) {
        this.name = name;
        BooleanSetting.enabled = enabled;
    }
    
    public boolean isEnabled() {
        return BooleanSetting.enabled;
    }
    
    public void setEnabled(final boolean enabled) {
        BooleanSetting.enabled = enabled;
    }
    
    public void toggle() {
        BooleanSetting.enabled = !BooleanSetting.enabled;
    }
    
    public boolean isOn() {
        return BooleanSetting.enabled;
    }
}
