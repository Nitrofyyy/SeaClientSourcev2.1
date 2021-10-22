// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.processor.generator;

import java.net.URL;
import java.net.HttpURLConnection;
import net.minecraft.world.WorldProvider;
import oshi.hardware.Processor;
import oshi.SystemInfo;
import org.apache.commons.lang3.time.DurationFormatUtils;
import pregenerator.ChunkPregenerator;
import com.google.gson.JsonPrimitive;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.common.DimensionManager;
import pregenerator.ConfigManager;
import java.io.DataOutputStream;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;
import java.io.ByteArrayOutputStream;
import net.minecraftforge.fml.common.FMLLog;
import java.io.RandomAccessFile;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonWriter;
import java.io.Writer;
import java.io.BufferedWriter;
import java.io.FileWriter;
import com.google.gson.JsonElement;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.FileReader;
import com.google.gson.JsonObject;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.File;
import net.minecraft.server.MinecraftServer;
import java.util.Iterator;
import net.minecraft.entity.player.EntityPlayer;
import java.io.IOException;
import net.minecraft.command.ICommandSender;
import pregenerator.impl.storage.GlobalListeners;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraft.world.World;
import java.util.ArrayList;
import java.util.UUID;
import java.util.List;

public class BenchmarkManager
{
    public static final BenchmarkManager INSTANCE;
    List<BenchmarkResult> results;
    UUID sessionInstance;
    UUID starter;
    boolean sendNextBenchmark;
    
    public BenchmarkManager() {
        this.results = new ArrayList<BenchmarkResult>();
        this.sessionInstance = UUID.randomUUID();
        this.starter = null;
        this.sendNextBenchmark = false;
    }
    
    public boolean isBenchmarkRunning() {
        return this.starter != null;
    }
    
    public void interruptBenchmark() {
        this.starter = null;
        this.results.clear();
    }
    
    public void startBenchmark(final UUID starter, final boolean sendAnalytics) {
        this.starter = starter;
        this.sendNextBenchmark = sendAnalytics;
    }
    
    public void addBenchmarkResult(final long original, final long chunks, final long time, final boolean small, final World type) {
        if (this.starter == null || chunks / (double)original < 0.75) {
            return;
        }
        this.results.add(new BenchmarkResult(original, chunks, time, time, small, this.createDimension(type)));
    }
    
    public void onBenchmarkFinished() {
        if (this.results.isEmpty() || this.starter == null) {
            this.results.clear();
            this.starter = null;
            return;
        }
        EntityPlayer player = FMLCommonHandler.instance().getMinecraftServerInstance().func_71203_ab().func_177451_a(this.starter);
        if (player != null && GlobalListeners.INSTANCE.containsListener(player)) {
            player = null;
        }
        int score = 0;
        long time = 0L;
        long chunks = 0L;
        for (final BenchmarkResult result : this.results) {
            score += result.terrainScore();
            time += result.terrainTime;
            chunks += result.chunks;
        }
        this.sendMessage(player, "Benchmark Scores");
        this.sendMessage(player, "Total Score [Chunks=" + chunks + this.time(", Time=", time) + ", Score=" + score + "]");
        for (final BenchmarkResult result : this.results) {
            this.sendMessage(player, this.toPascalCase(result.getPath()) + " Score [Chunks=" + result.chunks + this.time(", Time=", result.terrainTime) + ", Score=" + result.terrainScore() + "]");
        }
        this.sendMessage(player, "Score Rating: Milliseconds per Chunk. Lower => Better");
        this.saveResults(player);
        if (!this.sendNextBenchmark) {
            this.starter = null;
            this.results.clear();
            return;
        }
        try {
            new Thread(new AnalyticsTask(this.createData(), false), "Pregen Analytics").start();
        }
        catch (IOException ex) {}
        this.starter = null;
        this.results.clear();
    }
    
    public boolean reportMass() {
        try {
            final BufferedInputStream stream = new BufferedInputStream(new FileInputStream(new File(MinecraftServer.func_71276_C().func_71238_n(), "config/pregenCache.dat")));
            final int a = stream.available();
            if (a < 100) {
                stream.close();
                return false;
            }
            final byte[] read = new byte[a];
            stream.read(read);
            new Thread(new AnalyticsTask(read, true), "Pregen Analytics").start();
            stream.close();
            return true;
        }
        catch (Exception ex) {
            return false;
        }
    }
    
