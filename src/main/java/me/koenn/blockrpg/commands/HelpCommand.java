package me.koenn.blockrpg.commands;

import me.koenn.blockrpg.util.RPGMessageEmbed;
import net.dv8tion.jda.core.MessageBuilder;
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
public class HelpCommand implements ICommand {

    @Override
    public String getCommand() {
        return "help";
    }

    @Override
    public String getDescription() {
        return "Opens up this menu :)";
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
        StringBuilder stringBuilder = new StringBuilder();
        for (ICommand command : CommandManager.COMMAND_REGISTRY.getRegisteredObjects()) {
            if (command.isAlias()) {
                continue;
            }
            stringBuilder.append("**`\\").append(command.getCommand()).append("`:**\n").append(command.getDescription()).append("\n");
        }
        String help = stringBuilder.toString().trim();
        return new MessageBuilder().setEmbed(new RPGMessageEmbed(
                "BlockRPG Help Menu", help, executor
        )).build();
    }

    @Override
    public void callback(User executor, MessageChannel channel) {

    }
}
