package me.koenn.blockrpg.commands;

import me.koenn.blockrpg.world.village.Trade;
import me.koenn.blockrpg.world.village.Village;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * <p>
 * Copyright (C) Koenn - All Rights Reserved Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential Written by Koen Willemse, June 2017
 */
public class TestCommand implements ICommand {

    @Override
    public String getCommand() {
        return "test";
    }

    @Override
    public String getDescription() {
        return "Test!!!!";
    }

    @Override
    public int getRequiredArgs() {
        return 0;
    }

    @Override
    public boolean isAlias() {
        return false;
    }

    @Override
    public Message execute(User executor, MessageChannel channel, String[] args) {
        List<Trade> trades = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            trades.add(new Trade(ThreadLocalRandom.current().nextInt(6, 100)));
            if (ThreadLocalRandom.current().nextBoolean()) {
                break;
            }
        }
        return new MessageBuilder().append(new Village(6, trades.toArray(new Trade[trades.size()])).getDisplay()).build();
    }

    @Override
    public void callback(User executor, MessageChannel channel) {

    }
}
