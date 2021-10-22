// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.client.gui;

import org.lwjgl.input.Mouse;
import pregenerator.impl.network.packets.chunkRequest.TPChunkPacket;
import pregenerator.impl.network.packets.chunkRequest.RemoveStructurePacket;
import pregenerator.impl.network.packets.chunkRequest.StructureRequestPacket;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiButton;
import java.util.Iterator;
import java.util.List;
import pregenerator.base.api.network.PregenPacket;
import pregenerator.impl.network.packets.DimRequestPacket;
import pregenerator.ChunkPregenerator;
import pregenerator.base.impl.misc.ListMap;
import pregenerator.impl.structure.StructureData;
import pregenerator.base.impl.misc.SelectionList;
import pregenerator.base.impl.gui.GuiPregenBase;

public class GuiStructureView extends GuiPregenBase
{
    public static final SelectionList<StructureData> DEFAULT_VALUE;
    public static GuiStructureView INSTANCE;
    SelectionList<Integer> dimensions;
    int answerType;
    int packetsLeft;
    ListMap<String, StructureData> structures;
    int ticker;
    
    public GuiStructureView() {
        this.dimensions = new SelectionList<Integer>();
        this.answerType = -2;
        this.structures = new ListMap<String, StructureData>();
    }
    
    public void openUI() {
        this.ticker = 0;
        ChunkPregenerator.networking.sendPacketToServer(new DimRequestPacket(4));
    }
    
    public void onCleared() {
        this.answerType = -1;
        this.packetsLeft = 0;
        this.dimensions.clear();
        this.structures.clear();
        this.structures.getOrCreate("All");
    }
    
    public void noDataFound() {
        this.answerType = -2;
        this.ticker = 0;
    }
    
    public void addData(final List<StructureData> data, final int type) {
        this.packetsLeft = type;
        for (final StructureData entry : data) {
            this.structures.getOrCreate("All").addValue(entry);
            this.structures.getOrCreate(entry.getType()).addValue(entry);
        }
        if (type == 0) {
            this.answerType = 2;
            this.structures.setIndex(0);
        }
    }
    
    public void addDims(final List<Integer> list) {
        final boolean wasEmpty = this.dimensions.size() <= 0;
        this.dimensions.setValues(list);
        if (wasEmpty) {
            this.dimensions.setIndexFromValue(0);
            this.structures.clear();
            this.structures.getOrCreate("All");
        }
    }
    
    public int getDimension() {
        final Integer value = this.dimensions.getValue();
        return (value == null) ? 0 : value;
    }
    
    @Override
    public void func_73866_w_() {
        super.func_73866_w_();
        this.registerButton(0, -30, 88, 60, 20, "Back");
        this.registerButton(1, 100, -81, 20, 20, "-");
        this.registerButton(2, 121, -81, 20, 20, "+");
        this.registerButton(3, -145, -102, 95, 20, "Dim: " + this.getDimension());
        this.registerButton(4, -145, -80, 40, 20, func_146272_n() ? "Clear" : "Reload");
        this.registerButton(5, -50, -102, 60, 20, "Reload Dim");
        this.registerButton(6, 11, -102, 120, 20, "Type: " + this.structures.getCurrentKey());
        int offset = 51;
        final int index = 10;
        final SelectionList<StructureData> data = this.getData();
        for (int i = 0; i < 3 && i + data.getIndex() < data.size(); ++i) {
            this.registerButton(index, 83, -offset + 21, 60, 20, "Remove");
            this.registerButton(index + 1, -18, -offset, 100, 20, "Remove From World");
            this.registerButton(index + 2, 83, -offset, 60, 20, "Teleport").field_146124_l = (this.field_146297_k.field_71439_g.field_71093_bK == this.getDimension());
            offset -= 47;
        }
    }
    
