package me.koenn.blockrpg.items;

import me.koenn.blockrpg.battle.Battle;
import me.koenn.blockrpg.battle.CreatureType;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;

/**
 * <p>
 * Copyright (C) Koenn - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Koen Willemse, June 2017
 */
public interface IWeaponAction {

    String getActionName();

    Message execute(User executor, MessageChannel channel, Battle battle);

    Message executeCreature(CreatureType creature, User user, Battle battle);
}
