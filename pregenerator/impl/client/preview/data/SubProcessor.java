// 
// Decompiled by Procyon v0.5.36
// 

package pregenerator.impl.client.preview.data;

import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.Deque;

public class SubProcessor implements Runnable
{
    Thread thread;
    Deque<Runnable> tasks;
    boolean running;
    
    public SubProcessor() {
        this.tasks = new ConcurrentLinkedDeque<Runnable>();
        this.running = true;
        (this.thread = new Thread(this, "HeightMap Calculator")).setDaemon(true);
        this.thread.start();
    }
    
    @Override
    public void run() {
        while (this.running) {
            try {
                while (!this.tasks.isEmpty()) {
                    this.tasks.removeFirst().run();
                }
                Thread.sleep(1L);
            }
            catch (Exception e) {
                if (!this.running) {
                    return;
                }
                e.printStackTrace();
            }
        }
    }
    
    public void terminate() {
        if (!this.running) {
            return;
        }
        this.running = false;
        try {
            this.thread.interrupt();
            while (this.thread.isAlive()) {
                try {
                    Thread.sleep(1L);
                }
                catch (Exception ex) {}
            }
            this.thread = null;
            this.tasks.clear();
        }
        catch (Exception ex2) {}
    }
    
    public void addTask(final Runnable task) {
        this.tasks.add(task);
    }
}
