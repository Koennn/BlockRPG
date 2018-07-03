package me.koenn.blockrpg.items.itemaction;

import me.koenn.blockrpg.BlockRPG;
import me.koenn.blockrpg.data.Stats;
import me.koenn.blockrpg.items.IItemAction;
import me.koenn.blockrpg.items.Inventory;
import me.koenn.blockrpg.items.ItemStack;
import me.koenn.blockrpg.items.ItemType;
import me.koenn.blockrpg.util.RPGMessageEmbed;
import me.koenn.blockrpg.util.WorldHelper;
import me.koenn.blockrpg.world.Tile;
import me.koenn.blockrpg.world.World;
import me.koenn.blockrpg.world.mine.Mine;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;

@SuppressWarnings("unused")
public class PickaxeItemAction implements IItemAction {

    private final int size;

    public PickaxeItemAction(Integer size) {
        this.size = size;
    }

    @Override
    public Message execute(User executor, MessageChannel channel) {
        if (!WorldHelper.hasWorld(executor)) {
            return new MessageBuilder().append("Use **\\stats** first to start playing!").build();
        }
        World world = WorldHelper.getWorld(executor);
        if (world == null) {
            throw new NullPointerException("Unable to find users stats");
        }

        if (BlockRPG.getInstance().getUserBattles().get(executor.getIdLong()) != null) {
            return new MessageBuilder().append("You are in a battle right now!").build();
        }
        Tile tile = world.getTile(WorldHelper.getUserLocation(executor));
        if (tile.getProperty("mine") == null) {
            return new MessageBuilder().append("You not in a mine right now!").build();
        }
        Mine mine = (Mine) tile.getProperty("mine");

        ItemType resource = mine.getResource();
        int amount = mine.getAmount();
        int mined;

        if (amount <= 0) {
            return new MessageBuilder().append("This mine is empty!").build();
        } else if (amount < this.size) {
            mined = amount;
        } else {
            mined = this.size;
        }

        ItemStack minedItems = mine.mine(mined);
        ((Inventory) BlockRPG.getInstance().getStats(executor).get(Stats.Type.INVENTORY)).addItemStack(minedItems);

        return new MessageBuilder().setEmbed(new RPGMessageEmbed(
                String.format("You mined %sx %s", mined, minedItems.getType().getName()),
                String.format("**Left in mine:**\n%s", mine.getDisplay()),
                executor
        )).build();
    }

    @Override
    public boolean shouldRemoveItem() {
        return false;
    }
}
