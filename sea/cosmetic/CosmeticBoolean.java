// 
// Decompiled by Procyon v0.5.36
// 

package sea.cosmetic;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;

public class CosmeticBoolean
{
    public static boolean cape1;
    public static boolean haloblue;
    public static boolean halogreen;
    public static boolean snowcape;
    public static boolean skycape;
    public static boolean rickcape;
    public static boolean dragonWings;
    public static boolean dragonWings2;
    public static boolean dragonWings3;
    public static boolean dragonWings4;
    public static boolean dragonWings5;
    public static boolean dragonWings6;
    
    static {
        CosmeticBoolean.cape1 = true;
        CosmeticBoolean.haloblue = true;
        CosmeticBoolean.halogreen = false;
        CosmeticBoolean.snowcape = false;
        CosmeticBoolean.skycape = false;
        CosmeticBoolean.rickcape = false;
        CosmeticBoolean.dragonWings = true;
        CosmeticBoolean.dragonWings2 = false;
        CosmeticBoolean.dragonWings3 = false;
        CosmeticBoolean.dragonWings4 = false;
        CosmeticBoolean.dragonWings5 = false;
        CosmeticBoolean.dragonWings6 = false;
    }
    
    public static boolean isDragonWings4() {
        return CosmeticBoolean.dragonWings4;
    }
    
    public static boolean isDragonWings5() {
        return CosmeticBoolean.dragonWings5;
    }
    
    public static boolean isHalogreen() {
        return CosmeticBoolean.halogreen;
    }
    
    public static boolean isDragonWings6() {
        return CosmeticBoolean.dragonWings6;
    }
    
    public static boolean Cape1() {
        return CosmeticBoolean.cape1;
    }
    
    public static boolean isHaloblue() {
        return CosmeticBoolean.haloblue;
    }
    
    public static boolean isDragonWings3() {
        return CosmeticBoolean.dragonWings3;
    }
    
    public static boolean isDragonWings2() {
        return CosmeticBoolean.dragonWings2;
    }
    
    public static boolean DragonWings() {
        return CosmeticBoolean.dragonWings;
    }
    
    public static boolean shouldRenderTopHat(final AbstractClientPlayer player) {
        return player.getName().contains(Minecraft.getMinecraft().getSession().getUsername());
    }
    
    public static float[] getHaloColor(final AbstractClientPlayer player) {
        return new float[] { 0.0f, 51.0f, 153.0f, 255.0f };
    }
    
    public static boolean RickCape() {
        return CosmeticBoolean.rickcape;
    }
    
    public static boolean SnowCape() {
        return CosmeticBoolean.snowcape;
    }
    
    public static boolean SkyCape() {
        return CosmeticBoolean.skycape;
    }
}
