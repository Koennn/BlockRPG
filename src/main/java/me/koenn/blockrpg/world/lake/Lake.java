package me.koenn.blockrpg.world.lake;

import me.koenn.blockrpg.commands.CommandManager;
import me.koenn.blockrpg.items.ItemStack;
import me.koenn.blockrpg.items.ItemType;
import me.koenn.blockrpg.world.TileType;
import org.json.simple.JSONObject;

public class Lake implements TileType {

    private int fishes;

    public Lake() {
    }

    public Lake(int fishes) {
        this.fishes = fishes;
    }

    @Override
    public String getName() {
        return "lake";
    }

    @Override
    public String getDisplay() {
        return "**------------------------------------**\n" +
                String.format(" %sx :fish:\n", this.fishes) +
                "**------------------------------------**\n" +
                String.format("`Use %suse fishing rod to catch some fish.`", CommandManager.CMD_CHAR);
    }

    @Override
    public JSONObject getJSON() {
        JSONObject json = new JSONObject();
        json.put("class", this.getClass().getName());
        json.put("fishes", this.fishes);
        return json;
    }

    @Override
    public void fromJSON(JSONObject json) {
        this.fishes = Integer.parseInt(String.valueOf(json.get("fishes")));
    }

    public int getFishes() {
        return fishes;
    }

    public ItemStack fish() {
        this.fishes--;
        return new ItemStack(ItemType.getItem("fish"));
    }
}
