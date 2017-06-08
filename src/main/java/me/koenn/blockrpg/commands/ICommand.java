package me.koenn.blockrpg.commands;

import net.dv8tion.jda.core.entities.Message;
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

    Message execute(User executor, String[] args);
}
