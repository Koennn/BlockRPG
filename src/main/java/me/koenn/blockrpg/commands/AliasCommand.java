package me.koenn.blockrpg.commands;

import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;

public class AliasCommand implements ICommand {

    private final ICommand command;
    private final String alias;

    public AliasCommand(ICommand command, String alias) {
        this.command = command;
        this.alias = alias;
    }

    @Override
    public String getCommand() {
        return this.alias;
    }

    @Override
    public String getDescription() {
        return this.command.getDescription();
    }

    @Override
    public int getRequiredArgs() {
        return this.command.getRequiredArgs();
    }

    @Override
    public boolean isAlias() {
        return true;
    }

    @Override
    public Message execute(User executor, MessageChannel channel, String[] args) {
        return this.command.execute(executor, channel, args);
    }

    @Override
    public void callback(User executor, MessageChannel channel) {
        this.command.callback(executor, channel);
    }
}
