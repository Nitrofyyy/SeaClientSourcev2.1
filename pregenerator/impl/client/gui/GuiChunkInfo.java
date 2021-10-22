// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.client.gui;

import java.util.ArrayList;
import net.minecraft.entity.player.EntityPlayer;
import pregenerator.impl.misc.FilePos;
import org.lwjgl.input.Mouse;
import java.io.IOException;
import pregenerator.impl.network.packets.chunkRequest.TPChunkPacket;
import net.minecraft.client.gui.GuiScreen;
import pregenerator.impl.network.packets.chunkRequest.ChunkRequest;
import net.minecraft.client.gui.GuiButton;
import java.util.Collections;
import pregenerator.base.api.network.PregenPacket;
import pregenerator.impl.network.packets.DimRequestPacket;
import pregenerator.ChunkPregenerator;
import java.util.Arrays;
import pregenerator.impl.client.infos.InfoEntry;
import java.util.List;
import pregenerator.impl.tracking.ChunkEntry;
import java.util.Comparator;
import pregenerator.base.impl.misc.SelectionList;
import java.text.DecimalFormat;
import pregenerator.base.impl.gui.GuiPregenBase;

public class GuiChunkInfo extends GuiPregenBase
{
    public static final GuiChunkInfo INSTANCE;
    DecimalFormat format;
    public final SelectionList<Comparator<ChunkEntry>> comparators;
    public final List<String> comparatorNames;
    int ticker;
    int answerType;
    int packetsLeft;
    SelectionList<Integer> dimensions;
    SelectionList<ChunkEntry> chunks;
    
    public GuiChunkInfo() {
        this.format = InfoEntry.FORMAT;
        this.comparators = new SelectionList<Comparator<ChunkEntry>>(this.createList());
        this.comparatorNames = Arrays.asList("Distance", "EntityCount", "TileCount", "TickingTileCount", "RandomUpdates");
        this.answerType = -1;
        this.packetsLeft = 0;
        this.dimensions = new SelectionList<Integer>();
        this.chunks = (SelectionList<ChunkEntry>)new SelectionList().setNoLoop();
    }
    
    public void openUI() {
        ChunkPregenerator.networking.sendPacketToServer(new DimRequestPacket(1));
        this.ticker = 0;
    }
    
    public void onCleared() {
        this.answerType = -1;
        this.packetsLeft = 0;
        this.comparators.setIndex(0);
        this.dimensions.clear();
        this.chunks.clear();
    }
    
    public void addChunks(final List<ChunkEntry> list, final int left) {
        this.chunks.addValues(list);
        this.packetsLeft = left;
        this.answerType = 1;
        if (this.packetsLeft == 0) {
            this.answerType = 2;
            Collections.sort(this.chunks.getValues(), this.comparators.getValue());
            this.chunks.setIndex(0);
        }
    }
    
    public void noDataFound() {
        this.answerType = -2;
        this.ticker = 0;
    }
    
    public void addDims(final List<Integer> list) {
        final Object last = this.dimensions.getValue();
        final boolean wasEmpty = this.dimensions.size() <= 0;
        this.dimensions.setValues(list);
        if (wasEmpty) {
            this.dimensions.setIndexFromValue(0);
        }
        if (last != this.dimensions.getValue()) {
            this.chunks.clear();
        }
    }
    
    public int getDimension() {
        final Integer value = this.dimensions.getValue();
        return (value == null) ? 0 : value;
    }
    
