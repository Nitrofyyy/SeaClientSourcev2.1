// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.client.gui;

import org.lwjgl.input.Mouse;
import pregenerator.base.api.network.PregenPacket;
import pregenerator.impl.network.packets.chunkRequest.KillRequest;
import pregenerator.ChunkPregenerator;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiButton;
import java.util.Iterator;
import net.minecraft.world.biome.BiomeGenBase;
import java.util.HashMap;
import pregenerator.impl.tracking.ChunkEntry;
import pregenerator.base.impl.misc.SelectionList;
import java.util.Map;
import pregenerator.impl.misc.FilePos;
import pregenerator.base.impl.gui.GuiPregenBase;

public class GuiTypeInfo extends GuiPregenBase
{
    final FilePos position;
    final int type;
    Map<String, Integer> map;
    SelectionList<TypeEntry> list;
    
    public GuiTypeInfo(final ChunkEntry key, final int type) {
        this.list = (SelectionList<TypeEntry>)new SelectionList().setNoLoop();
        this.position = key.getPos();
        this.type = type;
        if (type == 0) {
            this.map = key.entityCount;
            for (final Map.Entry<String, Integer> entry : key.entityCount.entrySet()) {
                this.list.addValue(new TypeEntry(entry.getKey(), entry.getValue()));
            }
        }
        else if (type == 1) {
            this.map = key.tileEntityCount;
            for (final Map.Entry<String, Integer> entry : key.tileEntityCount.entrySet()) {
                this.list.addValue(new TypeEntry(entry.getKey(), entry.getValue()));
            }
        }
        else if (type == 2) {
            this.map = new HashMap<String, Integer>();
            for (final Map.Entry<Integer, Integer> entry2 : key.biomeCount.entrySet()) {
                final BiomeGenBase biome = BiomeGenBase.func_150568_d((int)entry2.getKey());
                if (biome == null) {
                    continue;
                }
                final String name = biome.field_76791_y;
                this.list.addValue(new TypeEntry(name, entry2.getValue()));
                this.map.put(name, entry2.getValue());
            }
        }
    }
    
    @Override
    public void func_73866_w_() {
        super.func_73866_w_();
        this.registerButton(0, -30, 75, 60, 20, "Back");
        this.registerButton(1, 100, -100, 20, 20, "-");
        this.registerButton(2, 121, -100, 20, 20, "+");
        if (this.type != 2) {
            int yOffset = -65;
            for (int i = 0; i + this.list.getIndex() < this.list.size() && i < 5; ++i) {
                this.registerButton(10 + i, 104, yOffset, 40, 20, "Delete");
                yOffset += 26;
            }
        }
    }
    
    protected void func_146284_a(final GuiButton button) {
        int id = button.field_146127_k;
        if (id == 0) {
            this.field_146297_k.func_147108_a((GuiScreen)GuiChunkInfo.INSTANCE);
        }
        else if (id == 1) {
            this.list.prev();
        }
        else if (id == 2) {
            this.list.next();
        }
        else if (id >= 10) {
            id -= 10;
            final TypeEntry theType = this.list.getValue(this.list.getIndex() + id);
            this.map.remove(theType.s);
            ChunkPregenerator.networking.sendPacketToServer(new KillRequest(GuiChunkInfo.INSTANCE.getDimension(), this.position, this.type, theType.s));
        }
    }
    
    public void func_73863_a(final int mouseX, final int mouseY, final float partialTicks) {
        final int effect = Mouse.getDWheel() / 120;
        this.list.setIndex(this.list.getIndex() - effect);
        this.func_73866_w_();
        this.func_146276_q_();
        this.drawRectangle(150, 100, 0, 0, -3750202, false);
        int startY = -55;
        int end = 0;
        for (int i = 0; i + this.list.getIndex() < this.list.size() && i < 5; ++i) {
            this.drawRectangle(145, 11, 0, startY, -7631989, true);
            startY += 26;
            ++end;
        }
        super.func_73863_a(mouseX, mouseY, partialTicks);
        this.drawCenterText((this.type == 0) ? "Entity Types" : ((this.type == 1) ? "TileEntity Types" : "Biome Types"), 4, -95, 4210752);
        this.drawLeftText(this.list.getIndex() + "/" + this.list.size(), 144, -75, 4210752);
        startY = -55;
        for (int i = 0; i < end; ++i) {
            final TypeEntry entry = this.list.getValue(this.list.getIndex() + i);
            this.drawText("Type: " + entry.s, -144, -9 + startY, 4210752);
            this.drawText("Count: " + entry.count, -144, 1 + startY, 4210752);
            startY += 26;
        }
    }
    
    public boolean func_73868_f() {
        return false;
    }
    
    public static class TypeEntry
    {
        String s;
        int count;
        
        public TypeEntry(final String id, final int amount) {
            this.s = id;
            this.count = amount;
        }
    }
}
