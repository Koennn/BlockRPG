package me.koenn.blockrpg.commands;

import net.dv8tion.jda.core.entities.Channel;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;

/**
 * <p>
 * Copyright (C) Koenn - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Koen Willemse, June 2017
 */
public interface ICommand {

    String getCommand();

    String getDescription();

    int getRequiredArgs();

    boolean isAlias();

    Message execute(User executor, MessageChannel channel, String[] args);

    void callback(Channel channel);
}
