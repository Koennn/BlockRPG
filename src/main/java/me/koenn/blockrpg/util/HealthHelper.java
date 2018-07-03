package me.koenn.blockrpg.util;

import me.koenn.blockrpg.BlockRPG;
import me.koenn.blockrpg.data.Stats;
import net.dv8tion.jda.core.entities.User;

public final class HealthHelper {

    public static void heal(User user, int amount) {
        int newHealth = getHealth(user) + amount;
        setHealth(user, newHealth < 100 ? newHealth : 100);
    }

    public static void damage(User user, int amount) {
        int newHealth = getHealth(user) - amount;
        setHealth(user, newHealth > 0 ? newHealth : 0);
    }

    public static int getHealth(User user) {
        return Integer.parseInt(String.valueOf(BlockRPG.getInstance().getStats(user).get(Stats.Type.HEALTH)));
    }

    public static void setHealth(User user, int health) {
        BlockRPG.getInstance().getStats(user).set("health", health);
    }
}
