package me.koenn.blockrpg.commands;

import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;

/**
 * <p>
 * Copyright (C) Koenn - All Rights Reserved Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential Written by Koen Willemse, September 2017
 */
public class MineCommand implements ICommand {

    @Override
    public String getCommand() {
        return "mine";
    }

    @Override
    public String getDescription() {
        return "Start mining a resource from a Mine.";
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
        return null;
    }

    @Override
    public void callback(User executor, MessageChannel channel) {

    }
}
