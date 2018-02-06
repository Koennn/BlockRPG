package me.koenn.blockrpg.items.itemaction;

import me.koenn.blockrpg.items.IItemAction;
import me.koenn.blockrpg.items.ItemType;
import me.koenn.blockrpg.util.HealthHelper;
import me.koenn.blockrpg.util.RPGMessageEmbed;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;

@SuppressWarnings("unused")
public class FoodItemAction implements IItemAction {

    private final String type;
    private final int restoreAmount;

    public FoodItemAction(String type, Integer restoreAmount) {
        this.type = type;
        this.restoreAmount = restoreAmount;
    }

    @Override
    public Message execute(User executor, MessageChannel channel) {
        HealthHelper.heal(executor, this.restoreAmount);
        return new MessageBuilder().setEmbed(new RPGMessageEmbed(
                String.format("You ate a %s %s", this.type, ItemType.getItem(this.type.toUpperCase()).getEmote()),
                String.format("**Restored %s health**\n**Your health:** %s", this.restoreAmount, HealthHelper.getHealth(executor)),
                executor)
        ).build();
    }

    @Override
    public boolean shouldRemoveItem() {
        return true;
    }
}
