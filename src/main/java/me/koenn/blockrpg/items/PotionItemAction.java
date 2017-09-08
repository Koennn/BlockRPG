package me.koenn.blockrpg.items;

import me.koenn.blockrpg.util.RPGMessageEmbed;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;

public class PotionItemAction implements IItemAction {

    private final String name;
    private final PotionType type;

    public PotionItemAction(String name, PotionType type) {
        this.name = name;
        this.type = type;
    }

    @Override
    public Message execute(User executor, MessageChannel channel) {
        this.type.getEffect().execute(executor, channel);
        return new MessageBuilder().setEmbed(new RPGMessageEmbed(
                String.format("You used a %s", this.name),
                this.type.getDescription(),
                executor
        )).build();
    }

    public enum PotionType {
        ;

        private final String name;
        private final IItemAction effect;
        private final String description;

        PotionType(String name, IItemAction effect, String description) {
            this.name = name;
            this.effect = effect;
            this.description = description;
        }

        public String getName() {
            return name;
        }

        public IItemAction getEffect() {
            return effect;
        }

        public String getDescription() {
            return description;
        }
    }
}
