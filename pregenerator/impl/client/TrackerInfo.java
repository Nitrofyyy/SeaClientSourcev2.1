// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.client;

import org.lwjgl.opengl.GL11;
import net.minecraft.client.gui.ScaledResolution;
import pregenerator.base.api.misc.IRenderHelper;
import pregenerator.base.api.network.PregenPacket;
import pregenerator.impl.network.packets.TrackerRequestPacket;
import pregenerator.ChunkPregenerator;
import java.util.Iterator;
import java.util.ArrayList;
import pregenerator.base.api.misc.IConfig;
import pregenerator.impl.client.trackerInfo.TrackerEntry;
import java.util.List;

public class TrackerInfo
{
    List<TrackerEntry> toRender;
    public boolean running;
    public boolean shouldShow;
    public boolean big;
    public int updateFrequency;
    public ScreenPosition xPos;
    public ScreenPosition yPos;
    int ticker;
    IConfig config;
    public boolean showDetailed;
    public int targetDim;
    
    public TrackerInfo(final IConfig config) {
        this.toRender = new ArrayList<TrackerEntry>();
        this.running = false;
        this.shouldShow = false;
        this.big = false;
        this.updateFrequency = 20;
        this.xPos = ScreenPosition.NEGATIVE;
        this.yPos = ScreenPosition.POSITIVE;
        this.ticker = 0;
        this.showDetailed = false;
        this.targetDim = 0;
        this.config = config;
    }
    
    public void loadConfig() {
        this.xPos = ScreenPosition.byXName(this.config.getString("tracking", "XPos", ScreenPosition.NEGATIVE.getXName()));
        this.yPos = ScreenPosition.byYName(this.config.getString("tracking", "YPos", ScreenPosition.POSITIVE.getYName()));
        this.updateFrequency = this.config.getInt("tracking", "Update Frequency", 20);
        this.shouldShow = this.config.getBoolean("tracking", "Show UI", false);
        this.showDetailed = this.config.getBoolean("tracking", "Show Detailed", false);
        this.targetDim = this.config.getInt("tracking", "Detailed Dimension", this.targetDim);
        this.big = this.config.getBoolean("tracking", "BigUI", false);
        this.showDetailed = this.config.getBoolean("tracking", "ShowDetailed", false);
    }
    
    public void setDetailed(final boolean detailed) {
        this.showDetailed = detailed;
        this.config.setBoolean("tracking", "ShowDetailed", detailed);
    }
    
    public void setBig(final boolean big) {
        this.big = big;
        this.config.setBoolean("tracking", "BigUI", big);
    }
    
    public void setShow(final boolean show) {
        this.shouldShow = show;
        this.config.setBoolean("tracking", "Show UI", show);
    }
    
    public void setUpdateFrequency(final int freq) {
        this.updateFrequency = freq;
        this.config.setInt("tracking", "Update Frequency", freq);
    }
    
    public void setXPos(final ScreenPosition newPos) {
        this.xPos = newPos;
        this.config.setString("tracking", "XPos", this.xPos.getXName());
    }
    
    public void setYPos(final ScreenPosition newPos) {
        this.yPos = newPos;
        this.config.setString("tracking", "YPos", this.yPos.getYName());
    }
    
    public void saveEntry(final TrackerEntry entry) {
        this.config.setBoolean("tracking", entry.getName(), entry.isActive());
    }
    
    public void updateList() {
        this.toRender.clear();
        for (final TrackerEntry entry : TrackerEntry.getRegistry()) {
            if (entry.isActive()) {
                this.toRender.add(entry);
            }
        }
    }
    
    public void update() {
        if (!this.shouldShow) {
            return;
        }
        ++this.ticker;
        if (this.ticker % (21 - this.updateFrequency) == 0) {
            ChunkPregenerator.networking.sendPacketToServer(new TrackerRequestPacket(this.toRender));
        }
    }
    
    public void render(final IRenderHelper helper, final ScaledResolution res) {
        if (!this.shouldShow || !this.running) {
            return;
        }
        final int width = 100;
        int screensize = -6;
        for (final TrackerEntry entry : this.toRender) {
            if (entry.shouldRender()) {
                screensize += entry.getYOffset();
            }
        }
        final int x = this.xPos.getXPosition(res, width, this.big);
        int y = this.yPos.getYPosition(res, screensize, this.xPos, this.big);
        final int center = width / 2;
        GL11.glPushMatrix();
        if (this.big) {
            GL11.glScalef(2.0f, 2.0f, 1.0f);
            GL11.glTranslatef((float)(-(x / 2 + width)), (float)(-(y / 2 + screensize)), 0.0f);
        }
        helper.renderArea(x - center - 12, y, width - 10, screensize);
        helper.renderCenterText(x - center, y - 12, width, "Server Stats");
        y -= 6;
        for (final TrackerEntry entry2 : this.toRender) {
            if (entry2.shouldRender()) {
                final float progress = this.clamp(0.0f, 1.0f, entry2.currentValue() / (float)entry2.maxValue());
                entry2.render(x, y, progress, width, helper);
                y += entry2.getYOffset();
            }
        }
        GL11.glPopMatrix();
    }
    
    private float clamp(final float min, final float max, final float current) {
        return (current < min) ? min : ((current > max) ? max : current);
    }
}
