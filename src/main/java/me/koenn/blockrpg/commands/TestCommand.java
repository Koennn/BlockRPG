package me.koenn.blockrpg.commands;

import me.koenn.blockrpg.image.MapGenerator;
import me.koenn.blockrpg.util.RPGMessageEmbed;
import me.koenn.blockrpg.util.WorldHelper;
import me.koenn.blockrpg.world.Tile;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.User;

/**
 * <p>
 * Copyright (C) Koenn - All Rights Reserved Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential Written by Koen Willemse, June 2017
 */
public class TestCommand implements ICommand {

    @Override
    public String getCommand() {
        return "test";
    }

    @Override
    public String getDescription() {
        return "Test!!!!";
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
        Tile tile = WorldHelper.getWorld(executor).getTile(WorldHelper.getUserLocation(executor));
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
