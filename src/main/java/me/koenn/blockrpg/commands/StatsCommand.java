package me.koenn.blockrpg.commands;

import me.koenn.blockrpg.BlockRPG;
import me.koenn.blockrpg.data.Stats;
import me.koenn.blockrpg.world.Vector2;
import me.koenn.blockrpg.world.World;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.*;
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
public class StatsCommand implements ICommand {

    @Override
    public String getCommand() {
        return "stats";
    }

    @Override
    public String getDescription() {
        return "See your awesome stats.";
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
        Stats stats;
        boolean newUser = false;
        BlockRPG blockRPG = BlockRPG.getInstance();
        if (blockRPG.hasStats(executor)) {
            stats = blockRPG.getStats(executor);
        } else {
            stats = new Stats(executor.getIdLong());
            blockRPG.addStats(stats);
            newUser = true;
        }

        if (!blockRPG.hasWorld(executor)) {
            blockRPG.addWorld(new World(executor.getIdLong()));
            blockRPG.setUserLocation(executor, new Vector2());
            newUser = true;
        }

        if (stats == null) {
            throw new NullPointerException("Unable to find users stats");
        }

        return new MessageBuilder().setEmbed(new MessageEmbedImpl()
                .setColor(Color.GREEN)
                .setTitle("Stats:")
                .setAuthor(new MessageEmbed.AuthorInfo(executor.getName(), executor.getEffectiveAvatarUrl(), executor.getEffectiveAvatarUrl(), ""))
                .setDescription((newUser ? "**Welcome new player!**\n\n" : "") + stats.getFormattedStats())
                .setFooter(new MessageEmbed.Footer("BlockRPG - BETA", "", ""))
                .setFields(new ArrayList<>())
        ).build();
    }

    @Override
    public void callback(Channel channel) {

    }
}
