package me.koenn.blockrpg.items.itemaction;

import me.koenn.blockrpg.BlockRPG;
import me.koenn.blockrpg.data.Stats;
import me.koenn.blockrpg.items.IItemAction;
import me.koenn.blockrpg.items.Inventory;
import me.koenn.blockrpg.items.ItemStack;
import me.koenn.blockrpg.util.RPGMessageEmbed;
import me.koenn.blockrpg.util.WorldHelper;
import me.koenn.blockrpg.world.Tile;
import me.koenn.blockrpg.world.World;
import me.koenn.blockrpg.world.forest.Forest;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;

public class AxeItemAction implements IItemAction {

    private final int size;

    public AxeItemAction(Integer size) {
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
        if (tile.getProperty("forest") == null) {
            return new MessageBuilder().append("You not in a forest right now!").build();
        }
        Forest forest = (Forest) tile.getProperty("forest");

        int size = forest.getSize();
        int chopped;

        if (size <= 0) {
            return new MessageBuilder().append("This forest is empty!").build();
        } else if (size < this.size) {
            chopped = size;
        } else {
            chopped = this.size;
        }

        ItemStack wood = forest.chop(chopped);
        ((Inventory) BlockRPG.getInstance().getStats(executor).get(Stats.Type.INVENTORY)).addItemStack(wood);

        return new MessageBuilder().setEmbed(new RPGMessageEmbed(
                String.format("You chopped %sx %s", chopped, wood.getType().getName()),
                String.format("**Left in forest:**\n%s", forest.getDisplay()),
                executor
        )).build();
    }

    @Override
    public boolean shouldRemoveItem() {
        return false;
    }
}
