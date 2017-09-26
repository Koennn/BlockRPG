package me.koenn.blockrpg.world;

import org.json.simple.JSONObject;

/**
 * <p>
 * Copyright (C) Koenn - All Rights Reserved Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential Written by Koen Willemse, September 2017
 */
public class Village implements TileType {

    private int inhabitants;
    private Object trades = null;

    public Village() {
    }

    public Village(int inhabitants) {
        this.inhabitants = inhabitants;
    }

    @Override
    public String getName() {
        return "village";
    }

    @Override
    public String getDisplay() {
        return String.format("Inhabitants: %s\nTrades: %s", this.inhabitants, this.trades);
    }

    @Override
    public JSONObject getJSON() {
        JSONObject json = new JSONObject();
        json.put("class", this.getClass().getName());
        json.put("inhabitants", this.inhabitants);
        json.put("trades", this.trades);
        return json;
    }

    @Override
    public void fromJSON(JSONObject json) {
        this.inhabitants = Integer.parseInt(String.valueOf(json.get("inhabitants")));
    }
}
