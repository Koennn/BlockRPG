package me.koenn.blockrpg.items.weapon;

import me.koenn.blockrpg.battle.Battle;
import me.koenn.blockrpg.battle.creature.Creature;
import me.koenn.blockrpg.battle.creature.CreatureType;
import me.koenn.blockrpg.items.IWeaponAction;
import me.koenn.blockrpg.util.Chance;
import me.koenn.blockrpg.util.HealthHelper;
import me.koenn.blockrpg.util.RPGMessageEmbed;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;

public class ActionShoot implements IWeaponAction {

    private static final int DAMAGE = 12;
    private static final int MISS_CHANCE = 20;

    @Override
    public String getActionName() {
        return "Shoot";
    }

    @Override
    public Message execute(User executor, MessageChannel channel, Battle battle) {
        if (Chance.of(MISS_CHANCE)) {
            return new MessageBuilder().setEmbed(new RPGMessageEmbed(
                    String.format("You used %s", this.getActionName()),
                    "**You missed!**\n", executor)
            ).build();
        }

        Creature opponent = battle.getOpponent();
        opponent.setHealth(opponent.getHealth() - DAMAGE);
        return new MessageBuilder().setEmbed(new RPGMessageEmbed(
                String.format("You used %s", this.getActionName()),
                String.format("**You dealt %s damage!**\n**%s's health:** %s", DAMAGE, opponent.getType().getName(), opponent.getHealth()),
                executor)
        ).build();
    }

    @Override
    public Message executeCreature(CreatureType creature, User user, Battle battle) {
        if (Chance.of(MISS_CHANCE)) {
            return new MessageBuilder().setEmbed(new RPGMessageEmbed(
                    String.format("%s used %s", creature.getName(), this.getActionName()),
                    "**He missed!**\n", user)
            ).build();
        }

        HealthHelper.damage(user, DAMAGE);
        battle.setUserHealth(HealthHelper.getHealth(user));
        return new MessageBuilder().setEmbed(new RPGMessageEmbed(
                String.format("%s used %s", creature.getName(), this.getActionName()),
                String.format("**%s dealt %s damage!**\n**Your health:** %s", creature.getName(), DAMAGE, battle.getUserHealth()),
                user)
        ).build();
    }
}
