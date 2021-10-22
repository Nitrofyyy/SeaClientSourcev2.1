// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.client.preview;

import pregenerator.impl.client.preview.data.IFileProvider;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import pregenerator.impl.client.preview.world.data.IChunkData;
import net.minecraft.world.gen.structure.StructureStart;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import org.lwjgl.input.Mouse;
import pregenerator.impl.storage.TaskStorage;
import java.io.IOException;
import java.util.Iterator;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.GuiScreen;
import java.util.Random;
import net.minecraft.client.gui.GuiButton;
import org.lwjgl.input.Keyboard;
import pregenerator.impl.processor.generator.ChunkProcessor;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.world.chunk.Chunk;
import pregenerator.base.impl.misc.PregenEvent;
import net.minecraftforge.common.MinecraftForge;
import java.util.Set;
import net.minecraft.client.Minecraft;
import java.util.List;
import java.util.ArrayList;
import net.minecraftforge.common.DimensionManager;
import java.util.Collection;
import pregenerator.ConfigManager;
import java.util.LinkedHashSet;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.client.gui.GuiCreateWorld;
import pregenerator.impl.misc.FilePos;
import pregenerator.impl.client.preview.texture.MoveableTexture;
import pregenerator.impl.client.preview.world.CustomServer;
import pregenerator.impl.client.preview.data.SubProcessor;
import pregenerator.impl.client.preview.data.MapManager;
import net.minecraft.client.gui.GuiTextField;
import pregenerator.base.impl.misc.SelectionList;
import pregenerator.impl.client.preview.world.WorldInstance;
import java.util.Map;
import pregenerator.impl.client.preview.world.WorldSeed;
import java.text.DecimalFormat;
import java.awt.Color;
import pregenerator.base.impl.gui.GuiSlider;
import pregenerator.base.impl.gui.GuiPregenBase;

public class GuiSeedPreview extends GuiPregenBase implements GuiSlider.ISlider
{
    public static final Color SLIME_COLOR;
    public static final DecimalFormat BIG_FORMAT;
    WorldSeed seed;
    Map<Integer, WorldInstance> dimensions;
    SelectionList<Integer> allDimensions;
    SelectionList<String> mapViews;
    GuiTextField seedName;
    boolean terrainOnly;
    boolean slimeChunks;
    boolean structures;
    public MapManager mapData;
    SubProcessor processor;
    CustomServer fakeServer;
    MoveableTexture displayTexture;
    int oldScale;
    int newScale;
    FilePos lastPos;
    boolean dragging;
    float scale;
    int globalRadius;
    boolean globalSquare;
    boolean keepWorld;
    boolean pregenRuns;
    
    public GuiSeedPreview(final GuiCreateWorld gui) {
        this.dimensions = new ConcurrentHashMap<Integer, WorldInstance>();
        this.allDimensions = new SelectionList<Integer>();
        this.mapViews = new SelectionList<String>(Arrays.asList("Block Colors", "Biome(Foliage) Colors", "Biome(Grass) Colors"));
        this.terrainOnly = true;
        this.slimeChunks = false;
        this.structures = false;
        this.mapData = new MapManager();
        this.processor = new SubProcessor();
        this.fakeServer = null;
        this.displayTexture = new MoveableTexture(3200);
        this.newScale = -1;
        this.lastPos = null;
        this.dragging = false;
        this.scale = 0.2f;
        this.globalRadius = 100;
        this.globalSquare = true;
        this.keepWorld = false;
        this.pregenRuns = false;
        (this.seed = new WorldSeed(gui)).removePreview(false);
        this.seed.createPaths();
        final Set<Integer> set = new LinkedHashSet<Integer>();
        set.addAll(ConfigManager.getDimensions());
        if (!ConfigManager.orderOnly) {
            set.addAll(Arrays.asList(DimensionManager.getStaticDimensionIDs()));
        }
        this.allDimensions.addValues(new ArrayList<Integer>(set));
        this.oldScale = Minecraft.func_71410_x().field_71474_y.field_74335_Z;
        this.onReopening();
    }
    
