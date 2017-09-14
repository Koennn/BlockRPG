package me.koenn.blockrpg.image;

import net.dv8tion.jda.core.utils.SimpleLog;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

/**
 * <p>
 * Copyright (C) Koenn - All Rights Reserved Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential Written by Koen Willemse, August 2017
 */
public class Texture {

    private static final SimpleLog logger = SimpleLog.getLog("TextureLoader");

    private final String label;
    private int[][] pixels;

    public Texture(String label, BufferedImage image) {
        this.label = label;
        if (image == null) {
            return;
        }
        this.pixels = new int[image.getWidth()][image.getHeight()];
        for (int xPixel = 0; xPixel < image.getWidth(); xPixel++) {
            for (int yPixel = 0; yPixel < image.getHeight(); yPixel++) {
                pixels[xPixel][yPixel] = image.getRGB(xPixel, yPixel);
            }
        }
    }

    public Texture(String label, String fileName) {
        this.label = label;
        BufferedImage image;
        try {
            image = ImageIO.read(MapGenerator.class.getClassLoader().getResource(fileName));
        } catch (Exception e) {
            logger.fatal(String.format("Error while loading texture \'%s\': %s", label, e));
            e.printStackTrace();
            return;
        }
        if (image == null) {
            logger.fatal(String.format("Error while loading texture \'%s\': %s", label, "Image can't be null!"));
            return;
        }
        this.pixels = new int[image.getWidth()][image.getHeight()];
        for (int xPixel = 0; xPixel < image.getWidth(); xPixel++) {
            for (int yPixel = 0; yPixel < image.getHeight(); yPixel++) {
                pixels[xPixel][yPixel] = image.getRGB(xPixel, yPixel);
            }
        }
    }

    public String getLabel() {
        return label;
    }

    public int[][] getPixels() {
        return pixels;
    }
}
