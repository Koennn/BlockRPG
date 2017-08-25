package me.koenn.blockrpg.commands;

import me.koenn.blockrpg.BlockRPG;
import me.koenn.blockrpg.battle.Battle;
import me.koenn.blockrpg.items.WeaponAction;
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
public class MoveCommand implements ICommand {

    @Override
    public String getCommand() {
        return "move";
    }

    @Override
    public String getDescription() {
        return "Use one of your offensive or defensive moves in a battle.";
    }

    @Override
    public int getRequiredArgs() {
        return 1;
    }

    @Override
    public boolean isAlias() {
        return false;
    }

    @Override
    public Message execute(User executor, MessageChannel channel, String[] args) {
        int moveId;
        try {
            moveId = Integer.parseInt(args[0]);
        } catch (NumberFormatException ex) {
            return new MessageBuilder().append("Use **\\move <move-id>** to use a move.").build();
        }

        Battle battle = BlockRPG.getInstance().getUserBattles().get(executor.getIdLong());
        if (battle == null) {
            return new MessageBuilder().append("You are not in a battle right now.").build();
        }

        WeaponAction move = battle.getUserMoves().get(moveId);
        return battle.executeMove(move, channel);
    }

    @Override
    public void callback(User executor, MessageChannel channel) {
        Battle battle = BlockRPG.getInstance().getUserBattles().get(executor.getIdLong());
        if (battle == null) {
            return;
        }

        battle.endTurn(channel);
    }
}
