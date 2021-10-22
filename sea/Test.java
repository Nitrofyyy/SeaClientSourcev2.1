// 
// Decompiled by Procyon v0.5.36
// 

package sea;

import sea.event.EventTarget;
import java.util.Iterator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import pregenerator.base.PregenAPI;
import net.optifine.util.MemoryMonitor;
import sea.event.entity.EntityJoinWorldEvent;
import pregenerator.PregenProxy;
import pregenerator.ChunkPregenerator;

public class Test
{
    private static ChunkPregenerator chunkp;
    private static PregenProxy pregen;
    private EntityJoinWorldEvent event;
    public static MemoryMonitor memory;
    private static PregenAPI pregenApi;
    public static boolean REMOVE_LOOK_AI;
    public static boolean REMOVE_LOOK_IDLE;
    public static boolean REPLACE_LOOK_HELPER;
    static Throwable throwabl;
    private static EntityPlayer player;
    
    static {
        Test.REMOVE_LOOK_AI = false;
        Test.REMOVE_LOOK_IDLE = false;
        Test.REPLACE_LOOK_HELPER = true;
    }
    
    public void test(ChunkPregenerator cp) {
        Test.chunkp = cp;
        cp = getChunkp();
        getChunkp();
        ChunkPregenerator.LOGGER.catching(Test.throwabl);
        Test.pregen = new PregenProxy();
        Test.memory = new MemoryMonitor();
        MemoryMonitor.update();
        MemoryMonitor.getMemory();
        MemoryMonitor.getUpdateRam();
        Test.pregenApi = new PregenAPI();
        Test.pregen.init();
        getClientPlayer();
    }
    
    @EventTarget
    public void onEntityJoinWorld(final EntityJoinWorldEvent event) {
        final Entity entity = event.entity;
        if (entity instanceof EntityLiving) {
            final EntityLiving living = (EntityLiving)entity;
            if (Test.REMOVE_LOOK_AI || Test.REMOVE_LOOK_IDLE) {
                final Iterator oldHelper = living.tasks.taskEntries.iterator();
                while (oldHelper.hasNext()) {
                    final Object obj = oldHelper.next();
                    if (obj instanceof EntityAITasks.EntityAITaskEntry) {
                        final EntityAITasks.EntityAITaskEntry task = (EntityAITasks.EntityAITaskEntry)obj;
                        if (Test.REMOVE_LOOK_AI && task.action instanceof EntityAIWatchClosest) {
                            oldHelper.remove();
                        }
                        else {
                            if (!Test.REMOVE_LOOK_IDLE || !(task.action instanceof EntityAILookIdle)) {
                                continue;
                            }
                            oldHelper.remove();
                        }
                    }
                }
            }
        }
    }
    
    public static ChunkPregenerator getChunkp() {
        return Test.chunkp;
    }
    
    public static EntityPlayer getClientPlayer() {
        return Test.player;
    }
}
