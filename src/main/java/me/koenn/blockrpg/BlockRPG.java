package me.koenn.blockrpg;

import me.koenn.blockrpg.battle.Battle;
import me.koenn.blockrpg.battle.creature.CreatureType;
import me.koenn.blockrpg.commands.CommandManager;
import me.koenn.blockrpg.data.FileLoader;
import me.koenn.blockrpg.data.Stats;
import me.koenn.blockrpg.image.ImageServer;
import me.koenn.blockrpg.util.Console;
import me.koenn.blockrpg.util.ThreadManager;
import me.koenn.blockrpg.world.Vector2;
import me.koenn.blockrpg.world.World;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import net.dv8tion.jda.core.utils.SimpleLog;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * Copyright (C) Koenn - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Koen Willemse, June 2017
 */
public final class BlockRPG {

    private static BlockRPG instance;
    private static ImageServer imageServer;
    private static SimpleLog logger;
    private static ThreadManager threadManager;
    private DiscordBot bot;
    private List<Stats> stats;
    private List<World> worlds;
    private HashMap<Long, Vector2> userLocations = new HashMap<>();
    private HashMap<Long, Battle> userBattles = new HashMap<>();

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

        logger.info("Loading files...");
        this.stats = FileLoader.loadStats(new File("stats.json"));
        this.worlds = FileLoader.loadWorlds(new File("worlds.json"));

        logger.info("Loading creatures...");
        try {
            CreatureType.loadCreatures(new File("data"));
        } catch (IOException e) {
            logger.fatal("Error while loading creatures: " + e);
            e.printStackTrace();
            return;
        }

        logger.info("Successfully loaded BlockRPG!");
    }

    public static SimpleLog getLogger() {
        return logger;
    }

    public static BlockRPG getInstance() {
        return instance;
    }

    public static ImageServer getImageServer() {
        return imageServer;
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

    public boolean hasWorld(User user) {
        return getWorld(user) != null;
    }

    public World getWorld(User user) {
        long userId = user.getIdLong();
        for (World world : this.worlds) {
            if (world.getUserId() == userId) {
                return world;
            }
        }
        return null;
    }

    public void addWorld(World world) {
        this.worlds.add(world);
        FileLoader.saveWorlds(new File("worlds.json"), this.worlds);
    }

    public Vector2 getUserLocation(User user) {
        return this.userLocations.get(user.getIdLong());
    }

    public void setUserLocation(User user, Vector2 location) {
        this.userLocations.put(user.getIdLong(), location);
    }

    public HashMap<Long, Battle> getUserBattles() {
        return userBattles;
    }

    public DiscordBot getBot() {
        return bot;
    }
}
