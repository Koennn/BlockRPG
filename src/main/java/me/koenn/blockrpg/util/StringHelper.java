package me.koenn.blockrpg.util;

public final class StringHelper {

    public static String concat(String[] words) {
        StringBuilder builder = new StringBuilder();
        for (String word : words) {
            builder.append(word).append("_");
        }
        String string = builder.toString();
        return string.substring(0, string.length() - 1);
    }
}
