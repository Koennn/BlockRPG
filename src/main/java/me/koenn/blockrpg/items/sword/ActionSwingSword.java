package me.koenn.blockrpg.items.sword;

import me.koenn.blockrpg.battle.Battle;
import me.koenn.blockrpg.battle.Creature;
import me.koenn.blockrpg.battle.CreatureType;
import me.koenn.blockrpg.items.IWeaponAction;
import me.koenn.blockrpg.util.HealthHelper;
import me.koenn.blockrpg.util.RPGMessageEmbed;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;

/**
 * <p>
 * Copyright (C) Koenn - All Rights Reserved Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential Written by Koen Willemse, August 2017
 */
public class ActionSwingSword implements IWeaponAction {

    private static final int DAMAGE = 5;

    @Override
    public String getActionName() {
        return "Swing Sword";
    }

    @Override
    public Message execute(User executor, MessageChannel channel, Battle battle) {
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
        HealthHelper.damage(user, DAMAGE);
        battle.setUserHealth(HealthHelper.getHealth(user));
        return new MessageBuilder().setEmbed(new RPGMessageEmbed(
                String.format("%s used %s", creature.getName(), this.getActionName()),
                String.format("**%s dealt %s damage!**\n**Your health:** %s", creature.getName(), DAMAGE, battle.getUserHealth()),
                user)
        ).build();
    }
}
