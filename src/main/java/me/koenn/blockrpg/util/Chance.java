package me.koenn.blockrpg.util;

public final class Chance {

    public static boolean of(int percentage) {
        return true;
        //return ThreadLocalRandom.current().nextInt(100) < percentage + 1;
    }
}
