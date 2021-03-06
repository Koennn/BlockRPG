package me.koenn.blockrpg.util;

import net.dv8tion.jda.core.utils.SimpleLog;

import java.util.ArrayList;
import java.util.List;

public class ThreadManager implements Thread.UncaughtExceptionHandler {

    private final List<Thread> activeThreads;
    private final SimpleLog logger;
    private boolean enabled;

    public ThreadManager() {
        this.activeThreads = new ArrayList<>();
        this.enabled = true;
        this.logger = SimpleLog.getLog("ThreadManager");
    }

    public void createThread(String name, Runnable target) {
        this.createThread(name, target, false);
    }

    public void createThread(String name, Runnable target, boolean loop) {
        Thread thread;
        if (loop) {
            thread = new Thread(() -> {
                while (this.enabled) {
                    target.run();
                }
            });
        } else {
            thread = new Thread(target);
        }

        thread.setUncaughtExceptionHandler(this);
        thread.setName(name);

        thread.start();
        this.activeThreads.add(thread);
    }

    public void disable() {
        this.logger.info("Stopping all threads...");
        this.enabled = false;

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            logger.fatal("Interrupted while waiting for threads to exit: " + e);
        }

        this.activeThreads.stream().filter(Thread::isAlive).forEach(Thread::interrupt);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable exception) {
        this.logger.fatal("Exception in thread \'" + thread.getName() + "\': " + exception);
        exception.printStackTrace();
    }
}
