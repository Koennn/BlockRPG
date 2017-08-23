package me.koenn.blockrpg.items;

import me.koenn.blockrpg.battle.Battle;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.entities.impl.MessageEmbedImpl;

import java.awt.*;
import java.util.ArrayList;

/**
 * <p>
 * Copyright (C) Koenn - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Koen Willemse, June 2017
 */
public enum ItemType {

    TEST_ITEM("Test Item"),
    BASIC_SWORD("Basic Sword", new WeaponAction[]{
            new WeaponAction() {
                @Override
                public String getActionName() {
                    return "Swing Sword";
                }

                @Override
                public Message execute(User executor, Battle battle) {
                    if (executor == null) {
                        return null;
                    } else {
                        battle.getOpponent().setHealth(battle.getOpponent().getHealth() - 5);
                        return new MessageBuilder().setEmbed(new MessageEmbedImpl()
                                .setColor(Color.GREEN)
                                .setTitle("You used " + this.getActionName())
                                .setAuthor(new MessageEmbed.AuthorInfo(executor.getName(), executor.getEffectiveAvatarUrl(), executor.getEffectiveAvatarUrl(), ""))
                                .setDescription("**You dealt 5 damage!**\n**" + battle.getOpponent().getType().getName() + "'s health:** " + battle.getOpponent().getHealth())
                                .setFooter(new MessageEmbed.Footer("BlockRPG - BETA", "", ""))
                                .setFields(new ArrayList<>())
                        ).build();
                    }
                }
            },
            new WeaponAction() {
                @Override
                public String getActionName() {
                    return "Block Attack";
                }

                @Override
                public Message execute(User executor, Battle battle) {
                    return null;
                }
            }
    });

    private final String name;
    private WeaponAction[] actions;

    ItemType(String name) {
        this.name = name;
    }

    ItemType(String name, WeaponAction[] actions) {
        this.name = name;
        this.actions = actions;
    }

    public String getName() {
        return name;
    }

    public WeaponAction[] getActions() {
        return actions;
    }
}
