// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.client.gui;

import org.lwjgl.input.Mouse;
import pregenerator.impl.network.packets.chunkRequest.KillWorldRequest;
import pregenerator.impl.network.packets.chunkRequest.EntityRequestPacket;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiButton;
import java.util.Iterator;
import java.util.Collections;
import java.util.Map;
import pregenerator.base.api.network.PregenPacket;
import pregenerator.impl.network.packets.DimRequestPacket;
import pregenerator.ChunkPregenerator;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Comparator;
import pregenerator.base.impl.misc.SelectionList;
import pregenerator.base.impl.gui.GuiPregenBase;

public class GuiWorldView extends GuiPregenBase
{
    public static final GuiWorldView ENTITIES;
    public static final GuiWorldView TILE_ENTITIES;
    SelectionList<Integer> dimensions;
    int answerType;
    int packetsLeft;
    SelectionList<GuiTypeInfo.TypeEntry> typeList;
    SelectionList<Comparator<GuiTypeInfo.TypeEntry>> sorters;
    List<String> sorterNames;
    int ticker;
    int type;
    
    public GuiWorldView(final int type) {
        this.dimensions = new SelectionList<Integer>();
        this.answerType = -2;
        this.typeList = new SelectionList<GuiTypeInfo.TypeEntry>();
        this.sorters = new SelectionList<Comparator<GuiTypeInfo.TypeEntry>>(this.createSorters());
        this.sorterNames = new ArrayList<String>(Arrays.asList("Count", "Name"));
        this.type = type;
    }
    
    public void openUI() {
        this.ticker = 0;
        ChunkPregenerator.networking.sendPacketToServer(new DimRequestPacket(2 + this.type));
    }
    
    public void onCleared() {
        this.answerType = -1;
        this.packetsLeft = 0;
        this.dimensions.clear();
        this.typeList.clear();
    }
    
    public void noDataFound() {
        this.answerType = -2;
        this.ticker = 0;
    }
    
    public void addDims(final List<Integer> list) {
        final boolean wasEmpty = this.dimensions.size() <= 0;
        this.dimensions.setValues(list);
        if (wasEmpty) {
            this.dimensions.setIndexFromValue(0);
            this.typeList.clear();
        }
    }
    
    public void addStuff(final Map<String, Integer> data, final int type) {
        for (final Map.Entry<String, Integer> entry : data.entrySet()) {
            this.typeList.addValue(new GuiTypeInfo.TypeEntry(entry.getKey(), entry.getValue()));
        }
        this.packetsLeft = type;
        this.answerType = 1;
        if (type == 0) {
            this.answerType = 2;
            Collections.sort(this.typeList.getValues(), this.sorters.getValue());
            this.typeList.setIndex(0);
        }
    }
    
    public int getDimension() {
        final Integer value = this.dimensions.getValue();
        return (value == null) ? 0 : value;
    }
    
    @Override
    public void func_73866_w_() {
        super.func_73866_w_();
        this.field_146292_n.clear();
        this.registerButton(0, -30, 85, 60, 20, "Back");
        this.registerButton(1, 100, -85, 20, 20, "-");
        this.registerButton(2, 121, -85, 20, 20, "+");
        this.registerButton(3, -145, -97, 95, 20, "Dim: " + this.getDimension());
        this.registerButton(4, -145, -75, 40, 20, func_146272_n() ? "Clear" : "Reload");
        this.registerButton(5, -50, -97, 60, 20, "Reload Dim");
        this.registerButton(6, 12, -97, 80, 20, "Sorter: " + this.sorterNames.get(this.sorters.getIndex()));
        int offset = -50;
        for (int i = 0; i + this.typeList.getIndex() < this.typeList.size() && i < 5; ++i) {
            this.registerButton(10 + i, 104, offset, 40, 20, "Delete");
            offset += 26;
        }
    }
    
