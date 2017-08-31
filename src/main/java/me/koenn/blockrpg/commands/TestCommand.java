package me.koenn.blockrpg.commands;

import me.koenn.blockrpg.BlockRPG;
import me.koenn.blockrpg.battle.Battle;
import me.koenn.blockrpg.image.BattleGenerator;
import me.koenn.blockrpg.image.ImageGenerator;
import me.koenn.blockrpg.image.Texture;
import me.koenn.blockrpg.util.SkinRenderer;
import net.dv8tion.jda.core.MessageBuilder;
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
public class TestCommand implements ICommand {

    @Override
    public String getCommand() {
        return "test";
    }

    @Override
    public String getDescription() {
        return "Test!!!!";
    }

    @Override
    public int getRequiredArgs() {
        return 0;
    }

    @Override
    public boolean isAlias() {
        return false;
    }

    @Override
    public Message execute(User executor, MessageChannel channel, String[] args) {
        Battle battle = BlockRPG.getInstance().getUserBattles().get(executor.getIdLong());
        BattleGenerator generator = new BattleGenerator(battle);
        return new MessageBuilder().append(generator.generate()).build();
    }

    @Override
    public void callback(User executor, MessageChannel channel) {

    }
}
