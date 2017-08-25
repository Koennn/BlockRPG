package me.koenn.blockrpg.commands;

import me.koenn.blockrpg.BlockRPG;
import me.koenn.blockrpg.battle.Battle;
import me.koenn.blockrpg.battle.Creature;
import me.koenn.blockrpg.battle.CreatureType;
import me.koenn.blockrpg.items.ItemStack;
import me.koenn.blockrpg.items.ItemType;
import me.koenn.blockrpg.util.Direction;
import me.koenn.blockrpg.util.MapGenerator;
import me.koenn.blockrpg.util.RPGMessageEmbed;
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
 * Copyright (C) Koenn - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Koen Willemse, June 2017
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
        if (world.isExplored(moved)) {
            return new MessageBuilder().append("You already explored this tile! Use **\\travel** to get there.").build();
        }
        if (random.nextInt(4) == 1) {
            Battle battle = new Battle(executor, channel, new Creature(CreatureType.SCARY_MONSTER, 50, new ItemStack(ItemType.BASIC_SWORD)), world.explore(moved));
            blockRPG.getUserBattles().put(executor.getIdLong(), battle);
            battle.start();
            return null;
        } else {
            Tile tile = world.explore(moved);
            blockRPG.setUserLocation(executor, tile.getLocation());
            MapGenerator.cachedMaps.clearCache(executor);
            String image = new MapGenerator(BlockRPG.getInstance().getWorld(executor), executor).generate(executor);
            return new MessageBuilder().setEmbed(new RPGMessageEmbed(
                    "You discovered a new tile:",
                    tile.toString(), executor
            ).setImage(new MessageEmbed.ImageInfo(image, "", 500, 500))).build();
        }

    }

    @Override
    public void callback(User executor, MessageChannel channel) {

    }
}
