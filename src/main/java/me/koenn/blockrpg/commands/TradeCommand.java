package me.koenn.blockrpg.commands;

import me.koenn.blockrpg.BlockRPG;
import me.koenn.blockrpg.items.Inventory;
import me.koenn.blockrpg.items.ItemStack;
import me.koenn.blockrpg.util.RPGMessageEmbed;
import me.koenn.blockrpg.util.WorldHelper;
import me.koenn.blockrpg.world.Tile;
import me.koenn.blockrpg.world.World;
import me.koenn.blockrpg.world.village.Trade;
import me.koenn.blockrpg.world.village.Village;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.entities.impl.MessageEmbedImpl;

import java.awt.*;
import java.util.ArrayList;

/**
 * <p>
 * Copyright (C) Koenn - All Rights Reserved Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential Written by Koen Willemse, September 2017
 */
public class TradeCommand implements ICommand {

    @Override
    public String getCommand() {
        return "trade";
    }

    @Override
    public String getDescription() {
        return "Trade with the village you're standing in.";
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
        if (!WorldHelper.hasWorld(executor)) {
            return new MessageBuilder().append("Use **\\stats** first to start playing!").build();
        }
        World world = WorldHelper.getWorld(executor);
        if (world == null) {
            throw new NullPointerException("Unable to find users stats");
        }

        if (BlockRPG.getInstance().getUserBattles().get(executor.getIdLong()) != null) {
            return new MessageBuilder().append("You are in a battle right now!").build();
        }
        Tile tile = world.getTile(WorldHelper.getUserLocation(executor));
        if (tile.getProperty("village") == null) {
            return new MessageBuilder().append("You not in a village right now!").build();
        }
        Village village = (Village) tile.getProperty("village");

        if (args.length == 0) {
            return new MessageBuilder().setEmbed(new RPGMessageEmbed("Village", village.getDisplay(), executor)).build();
        } else {
            int id;
            try {
                id = Integer.parseInt(args[0]) - 1;
            } catch (NumberFormatException ex) {
                return new MessageBuilder().append("Trade id must be a number!").build();
            }
            if (id < 0 || id > village.getTrades().size()) {
                return new MessageBuilder().append(String.format("There's no trade with id %s", id)).build();
            }

            Trade trade = village.getTrades().get(id);
            Inventory inventory = (Inventory) BlockRPG.getInstance().getStats(executor).get("inventory");

            ItemStack cost = trade.getCost();
            ItemStack offer = trade.getOffer();

            if (!inventory.hasItem(cost.getType())) {
                return new MessageBuilder().append(String.format("You don't have %s %s", cost.getAmount(), cost)).build();
            }

            inventory.removeItemStack(cost);
            inventory.addItemStack(offer);

            return new MessageBuilder().setEmbed(new MessageEmbedImpl()
                    .setColor(Color.GREEN)
                    .setTitle("Inventory:")
                    .setAuthor(new MessageEmbed.AuthorInfo(executor.getName(), "", executor.getEffectiveAvatarUrl(), ""))
                    .setDescription(inventory.getFormattedString())
                    .setFooter(new MessageEmbed.Footer("BlockRPG - BETA", "", ""))
                    .setFields(new ArrayList<>())
            ).build();
        }
    }

    @Override
    public void callback(User executor, MessageChannel channel) {

    }
}
