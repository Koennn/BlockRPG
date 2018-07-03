package me.koenn.blockrpg.commands;

import me.koenn.blockrpg.BlockRPG;
import me.koenn.blockrpg.data.Stats;
import me.koenn.blockrpg.items.Inventory;
import me.koenn.blockrpg.items.ItemStack;
import me.koenn.blockrpg.util.Checker;
import me.koenn.blockrpg.util.RPGMessageEmbed;
import me.koenn.blockrpg.util.WorldHelper;
import me.koenn.blockrpg.world.World;
import me.koenn.blockrpg.world.village.Trade;
import me.koenn.blockrpg.world.village.Village;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;

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
        if (!Checker.checkHasWorld(executor)) {
            return new MessageBuilder().append("Use **\\stats** first to start playing!").build();
        }
        if (Checker.checkInBattle(executor)) {
            return new MessageBuilder().append("You are in a battle right now!").build();
        }
        World world = WorldHelper.getWorld(executor);
        if (Checker.checkOnTile("village", executor, world)) {
            return new MessageBuilder().append("You not in a village right now!").build();
        }


        Village village = (Village) world.getTile(WorldHelper.getUserLocation(executor)).getProperty("village");
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
            Inventory inventory = (Inventory) BlockRPG.getInstance().getStats(executor).get(Stats.Type.INVENTORY);

            ItemStack cost = trade.getCost();
            ItemStack offer = trade.getOffer();

            if (!inventory.hasItem(cost.getType())) {
                return new MessageBuilder().append(String.format("You don't have %s %s", cost.getAmount(), cost)).build();
            }

            inventory.removeItemStack(cost);
            inventory.addItemStack(offer);

            return new MessageBuilder().setEmbed(new RPGMessageEmbed(
                    "Trade successful!",
                    String.format("You traded %s!", trade.getDisplay().replace(trade.getDisplay().split(" ")[0], "")),
                    executor
            )).build();
        }
    }

    @Override
    public void callback(User executor, MessageChannel channel) {

    }
}
