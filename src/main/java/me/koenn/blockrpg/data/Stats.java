package me.koenn.blockrpg.data;

import me.koenn.blockrpg.items.Inventory;
import me.koenn.blockrpg.items.ItemStack;
import me.koenn.blockrpg.items.ItemType;
import me.koenn.blockrpg.util.FancyString;
import me.koenn.blockrpg.util.Formatter;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <p>
 * Copyright (C) Koenn - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Koen Willemse, June 2017
 */
public class Stats {

    private final long userId;
    private LinkedHashMap<String, Object> stats = new LinkedHashMap<>();

    public Stats(long userId) {
        this.userId = userId;
        Inventory inventory = new Inventory();
        inventory.getItems().add(new ItemStack(ItemType.TEST_ITEM));
        this.stats.put("0health", 100);
        this.stats.put("1inventory", inventory);
        this.stats.put("2weapon", null);
        this.stats.put("3level", 1);
        this.stats.put("4kills", 0);
        this.stats.put("5deaths", 0);
        this.stats.put("6skillPoints", new SkillPoints());
        this.stats.put("7userId", userId);
    }

    public Stats(long userId, JSONObject statsObject) {
        this.userId = userId;
        HashMap<Integer, Map.Entry<String, Object>> unorderedStats = new HashMap<>();
        for (Object statName : statsObject.keySet()) {
            Map.Entry<String, Object> entry = new Map.Entry<String, Object>() {
                @Override
                public String getKey() {
                    return (String) statName;
                }

                @Override
                public Object getValue() {
                    return Formatter.readable((String) statName, statsObject.get(statName));
                }

                @Override
                public Object setValue(Object value) {
                    return null;
                }
            };
            unorderedStats.put(Integer.parseInt(((String) statName).substring(0, 1)), entry);
        }

        for (int i = 0; i < unorderedStats.size(); i++) {
            this.stats.put(unorderedStats.get(i).getKey(), unorderedStats.get(i).getValue());
        }
    }

    public String getFormattedStats() {
        StringBuilder stringBuilder = new StringBuilder();
        for (String statName : this.stats.keySet()) {
            if (statName.equals("7userId")) {
                continue;
            }
            stringBuilder.append("**").append(new FancyString(statName.substring(1))).append(":** ").append(Formatter.format(stats.get(statName))).append("\n");
        }
        return stringBuilder.toString().trim();
    }

    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();
        int index = 0;
        for (String statName : this.stats.keySet()) {
            jsonObject.put(index + statName, Formatter.savable(this.stats.get(statName)));
            index++;
        }
        return jsonObject;
    }

    public Object get(String name) {
        return this.stats.get(name);
    }

    public long getUserId() {
        return userId;
    }
}
