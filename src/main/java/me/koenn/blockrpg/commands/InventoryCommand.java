package me.koenn.blockrpg.commands;

import me.koenn.blockrpg.BlockRPG;
import me.koenn.blockrpg.data.Stats;
import me.koenn.blockrpg.items.Inventory;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.entities.impl.MessageEmbedImpl;

import java.awt.*;
import java.time.OffsetDateTime;
import java.util.ArrayList;

/**
 * <p>
 * Copyright (C) Koenn - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Koen Willemse, June 2017
 */
public class InventoryCommand implements ICommand {

    @Override
    public String getCommand() {
        return "inventory";
    }

    @Override
    public String getDescription() {
        return "See what's in your inventory.";
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
        BlockRPG blockRPG = BlockRPG.getInstance();
        if (blockRPG.hasStats(executor)) {
            stats = blockRPG.getStats(executor);
        } else {
            return new MessageBuilder().append("Use **\\stats** first to start playing!").build();
        }

        if (stats == null) {
            throw new NullPointerException("Unable to find users stats");
        }

        return new MessageBuilder().setEmbed(new MessageEmbedImpl()
                .setColor(Color.GREEN)
                .setTitle("Inventory:")
                .setAuthor(new MessageEmbed.AuthorInfo(executor.getName(), "", executor.getEffectiveAvatarUrl(), ""))
                .setDescription(((Inventory) stats.get("1inventory")).getFormattedString())
                .setFooter(new MessageEmbed.Footer("BlockRPG - BETA", "", ""))
                .setFields(new ArrayList<>())
        ).build();
    }
}
