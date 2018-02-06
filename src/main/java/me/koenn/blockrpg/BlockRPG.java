package me.koenn.blockrpg;

import me.koenn.blockrpg.battle.Battle;
import me.koenn.blockrpg.battle.creature.CreatureType;
import me.koenn.blockrpg.commands.CommandManager;
import me.koenn.blockrpg.data.FileLoader;
import me.koenn.blockrpg.data.Stats;
import me.koenn.blockrpg.image.ImageServer;
import me.koenn.blockrpg.image.MapGenerator;
import me.koenn.blockrpg.items.ItemType;
import me.koenn.blockrpg.util.Autosaver;
import me.koenn.blockrpg.util.Console;
import me.koenn.blockrpg.util.ThreadManager;
import me.koenn.blockrpg.util.TimeMeasurer;
import me.koenn.blockrpg.world.World;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import net.dv8tion.jda.core.utils.SimpleLog;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * Copyright (C) Koenn - All Rights Reserved Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential Written by Koen Willemse, June 2017
 */
public final class BlockRPG {

    public static final boolean ENABLE_LOCAL_SERVER = false;
    public static final SimpleLog LOGGER = SimpleLog.getLog("BlockRPG");

    private static BlockRPG instance;
    private static ThreadManager threadManager;
    private final HashMap<Long, Battle> userBattles = new HashMap<>();
    private DiscordBot bot;
    private List<Stats> stats;
    private List<World> worlds;

    private BlockRPG(String token) {
        LOGGER.info("Starting BlockRPG...");
        TimeMeasurer timeMeasurer = new TimeMeasurer().start();
        threadManager = new ThreadManager();
        Runtime.getRuntime().addShutdownHook(new Thread(this::exit, "shutdown-thread"));

        LOGGER.info("Loading JDA...");
        try {
            this.bot = new DiscordBot(token);
        } catch (LoginException l) {
            LOGGER.fatal("Unable to log in to Discord: " + l);
            l.printStackTrace();
            return;
        } catch (InterruptedException i) {
            LOGGER.fatal("Interrupted while starting bot: " + i);
            i.printStackTrace();
            return;
        } catch (RateLimitedException r) {
            LOGGER.fatal("Rate-limited by Discord, hold on!");
            main(new String[]{token});
            return;
        }
        this.bot.addListener(new CommandManager());

        LOGGER.info("Loading commands...");
        CommandManager.registerCommands();

        LOGGER.info("Loading items...");
        try {
            ItemType.loadItemTypes(new File("data"));
        } catch (Exception e) {
            LOGGER.fatal("Error while loading items: " + e);
            e.printStackTrace();
            return;
        }

        LOGGER.info("Loading creatures...");
        try {
            CreatureType.loadCreatures(new File("data"));
        } catch (Exception e) {
            LOGGER.fatal("Error while loading creatures: " + e);
            e.printStackTrace();
            return;
        }

        LOGGER.info("Loading data files...");
        this.stats = FileLoader.loadStats(new File("stats.json"));
        this.worlds = FileLoader.loadWorlds(new File("worlds.json"));

        LOGGER.info("Loading textures...");
        MapGenerator.loadTextures();

        LOGGER.info("Starting threads..");
        threadManager.createThread("image-server", new ImageServer());
        threadManager.createThread("console", new Console(), true);
        threadManager.createThread("autosaver", new Autosaver(), true);
        threadManager.createThread("timer", new TimeMeasurer.TimerThread(), true);

        LOGGER.info("Successfully loaded BlockRPG!");
        LOGGER.info(String.format("Startup took %ss", round(timeMeasurer.stop() / 1000.0, 2)));
    }

    public static BlockRPG getInstance() {
        return instance;
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            LOGGER.fatal("No token provided!");
            return;
        }

        instance = new BlockRPG(args[0]);
    }

    public static ThreadManager getThreadManager() {
        return threadManager;
    }

    private static double round(double value, int precision) {
        int scale = (int) Math.pow(10, precision);
        return (double) Math.round(value * scale) / scale;
    }

    private void exit() {
        LOGGER.info("Shutting down BlockRPG...");
        if (ImageServer.server != null) {
            ImageServer.server.stop(0);
        }

        try {
            FileLoader.saveStats(new File("stats.json"), BlockRPG.getInstance().getStats());
            FileLoader.saveWorlds(new File("worlds.json"), BlockRPG.getInstance().getWorlds());
        } catch (Exception ex) {
            LOGGER.warn("Failed to save files while shutting down!");
        }

        if (bot != null && bot.getJda() != null) {
            bot.getJda().shutdown();

            LOGGER.info("Waiting for bot to disconnect");
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                LOGGER.fatal("Shutdown thread got interrupted: " + e);
            }
        }

        threadManager.disable();
    }

    public boolean hasStats(User user) {
        return getStats(user) != null;
    }

    public Stats getStats(User user) {
        long userId = user.getIdLong();
        for (Stats stats : this.stats) {
            if (stats.getUserId() == userId) {
                return stats;
            }
        }
        return null;
    }

    public void addStats(Stats stats) {
        this.stats.add(stats);
        FileLoader.saveStats(new File("stats.json"), this.stats);
    }

    public HashMap<Long, Battle> getUserBattles() {
        return userBattles;
    }

    public List<World> getWorlds() {
        return worlds;
    }

    public List<Stats> getStats() {
        return stats;
    }
}