    public boolean func_73868_f() {
        return false;
    }
    
    public void onReopening() {
        MinecraftForge.EVENT_BUS.register((Object)this);
        this.displayTexture.createTextureIfMissing();
        this.displayTexture.centerTexture();
        if (this.newScale != -1) {
            Minecraft.func_71410_x().field_71474_y.field_74335_Z = this.newScale;
        }
    }
    
    @SubscribeEvent
    public void onChunkPreviewed(final PregenEvent event) {
        final Chunk chunk = event.getChunk();
        final WorldInstance instance = this.dimensions.get(chunk.func_177412_p().field_73011_w.func_177502_q());
        if (instance == null) {
            return;
        }
        instance.addChunk(chunk);
    }
    
    @SubscribeEvent
    public void onChunkSaved(final ChunkDataEvent.Save event) {
        final Chunk chunk = event.getChunk();
        final WorldInstance instance = this.dimensions.get(chunk.func_177412_p().field_73011_w.func_177502_q());
        if (instance == null) {
            return;
        }
        instance.addChunk(chunk);
    }
    
    @Override
    public void func_73866_w_() {
        super.func_73866_w_();
        this.registerUnmovedButton(0, 30, 52, 79, 14, "Random");
        this.registerUnmovedButton(1, 111, 52, 79, 14, "Apply Seed");
        final WorldInstance instance = this.getCurrentWorld();
        this.registerButton(new GuiSlider(3, 51, 78, 118, 14, "Radius: ", " Chunks", 1.0, 1000.0, (instance != null) ? instance.getRadius() : ((double)this.globalRadius), false, true, this).setScrollEffect(1.0));
        this.registerUnmovedButton(4, 30, 78, 20, 14, "<");
        this.registerUnmovedButton(5, 170, 78, 20, 14, ">");
        this.registerUnmovedButton(20, 30, 93, 79, 14, "Shape: Square");
        this.registerUnmovedButton(2, 111, 93, 79, 14, "Apply Size");
        this.registerUnmovedButton(6, 30, 108, 79, 14, this.terrainOnly ? "Terrain Gen" : "Full Gen");
        this.registerUnmovedButton(7, 111, 108, 79, 14, "Add Post");
        this.registerUnmovedButton(8, 30, 123, 160, 14, "Dim: " + this.allDimensions.getValue() + " (" + WorldSeed.getDimensionName(this.allDimensions.getValue()) + ")");
        this.registerUnmovedButton(9, 30, 148, 79, 14, "Start Gen");
        this.registerUnmovedButton(10, 111, 148, 79, 14, "Stop Gen");
        this.registerButton(new GuiSlider(11, 51, 163, 118, 14, "Speed: ", " ms/t", 10.0, 1000.0, ChunkProcessor.INSTANCE.getMaxTime(), false, true, this).setScrollEffect(1.0));
        this.registerUnmovedButton(12, 30, 163, 20, 14, "<");
        this.registerUnmovedButton(13, 170, 163, 20, 14, ">");
        this.registerUnmovedButton(14, 30, 189, 160, 14, "Map View: " + this.mapViews.getValue());
        this.registerUnmovedButton(15, 30, 204, 79, 14, "Slime Chunks");
        this.registerUnmovedButton(16, 111, 204, 79, 14, "Structures");
        this.registerUnmovedButton(30, 30, this.field_146295_m - 36, 79, 14, "Back");
        this.registerUnmovedButton(31, 111, this.field_146295_m - 36, 79, 14, "Keep World");
        this.registerUnmovedButton(99, 0, 0, 70, 15, "Lower Scale");
        this.registerUnmovedButton(100, this.field_146294_l - 202, 20, 20, 15, "GC");
        (this.seedName = new GuiTextField(32, this.field_146297_k.field_71466_p, 31, 40, 158, 10)).func_146205_d(true);
        this.seedName.func_146180_a(this.seed.getTextSeed());
        Keyboard.enableRepeatEvents(true);
    }
    
