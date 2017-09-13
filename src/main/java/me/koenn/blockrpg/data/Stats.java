package me.koenn.blockrpg.data;

import me.koenn.blockrpg.items.Inventory;
import me.koenn.blockrpg.util.FancyString;
import me.koenn.blockrpg.util.Formatter;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <p>
 * Copyright (C) Koenn - All Rights Reserved Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential Written by Koen Willemse, June 2017
 */
public class Stats {

    private final long userId;
    private final LinkedHashMap<String, Object> stats = new LinkedHashMap<>();

    public Stats(long userId) {
        this.userId = userId;
        this.stats.put("health", 100);
        this.stats.put("inventory", new Inventory());
        this.stats.put("weapon", null);
        this.stats.put("level", 1);
        this.stats.put("kills", 0);
        this.stats.put("deaths", 0);
        this.stats.put("skillPoints", new SkillPoints());
        this.stats.put("userId", userId);
    }

    public Stats(long userId, JSONObject statsObject) {
        this.userId = userId;
        HashMap<Integer, Map.Entry<String, Object>> unorderedStats = new HashMap<>();
        for (Object statName : statsObject.keySet()) {
            String name = ((String) statName).substring(1);
            Map.Entry<String, Object> entry = new Map.Entry<String, Object>() {
                @Override
                public String getKey() {
                    return name;
                }

                @Override
                public Object getValue() {
                    return Formatter.readable(name, statsObject.get(statName));
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
            if (statName.equals("userId")) {
                continue;
            }
            stringBuilder.append(String.format("**%s:** %s\n", new FancyString(statName), Formatter.format(stats.get(statName))));
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

    public void set(String name, Object obj) {
        this.stats.put(name, obj);
    }

    public long getUserId() {
        return userId;
    }
}
