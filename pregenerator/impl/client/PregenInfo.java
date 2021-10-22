// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.client;

import pregenerator.base.api.network.PregenPacket;
import pregenerator.impl.network.packets.RequestPacket;
import java.util.Collection;
import pregenerator.ChunkPregenerator;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.gui.ScaledResolution;
import pregenerator.base.api.misc.IRenderHelper;
import java.util.Iterator;
import java.util.ArrayList;
import pregenerator.base.api.misc.IConfig;
import pregenerator.impl.client.infos.InfoEntry;
import java.util.List;

public class PregenInfo
{
    public List<InfoEntry> toRender;
    public boolean running;
    public boolean shouldShow;
    public boolean big;
    public int updateFrequency;
    public ScreenPosition xPos;
    public ScreenPosition yPos;
    int ticker;
    IConfig config;
    
    public PregenInfo(final IConfig config) {
        this.toRender = new ArrayList<InfoEntry>();
        this.running = false;
        this.shouldShow = false;
        this.big = false;
        this.updateFrequency = 20;
        this.xPos = ScreenPosition.POSITIVE;
        this.yPos = ScreenPosition.POSITIVE;
        this.ticker = 0;
        this.config = config;
    }
    
    public void loadConfig() {
        this.xPos = ScreenPosition.byXName(this.config.getString("general", "XPos", ScreenPosition.POSITIVE.getXName()));
        this.yPos = ScreenPosition.byYName(this.config.getString("general", "YPos", ScreenPosition.POSITIVE.getYName()));
        this.updateFrequency = this.config.getInt("general", "Update Frequency", 20);
        this.shouldShow = this.config.getBoolean("general", "Show UI", true);
        this.big = this.config.getBoolean("general", "BigUI", false);
    }
    
    public void setShow(final boolean show) {
        this.shouldShow = show;
        this.config.setBoolean("general", "Show UI", show);
    }
    
    public void setBig(final boolean big) {
        this.big = big;
        this.config.setBoolean("general", "BigUI", big);
    }
    
    public void setUpdateFrequency(final int freq) {
        this.updateFrequency = freq;
        this.config.setInt("general", "Update Frequency", freq);
    }
    
    public void setXPos(final ScreenPosition newPos) {
        this.xPos = newPos;
        this.config.setString("general", "XPos", this.xPos.getXName());
    }
    
    public void setYPos(final ScreenPosition newPos) {
        this.yPos = newPos;
        this.config.setString("general", "YPos", this.yPos.getYName());
    }
    
    public void saveEntry(final InfoEntry entry) {
        this.config.setBoolean("general", entry.getName(), entry.isActive());
    }
    
    public void updateList() {
        this.toRender.clear();
        for (final InfoEntry entry : InfoEntry.getRegistry()) {
            if (entry.isActive()) {
                this.toRender.add(entry);
            }
        }
    }
    
    public void render(final IRenderHelper helper, final ScaledResolution res) {
        if (!this.shouldShow || !this.running) {
            return;
        }
        final int width = 100;
        int screensize = -6;
        for (final InfoEntry entry : this.toRender) {
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
        helper.renderCenterText(x - center, y - 12, width, "Pregenerator Info");
        y -= 6;
        for (final InfoEntry entry2 : this.toRender) {
            if (entry2.shouldRender()) {
                final float progress = this.clamp(0.0f, 1.0f, entry2.currentValue() / (float)entry2.maxValue());
                entry2.render(x, y, progress, width, helper);
                y += entry2.getYOffset();
            }
        }
        GL11.glPopMatrix();
    }
    
    public void update() {
        if (!this.shouldShow) {
            return;
        }
        ++this.ticker;
        if (this.ticker % (21 - this.updateFrequency) == 0) {
            ChunkPregenerator.networking.sendPacketToServer(new RequestPacket(new ArrayList<InfoEntry>(this.toRender)));
        }
    }
    
    private float clamp(final float min, final float max, final float current) {
        return (current < min) ? min : ((current > max) ? max : current);
    }
}