    public void updateButtons() {
        final boolean isRunning = ChunkProcessor.INSTANCE.isRunning();
        if (isRunning) {
            this.seedName.func_146195_b(false);
        }
        this.getIDButton(0).field_146124_l = !isRunning;
        this.getIDButton(1).field_146124_l = !isRunning;
        this.getIDButton(2).field_146124_l = !isRunning;
        this.getIDButton(3).field_146124_l = !isRunning;
        this.getIDButton(4).field_146124_l = !isRunning;
        this.getIDButton(5).field_146124_l = !isRunning;
        this.getIDButton(6).field_146124_l = !isRunning;
        this.getIDButton(7).field_146124_l = !isRunning;
        this.getIDButton(8).field_146124_l = !isRunning;
        this.getIDButton(9).field_146124_l = !isRunning;
        this.getIDButton(10).field_146124_l = isRunning;
        this.getIDButton(20).field_146124_l = !isRunning;
        this.getIDButton(30).field_146124_l = !isRunning;
        this.getIDButton(31).field_146124_l = !isRunning;
        this.getIDButton(15).field_146125_m = (this.field_146295_m >= 255);
        this.getIDButton(16).field_146125_m = (this.field_146295_m >= 255);
        this.getIDButton(99).field_146125_m = (this.field_146295_m < 255);
        if (ChunkProcessor.INSTANCE.isRunning()) {
            this.pregenRuns = true;
        }
        else if (this.pregenRuns) {
            this.stopTask();
        }
        if (this.fakeServer == null) {
            this.createServer();
        }
        final WorldInstance instance = this.getCurrentWorld();
        final boolean shape = (instance == null) ? this.globalSquare : instance.isSquare();
        this.getIDButton(20).field_146126_j = "Shape: " + (shape ? "Square" : "Circle");
    }
    
    public void func_146281_b() {
        this.stopTask();
        if (Minecraft.func_71410_x().field_71474_y.field_74335_Z != this.oldScale) {
            Minecraft.func_71410_x().field_71474_y.field_74335_Z = this.oldScale;
        }
        MinecraftForge.EVENT_BUS.unregister((Object)this);
        if (!this.keepWorld) {
            this.removePreview(false);
        }
        this.displayTexture.removeTexture();
        Keyboard.enableRepeatEvents(false);
        super.func_146281_b();
    }
    
    public boolean removePreview(final boolean tempOnly) {
        this.mapData.shutdown();
        this.processor.terminate();
        return this.seed.forceRemovePreview(tempOnly);
    }
    
