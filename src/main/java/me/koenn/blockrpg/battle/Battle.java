package me.koenn.blockrpg.battle;

import me.koenn.blockrpg.BlockRPG;
import me.koenn.blockrpg.items.Inventory;
import me.koenn.blockrpg.items.ItemStack;
import me.koenn.blockrpg.items.WeaponAction;
import me.koenn.blockrpg.world.Tile;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.entities.impl.MessageEmbedImpl;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * <p>
 * Copyright (C) Koenn - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Koen Willemse, June 2017
 */
public class Battle {

    private final User user;
    private final MessageChannel channel;
    private final Creature opponent;
    private final Tile location;
    private final LinkedHashMap<Integer, WeaponAction> userMoves = new LinkedHashMap<>();

    public Battle(User user, MessageChannel channel, Creature opponent, Tile location) {
        this.user = user;
        this.channel = channel;
        this.opponent = opponent;
        this.location = location;
    }

    public void start() {
        StringBuilder yourMoves = new StringBuilder();
        int index = 1;
        for (ItemStack item : ((Inventory) BlockRPG.getInstance().getStats(user).get("1inventory")).getItems()) {
            if (item.getType().getActions() != null) {
                for (WeaponAction action : item.getType().getActions()) {
                    userMoves.put(index, action);
                    yourMoves.append("`  \\move ").append(index).append("`: ").append(action.getActionName()).append("\n");
                    index++;
                }
            }
        }
        this.channel.sendMessage(new MessageBuilder().setEmbed(new MessageEmbedImpl()
                .setColor(Color.GREEN)
                .setTitle("You encountered a **" + this.opponent.getType().getName() + "**")
                .setAuthor(new MessageEmbed.AuthorInfo(this.user.getName(), this.user.getEffectiveAvatarUrl(), this.user.getEffectiveAvatarUrl(), ""))
                .setDescription("" +
                        "**Your health:** " + BlockRPG.getInstance().getStats(user).get("0health") + "/100\n" +
                        "**" + this.opponent.getType().getName() + "'s health:** " + this.opponent.getHealth() + "/" + this.opponent.getMaxHealth() + "\n\n" +
                        "**Your possible moves:**\n" + yourMoves.toString().trim()
                )
                .setFooter(new MessageEmbed.Footer("BlockRPG - BETA", "", ""))
                .setFields(new ArrayList<>())
        ).build()).queue();
    }

    public Message executeMove(WeaponAction move) {
        Message message = move.execute(this.user, this);
        if (message != null) {
            return message;
        } else {
            return new MessageBuilder().append("Error while executing move ").append(move).build();
        }
    }

    public User getUser() {
        return user;
    }

    public MessageChannel getChannel() {
        return channel;
    }

    public Creature getOpponent() {
        return opponent;
    }

    public Tile getLocation() {
        return location;
    }

    public LinkedHashMap<Integer, WeaponAction> getUserMoves() {
        return userMoves;
    }
}
