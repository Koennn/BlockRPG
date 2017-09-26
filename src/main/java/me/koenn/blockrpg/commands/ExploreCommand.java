package me.koenn.blockrpg.commands;

import me.koenn.blockrpg.BlockRPG;
import me.koenn.blockrpg.image.MapGenerator;
import me.koenn.blockrpg.util.RPGMessageEmbed;
import me.koenn.blockrpg.util.WorldHelper;
import me.koenn.blockrpg.world.Direction;
import me.koenn.blockrpg.world.Tile;
import me.koenn.blockrpg.world.Vector2;
import me.koenn.blockrpg.world.World;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.User;

import java.util.Random;

/**
 * <p>
 * Copyright (C) Koenn - All Rights Reserved Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential Written by Koen Willemse, June 2017
 */
public class ExploreCommand implements ICommand {

    private static final Random random = new Random();

    @Override
    public String getCommand() {
        return "explore";
    }

    @Override
    public String getDescription() {
        return "Explore your beautiful world.";
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
        if (!WorldHelper.hasWorld(executor)) {
            return new MessageBuilder().append("Use **\\stats** first to start playing!").build();
        }
        World world = WorldHelper.getWorld(executor);
        if (world == null) {
            throw new NullPointerException("Unable to find users world");
        }

        if (BlockRPG.getInstance().getUserBattles().get(executor.getIdLong()) != null) {
            return new MessageBuilder().append("You are in a battle right now!").build();
        }

        Vector2 moved = Direction.valueOf(args[0].toUpperCase()).move(WorldHelper.getUserLocation(executor).clone());
        if (world.isExplored(moved)) {
            return new MessageBuilder().append("You already explored this tile! Use **\\travel** to get there.").build();
        }

        Tile tile = world.explore(moved);

        Message battleMessage = world.getBattle(executor, channel, tile);
        if (battleMessage != null) {
            return battleMessage;
        }

        WorldHelper.setUserLocation(executor, tile.getLocation());
        MapGenerator.cachedMaps.clearCache(executor);
        String image = new MapGenerator(WorldHelper.getWorld(executor), executor).generate(executor);
        return new MessageBuilder().setEmbed(new RPGMessageEmbed(
                "You discovered a new tile:",
                tile.toString(), executor
        ).setImage(new MessageEmbed.ImageInfo(image, "", 500, 500))).build();
    }

    @Override
    public void callback(User executor, MessageChannel channel) {

    }
}
