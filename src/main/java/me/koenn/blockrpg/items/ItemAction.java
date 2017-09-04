package me.koenn.blockrpg.items;

import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;

public interface ItemAction {

    Message execute(User executor, MessageChannel channel);
}
