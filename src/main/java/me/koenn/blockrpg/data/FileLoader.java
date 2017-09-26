package me.koenn.blockrpg.data;

import me.koenn.blockrpg.world.World;
import net.dv8tion.jda.core.utils.SimpleLog;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Copyright (C) Koenn - All Rights Reserved Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential Written by Koen Willemse, June 2017
 */
public final class FileLoader {

    private static final SimpleLog LOGGER = SimpleLog.getLog("FileLoader");

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
        LOGGER.info(String.format("Loaded file \'%s\'", file.getName()));
        return statsList;
    }

    public static void saveStats(File file, List<Stats> statsList) {
        final JSONManager jsonManager = new JSONManager(file.getName());
        final JSONArray statsArray = new JSONArray();
        statsList.forEach(stats -> statsArray.add(stats.toJSON()));
        jsonManager.setInBody("stats", statsArray);
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
        LOGGER.info(String.format("Loaded file \'%s\'", file.getName()));
        return worldList;
    }

    public static void saveWorlds(File file, List<World> worldList) {
        final JSONManager jsonManager = new JSONManager(file.getName());
        final JSONArray worldsArray = new JSONArray();
        worldList.forEach(world -> worldsArray.add(world.toJSON()));
        jsonManager.setInBody("worlds", worldsArray);
    }
}