    protected void func_146284_a(final GuiButton button) {
        final int id = button.field_146127_k;
        if (id == 0) {
            this.seedName.func_146180_a(Long.toString(new Random().nextLong()));
        }
        else if (id == 1) {
            this.applyChanges();
        }
        else if (id == 2) {
            this.applyNewSize();
        }
        else if (id == 4 || id == 5) {
            final GuiSlider slider = (GuiSlider)this.getIDButton(3);
            int value = (id == 5) ? 1 : -1;
            value *= (GuiScreen.func_146271_m() ? 10 : 1);
            slider.setValue(slider.getValueInt() + value);
            slider.updateSlider();
        }
        else if (id == 6) {
            this.terrainOnly = !this.terrainOnly;
            button.field_146126_j = (this.terrainOnly ? "Terrain Gen" : "Full Gen");
        }
        else if (id == 7) {
            this.startTask(2);
        }
        else if (id == 8) {
            if (GuiScreen.func_146272_n()) {
                this.allDimensions.prev();
            }
            else {
                this.allDimensions.next();
            }
            button.field_146126_j = "Dim: " + this.allDimensions.getValue() + " (" + WorldSeed.getDimensionName(this.allDimensions.getValue()) + ")";
            this.toggleDimension();
        }
        else if (id == 9) {
            this.startTask(this.terrainOnly ? 0 : 1);
        }
        else if (id == 10) {
            this.stopTask();
        }
        else if (id == 12 || id == 13) {
            final GuiSlider slider = (GuiSlider)this.getIDButton(11);
            int value = (id == 13) ? 1 : -1;
            value *= (GuiScreen.func_146271_m() ? 10 : 1);
            slider.setValue(slider.getValueInt() + value);
            slider.updateSlider();
        }
        else if (id == 14) {
            if (GuiScreen.func_146272_n()) {
                this.mapViews.prev();
            }
            else {
                this.mapViews.next();
            }
            button.field_146126_j = "Map View: " + this.mapViews.getValue();
            this.updateTexture();
        }
        else if (id == 15) {
            this.slimeChunks = !this.slimeChunks;
        }
        else if (id == 16) {
            this.structures = !this.structures;
        }
        else if (id == 20) {
            final WorldInstance instance = this.getCurrentWorld();
            if (instance != null) {
                instance.toggleShape();
            }
            else {
                this.globalSquare = !this.globalSquare;
            }
        }
        else if (id == 30) {
            this.field_146297_k.func_147108_a((GuiScreen)this.seed.getPrevGui());
        }
        else if (id == 31) {
            boolean save = true;
            for (final WorldInstance instance2 : this.dimensions.values()) {
                if (!instance2.isSaveToUse()) {
                    save = false;
                }
            }
            this.keepWorld = true;
            this.field_146297_k.func_147108_a((GuiScreen)new GuiKeepWorld(this, save));
            this.keepWorld = false;
        }
        else if (id == 99) {
            if (this.field_146295_m >= 255) {
                return;
            }
            final int oldScale = this.oldScale;
            this.field_146297_k.field_71474_y.field_74335_Z = new ScaledResolution(this.field_146297_k).func_78325_e() - 1;
            final ScaledResolution scaledresolution = new ScaledResolution(this.field_146297_k);
            final int i = scaledresolution.func_78326_a();
            final int j = scaledresolution.func_78328_b();
            this.func_146280_a(this.field_146297_k, i, j);
            this.oldScale = oldScale;
        }
        else if (id == 100) {
            System.gc();
        }
    }
    
    @Override
    public void onChangeSliderValue(final GuiSlider slider) {
        if (slider.field_146127_k == 3) {
            this.globalRadius = slider.getValueInt();
        }
        if (slider.field_146127_k == 11) {
            ChunkProcessor.INSTANCE.setMaxTime(slider.getValueInt());
        }
    }
    
    protected void func_73869_a(final char typedChar, final int keyCode) throws IOException {
        super.func_73869_a(typedChar, keyCode);
        if (this.seedName.func_146206_l()) {
            this.seedName.func_146201_a(typedChar, keyCode);
        }
    }
    
    protected void func_73864_a(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        super.func_73864_a(mouseX, mouseY, mouseButton);
        this.seedName.func_146192_a(mouseX, mouseY, mouseButton);
        if (this.seedName.func_146206_l()) {
            return;
        }
        if (this.isInFrame(mouseX, mouseY)) {
            this.dragging = true;
            this.lastPos = new FilePos(mouseX, mouseY);
        }
    }
    
    protected void func_146286_b(final int mouseX, final int mouseY, final int state) {
        super.func_146286_b(mouseX, mouseY, state);
        this.dragging = false;
    }
    
    public void applyChanges() {
        final long newSeed = WorldSeed.makeSeed(this.seedName.func_146179_b());
        if (newSeed == this.seed.getSeed()) {
            return;
        }
        this.seed.setSeed(this.seedName.func_146179_b());
        if (this.fakeServer != null) {
            this.seed.destroyServer(this.fakeServer);
            this.fakeServer = null;
        }
        for (final Integer value : this.dimensions.keySet()) {
            this.mapData.removeDimension(value);
        }
        this.dimensions.clear();
        this.seed.removePreview(false);
        this.displayTexture.centerTexture();
        this.displayTexture.clearTexture();
        this.updateTexture();
    }
    
