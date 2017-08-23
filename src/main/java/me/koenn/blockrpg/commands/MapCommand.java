package me.koenn.blockrpg.commands;

import me.koenn.blockrpg.BlockRPG;
import me.koenn.blockrpg.util.MapGenerator;
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
public class MapCommand implements ICommand {

    @Override
    public String getCommand() {
        return "map";
    }

    @Override
    public String getDescription() {
        return "Check out your awesome world.";
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
        String image = new MapGenerator(BlockRPG.getInstance().getWorld(executor), executor).generate();
        return new MessageBuilder().setEmbed(new MessageEmbedImpl()
                .setColor(Color.GREEN)
                .setTitle("Map overview:")
                .setAuthor(new MessageEmbed.AuthorInfo(executor.getName(), executor.getEffectiveAvatarUrl(), executor.getEffectiveAvatarUrl(), ""))
                .setImage(new MessageEmbed.ImageInfo(image, "", 500, 500))
                .setDescription("**Green:** You\n**Red:** Unexplored Tile\n**Blue:** Explored Tile\n**Yellow:** Home Tile")
                .setFooter(new MessageEmbed.Footer("BlockRPG - BETA", "", ""))
                .setFields(new ArrayList<>())
        ).build();
    }
}
