package me.koenn.blockrpg.commands;

import me.koenn.blockrpg.util.registry.Registry;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

/**
 * <p>
 * Copyright (C) Koenn - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Koen Willemse, June 2017
 */
public class CommandManager extends ListenerAdapter {

    public static final Registry<ICommand> COMMAND_REGISTRY = new Registry<>(ICommand::getCommand);

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        final Message message = event.getMessage();
        final String content = message.getContent();

        if (!content.startsWith("\\")) {
            return;
        }

        final String[] split = content.split(" ");
        final String[] args = new String[split.length - 1];
        System.arraycopy(split, 1, args, 0, args.length);
        final String commandString = split[0].replace("\\", "");

        ICommand command = COMMAND_REGISTRY.get(commandString);
        if (command == null) {
            return;
        }

        if (command.getRequiredArgs() > args.length) {
            return;
        }

        final User executor = event.getAuthor();
        final TextChannel channel = event.getTextChannel();

        channel.sendTyping().queue();
        channel.sendMessage(command.execute(executor, args)).queue();
    }

    public static void registerCommands() {
        COMMAND_REGISTRY.register(new HelpCommand());
    }
}
