package me.koenn.blockrpg;

import me.koenn.blockrpg.battle.Battle;
import me.koenn.blockrpg.commands.CommandManager;
import me.koenn.blockrpg.data.FileLoader;
import me.koenn.blockrpg.data.Stats;
import me.koenn.blockrpg.world.Vector2;
import me.koenn.blockrpg.world.World;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.exceptions.RateLimitedException;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

/**
 * <p>
 * Copyright (C) Koenn - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Koen Willemse, June 2017
 */
public final class BlockRPG {

    private static BlockRPG instance;
    private Logger logger;
    private DiscordBot bot;
    private List<Stats> stats;
    private List<World> worlds;
    private HashMap<Long, Vector2> userLocations = new HashMap<>();
    private HashMap<Long, Battle> userBattles = new HashMap<>();

    private BlockRPG(String token) {
        this.logger = Logger.getLogger("BlockRPG");

        try {
            this.bot = new DiscordBot(token);
        } catch (LoginException | InterruptedException | RateLimitedException e) {
            this.logger.severe("Error while starting bot: " + e);
            e.printStackTrace();
        }

        this.bot.addListener(new CommandManager());

        CommandManager.registerCommands();

        File statsFile = new File("stats.json");
        if (!statsFile.exists()) {
            try {
                statsFile.createNewFile();
            } catch (IOException e) {
                this.logger.severe("Error while loading stats file: " + e);
                e.printStackTrace();
            }
        }
        this.stats = FileLoader.loadStats(statsFile);

        File worldsFile = new File("worlds.json");
        if (!worldsFile.exists()) {
            try {
                worldsFile.createNewFile();
            } catch (IOException e) {
                this.logger.severe("Error while loading stats file: " + e);
                e.printStackTrace();
            }
        }
        this.worlds = FileLoader.loadWorlds(worldsFile);
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

    public Logger getLogger() {
        return logger;
    }

    public DiscordBot getBot() {
        return bot;
    }

    public HashMap<Long, Battle> getUserBattles() {
        return userBattles;
    }
}