    public void applyNewSize() {
        final int radius = ((GuiSlider)this.getIDButton(3)).getValueInt();
        this.displayTexture.resizeTexture(radius * 32);
        this.displayTexture.centerTexture();
        this.updateTexture();
        final WorldInstance current = this.getCurrentWorld();
        if (current != null) {
            current.setRadius(radius);
        }
    }
    
    public void toggleDimension() {
        for (final WorldInstance instance : this.dimensions.values()) {
            instance.setFocus(this.allDimensions.getValue(), this.mapViews.getIndex());
        }
        final WorldInstance instance2 = this.getCurrentWorld();
        if (instance2 != null) {
            final GuiSlider slider = (GuiSlider)this.getIDButton(3);
            slider.setValue(instance2.getRadius());
            slider.updateSlider();
        }
        this.displayTexture.resizeTexture(((GuiSlider)this.getIDButton(3)).getValueInt() * 32);
        this.displayTexture.centerTexture();
        this.displayTexture.clearTexture();
        this.updateTexture();
    }
    
    public void updateTexture() {
        final WorldInstance instance = this.getCurrentWorld();
        if (instance != null) {
            instance.reload(this.mapViews.getIndex());
        }
    }
    
    public void createServer() {
        this.fakeServer = this.seed.createServer();
    }
    
    public void startServer() {
        if (this.fakeServer.func_71200_ad()) {
            return;
        }
        this.fakeServer.func_71256_s();
        while (!this.fakeServer.func_71200_ad()) {
            try {
                Thread.sleep(1L);
            }
            catch (Exception ex) {}
        }
    }
    
    public void startTask(final int state) {
        if (ChunkProcessor.INSTANCE.isRunning()) {
            return;
        }
        this.startServer();
        TaskStorage.getStorage().clearAll();
        final WorldInstance instance = this.getOrCreateWorld();
        ChunkProcessor.INSTANCE.startTask(instance.createTask(state).setPreview());
    }
    
    public void stopTask() {
        this.pregenRuns = false;
        this.seed.destroyServer(this.fakeServer);
        this.fakeServer = null;
    }
    
    public WorldInstance getOrCreateWorld() {
        WorldInstance instance = this.dimensions.get(this.allDimensions.getValue());
        if (instance == null) {
            instance = new WorldInstance(this.allDimensions.getValue(), this.globalSquare, this.mapData.createDimension(this.allDimensions.getValue()));
            this.dimensions.put(this.allDimensions.getValue(), instance);
            final int radius = ((GuiSlider)this.getIDButton(3)).getValueInt();
            instance.setRadius(radius);
            this.displayTexture.resizeTexture(radius * 32);
            this.displayTexture.centerTexture();
        }
        return instance;
    }
    
    public WorldInstance getCurrentWorld() {
        return this.dimensions.get(this.allDimensions.getValue());
    }
    
