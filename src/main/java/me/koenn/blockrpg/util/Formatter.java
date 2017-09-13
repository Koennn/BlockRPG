package me.koenn.blockrpg.util;

import me.koenn.blockrpg.data.SkillPoints;
import me.koenn.blockrpg.items.Inventory;
import me.koenn.blockrpg.items.ItemStack;
import org.json.simple.JSONObject;

/**
 * <p>
 * Copyright (C) Koenn - All Rights Reserved Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential Written by Koen Willemse, June 2017
 */
public final class Formatter {

    private static final String NULL = "None";
    private static final String INV = "Use **\\inv** to see your inventory.";

    public static String format(Object object) {
        if (object == null || object == "null") {
            return NULL;
        }
        if (object instanceof Inventory) {
            return INV;
        } else if (object instanceof ItemStack) {
            return ((ItemStack) object).getType().getName();
        } else if (object instanceof SkillPoints) {
            return formatJSON(((SkillPoints) object).getJson());
        } else {
            return String.valueOf(object).replace("null", NULL);
        }
    }

    public static String formatJSON(JSONObject json) {
        StringBuilder builder = new StringBuilder();
        for (Object key : json.keySet()) {
            builder.append(String.format("\n    %s: %s", new FancyString(key), json.get(key)));
        }
        return builder.toString();
    }

    public static Object savable(Object object) {
        if (object == null) {
            return "null";
        }
        if (object instanceof Inventory) {
            return ((Inventory) object).getJson();
        } else if (object instanceof ItemStack) {
            return ((ItemStack) object).getJson();
        } else if (object instanceof SkillPoints) {
            return ((SkillPoints) object).getJson();
        } else {
            return object;
        }
    }

    public static Object readable(String objectName, Object object) {
        if (object == null) {
            return null;
        }
        if (objectName.endsWith("inventory")) {
            return new Inventory((JSONObject) object);
        } else if (objectName.endsWith("skillPoints")) {
            return new SkillPoints((JSONObject) object);
        } else if (object instanceof JSONObject) {
            return new ItemStack((JSONObject) object);
        } else {
            return object;
        }
    }
}
