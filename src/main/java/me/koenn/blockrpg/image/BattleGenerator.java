package me.koenn.blockrpg.image;

import me.koenn.blockrpg.battle.Battle;
import me.koenn.blockrpg.commands.CommandManager;
import me.koenn.blockrpg.items.IWeaponAction;
import me.koenn.blockrpg.util.Cache;
import net.dv8tion.jda.core.entities.User;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedHashMap;

/**
 * <p>
 * Copyright (C) Koenn - All Rights Reserved Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential Written by Koen Willemse, August 2017
 */
public class BattleGenerator {

    public static final Cache<User, String> cachedBattles = new Cache<>();

    private static final Font FONT = new Font("Arial", Font.BOLD, 20);
    private static final Color COLOR = Color.getColor("4286f4");

    private final Battle battle;

    public BattleGenerator(Battle battle) {
        this.battle = battle;
    }

    public static BufferedImage readImage(String link) {
        try {
            final HttpURLConnection connection = (HttpURLConnection) new URL(link).openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_5) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.65 Safari/537.31");
            return resize(ImageIO.read(connection.getInputStream()), 128, 128);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static BufferedImage resize(BufferedImage img, int newW, int newH) {
        Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
        return dimg;
    }

    public String generate(boolean clearCache) {
        if (clearCache) {
            cachedBattles.clearCache(this.battle.getUser());
        }
        return this.generate();
    }

    public String generate() {
        if (cachedBattles.isCached(this.battle.getUser())) {
            return cachedBattles.get(this.battle.getUser());
        }

        ImageGenerator generator = new ImageGenerator(400, 400);
        generator.drawBackground(Color.DARK_GRAY);

        Texture userAvatar = new Texture("userAvatar", readImage(this.battle.getUser().getEffectiveAvatarUrl()));
        generator.draw(10, 50, userAvatar);
        generator.drawString(this.battle.getUser().getName(), FONT, COLOR, 10, 40);
        generator.drawString(String.format("Health: %s", battle.getUserHealth()), FONT, COLOR, 10, 215);

        //Texture opponentTexture = this.battle.getOpponent().getType().getTexture();
        generator.draw(250, 50, userAvatar /*TMP!*/);
        generator.drawString(this.battle.getOpponent().getType().getName(), FONT, COLOR, 250, 40);
        generator.drawString(String.format("Health: %s", this.battle.getOpponent().getHealth()), FONT, COLOR, 250, 215);

        int y = 250;
        LinkedHashMap<Integer, IWeaponAction> moves = this.battle.getUserMoves();
        for (int moveIndex : moves.keySet()) {
            String moveString = String.format("%smove %s: %s", CommandManager.CMD_CHAR, moveIndex, moves.get(moveIndex).getActionName());
            generator.drawString(moveString, FONT, COLOR, 10, y);
            y += FONT.getSize();
            if (y + FONT.getSize() >= 400) {
                break;
            }
        }

        String image = generator.generate(battle.getUser());
        cachedBattles.put(this.battle.getUser(), image);
        return image;
    }
}
