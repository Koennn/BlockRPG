package me.koenn.blockrpg.commands;

import me.koenn.blockrpg.BlockRPG;
import me.koenn.blockrpg.util.RPGMessageEmbed;
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
 * Copyright (C) Koenn - All Rights Reserved Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential Written by Koen Willemse, June 2017
 */
public class CommandManager extends ListenerAdapter {

    public static final Registry<ICommand> COMMAND_REGISTRY = new Registry<>(ICommand::getCommand);
    public static final String ALLOWED_CHANNEL = "development";
    public static final String CMD_CHAR = "\\";

    public static void registerAlias(ICommand command, String alias) {
        COMMAND_REGISTRY.register(new AliasCommand(command, alias));
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
        COMMAND_REGISTRY.register(new InfoCommand());
    }

    private static void addReaction(Message message, String emote) {
        if (!message.getChannel().getType().equals(ChannelType.PRIVATE)) {
            message.addReaction(message.getGuild().getEmotesByName(emote, false).get(0)).queue();
        }
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        this.interpret(event.getMessage(), event.getAuthor(), event.getChannel());
    }

    private void interpret(Message message, User executor, MessageChannel channel) {
        String content = message.getContent();
        if (!content.startsWith(CMD_CHAR)) {
            return;
        }

        if (!channel.getName().equals(ALLOWED_CHANNEL) && !channel.getType().equals(ChannelType.PRIVATE)) {
            return;
        }

        String[] split = content.split(" ");
        String[] args = new String[split.length - 1];
        System.arraycopy(split, 1, args, 0, args.length);
        String commandString = split[0].replace(CMD_CHAR, "");

        ICommand command = COMMAND_REGISTRY.get(commandString);
        if (command == null) {
            addReaction(message, "cross");
            channel.sendTyping().queue(void1 -> channel.sendMessage(new MessageBuilder().setEmbed(new RPGMessageEmbed(
                    String.format("Unknown command %s", commandString),
                    String.format("Use %shelp for a list of commands", CMD_CHAR),
                    executor
            )).build()).queue());
            return;
        }

        if (command.getRequiredArgs() > args.length) {
            addReaction(message, "cross");
            channel.sendTyping().queue(void1 -> channel.sendMessage(new MessageBuilder().setEmbed(new RPGMessageEmbed(
                    String.format("Command %s requires additional arguments.", command.getCommand()),
                    command.getDescription(), executor
            )).build()).queue());
            return;
        }

        addReaction(message, "check");
        BlockRPG.getLogger().info(String.format("User \'%s\' executed command \'%s\'", executor.getName(), command.getCommand()));

        BlockRPG.getThreadManager().createThread(String.format("%s-cmd-thread", executor.getId()), () -> channel.sendTyping().queue(void1 -> {
            Message response;
            boolean error = false;
            try {
                response = command.execute(executor, channel, args);
            } catch (Exception ex) {
                response = new MessageBuilder().append(String.format("Error while executing command \'%s\': %s", command.getCommand(), ex)).build();
                BlockRPG.getLogger().fatal(String.format("Error while executing command \'%s\': %s", command.getCommand(), ex));
                ex.printStackTrace();
                error = true;
            }
            boolean callback = !error;

            if (response != null) {
                channel.sendMessage(response).queue(void2 -> {
                    if (callback) {
                        command.callback(executor, channel);
                    }
                });
            }
        }));
    }
}
