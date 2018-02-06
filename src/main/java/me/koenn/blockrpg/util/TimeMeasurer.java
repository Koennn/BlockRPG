package me.koenn.blockrpg.util;

import me.koenn.blockrpg.BlockRPG;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Copyright (C) Koenn - All Rights Reserved Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential Written by Koen Willemse, September 2017
 */
public class TimeMeasurer {

    private static final List<TimeMeasurer> timeMeasurers = new ArrayList<>();

    private long startTime;
    private long actionTime;
    private Runnable action;

    public TimeMeasurer start() {
        this.startTime = System.currentTimeMillis();
        timeMeasurers.add(this);
        return this;
    }

    public long stop() {
        timeMeasurers.remove(this);
        return System.currentTimeMillis() - this.startTime;
    }

    public void on(long time, Runnable action) {
        this.actionTime = this.startTime + time;
        this.action = action;
    }

    private void tick() {
        if (this.action != null && System.currentTimeMillis() > this.actionTime) {
            this.action.run();
            this.action = null;
        }
    }

    public static class TimerThread implements Runnable {

        @Override
        public void run() {
            timeMeasurers.forEach(TimeMeasurer::tick);

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                BlockRPG.LOGGER.warn("TimeMeasurer thread interrupted!");
            }
        }
    }
}
