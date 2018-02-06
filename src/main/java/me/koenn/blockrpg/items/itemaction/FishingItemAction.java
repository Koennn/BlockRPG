package me.koenn.blockrpg.items.itemaction;

import me.koenn.blockrpg.BlockRPG;
import me.koenn.blockrpg.items.IItemAction;
import me.koenn.blockrpg.items.Inventory;
import me.koenn.blockrpg.items.ItemStack;
import me.koenn.blockrpg.util.RPGMessageEmbed;
import me.koenn.blockrpg.util.WorldHelper;
import me.koenn.blockrpg.world.Tile;
import me.koenn.blockrpg.world.World;
import me.koenn.blockrpg.world.lake.Lake;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;

@SuppressWarnings("unused")
public class FishingItemAction implements IItemAction {

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
        if (tile.getProperty("lake") == null) {
            return new MessageBuilder().append("You not in a lake right now!").build();
        }
        Lake lake = (Lake) tile.getProperty("lake");

        int amount = lake.getFishes();
        int caught;

        if (amount > 0) {
            caught = 1;
        } else {
            return new MessageBuilder().append("This lake is empty!").build();
        }

        ItemStack caughtFish = lake.fish();
        ((Inventory) BlockRPG.getInstance().getStats(executor).get("inventory")).addItemStack(caughtFish);

        return new MessageBuilder().setEmbed(new RPGMessageEmbed(
                String.format("You caught %sx %s", caught, caughtFish.getType().getName()),
                String.format("**Left in lake:**\n%s", lake.getDisplay()),
                executor
        )).build();
    }

    @Override
    public boolean shouldRemoveItem() {
        return false;
    }
}
