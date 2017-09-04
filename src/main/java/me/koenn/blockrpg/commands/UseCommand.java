package me.koenn.blockrpg.commands;

import me.koenn.blockrpg.BlockRPG;
import me.koenn.blockrpg.items.Inventory;
import me.koenn.blockrpg.items.ItemStack;
import me.koenn.blockrpg.items.ItemType;
import me.koenn.blockrpg.util.RPGMessageEmbed;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;

public class UseCommand implements ICommand {

    private static String concat(String[] words) {
        StringBuilder builder = new StringBuilder();
        for (String word : words) {
            builder.append(word).append("_");
        }
        String string = builder.toString();
        return string.substring(0, string.length() - 1);
    }

    @Override
    public String getCommand() {
        return "use";
    }

    @Override
    public String getDescription() {
        return "Use an item on yourself.";
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
        if (!BlockRPG.getInstance().hasStats(executor)) {
            return new MessageBuilder().append("Use **\\stats** first to start playing!").build();
        }

        String typeString = concat(args);
        ItemType type;
        try {
            type = ItemType.valueOf(typeString.toUpperCase());
        } catch (Exception ex) {
            return new MessageBuilder().setEmbed(new RPGMessageEmbed(String.format("%s is not a valid item", typeString), "", executor)).build();
        }

        Inventory inventory = (Inventory) BlockRPG.getInstance().getStats(executor).get("inventory");
        if (!inventory.hasItem(type)) {
            return new MessageBuilder().setEmbed(new RPGMessageEmbed(String.format("You don't have a %s", type.getName()), "", executor)).build();
        }

        if (!type.hasItemAction()) {
            return new MessageBuilder().setEmbed(new RPGMessageEmbed(String.format("You cant use a %s", type.getName()), "", executor)).build();
        }

        ItemStack stack = inventory.getItemStack(type);
        stack.setAmount(stack.getAmount() - 1);
        if (stack.getAmount() <= 0) {
            inventory.getItems().remove(stack);
        }

        return type.getItemAction().execute(executor, channel);
    }

    @Override
    public void callback(User executor, MessageChannel channel) {

    }
}