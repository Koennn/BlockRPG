package me.koenn.blockrpg.battle;

import me.koenn.blockrpg.BlockRPG;
import me.koenn.blockrpg.battle.creature.Creature;
import me.koenn.blockrpg.data.Stats;
import me.koenn.blockrpg.image.BattleGenerator;
import me.koenn.blockrpg.image.MapGenerator;
import me.koenn.blockrpg.items.IWeaponAction;
import me.koenn.blockrpg.items.Inventory;
import me.koenn.blockrpg.items.ItemStack;
import me.koenn.blockrpg.util.HealthHelper;
import me.koenn.blockrpg.util.RPGMessageEmbed;
import me.koenn.blockrpg.util.ThreadHelper;
import me.koenn.blockrpg.util.WorldHelper;
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
        this.userHealth = HealthHelper.getHealth(user);
    }

    private static void sendExplored(MessageChannel channel, Battle battle) {
        channel.sendTyping().queue(a -> {
            String image = new MapGenerator(WorldHelper.getWorld(battle.user), battle.user).generate(battle.user, true);
            channel.sendMessage(new MessageBuilder().setEmbed(new RPGMessageEmbed(
                    "You discovered a new tile:",
                    battle.location.toString(), battle.user
            ).setImage(new MessageEmbed.ImageInfo(image, "", 500, 500))).build()).queue();
        });
    }

    public Message start() {
        int index = 1;
        for (ItemStack item : ((Inventory) BlockRPG.getInstance().getStats(user).get(Stats.Type.INVENTORY)).getItems()) {
            if (item.getType().getActions() != null) {
                for (IWeaponAction action : item.getType().getActions()) {
                    userMoves.put(index, action);
                    index++;
                }
            }
        }

        String image = new BattleGenerator(this).generate(true);
        return new MessageBuilder().setEmbed(new MessageEmbedImpl()
                .setColor(Color.GREEN)
                .setTitle("You encountered a **" + this.opponent.getType().getName() + "**")
                .setAuthor(new MessageEmbed.AuthorInfo(this.user.getName(), this.user.getEffectiveAvatarUrl(), this.user.getEffectiveAvatarUrl(), ""))
                .setImage(new MessageEmbed.ImageInfo(image, "", 800, 400))
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
            return new MessageBuilder().append(String.format("Error while executing move \'%s\': %s", move.getActionName(), message)).build();
        }
    }

    public void endTurn(MessageChannel channel) {
        this.usedMove = false;

        if (this.checkBattleOver()) {
            BlockRPG blockRPG = BlockRPG.getInstance();
            blockRPG.getUserBattles().remove(this.user.getIdLong());
            WorldHelper.setUserLocation(this.user, this.location.getLocation());
            new Thread(() -> {
                ThreadHelper.sleep(200);
                sendExplored(channel, this);
            }).start();
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
            World world = WorldHelper.getWorld(user);
            if (world == null) {
                throw new NullPointerException("Unable to find users world");
            }

            ItemStack loot = this.opponent.getType().getLootTable().getLoot();
            Stats stats = BlockRPG.getInstance().getStats(this.user);
            ((Inventory) stats.get(Stats.Type.INVENTORY)).addItemStack(loot);
            WorldHelper.setUserLocation(this.user, this.location.getLocation());
            channel.sendMessage(new MessageBuilder().setEmbed(new RPGMessageEmbed(
                    String.format("You killed %s", this.opponent.getType().getName()),
                    String.format("**Your health:** %s\n**Loot received:** %s", this.userHealth, loot.toString()),
                    this.user
            )).build()).queue();
            stats.add(Stats.Type.KILLS, 1);
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
