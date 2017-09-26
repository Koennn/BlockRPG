package me.koenn.blockrpg.util;

import me.koenn.blockrpg.BlockRPG;
import me.koenn.blockrpg.data.FileLoader;
import me.koenn.blockrpg.items.ItemStack;
import me.koenn.blockrpg.items.ItemType;
import me.koenn.blockrpg.world.Tile;
import me.koenn.blockrpg.world.Vector2;
import me.koenn.blockrpg.world.World;
import me.koenn.blockrpg.world.village.Trade;
import me.koenn.blockrpg.world.village.Village;
import net.dv8tion.jda.core.entities.User;

import java.io.File;
import java.util.List;
import java.util.Random;

/**
 * <p>
 * Copyright (C) Koenn - All Rights Reserved Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential Written by Koen Willemse, August 2017
 */
public final class WorldHelper {

    public static List<World> getWorlds() {
        return BlockRPG.getInstance().getWorlds();
    }

    public static World getWorld(User user) {
        long userId = user.getIdLong();
        for (World world : getWorlds()) {
            if (world.getUserId() == userId) {
                return world;
            }
        }
        return null;
    }

    public static boolean hasWorld(User user) {
        return getWorld(user) != null;
    }

    public static void addWorld(World world) {
        getWorlds().add(world);
        FileLoader.saveWorlds(new File("worlds.json"), getWorlds());
    }

    public static Vector2 getUserLocation(User user) {
        return getWorld(user).getLocation();
    }

    public static void setUserLocation(User user, Vector2 location) {
        getWorld(user).setLocation(location);
    }

    public static void generateProperties(Tile tile, Random random) {
        if (random.nextInt(10) == 1) {
            tile.setProperty("village", generateVillage(random));
        }
    }

    private static Village generateVillage(Random random) {
        int inhabitants = random.nextInt(10);
        Trade[] trades = new Trade[]{
                new Trade(new ItemStack(ItemType.getItem("carrot"), 2), new ItemStack(ItemType.getItem("cookie")))
        };
        return new Village(inhabitants, trades);
    }
}
