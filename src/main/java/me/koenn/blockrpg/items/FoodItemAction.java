package me.koenn.blockrpg.items;

import me.koenn.blockrpg.util.HealthHelper;
import me.koenn.blockrpg.util.RPGMessageEmbed;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;

public class FoodItemAction implements ItemAction {

    private final String type;
    private final int restoreAmount;

    public FoodItemAction(String type, int restoreAmount) {
        this.type = type;
        this.restoreAmount = restoreAmount;
    }

    @Override
    public Message execute(User executor, MessageChannel channel) {
        HealthHelper.heal(executor, this.restoreAmount);
        return new MessageBuilder().setEmbed(new RPGMessageEmbed(
                String.format("You ate a %s %s", this.type, ItemType.valueOf(this.type.toUpperCase()).getEmote()),
                String.format("**Restored %s health**\n**Your health:** %s", this.restoreAmount, HealthHelper.getHealth(executor)),
                executor)
        ).build();
    }
}