    public void func_73863_a(final int mouseX, final int mouseY, final float partialTicks) {
        this.updateButtons();
        this.process();
        this.func_146276_q_();
        this.drawFrame();
        if (this.seedName != null) {
            this.seedName.func_146194_f();
        }
        if (this.isInFrame(mouseX, mouseY)) {
            final int effect = Mouse.getDWheel() / 120;
            if (effect != 0) {
                final float data = 1.0f + (func_146272_n() ? 2.0f : 0.1f) * effect;
                this.scale *= data;
                if (this.scale < 0.01f) {
                    this.scale = 0.01f;
                }
                else if (this.scale > 60.0f) {
                    this.scale = 60.0f;
                }
            }
        }
        if (this.dragging) {
            this.displayTexture.moveTexture(-((this.lastPos.x - mouseX) / this.scale), -((this.lastPos.z - mouseY) / this.scale));
            this.lastPos = new FilePos(mouseX, mouseY);
        }
        final ScaledResolution res = new ScaledResolution(this.field_146297_k);
        final double scaleW = this.field_146297_k.field_71443_c / res.func_78327_c();
        final double scaleH = this.field_146297_k.field_71440_d / res.func_78324_d();
        GL11.glEnable(3089);
        GL11.glScissor((int)(200.0 * scaleW), (int)(40.0 * scaleH), (int)(this.field_146297_k.field_71443_c - 240.0 * scaleW), (int)(this.field_146297_k.field_71440_d - 80.0 * scaleH));
        GlStateManager.func_179094_E();
        this.displayTexture.translate(this.centerX + 70.0f, (float)this.centerY, this.scale);
        this.displayTexture.render(this.slimeChunks, new MoveableTexture.IRenderFunction() {
            @Override
            public void render(final float x, final float y, final int width, final int height) {
                GuiSeedPreview.this.renderTexture(x, y, (float)width, (float)height, GuiSeedPreview.this.field_73735_i);
            }
        });
        this.drawExtras();
        GlStateManager.func_179121_F();
        GL11.glDisable(3089);
        super.func_73863_a(mouseX, mouseY, partialTicks);
        this.drawPostFrame();
        if (!this.isInFrame(mouseX, mouseY)) {
            this.handleButtonToolTips(mouseX, mouseY);
            return;
        }
        final WorldInstance data2 = this.getCurrentWorld();
        if (data2 == null) {
            return;
        }
        final FilePos pos = this.getMousePosition(data2.getRadius() * 16, mouseX, mouseY);
        if (pos == null) {
            return;
        }
        final IChunkData chunk = data2.getChunk(pos.x >> 4, pos.z >> 4);
        if (chunk == null) {
            return;
        }
        final int x = pos.x & 0xF;
        final int z = pos.z & 0xF;
        final List<String> toDraw = new ArrayList<String>();
        toDraw.add("Position: [X=" + pos.x + ", Y=" + chunk.getHeight(x, z) + ", Z=" + pos.z + "]");
        toDraw.add("Chunk: [X=" + (pos.x >> 4) + ", Z=" + (pos.z >> 4) + "]");
        toDraw.add("Biome: " + BiomeGenBase.func_180276_a(chunk.getBiome(x, z), BiomeGenBase.field_76772_c).field_76791_y);
        if (chunk.isSlimeChunk()) {
            toDraw.add("Slime Chunk");
        }
        for (final Map.Entry<String, Set<StructureStart>> entry : data2.getStructures().entrySet()) {
            final String name = entry.getKey();
            for (final StructureStart start : entry.getValue()) {
                final int growth = name.equalsIgnoreCase("temple") ? 20 : 0;
                final StructureBoundingBox box = start.func_75071_a();
                if (start.func_75069_d() && pos.x >= box.field_78897_a - growth && pos.x <= box.field_78893_d + growth && pos.z >= box.field_78896_c - growth && pos.z <= box.field_78892_f + growth) {
                    toDraw.add(name);
                    break;
                }
            }
        }
        this.drawListText(toDraw, mouseX, mouseY);
    }
    
    public void process() {
        for (final WorldInstance instance : this.dimensions.values()) {
            instance.update(this.mapViews.getIndex(), this.processor);
            if (instance.isFocus()) {
                instance.render(this.displayTexture);
            }
        }
    }
    
    public FilePos getMousePosition(final int radius, final int mouseX, final int mouseY) {
        final int posX = (int)(this.displayTexture.getX() - (mouseX - (this.field_146294_l + 140) / 2) / this.scale);
        final int posZ = (int)(this.displayTexture.getY() - (mouseY - this.field_146295_m / 2) / this.scale);
        return (posX > 0 || posZ > 0 || posX <= radius * -2 || posZ <= radius * -2) ? null : new FilePos(Math.abs(posX) - radius, Math.abs(posZ) - radius);
    }
    