    protected void func_146284_a(final GuiButton button) throws IOException {
        int id = button.field_146127_k;
        if (id == 2) {
            this.openUI();
        }
        else if (id == 0) {
            if (func_146272_n()) {
                this.dimensions.prev();
                this.chunks.clear();
            }
            else {
                this.dimensions.next();
                this.chunks.clear();
            }
        }
        else if (id == 1) {
            this.chunks.clear();
            if (func_146272_n()) {
                this.answerType = -1;
            }
            else {
                ChunkPregenerator.networking.sendPacketToServer(new ChunkRequest(this.getDimension()));
                this.answerType = 0;
            }
        }
        else if (id == 3) {
            this.chunks.next();
        }
        else if (id == 4) {
            this.chunks.prev();
        }
        else if (id == 5) {
            if (func_146272_n()) {
                this.comparators.prev();
                Collections.sort(this.chunks.getValues(), this.comparators.getValue());
                this.chunks.setIndex(0);
            }
            else {
                this.comparators.next();
                Collections.sort(this.chunks.getValues(), this.comparators.getValue());
                this.chunks.setIndex(0);
            }
        }
        else if (id == 6) {
            this.field_146297_k.func_147108_a((GuiScreen)new GuiWorldStuff(new GuiPregenMenu()));
        }
        else if (id >= 10) {
            id -= 10;
            final int index = id / 4;
            final int buttonID = id % 4;
            final ChunkEntry entry = this.chunks.getValue(this.chunks.getIndex() + index);
            if (entry == null) {
                return;
            }
            if (buttonID == 0 || buttonID == 1 || buttonID == 2) {
                this.field_146297_k.func_147108_a((GuiScreen)new GuiTypeInfo(entry, buttonID));
            }
            else if (buttonID == 3) {
                this.field_146297_k.func_147108_a((GuiScreen)null);
                ChunkPregenerator.networking.sendPacketToServer(new TPChunkPacket(entry.getPos()));
            }
        }
    }
    
    @Override
    public void func_73866_w_() {
        super.func_73866_w_();
        this.registerButton(0, -130, -118, 95, 20, "Dim: " + this.getDimension());
        this.registerButton(1, -130, -97, 40, 20, func_146272_n() ? "Clear" : "Reload");
        this.registerButton(2, -35, -118, 60, 20, "Reload Dim");
        this.registerButton(3, 150, -97, 20, 20, "+");
        this.registerButton(4, 129, -97, 20, 20, "-");
        this.registerButton(5, 26, -118, 130, 20, "Sorter: " + this.comparatorNames.get(this.comparators.getIndex()));
        this.registerButton(6, 68, -97, 60, 20, "Back");
        int offset = 52;
        int index = 10;
        for (int i = 0; i < 3 && i + this.chunks.getIndex() < this.chunks.size(); ++i) {
            this.registerButton(index, 105, -offset, 60, 20, "Entities");
            this.registerButton(index + 1, 105, -offset + 25, 60, 20, "TileEntities");
            this.registerButton(index + 2, 40, -offset, 60, 20, "Biomes");
            this.registerButton(index + 3, 40, -offset + 25, 60, 20, "Teleport").field_146124_l = (this.field_146297_k.field_71439_g.field_71093_bK == this.getDimension());
            offset -= 60;
            index += 4;
        }
    }
    
    public void func_73863_a(final int mouseX, final int mouseY, final float partialTicks) {
        final int effect = Mouse.getDWheel() / 120;
        this.chunks.setIndex(this.chunks.getIndex() - effect * (GuiScreen.func_146271_m() ? 10 : 1));
        this.func_73866_w_();
        ++this.ticker;
        this.func_146276_q_();
        this.drawRectangle(160, 120, 20, 0, -3750202, false);
        int data = -33;
        final boolean shift = func_146272_n();
        int drawn = 0;
        for (int i = 0; i < 3 && i + this.chunks.getIndex() < this.chunks.size(); ++i) {
            this.drawRectangle(150, 28, 20, data, -7631989, true);
            data += 60;
            ++drawn;
        }
        super.func_73863_a(mouseX, mouseY, partialTicks);
        this.drawLeftText(this.chunks.getIndex() + "/" + this.chunks.size(), 170, -70, 4210752);
        if (this.answerType == -1) {
            this.drawText("<-- Require Reloading", -85, -90, 4210752);
        }
        else if (this.answerType == -2) {
            this.drawText("No Chunks Found", -85, -90, 4210752);
            if (this.ticker >= 200) {
                this.answerType = 2;
            }
        }
        else if (this.answerType == 1) {
            this.drawText("Awaiting Packets: " + this.packetsLeft, -85, -90, 4210752);
        }
        else if (this.answerType != 2) {
            this.drawText("Reloading: ", -85, -90, 4210752);
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
            this.drawText(build.toString(), -33, -90, 4210752);
        }
        int yOffset = 60;
        for (int k = 0; k < drawn; ++k) {
            this.drawData(yOffset, k, shift);
            yOffset -= 60;
        }
    }
    
