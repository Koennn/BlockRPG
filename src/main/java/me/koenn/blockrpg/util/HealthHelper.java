package me.koenn.blockrpg.util;

import me.koenn.blockrpg.BlockRPG;
import me.koenn.blockrpg.data.Stats;
import net.dv8tion.jda.core.entities.User;

public final class HealthHelper {

    public static void heal(User user, int amount) {
        int newHealth = getHealth(user) + amount;
        setHealth(user, newHealth <= 100 ? newHealth : 100);
    }

    public static void damage(User user, int amount) {
        setHealth(user, getHealth(user) - amount);
    }

    public static int getHealth(User user) {
        return (int) BlockRPG.getInstance().getStats(user).get("health");
    }

    public static void setHealth(User user, int health) {
        Stats stats = BlockRPG.getInstance().getStats(user);
        stats.set("health", health);
    }
}
