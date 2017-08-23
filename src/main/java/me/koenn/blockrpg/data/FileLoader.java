package me.koenn.blockrpg.data;

import me.koenn.blockrpg.world.World;
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
        final JSONManager jsonManager = new JSONManager(file.getName());
        if (!jsonManager.getBody().containsKey("stats")) {
            return new ArrayList<>();
        }
        final JSONArray statsArray = (JSONArray) jsonManager.getFromBody("stats");
        final List<Stats> statsList = new ArrayList<>();
        for (Object statsObject : statsArray) {
            final JSONObject userStats = (JSONObject) statsObject;
            final long userId = (long) userStats.get("7userId");
            statsList.add(new Stats(userId, userStats));
        }
        return statsList;
    }

    public static void saveStats(File file, List<Stats> statsList) {
        /*final JSONManager jsonManager = new JSONManager(file.getName());
        final JSONArray statsArray = new JSONArray();
        for (final Stats stats : statsList) {
            statsArray.add(stats.toJSON());
        }
        jsonManager.setInBody("stats", statsArray);*/
    }

    public static List<World> loadWorlds(File file) {
        final JSONManager jsonManager = new JSONManager(file.getName());
        if (!jsonManager.getBody().containsKey("worlds")) {
            return new ArrayList<>();
        }
        final JSONArray worlds = (JSONArray) jsonManager.getFromBody("worlds");
        final List<World> worldList = new ArrayList<>();
        for (Object worldObject : worlds) {
            worldList.add(new World((JSONObject) worldObject));
        }
        return worldList;
    }

    public static void saveWorlds(File file, List<World> worldList) {
        /*final JSONManager jsonManager = new JSONManager(file.getName());
        final JSONArray worldsArray = new JSONArray();
        for (final World world : worldList) {
            worldsArray.add(world.toJson());
        }
        jsonManager.setInBody("worlds", worldsArray);*/
    }
}
