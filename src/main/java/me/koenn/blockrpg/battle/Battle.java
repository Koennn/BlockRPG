package me.koenn.blockrpg.battle;

import me.koenn.blockrpg.BlockRPG;
import me.koenn.blockrpg.image.MapGenerator;
import me.koenn.blockrpg.items.IWeaponAction;
import me.koenn.blockrpg.items.Inventory;
import me.koenn.blockrpg.items.ItemStack;
import me.koenn.blockrpg.util.RPGMessageEmbed;
import me.koenn.blockrpg.world.Tile;
import me.koenn.blockrpg.world.World;
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
 * Copyright (C) Koenn - All Rights Reserved Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential Written by Koen Willemse, June 2017
 */
public class Battle {

    private final User user;
    private final MessageChannel channel;
    private final Creature opponent;
    private final Tile location;
    private final LinkedHashMap<Integer, IWeaponAction> userMoves = new LinkedHashMap<>();
    private int userHealth;
    private boolean usedMove;

    public Battle(User user, MessageChannel channel, Creature opponent, Tile location) {
        this.user = user;
        this.channel = channel;
        this.opponent = opponent;
        this.location = location;
        this.userHealth = (int) BlockRPG.getInstance().getStats(user).get("health");
    }

    public Message start() {
        StringBuilder yourMoves = new StringBuilder();
        int index = 1;
        for (ItemStack item : ((Inventory) BlockRPG.getInstance().getStats(user).get("inventory")).getItems()) {
            if (item.getType().getActions() != null) {
                for (IWeaponAction action : item.getType().getActions()) {
                    userMoves.put(index, action);
                    yourMoves.append("`  \\move ").append(index).append("`: ").append(action.getActionName()).append("\n");
                    index++;
                }
            }
        }
        return new MessageBuilder().setEmbed(new MessageEmbedImpl()
                .setColor(Color.GREEN)
                .setTitle("You encountered a **" + this.opponent.getType().getName() + "**")
                .setAuthor(new MessageEmbed.AuthorInfo(this.user.getName(), this.user.getEffectiveAvatarUrl(), this.user.getEffectiveAvatarUrl(), ""))
                .setDescription("" +
                        "**Your health:** " + BlockRPG.getInstance().getStats(user).get("health") + "/100\n" +
                        "**" + this.opponent.getType().getName() + "'s health:** " + this.opponent.getHealth() + "/" + this.opponent.getMaxHealth() + "\n\n" +
                        "**Your possible moves:**\n" + yourMoves.toString().trim()
                )
                .setFooter(new MessageEmbed.Footer("BlockRPG - BETA", "", ""))
                .setFields(new ArrayList<>())
        ).build();
    }

    public Message executeMove(IWeaponAction move, MessageChannel channel) {
        Message message = move.execute(this.user, channel, this);
        if (message != null) {
            this.usedMove = true;
            return message;
        } else {
            return new MessageBuilder().append("Error while executing move ").append(move).build();
        }
    }

    public void endTurn(MessageChannel channel) {
        this.usedMove = false;

        if (this.checkBattleOver()) {
            BlockRPG blockRPG = BlockRPG.getInstance();
            blockRPG.getUserBattles().remove(this.user.getIdLong());
            blockRPG.setUserLocation(this.user, this.location.getLocation());
            channel.sendTyping().queue(a -> {
                String image = new MapGenerator(blockRPG.getWorld(this.user), this.user).generate(this.user, true);
                channel.sendMessage(new MessageBuilder().setEmbed(new RPGMessageEmbed(
                        "You discovered a new tile:",
                        this.location.toString(), this.user
                ).setImage(new MessageEmbed.ImageInfo(image, "", 500, 500))).build()).queue();
            });
            return;
        }

        channel.sendTyping().queue(a -> channel.sendMessage(this.opponent.getType().doMove(this)).queue());
    }

    private boolean checkBattleOver() {
        if (this.userHealth <= 0) {
            channel.sendMessage("Error while processing!").queue();
            return true;
        }

        if (this.opponent.getHealth() <= 0) {
            World world = BlockRPG.getInstance().getWorld(user);
            if (world == null) {
                throw new NullPointerException("Unable to find users world");
            }

            BlockRPG.getInstance().setUserLocation(this.user, this.location.getLocation());
            channel.sendMessage(new MessageBuilder().setEmbed(new RPGMessageEmbed(
                    String.format("You killed %s", this.opponent.getType().getName()),
                    String.format("**Your health:** %s", this.userHealth),
                    this.user)
            ).build()).queue();
            return true;
        }
        return false;
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

    public LinkedHashMap<Integer, IWeaponAction> getUserMoves() {
        return userMoves;
    }

    public int getUserHealth() {
        return userHealth;
    }

    public void setUserHealth(int userHealth) {
        this.userHealth = userHealth;
    }

    public boolean hasUsedMove() {
        return this.usedMove;
    }
}
