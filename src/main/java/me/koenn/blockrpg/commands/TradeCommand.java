package me.koenn.blockrpg.commands;

import me.koenn.blockrpg.BlockRPG;
import me.koenn.blockrpg.util.RPGMessageEmbed;
import me.koenn.blockrpg.util.WorldHelper;
import me.koenn.blockrpg.world.Tile;
import me.koenn.blockrpg.world.World;
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
            int id = Integer.parseInt(args[0]);
        }
        return null;
    }

    @Override
    public void callback(User executor, MessageChannel channel) {

    }
}
