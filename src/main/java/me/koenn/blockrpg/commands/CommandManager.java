package me.koenn.blockrpg.commands;

import me.koenn.blockrpg.util.registry.Registry;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
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
    public static final String ALLOWED_CHANNEL = "development";

    public static void registerAlias(ICommand command, String alias) {
        COMMAND_REGISTRY.register(new ICommand() {
            @Override
            public String getCommand() {
                return alias;
            }

            @Override
            public String getDescription() {
                return command.getDescription();
            }

            @Override
            public int getRequiredArgs() {
                return command.getRequiredArgs();
            }

            @Override
            public boolean isAlias() {
                return true;
            }

            @Override
            public Message execute(User executor, MessageChannel channel, String[] args) {
                return command.execute(executor, channel, args);
            }

            @Override
            public void callback(User executor, MessageChannel channel) {

            }
        });
    }

    public static void registerCommands() {
        registerAlias(COMMAND_REGISTRY.register(new HelpCommand()), "?");
        COMMAND_REGISTRY.register(new StatsCommand());
        registerAlias(COMMAND_REGISTRY.register(new InventoryCommand()), "inv");
        COMMAND_REGISTRY.register(new ExploreCommand());
        COMMAND_REGISTRY.register(new TravelCommand());
        registerAlias(COMMAND_REGISTRY.register(new MoveCommand()), "mv");
        COMMAND_REGISTRY.register(new MapCommand());
        COMMAND_REGISTRY.register(new TestCommand());
        COMMAND_REGISTRY.register(new UseCommand());
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        this.interpret(event.getMessage().getContent(), event.getAuthor(), event.getChannel());
    }

    private void interpret(String content, User executor, MessageChannel channel) {
        if (!content.startsWith("\\")) {
            return;
        }

        if (!channel.getName().equals(ALLOWED_CHANNEL) && !channel.getType().equals(ChannelType.PRIVATE)) {
            return;
        }

        String[] split = content.split(" ");
        String[] args = new String[split.length - 1];
        System.arraycopy(split, 1, args, 0, args.length);
        String commandString = split[0].replace("\\", "");

        ICommand command = COMMAND_REGISTRY.get(commandString);
        if (command == null) {
            return;
        }

        if (command.getRequiredArgs() > args.length) {
            return;
        }

        channel.sendTyping().queue(void1 -> {
            Message message;
            boolean error = false;
            try {
                message = command.execute(executor, channel, args);
            } catch (Exception ex) {
                message = new MessageBuilder().append(String.format("Error while executing command \'%s\': %s", command.getCommand(), ex)).build();
                ex.printStackTrace();
                error = true;
            }
            boolean callback = !error;

            if (message != null) {
                channel.sendMessage(message).queue(void2 -> {
                    if (callback) {
                        command.callback(executor, channel);
                    }
                });
            }
        });
    }
}