    private void drawData(final int yOffset, final int index, final boolean shift) {
        final ChunkEntry entry = this.chunks.getValue(this.chunks.getIndex() + index);
        if (entry == null) {
            return;
        }
        final EntityPlayer player = this.field_146297_k.field_71439_g;
        if (shift) {
            final FilePos pos = new FilePos((int)player.field_70165_t, (int)player.field_70161_v);
            this.drawText("BlockX: " + this.format.format(entry.xPos * 16), -128, -yOffset, 4210752);
            this.drawText("BlockZ: " + this.format.format(entry.zPos * 16), -128, -yOffset + 9, 4210752);
            this.drawText("Dist: " + this.format.format(pos.getSqDistance(entry.xPos * 16, entry.zPos * 16)) + " B", -28, -yOffset, 4210752);
        }
        else {
            final FilePos pos = new FilePos((int)player.field_70165_t >> 4, (int)player.field_70161_v >> 4);
            this.drawText("ChunkX: " + this.format.format(entry.xPos), -128, -yOffset, 4210752);
            this.drawText("ChunkZ: " + this.format.format(entry.zPos), -128, -yOffset + 9, 4210752);
            this.drawText("Dist: " + this.format.format(pos.getSqDistance(entry.xPos, entry.zPos)) + " C", -28, -yOffset, 4210752);
        }
        this.drawText("Loaded Entities: " + this.format.format(entry.entities), -128, -yOffset + 18, 4210752);
        this.drawText("Loaded TileEntities: " + this.format.format(entry.tileEntities), -128, -yOffset + 27, 4210752);
        this.drawText("Ticking TileEntities: " + this.format.format(entry.tickableTileEntities), -128, -yOffset + 36, 4210752);
        this.drawText("Random Tick Blocks: " + this.format.format(entry.randomUpdateBlocks), -128, -yOffset + 45, 4210752);
    }
    
    public boolean func_73868_f() {
        return false;
    }
    
    public List<Comparator<ChunkEntry>> createList() {
        final List<Comparator<ChunkEntry>> list = new ArrayList<Comparator<ChunkEntry>>();
        list.add(new Comparator<ChunkEntry>() {
            @Override
            public int compare(final ChunkEntry o1, final ChunkEntry o2) {
                final int firstDistance = this.getDistance(o1.getPos());
                final int secondDistance = this.getDistance(o2.getPos());
                if (firstDistance > secondDistance) {
                    return -1;
                }
                if (secondDistance > firstDistance) {
                    return 1;
                }
                return 0;
            }
            
            public int getDistance(final FilePos pos) {
                final EntityPlayer player = GuiChunkInfo.this.field_146297_k.field_71439_g;
                return pos.getDistance((int)player.field_70165_t, (int)player.field_70161_v);
            }
        });
        list.add(new Comparator<ChunkEntry>() {
            @Override
            public int compare(final ChunkEntry o1, final ChunkEntry o2) {
                if (o1.entities > o2.entities) {
                    return -1;
                }
                if (o2.entities > o1.entities) {
                    return 1;
                }
                return 0;
            }
        });
        list.add(new Comparator<ChunkEntry>() {
            @Override
            public int compare(final ChunkEntry o1, final ChunkEntry o2) {
                if (o1.tileEntities > o2.tileEntities) {
                    return -1;
                }
                if (o2.tileEntities > o1.tileEntities) {
                    return 1;
                }
                return 0;
            }
        });
        list.add(new Comparator<ChunkEntry>() {
            @Override
            public int compare(final ChunkEntry o1, final ChunkEntry o2) {
                if (o1.tickableTileEntities > o2.tickableTileEntities) {
                    return -1;
                }
                if (o2.tickableTileEntities > o1.tickableTileEntities) {
                    return 1;
                }
                return 0;
            }
        });
        list.add(new Comparator<ChunkEntry>() {
            @Override
            public int compare(final ChunkEntry o1, final ChunkEntry o2) {
                if (o1.randomUpdateBlocks > o2.randomUpdateBlocks) {
                    return -1;
                }
                if (o2.randomUpdateBlocks > o1.randomUpdateBlocks) {
                    return 1;
                }
                return 0;
            }
        });
        return list;
    }
    
    static {
        INSTANCE = new GuiChunkInfo();
    }
}