    protected void func_146284_a(final GuiButton button) {
        int id = button.field_146127_k;
        if (id == 0) {
            this.field_146297_k.func_147108_a((GuiScreen)new GuiWorldStuff(new GuiPregenMenu()));
        }
        else if (id == 2) {
            this.typeList.next();
        }
        else if (id == 1) {
            this.typeList.prev();
        }
        if (id == 3) {
            if (func_146272_n()) {
                this.dimensions.prev();
                this.typeList.clear();
            }
            else {
                this.dimensions.next();
                this.typeList.clear();
            }
        }
        else if (id == 4) {
            this.typeList.clear();
            if (func_146272_n()) {
                this.answerType = -1;
            }
            else {
                ChunkPregenerator.networking.sendPacketToServer(new EntityRequestPacket(this.getDimension(), this.type == 1));
                this.answerType = 0;
            }
        }
        else if (id == 5) {
            ChunkPregenerator.networking.sendPacketToServer(new DimRequestPacket(2 + this.type));
        }
        else if (id == 6) {
            if (func_146272_n()) {
                this.sorters.prev();
                Collections.sort(this.typeList.getValues(), this.sorters.getValue());
                this.typeList.setIndex(0);
            }
            else {
                this.sorters.next();
                Collections.sort(this.typeList.getValues(), this.sorters.getValue());
                this.typeList.setIndex(0);
            }
        }
        else if (id >= 10) {
            id -= 10;
            final GuiTypeInfo.TypeEntry entry = this.typeList.removeIndex(this.typeList.getIndex() + id);
            ChunkPregenerator.networking.sendPacketToServer(new KillWorldRequest(this.getDimension(), entry.s, this.type == 1));
        }
    }
    
    public void func_73863_a(final int mouseX, final int mouseY, final float partialTicks) {
        final int effect = Mouse.getDWheel() / 120;
        this.typeList.setIndex(this.typeList.getIndex() - effect);
        this.func_73866_w_();
        ++this.ticker;
        this.func_146276_q_();
        this.drawRectangle(150, 110, 0, 0, -3750202, false);
        int startY = -40;
        int end = 0;
        for (int i = 0; i + this.typeList.getIndex() < this.typeList.size() && i < 5; ++i) {
            this.drawRectangle(145, 11, 0, startY, -7631989, true);
            startY += 26;
            ++end;
        }
        super.func_73863_a(mouseX, mouseY, partialTicks);
        this.drawCenterText((this.type == 0) ? "Entity Types" : "TileEntity Types", 4, -109, 4210752);
        this.drawLeftText(this.typeList.getIndex() + "/" + this.typeList.size(), 144, -60, 4210752);
        if (this.answerType == -1) {
            this.drawText("<-- Require Reloading", -100, -68, 4210752);
        }
        else if (this.answerType == -2) {
            this.drawText("No " + ((this.type == 0) ? "Entities" : "TileEntities") + " Found", -100, -68, 4210752);
            if (this.ticker >= 200) {
                this.answerType = 2;
            }
        }
        else if (this.answerType == 1) {
            this.drawText("Awaiting Packets: " + this.packetsLeft, -100, -68, 4210752);
        }
        else if (this.answerType != 2) {
            this.drawText("Reloading: ", -100, -68, 4210752);
            final StringBuilder build = new StringBuilder();
            final int index = this.ticker / 20 % 5;
            for (int j = 0; j < 5; ++j) {
                if (j == index) {
                    build.append("O");
                }
                else {
                    build.append("o");
                }
            }
            this.drawText(build.toString(), -45, -68, 4210752);
        }
        startY = -40;
        for (int i = 0; i < end; ++i) {
            final GuiTypeInfo.TypeEntry entry = this.typeList.getValue(i + this.typeList.getIndex());
            this.drawText("Type: " + entry.s, -144, -9 + startY, 4210752);
            this.drawText("Count: " + entry.count, -144, 1 + startY, 4210752);
            startY += 26;
        }
    }
    
    public boolean func_73868_f() {
        return false;
    }
    
    public List<Comparator<GuiTypeInfo.TypeEntry>> createSorters() {
        final List<Comparator<GuiTypeInfo.TypeEntry>> sorters = new ArrayList<Comparator<GuiTypeInfo.TypeEntry>>();
        sorters.add(new Comparator<GuiTypeInfo.TypeEntry>() {
            @Override
            public int compare(final GuiTypeInfo.TypeEntry o1, final GuiTypeInfo.TypeEntry o2) {
                if (o1.count > o2.count) {
                    return -1;
                }
                if (o2.count > o1.count) {
                    return 1;
                }
                return 0;
            }
        });
        sorters.add(new Comparator<GuiTypeInfo.TypeEntry>() {
            @Override
            public int compare(final GuiTypeInfo.TypeEntry o1, final GuiTypeInfo.TypeEntry o2) {
                return o1.s.compareTo(o2.s);
            }
        });
        return sorters;
    }
    
    static {
        ENTITIES = new GuiWorldView(0);
        TILE_ENTITIES = new GuiWorldView(1);
    }
}
