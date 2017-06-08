package me.koenn.blockrpg.data;

import me.koenn.blockrpg.items.Inventory;
import me.koenn.blockrpg.items.ItemStack;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Copyright (C) Koenn - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Koen Willemse, June 2017
 */
public final class FileLoader {

    public static List<Stats> loadStats(File file) {
        JSONManager jsonManager = new JSONManager(file.getName());
        JSONArray statsArray = (JSONArray) jsonManager.getFromBody("stats");
        List<Stats> statsList = new ArrayList<>();
        for (Object statsObject : statsArray) {
            JSONObject statsJson = (JSONObject) statsObject;
            final int health = Math.toIntExact((Long) statsJson.get("health"));
            final Inventory inventory = new Inventory((JSONObject) statsJson.get("inventory"));
            final ItemStack weapon = new ItemStack((JSONObject) statsJson.get("weapon"));
            final ItemStack[] armor = loadArmor((JSONObject) statsJson.get("armor"));
            final int level = Math.toIntExact((Long) statsJson.get("level"));
            final int kills = Math.toIntExact((Long) statsJson.get("kills"));
            final int deaths = Math.toIntExact((Long) statsJson.get("deaths"));
            final int takedowns = Math.toIntExact((Long) statsJson.get("takedowns"));
            final SkillPoints skillPoints = new SkillPoints((JSONObject) statsJson.get("skillPoints"));
            statsList.add(new Stats(health, inventory, weapon, armor, level, kills, deaths, takedowns, skillPoints));
        }
        return statsList;
    }

    public static void save(File file, List<Stats> statsList) {
        JSONManager jsonManager = new JSONManager(file.getName());
        JSONArray statsArray = new JSONArray();
        for (Stats stats : statsList) {
            JSONObject statsObject = new JSONObject();
            statsObject.put("health", stats.getHealth());
            statsObject.put("inventory", stats.getInventory().getJson());
            statsObject.put("weapon", stats.getWeapon().getJson());
            statsObject.put("armor", saveArmor(stats.getArmor()));
            statsObject.put("level", stats.getLevel());
            statsObject.put("kills", stats.getKills());
            statsObject.put("deaths", stats.getDeaths());
            statsObject.put("takedowns", stats.getTakedowns());
            statsObject.put("skillPoints", stats.getSkillPoints().getJson());
            statsArray.add(statsObject);
        }
        jsonManager.setInBody("stats", statsArray);
    }

    private static ItemStack[] loadArmor(JSONObject armor) {
        ItemStack[] armorArray = new ItemStack[4];
        JSONArray jsonArray = (JSONArray) armor.get("items");
        for (int i = 0; i < armorArray.length; i++) {
            armorArray[i] = new ItemStack((JSONObject) jsonArray.get(i));
        }
        return armorArray;
    }

    private static JSONObject saveArmor(ItemStack[] armor) {
        JSONObject armorObject = new JSONObject();
        JSONArray items = new JSONArray();
        for (ItemStack itemStack : armor) {
            items.add(itemStack.getJson());
        }
        armorObject.put("items", items);
        return armorObject;
    }
}
