package me.koenn.blockrpg.util;

import me.koenn.blockrpg.BlockRPG;
import me.koenn.blockrpg.data.FileLoader;
import net.dv8tion.jda.core.utils.SimpleLog;

import java.io.File;

/**
 * <p>
 * Copyright (C) Koenn - All Rights Reserved Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential Written by Koen Willemse, August 2017
 */
public class Autosaver implements Runnable {

    private final SimpleLog logger = SimpleLog.getLog("Autosaver");

    @Override
    public void run() {
        try {
            Thread.sleep(60000);
        } catch (InterruptedException e) {
            this.logger.info("Autosaver interrupted!");
            return;
        }

        this.logger.info("Saving stats and worlds...");

        BlockRPG instance = BlockRPG.getInstance();
        if (instance.getStats() != null) {
            FileLoader.saveStats(new File("stats.json"), BlockRPG.getInstance().getStats());
        }
        if (instance.getWorlds() != null) {
            FileLoader.saveWorlds(new File("worlds.json"), BlockRPG.getInstance().getWorlds());
        }
    }
}
