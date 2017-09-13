package me.koenn.blockrpg.util;

import net.dv8tion.jda.core.utils.SimpleLog;

import java.util.ArrayList;
import java.util.List;

public class ThreadManager implements Thread.UncaughtExceptionHandler {

    private final List<Thread> activeThreads;
    private boolean enabled;
    private final SimpleLog logger;

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

        this.activeThreads.add(thread);
        thread.start();

        this.logger.info(String.format("Created and loaded thread \'%s\'", name));
    }

    public void disable() {
        this.logger.info("Stopping all threads...");
        this.enabled = false;
        this.activeThreads.stream().filter(Thread::isAlive).forEach(Thread::interrupt);

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            logger.fatal("Error while waiting for threads to exit: " + e);
        }
    }

    @Override
    public void uncaughtException(Thread thread, Throwable exception) {
        this.logger.fatal("Exception in thread \'" + thread.getName() + "\': " + exception);
        exception.printStackTrace();
    }
}
