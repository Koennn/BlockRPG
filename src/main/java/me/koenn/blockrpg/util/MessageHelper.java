package me.koenn.blockrpg.util;

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
public final class MessageHelper {

    public static Message battleMoveMessage(User executor, String usedAction, String opponentName, Battle battle) {
        return new MessageBuilder().setEmbed(new MessageEmbedImpl()
                .setColor(Color.GREEN)
                .setTitle("You used " + usedAction)
                .setAuthor(new MessageEmbed.AuthorInfo(executor.getName(), executor.getEffectiveAvatarUrl(), executor.getEffectiveAvatarUrl(), ""))
                .setDescription("**You dealt 5 damage!**\n**" + opponentName + "'s health:** " + battle.getOpponent().getHealth())
                .setFooter(new MessageEmbed.Footer("BlockRPG - BETA", "", ""))
                .setFields(new ArrayList<>())
        ).build();
    }

    private static String battleYouDescription(int damage, String opponentName, Battle battle) {
        return "**You dealt " + damage + " damage!**\n**" + opponentName + "'s health:** " + battle.getOpponent().getHealth();
    }

    private static String battleOpponentDescription(int damage, String opponentName, Battle battle) {
        return "**You dealt " + damage + " damage!**\n**" + opponentName + "'s health:** " + battle.getOpponent().getHealth();
    }
}