    private void saveResults(final EntityPlayer player) {
        this.sendMessage(player, "Saving Results for personal Use. Pregen_Benchmarks.json can be found in the ServerFolder");
        final File base = FMLCommonHandler.instance().getMinecraftServerInstance().func_71238_n();
        final File userBenchmarks = new File(base, "Pregen_Benchmarks.json");
        JsonObject obj = new JsonObject();
        if (userBenchmarks.exists()) {
            try {
                final BufferedReader reader = new BufferedReader(new FileReader(userBenchmarks));
                obj = new JsonParser().parse((Reader)reader).getAsJsonObject();
                reader.close();
            }
            catch (Exception ex) {}
        }
        final JsonArray array = obj.has("results") ? obj.getAsJsonArray("results") : new JsonArray();
        obj.add("results", (JsonElement)array);
        array.add((JsonElement)this.createUserData());
        try {
            final JsonWriter writer = new JsonWriter((Writer)new BufferedWriter(new FileWriter(userBenchmarks)));
            writer.setIndent(" ");
            Streams.write((JsonElement)obj, writer);
            writer.flush();
            writer.close();
        }
        catch (Exception ex2) {}
        final File cache = new File(base, "config/pregenCache.dat");
        boolean deleteOnCrash = false;
        try {
            final RandomAccessFile file = new RandomAccessFile(cache, "rw");
            final byte[] toAdd = this.createData();
            if (file.length() <= 0L) {
                file.writeInt(0);
                file.writeLong(12L);
                file.seek(0L);
                deleteOnCrash = true;
            }
            final int benchmarks = file.readInt();
            final long endBytes = file.readLong();
            file.seek(endBytes);
            file.writeInt(toAdd.length);
            deleteOnCrash = true;
            file.write(toAdd);
            file.seek(0L);
            file.writeInt(benchmarks + 1);
            file.writeLong(endBytes + toAdd.length + 4L);
            file.close();
        }
        catch (Exception e) {
            e.printStackTrace();
            if (deleteOnCrash) {
                FMLLog.getLogger().warn("Writing to Cache has crashed.");
                try {
                    if (cache.exists()) {
                        cache.delete();
                    }
                }
                catch (Exception ex3) {}
            }
        }
    }
    
