package me.koenn.blockrpg;

import me.koenn.blockrpg.commands.CommandManager;
import me.koenn.blockrpg.data.FileLoader;
import me.koenn.blockrpg.data.Stats;
import net.dv8tion.jda.core.exceptions.RateLimitedException;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.io.IOException;
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

        File file = new File("stats.json");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                this.logger.severe("Error while loading stats file: " + e);
                e.printStackTrace();
            }
        }
        this.stats = FileLoader.loadStats(file);
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

    public Logger getLogger() {
        return logger;
    }

    public DiscordBot getBot() {
        return bot;
    }
}