    public void drawExtras() {
        final WorldInstance data = this.getCurrentWorld();
        if (data != null) {
            GlStateManager.func_179109_b(this.displayTexture.getWidth() / 2.0f, this.displayTexture.getHeight() / 2.0f, 0.0f);
            final float offsetX = this.displayTexture.getX();
            final float offsetY = this.displayTexture.getY();
            if (this.slimeChunks) {
                final Tessellator tessellator = Tessellator.func_178181_a();
                final WorldRenderer worldrenderer = tessellator.func_178180_c();
                GlStateManager.func_179147_l();
                GlStateManager.func_179090_x();
                GlStateManager.func_179120_a(770, 771, 1, 0);
                final int color = GuiSeedPreview.SLIME_COLOR.getRGB();
                GlStateManager.func_179131_c((color >> 16 & 0xFF) / 255.0f, (color >> 8 & 0xFF) / 255.0f, (color & 0xFF) / 255.0f, (color >> 24 & 0xFF) / 255.0f);
                worldrenderer.func_181668_a(7, DefaultVertexFormats.field_181705_e);
                for (final FilePos pos : data.getSlimeChunks()) {
                    final float minX = pos.x * 16 + offsetX;
                    final float minY = pos.z * 16 + offsetY;
                    this.drawArea(minX, minY, minX + 16.0f, minY + 16.0f, GuiSeedPreview.SLIME_COLOR.getRGB(), worldrenderer);
                }
                tessellator.func_78381_a();
                GlStateManager.func_179098_w();
                GlStateManager.func_179084_k();
            }
            if (this.structures) {
                final int range = data.getRadius() * 16;
                for (final Map.Entry<String, Set<StructureStart>> starts : data.getStructures().entrySet()) {
                    final String name = starts.getKey();
                    if (name.equalsIgnoreCase("mineshaft") && !GuiScreen.func_146272_n()) {
                        continue;
                    }
                    final int color2 = Integer.MIN_VALUE | ((name.hashCode() & 0xAAAAAA) + 4473924 & 0xFFFFFF);
                    final int growth = name.equalsIgnoreCase("temple") ? 20 : 0;
                    for (final StructureStart start : starts.getValue()) {
                        final StructureBoundingBox box = start.func_75071_a();
                        if (start.func_75069_d() && box.field_78893_d + growth > -range && box.field_78897_a - growth < range && box.field_78892_f + growth > -range) {
                            if (box.field_78896_c - growth >= range) {
                                continue;
                            }
                            this.drawArea(box.field_78897_a + offsetX - growth, box.field_78896_c + offsetY - growth, box.field_78893_d + offsetX + growth, box.field_78892_f + offsetY + growth, color2);
                        }
                    }
                }
            }
        }
    }
    
    public void drawPostFrame() {
        this.drawSimpleText("Seed: ", 30, 30, 4210752);
        this.drawSimpleText("Generation Options: ", 30, 70, 4210752);
        this.drawSimpleText("Generation Control", 30, 140, 4210752);
        this.drawSimpleText("Toggle Overlays", 30, 180, 4210752);
        if (this.field_146295_m < 255) {
            this.drawSimpleText("Window height is to small", 30, 21, Color.RED.getRGB());
        }
        final long[] data = WorldSeed.getData();
        this.drawSimpleText("Ram: " + WorldSeed.value(data[0]) + " MB / " + WorldSeed.value(data[2]) + " MB", this.field_146294_l - 178, 24, 4210752);
        final ChunkProcessor instance = ChunkProcessor.INSTANCE;
        this.drawSimpleText(instance.getCurrentProcessed() + " / " + instance.getMaxProcess() + " Chunks", 201, this.field_146295_m - 31, 4210752);
    }
    