    private byte[] createData() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final DataOutputStream stream = new DataOutputStream(new GZIPOutputStream(out));
        stream.writeInt(0);
        stream.writeInt(1);
        stream.writeInt(31520312);
        stream.writeLong(this.starter.getMostSignificantBits());
        stream.writeLong(this.starter.getLeastSignificantBits());
        stream.writeLong(this.sessionInstance.getMostSignificantBits());
        stream.writeLong(this.sessionInstance.getLeastSignificantBits());
        stream.writeInt(1);
        stream.writeBoolean(false);
        stream.writeInt(ConfigManager.msPerTick);
        stream.writeLong(DimensionManager.getWorld(0).func_72905_C());
        stream.writeUTF(System.getProperty("java.version"));
        stream.writeLong(Runtime.getRuntime().maxMemory());
        stream.writeUTF(this.getCPUName());
        stream.writeInt(Runtime.getRuntime().availableProcessors());
        stream.writeUTF("1.8.9");
        int amount = Math.min(this.results.size(), 1024);
        stream.writeShort(amount);
        for (int i = 0; i < amount; ++i) {
            this.results.get(i).write(stream);
        }
        final List<ModContainer> mods = (List<ModContainer>)Loader.instance().getActiveModList();
        amount = Math.min(mods.size(), 2048);
        stream.writeShort(amount);
        for (final ModContainer mod : mods) {
            stream.writeUTF((mod.getModId() + ";" + mod.getVersion()).toLowerCase());
        }
        stream.close();
        return out.toByteArray();
    }
    
    private JsonObject createUserData() {
        final JsonObject obj = new JsonObject();
        obj.addProperty("seed", (Number)DimensionManager.getWorld(0).func_72905_C());
        obj.addProperty("game_version", "1.8.9");
        obj.addProperty("starter", this.starter.toString());
        obj.addProperty("ms_per_tick", (Number)ConfigManager.msPerTick);
        obj.addProperty("allocated_ram", (Number)Runtime.getRuntime().maxMemory());
        JsonArray data = new JsonArray();
        for (final BenchmarkResult result : this.results) {
            data.add((JsonElement)result.save());
        }
        obj.add("results", (JsonElement)data);
        data = new JsonArray();
        for (final ModContainer mod : Loader.instance().getActiveModList()) {
            data.add((JsonElement)new JsonPrimitive((mod.getModId() + ";" + mod.getVersion()).toLowerCase()));
        }
        obj.add("mods", (JsonElement)data);
        return obj;
    }
    
    private void sendMessage(final EntityPlayer player, final String message) {
        if (player != null) {
            ChunkPregenerator.pregenBase.sendChatMessage(player, message);
        }
        GlobalListeners.INSTANCE.sendChatMessage(message);
    }
    
    private String time(final String prefix, final long value) {
        return prefix + DurationFormatUtils.formatDuration(value, "HH:mm:ss");
    }
    
    private String toPascalCase(final String input) {
        final StringBuilder builder = new StringBuilder();
        for (final String s : input.replaceAll("_", " ").replaceAll("-", " ").split(" ")) {
            builder.append(this.firstLetterUppercase(s)).append(" ");
        }
        return builder.substring(0, builder.length() - 1);
    }
    
    private String firstLetterUppercase(final String string) {
        if (string == null || string.isEmpty()) {
            return string;
        }
        final String first = Character.toString(string.charAt(0));
        return string.replaceFirst(first, first.toUpperCase());
    }
    
    private String getCPUName() {
        try {
            final Processor[] aprocessor = new SystemInfo().getHardware().getProcessors();
            return String.format("%dx %s", aprocessor.length, aprocessor[0]).replaceAll("\\s+", " ").toLowerCase();
        }
        catch (Throwable var3) {
            return "<unknown>";
        }
    }
    
    private String createDimension(final World world) {
        final WorldProvider provider = world.field_73011_w;
        return "minecraft:" + provider.func_80007_l().toLowerCase().replaceAll(" ", "_");
    }
    
    static {
        INSTANCE = new BenchmarkManager();
    }
    
    private class AnalyticsTask implements Runnable
    {
        byte[] data;
        boolean massReport;
        
        public AnalyticsTask(final byte[] data, final boolean massReport) {
            this.data = data;
            this.massReport = massReport;
        }
        
        @Override
        public void run() {
            try {
                final HttpURLConnection connection = (HttpURLConnection)new URL(this.massReport ? "https://pregen.speiger.com/mass" : "https://pregen.speiger.com").openConnection();
                connection.setDoOutput(true);
                connection.setUseCaches(false);
                connection.setRequestMethod("POST");
                connection.addRequestProperty("Content-Type", "binary");
                connection.addRequestProperty("Connection", "close");
                final OutputStream out = connection.getOutputStream();
                out.write(this.data);
                out.flush();
                out.close();
                connection.getResponseCode();
                connection.disconnect();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public static class BenchmarkResult
    {
        long originalChunks;
        long chunks;
        long lightTime;
        long terrainTime;
        boolean small;
        String dim;
        
        public BenchmarkResult(final long originalChunks, final long chunks, final long lightTime, final long terrainTime, final boolean small, final String dim) {
            this.originalChunks = originalChunks;
            this.chunks = chunks;
            this.lightTime = lightTime;
            this.terrainTime = terrainTime;
            this.small = small;
            this.dim = dim;
        }
        
        public String getPath() {
            return this.dim.split(":")[1];
        }
        
        public int terrainScore() {
            return (int)(this.terrainTime / this.chunks);
        }
        
        public void write(final DataOutputStream stream) throws IOException {
            stream.writeLong(this.originalChunks);
            stream.writeLong(this.chunks);
            stream.writeLong(this.terrainTime);
            stream.writeLong(this.lightTime);
            stream.writeBoolean(this.small);
            stream.writeUTF(this.dim);
        }
        
        public JsonObject save() {
            final JsonObject obj = new JsonObject();
            obj.addProperty("original_size", (Number)this.originalChunks);
            obj.addProperty("chunks", (Number)this.chunks);
            obj.addProperty("score", (Number)this.terrainScore());
            obj.addProperty("time", (Number)this.terrainTime);
            obj.addProperty("dim", this.dim.toString());
            obj.addProperty("size", (Number)(this.small ? 40000 : 250000));
            return obj;
        }
    }
}
