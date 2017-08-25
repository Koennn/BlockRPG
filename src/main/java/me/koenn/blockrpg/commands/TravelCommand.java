package me.koenn.blockrpg.commands;

import me.koenn.blockrpg.BlockRPG;
import me.koenn.blockrpg.util.Direction;
import me.koenn.blockrpg.util.MapGenerator;
import me.koenn.blockrpg.world.Tile;
import me.koenn.blockrpg.world.Vector2;
import me.koenn.blockrpg.world.World;
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
 * Copyright (C) Koenn - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Koen Willemse, June 2017
 */
public class TravelCommand implements ICommand {

    @Override
    public String getCommand() {
        return "travel";
    }

    @Override
    public String getDescription() {
        return "Travel to an explore tile in your world.";
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
        BlockRPG blockRPG = BlockRPG.getInstance();
        if (!blockRPG.hasWorld(executor)) {
            return new MessageBuilder().append("Use **\\stats** first to start playing!").build();
        }
        World world = blockRPG.getWorld(executor);
        if (world == null) {
            throw new NullPointerException("Unable to find users stats");
        }

        Direction direction = Direction.valueOf(args[0].toUpperCase());
        Vector2 location = blockRPG.getUserLocation(executor).clone();
        Vector2 moved = direction.move(location);
        if (!world.isExplored(moved)) {
            return new MessageBuilder().append("You haven't explored this tile! Use **\\explore** to explore it.").build();
        }
        blockRPG.setUserLocation(executor, moved);
        Tile tile = world.getTile(moved);
        MapGenerator.cachedMaps.clearCache(executor);
        String image = new MapGenerator(BlockRPG.getInstance().getWorld(executor), executor).generate(executor);
        return new MessageBuilder().setEmbed(new MessageEmbedImpl()
                .setColor(Color.GREEN)
                .setTitle("You traveled to the following tile:")
                .setAuthor(new MessageEmbed.AuthorInfo(executor.getName(), "", executor.getEffectiveAvatarUrl(), ""))
                .setDescription(tile.toString())
                .setImage(new MessageEmbed.ImageInfo(image, "", 500, 500))
                .setFooter(new MessageEmbed.Footer("BlockRPG - BETA", "", ""))
                .setFields(new ArrayList<>())
        ).build();
    }

    @Override
    public void callback(User executor, MessageChannel channel) {

    }
}
