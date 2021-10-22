// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.client;

import net.minecraft.client.gui.ScaledResolution;

public enum ScreenPosition
{
    NEGATIVE("Left", "Top"), 
    CENTER("Center", "Center"), 
    POSITIVE("Right", "Bottom");
    
    String xValue;
    String yValue;
    
    private ScreenPosition(final String x, final String y) {
        this.xValue = x;
        this.yValue = y;
    }
    
    public static ScreenPosition byXName(final String name) {
        if (name.equalsIgnoreCase("Left")) {
            return ScreenPosition.NEGATIVE;
        }
        if (name.equalsIgnoreCase("Right")) {
            return ScreenPosition.POSITIVE;
        }
        return ScreenPosition.CENTER;
    }
    
    public static ScreenPosition byYName(final String name) {
        if (name.equalsIgnoreCase("Top")) {
            return ScreenPosition.NEGATIVE;
        }
        if (name.equalsIgnoreCase("Bottom")) {
            return ScreenPosition.POSITIVE;
        }
        return ScreenPosition.CENTER;
    }
    
    public String getXName() {
        return this.xValue;
    }
    
    public String getYName() {
        return this.yValue;
    }
    
    public ScreenPosition getNext() {
        if (this == ScreenPosition.NEGATIVE) {
            return ScreenPosition.CENTER;
        }
        if (this == ScreenPosition.CENTER) {
            return ScreenPosition.POSITIVE;
        }
        return ScreenPosition.NEGATIVE;
    }
    
    public int getXPosition(final ScaledResolution res, final int width, final boolean big) {
        if (big) {
            switch (this) {
                case CENTER: {
                    return (int)(res.func_78326_a() / 2.0) + 200;
                }
                case POSITIVE: {
                    return res.func_78326_a() + 80;
                }
                default: {
                    return width * 3 + 20;
                }
            }
        }
        else {
            switch (this) {
                case CENTER: {
                    return res.func_78326_a() / 2;
                }
                case POSITIVE: {
                    return res.func_78326_a() - (width - 40);
                }
                default: {
                    return 60;
                }
            }
        }
    }
    
    public int getYPosition(final ScaledResolution res, final int height, final ScreenPosition xPos, final boolean big) {
        if (big) {
            switch (this) {
                case CENTER: {
                    return res.func_78328_b() / 2 + 50;
                }
                case POSITIVE: {
                    if (xPos == ScreenPosition.CENTER) {
                        return res.func_78328_b() - height / 2;
                    }
                    return res.func_78328_b() - 20;
                }
                default: {
                    return height * 3 - 10;
                }
            }
        }
        else {
            switch (this) {
                case CENTER: {
                    return res.func_78328_b() / 2;
                }
                case POSITIVE: {
                    if (xPos == ScreenPosition.CENTER) {
                        return res.func_78328_b() - (height + 30);
                    }
                    return res.func_78328_b() - (height + 5);
                }
                default: {
                    return 20;
                }
            }
        }
    }
}
