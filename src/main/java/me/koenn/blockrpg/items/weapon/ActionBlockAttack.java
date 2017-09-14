package me.koenn.blockrpg.items.weapon;

import me.koenn.blockrpg.battle.Battle;
import me.koenn.blockrpg.battle.creature.CreatureType;
import me.koenn.blockrpg.items.IWeaponAction;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;

/**
 * <p>
 * Copyright (C) Koenn - All Rights Reserved Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential Written by Koen Willemse, August 2017
 */
public class ActionBlockAttack implements IWeaponAction {

    @Override
    public String getActionName() {
        return "Block Attack";
    }

    @Override
    public Message execute(User executor, MessageChannel channel, Battle battle) {
        return null;
    }

    @Override
    public Message executeCreature(CreatureType creature, User user, Battle battle) {
        return null;
    }
}
