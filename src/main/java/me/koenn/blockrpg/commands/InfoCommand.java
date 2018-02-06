package me.koenn.blockrpg.commands;

import me.koenn.blockrpg.items.ItemType;
import me.koenn.blockrpg.util.RPGMessageEmbed;
import me.koenn.blockrpg.util.StringHelper;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;

public class InfoCommand implements ICommand {

    @Override
    public String getCommand() {
        return "info";
    }

    @Override
    public String getDescription() {
        return "See information about an item.";
    }

    @Override
    public int getRequiredArgs() {
        return 1;
    }

    @Override
    public boolean isAlias() {
        return false;
    }

    @Override
    public Message execute(User executor, MessageChannel channel, String[] args) {
        String typeString = StringHelper.concat(args);
        ItemType type;
        try {
            type = ItemType.getItem(typeString.toLowerCase());
        } catch (Exception ex) {
            return new MessageBuilder().setEmbed(new RPGMessageEmbed(String.format("%s is not a valid item", typeString), "", executor)).build();
        }

        if (type == null) {
            return new MessageBuilder().setEmbed(new RPGMessageEmbed(String.format("%s is not a valid item", typeString), "", executor)).build();
        }

        return new MessageBuilder().setEmbed(new RPGMessageEmbed(
                String.format("Information about %s %s", type.getName(), type.getEmote()),
                String.format("```\n%s\n```", type.getDescription()),
                executor
        )).build();
    }

    @Override
    public void callback(User executor, MessageChannel channel) {

    }
}
