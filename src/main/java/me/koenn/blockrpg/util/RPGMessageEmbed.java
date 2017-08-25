package me.koenn.blockrpg.util;

import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.entities.impl.MessageEmbedImpl;

import java.awt.*;
import java.util.ArrayList;

/**
 * <p>
 * Copyright (C) Koenn - All Rights Reserved Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential Written by Koen Willemse, August 2017
 */
public class RPGMessageEmbed extends MessageEmbedImpl {

    public RPGMessageEmbed(String title, String description, User author) {
        this.setFields(new ArrayList<>())
                .setTitle(title)
                .setDescription(description)
                .setAuthor(new AuthorInfo(author.getName(), null, author.getEffectiveAvatarUrl(), ""))
                .setFooter(new MessageEmbed.Footer("BlockRPG - BETA", "", ""))
                .setColor(Color.GREEN);
    }
}