    public void drawFrame() {
        this.drawSimpleRect(20, 20, this.field_146294_l - 20, this.field_146295_m - 20, -3750202, false);
        this.drawSimpleRect(200, 40, this.field_146294_l - 40, this.field_146295_m - 40, -7631989, true);
        final long[] data = WorldSeed.getData();
        this.drawSimpleRect(this.field_146294_l - 180, 22, this.field_146294_l - 40, 33, -7631989, true);
        this.drawSimpleRect(200, this.field_146295_m - 35, this.field_146294_l - 40, this.field_146295_m - 22, -7631989, true);
        double value = data[0] / (double)data[2];
        func_73734_a(this.field_146294_l - 180, 22, this.field_146294_l - 40 - (int)(140.0 - 140.0 * value), 33, Color.GREEN.getRGB());
        final ChunkProcessor instance = ChunkProcessor.INSTANCE;
        value = ((instance.getTask() != null) ? (instance.getCurrentProcessed() / (double)instance.getMaxProcess()) : 0.0);
        final int toRender = this.field_146294_l - 200 - 40;
        func_73734_a(200, this.field_146295_m - 35, this.field_146294_l - 40 - (int)(toRender - toRender * value), this.field_146295_m - 22, Color.GREEN.getRGB());
    }
    
    public void handleButtonToolTips(final int mouseX, final int mouseY) {
        GuiButton button = this.getIDButton(3);
        if (button.func_146115_a()) {
            final List<String> list = new ArrayList<String>();
            final int radius = ((GuiSlider)button).getValueInt();
            final int worldSize = radius * 32;
            int value = radius * 2;
            value *= value;
            list.add("WorldSize: " + worldSize + "x" + worldSize + " Blocks");
            list.add("");
            list.add("Expected Memory Usage");
            list.add("Ram Usage: ");
            list.add("PregenTask: +" + GuiSeedPreview.BIG_FORMAT.format(WorldSeed.value(value * 320L)) + " MB");
            list.add("");
            list.add("GPU Ram Usage: " + GuiSeedPreview.BIG_FORMAT.format(WorldSeed.value(value * 1024L)) + " MB");
            list.add("");
            list.add("Hard Drive Usage: (Accumulate Per Generated Dimension)");
            list.add("World Usage: +" + GuiSeedPreview.BIG_FORMAT.format(WorldSeed.value(value * 8192L)) + " MB");
            list.add("Preview Texture: +" + GuiSeedPreview.BIG_FORMAT.format(WorldSeed.value(value * IFileProvider.FileType.Chunk_Data.getOffset())) + " MB (Temporary)");
            list.add("HeightMap: +" + GuiSeedPreview.BIG_FORMAT.format(WorldSeed.value(value * IFileProvider.FileType.HeightData.getOffset())) + " MB (Temporary)");
            this.drawListText(list, mouseX, mouseY + 30);
        }
        button = this.getIDButton(11);
        if (button.func_146115_a() && ((GuiSlider)button).getValueInt() > 250) {
            final List<String> list = new ArrayList<String>();
            list.add("Anything above makes 250ms/t makes no sense and actually slows down the speed");
            this.drawListText(list, mouseX, mouseY - 15);
        }
        if (this.getIDButton(6).func_146115_a()) {
            this.drawListText(Arrays.asList("Press to toggle if only the Terrain should be generated or if the World should be fully Generated"), mouseX, mouseY - 15);
        }
        if (this.getIDButton(15).func_146115_a()) {
            this.drawListText(Arrays.asList("Press To Toggle the Slime Chunk Rendering on Map"), mouseX, mouseY - 15);
        }
        if (this.getIDButton(16).func_146115_a()) {
            this.drawListText(Arrays.asList("Press To Toggle the Structure Rendering on Map"), mouseX, mouseY - 15);
        }
        if (this.getIDButton(100).func_146115_a()) {
            this.drawListText(Arrays.asList("Java Memory Cleanup (also known as Garbage Collection)", "Will Freeze the Game for a couple seconds!"), mouseX, mouseY + 30);
        }
    }
    
    public boolean isInFrame(final int mouseX, final int mouseY) {
        return mouseX >= 200 && mouseX <= this.field_146294_l - 40 && mouseY >= 40 && mouseY <= this.field_146295_m - 40;
    }
    
    static {
        SLIME_COLOR = new Color(0, 255, 255, 128);
        BIG_FORMAT = new DecimalFormat("###,###");
    }
}
