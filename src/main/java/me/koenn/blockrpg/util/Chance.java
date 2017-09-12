package me.koenn.blockrpg.util;

import java.util.concurrent.ThreadLocalRandom;

public final class Chance {

    public static boolean of(int percentage) {
        return ThreadLocalRandom.current().nextInt(percentage) < percentage + 1;
    }
}
