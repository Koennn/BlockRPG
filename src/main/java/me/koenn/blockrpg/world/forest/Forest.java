package me.koenn.blockrpg.world.forest;

import me.koenn.blockrpg.commands.CommandManager;
import me.koenn.blockrpg.items.ItemStack;
import me.koenn.blockrpg.items.ItemType;
import me.koenn.blockrpg.world.TileType;
import org.json.simple.JSONObject;

public class Forest implements TileType {

    private int size;

    public Forest() {
    }

    public Forest(int size) {
        this.size = size;
    }

    @Override
    public String getName() {
        return "Forest";
    }

    @Override
    public String getDisplay() {
        return "**------------------------------------**\n" +
                String.format(" %s trees\n", this.size) +
                "**------------------------------------**\n" +
                String.format("`Use %suse axe to mine a resource.`", CommandManager.CMD_CHAR);

    }

    @Override
    public JSONObject getJSON() {
        JSONObject json = new JSONObject();
        json.put("size", this.size);
        return json;
    }

    @Override
    public void fromJSON(JSONObject json) {
        this.size = Integer.parseInt(String.valueOf(json.get("size")));
    }

    public int getSize() {
        return size;
    }

    public ItemStack chop(int amount) {
        this.size -= amount;
        return new ItemStack(ItemType.getItem("wood"), amount);
    }
}