    protected void func_146284_a(final GuiButton button) {
        int id = button.field_146127_k;
        if (id == 0) {
            this.field_146297_k.func_147108_a((GuiScreen)new GuiWorldStuff(new GuiPregenMenu()));
        }
        else if (id == 2) {
            this.getData().next();
        }
        else if (id == 1) {
            this.getData().prev();
        }
        if (id == 3) {
            if (func_146272_n()) {
                this.dimensions.prev();
                this.structures.clear();
                this.structures.getOrCreate("All");
            }
            else {
                this.dimensions.next();
                this.structures.clear();
                this.structures.getOrCreate("All");
            }
        }
        else if (id == 4) {
            this.structures.clear();
            this.structures.getOrCreate("All");
            if (func_146272_n()) {
                this.answerType = -1;
            }
            else {
                ChunkPregenerator.networking.sendPacketToServer(new StructureRequestPacket(this.getDimension()));
                this.answerType = 0;
            }
        }
        else if (id == 5) {
            ChunkPregenerator.networking.sendPacketToServer(new DimRequestPacket(4));
        }
        else if (id == 6) {
            if (func_146272_n()) {
                this.structures.prev();
            }
            else {
                this.structures.next();
            }
            button.field_146126_j = "Type: " + this.structures.getCurrentKey();
        }
        else if (id >= 10) {
            id -= 10;
            final int index = id / 3;
            final int buttonID = id % 3;
            final SelectionList<StructureData> values = this.getData();
            final StructureData data = values.getValue(values.getIndex() + index);
            if (buttonID == 0 || buttonID == 1) {
                ChunkPregenerator.networking.sendPacketToServer(new RemoveStructurePacket(this.getDimension(), data, buttonID == 1));
                values.removeIndex(values.getIndex() + index);
            }
            else if (buttonID == 2) {
                ChunkPregenerator.networking.sendPacketToServer(new TPChunkPacket(data.getPos().toChunkPos()));
            }
        }
    }
    
    public void func_73863_a(final int mouseX, final int mouseY, final float partialTicks) {
        final int effect = Mouse.getDWheel() / 120;
        final SelectionList<StructureData> data = this.getData();
        data.setIndex(data.getIndex() - effect * (GuiScreen.func_146271_m() ? 10 : 1));
        this.func_73866_w_();
        ++this.ticker;
        this.func_146276_q_();
        this.drawRectangle(150, 110, 0, 0, -3750202, false);
        int startY = -30;
        int end = 0;
        for (int i = 0; i + data.getIndex() < data.size() && i < 3; ++i) {
            this.drawRectangle(145, 22, 0, startY, -7631989, true);
            startY += 47;
            ++end;
        }
        super.func_73863_a(mouseX, mouseY, partialTicks);
        this.drawCenterText("Structure View", 4, -110, 4210752);
        this.drawLeftText(data.getIndex() + "/" + data.size(), 144, -60, 4210752);
        if (this.answerType == -1) {
            this.drawText("<-- Require Reloading", -100, -68, 4210752);
        }
        else if (this.answerType == -2) {
            this.drawText("No Structures Found", -100, -68, 4210752);
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
        startY = -49;
        for (int i = 0; i < end; ++i) {
            final StructureData entry = data.getValue(data.getIndex() + i);
            this.drawText("Type: " + entry.getType(), -144, startY, 4210752);
            startY += 10;
            this.drawText("BlockX: " + entry.getX(), -144, startY, 4210752);
            startY += 10;
            this.drawText("BlockZ: " + entry.getZ(), -144, startY, 4210752);
            startY += 10;
            this.drawText("Will Generate: " + entry.isSuitable(), -144, startY, 4210752);
            this.drawText("Parts: " + entry.getParts(), -40, startY, 4210752);
            startY += 17;
        }
    }
    
    public boolean func_73868_f() {
        return false;
    }
    
    private SelectionList<StructureData> getData() {
        final SelectionList<StructureData> data = this.structures.getCurrentValues();
        return (data == null) ? GuiStructureView.DEFAULT_VALUE : data;
    }
    
    static {
        DEFAULT_VALUE = new SelectionList<StructureData>();
        GuiStructureView.INSTANCE = new GuiStructureView();
    }
}
