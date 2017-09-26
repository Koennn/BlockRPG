package me.koenn.blockrpg;

import me.koenn.blockrpg.battle.Battle;
import me.koenn.blockrpg.battle.creature.CreatureType;
import me.koenn.blockrpg.commands.CommandManager;
import me.koenn.blockrpg.data.FileLoader;
import me.koenn.blockrpg.data.Stats;
import me.koenn.blockrpg.image.BattleGenerator;
import me.koenn.blockrpg.image.ImageServer;
import me.koenn.blockrpg.image.MapGenerator;
import me.koenn.blockrpg.items.ItemType;
import me.koenn.blockrpg.util.Autosaver;
import me.koenn.blockrpg.util.Console;
import me.koenn.blockrpg.util.ThreadManager;
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

    private static BlockRPG instance;
    private static ImageServer imageServer;
    private static SimpleLog logger;
    private static ThreadManager threadManager;
    private final HashMap<Long, Battle> userBattles = new HashMap<>();
    private DiscordBot bot;
    private List<Stats> stats;
    private List<World> worlds;

    private BlockRPG(String token) {
        logger = SimpleLog.getLog("BlockRPG");
        logger.info("Starting BlockRPG...");
        threadManager = new ThreadManager();

        Runtime.getRuntime().addShutdownHook(new Thread(this::exit, "shutdown-thread"));

        logger.info("Loading JDA...");
        try {
            this.bot = new DiscordBot(token);
        } catch (LoginException | InterruptedException | RateLimitedException e) {
            logger.fatal("Error while starting bot: " + e);
            e.printStackTrace();
        }

        this.bot.addListener(new CommandManager());

        logger.info("Loading commands...");
        CommandManager.registerCommands();

        logger.info("Starting threads..");
        threadManager.createThread("image-server", () -> imageServer = new ImageServer());
        threadManager.createThread("console", new Console(), true);

        logger.info("Loading items...");
        try {
            ItemType.loadItemTypes(new File("data"));
        } catch (Exception e) {
            logger.fatal("Error while loading items: " + e);
            e.printStackTrace();
            return;
        }

        logger.info("Loading data files...");
        this.stats = FileLoader.loadStats(new File("stats.json"));
        this.worlds = FileLoader.loadWorlds(new File("worlds.json"));

        logger.info("Loading creatures...");
        try {
            CreatureType.loadCreatures(new File("data"));
        } catch (Exception e) {
            logger.fatal("Error while loading creatures: " + e);
            e.printStackTrace();
            return;
        }

        logger.info("Starting autosaver...");
        threadManager.createThread("autosaver", new Autosaver(), true);

        MapGenerator.loadTextures();
        BattleGenerator.loadTextures();

        logger.info("Successfully loaded BlockRPG!");
    }

    public static SimpleLog getLogger() {
        return logger;
    }

    public static BlockRPG getInstance() {
        return instance;
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("No token provided!");
            return;
        }

        instance = new BlockRPG(args[0]);
    }

    private void exit() {
        logger.info("Shutting down BlockRPG...");
        if (ImageServer.server != null) {
            ImageServer.server.stop(0);
        }

        FileLoader.saveStats(new File("stats.json"), BlockRPG.getInstance().getStats());
        FileLoader.saveWorlds(new File("worlds.json"), BlockRPG.getInstance().getWorlds());

        if (bot != null && bot.getJda() != null) {
            bot.getJda().shutdown();
        }

        logger.info("Waiting for bot to disconnect");
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            logger.fatal("Shutdown thread got interrupted: " + e);
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

    public static ThreadManager getThreadManager() {
        return threadManager;
    }
}
