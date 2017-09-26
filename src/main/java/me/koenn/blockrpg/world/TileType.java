package me.koenn.blockrpg.world;

import org.json.simple.JSONObject;

/**
 * <p>
 * Copyright (C) Koenn - All Rights Reserved Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential Written by Koen Willemse, September 2017
 */
public interface TileType {

    String getName();

    String getDisplay();

    JSONObject getJSON();

    void fromJSON(JSONObject json);
}
