package me.koenn.blockrpg.util;

import me.koenn.blockrpg.BlockRPG;
import me.koenn.blockrpg.world.World;
import net.dv8tion.jda.core.entities.User;

public final class Checker {

    public static boolean checkHasWorld(User user) {
        return WorldHelper.hasWorld(user);
    }

    public static boolean checkInBattle(User user) {
        return BlockRPG.getInstance().getUserBattles().get(user.getIdLong()) != null;
    }

    public static boolean checkOnTile(String tileType, User user, World world) {
        return world.getTile(WorldHelper.getUserLocation(user)).getProperty(tileType) != null;
    }
}
