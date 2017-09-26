package me.koenn.blockrpg.image;

import net.dv8tion.jda.core.utils.SimpleLog;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * <p>
 * Copyright (C) Koenn - All Rights Reserved Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential Written by Koen Willemse, August 2017
 */
public class Texture {

    private static final SimpleLog logger = SimpleLog.getLog("TextureLoader");

    private BufferedImage image;
    private int[][] pixels;

    public Texture(String label, BufferedImage image) {
        this.image = image;
        if (image == null) {
            return;
        }
        this.loadPixels();
        logger.info(String.format("Successfully loaded texture \'%s\' from BufferedImage!", label));
    }

    public Texture(String label, String fileName) {
        try {
            this.image = ImageIO.read(MapGenerator.class.getClassLoader().getResource(fileName));
        } catch (Exception e) {
            logger.fatal(String.format("Error while loading texture \'%s\': %s", label, e));
            e.printStackTrace();
            return;
        }
        if (this.image == null) {
            logger.fatal(String.format("Error while loading texture \'%s\': %s", label, "Image can't be null!"));
            return;
        }
        this.loadPixels();
        logger.info(String.format("Successfully loaded texture \'%s\' from \'%s\'!", label, fileName));
    }

    public Texture resize(int newW, int newH) {
        Image tmp = this.image.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
        this.image = dimg;
        this.loadPixels();
        return this;
    }

    private void loadPixels() {
        this.pixels = new int[this.image.getWidth()][this.image.getHeight()];
        for (int xPixel = 0; xPixel < this.image.getWidth(); xPixel++) {
            for (int yPixel = 0; yPixel < this.image.getHeight(); yPixel++) {
                pixels[xPixel][yPixel] = this.image.getRGB(xPixel, yPixel);
            }
        }
    }

    public int[][] getPixels() {
        return pixels;
    }

    public BufferedImage getImage() {
        return this.image;
    }
}
