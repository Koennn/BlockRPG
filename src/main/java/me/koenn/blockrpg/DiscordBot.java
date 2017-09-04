package me.koenn.blockrpg;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import javax.security.auth.login.LoginException;

/**
 * <p>
 * Copyright (C) Koenn - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Koen Willemse, June 2017
 */
public class DiscordBot {

    private final JDA jda;

    public DiscordBot(String token) throws LoginException, InterruptedException, RateLimitedException {
        this.jda = new JDABuilder(AccountType.BOT)
                .setToken(token)
                .setAutoReconnect(true)
                .setGame(Game.of("in development!"))
                .setStatus(OnlineStatus.DO_NOT_DISTURB)
                .buildBlocking();
    }

    public JDA getJda() {
        return jda;
    }

    public void addListener(ListenerAdapter listener) {
        this.jda.addEventListener(listener);
    }
}
