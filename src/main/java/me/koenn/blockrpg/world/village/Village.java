package me.koenn.blockrpg.world.village;

import me.koenn.blockrpg.commands.CommandManager;
import me.koenn.blockrpg.world.TileType;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.Collections;
import java.util.LinkedList;

/**
 * <p>
 * Copyright (C) Koenn - All Rights Reserved Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential Written by Koen Willemse, September 2017
 */
public class Village implements TileType {

    private int inhabitants;
    private LinkedList<Trade> trades;

    @SuppressWarnings("unused")
    public Village() {
    }

    public Village(int inhabitants, Trade[] trades) {
        this.inhabitants = inhabitants;
        this.trades = new LinkedList<>();
        Collections.addAll(this.trades, trades);
    }

    @Override
    public String getName() {
        return "village";
    }

    @Override
    public String getDisplay() {
        StringBuilder builder = new StringBuilder();
        builder.append("**------------------------------------**\n");
        builder.append(String.format("**Inhabitants:** %s\n**Trades:**\n", this.inhabitants));
        for (Trade trade : this.trades) {
            builder.append(String.format(" **%s)** %s\n", this.trades.indexOf(trade) + 1, trade.getDisplay()));
        }
        builder.append("**------------------------------------**\n");
        builder.append(String.format("`Use %strade <id> to perform a trade.`", CommandManager.CMD_CHAR));
        return builder.toString();
    }

    @Override
    public JSONObject getJSON() {
        JSONObject json = new JSONObject();
        json.put("class", this.getClass().getName());
        json.put("inhabitants", this.inhabitants);
        JSONArray trades = new JSONArray();
        this.trades.forEach(trade -> trades.add(trade.toJSON()));
        json.put("trades", trades);
        return json;
    }

    @Override
    public void fromJSON(JSONObject json) {
        this.inhabitants = Integer.parseInt(String.valueOf(json.get("inhabitants")));
        this.trades = new LinkedList<>();
        ((JSONArray) json.get("trades")).forEach(trade -> this.trades.add(new Trade((JSONObject) trade)));
    }

    public LinkedList<Trade> getTrades() {
        return trades;
    }
}
