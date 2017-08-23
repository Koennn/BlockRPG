package me.koenn.blockrpg.util;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import me.koenn.blockrpg.BlockRPG;
import me.koenn.blockrpg.world.Vector2;
import me.koenn.blockrpg.world.World;
import net.dv8tion.jda.core.entities.User;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;

/**
 * <p>
 * Copyright (C) Koenn - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Koen Willemse, June 2017
 */
public class MapGenerator {

    private static final int[][] HOME_TILE = readImage("home_tile.png");
    private static final int[][] EXPLORED_TILE = readImage("explored_tile.png");
    private static final int[][] UNEXPLORED_TILE = readImage("unexplored_tile.png");
    private static final int[][] CURRENT_TILE = readImage("current_tile.png");
    private final World world;
    private final Vector2 center;

    public MapGenerator(World world, User owner) {
        this.world = world;
        this.center = BlockRPG.getInstance().getUserLocation(owner);
    }

    public String generate() {
        try {
            return upload(drawMap());
        } catch (IOException | ParseException e) {
            BlockRPG.getInstance().getLogger().severe("Error while generating image " + e);
            e.printStackTrace();
            return null;
        }
    }

    private String upload(BufferedImage image) throws IOException, ParseException {
        URL url = new URL("https://api.imgur.com/3/image");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        ImageIO.write(image, "png", byteArray);
        byte[] byteImage = byteArray.toByteArray();
        String dataImage = Base64.encode(byteImage);
        String data = URLEncoder.encode("image", "UTF-8") + "=" + URLEncoder.encode(dataImage, "UTF-8");

        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization", "Client-ID d56526adb592623");
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        conn.connect();
        StringBuilder stb = new StringBuilder();
        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
        wr.write(data);
        wr.flush();

        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        while ((line = rd.readLine()) != null) {
            stb.append(line).append("\n");
        }
        wr.close();
        rd.close();

        JSONParser parser = new JSONParser();
        JSONObject object = (JSONObject) parser.parse(stb.toString());

        return (String) ((JSONObject) object.get("data")).get("link");
    }

    private BufferedImage drawMap() {
        BufferedImage image = new BufferedImage(503, 503, BufferedImage.TYPE_INT_RGB);
        int realX, realY = 0;
        for (int y = 0; y < 10; y++) {
            realY += 3;
            realX = 3;
            for (int x = 0; x < 10; x++) {
                int py = realY;
                Vector2 tile = translate(realX, realY);
                int[][] texture = getTileTexture(tile);
                if (texture == null) {
                    continue;
                }
                for (int tx = 0; tx < 47; tx++) {
                    for (int ty = 0; ty < 47; ty++) {
                        image.setRGB(realX, realY, texture[tx][ty]);
                        realY += 1;
                    }
                    realX += 1;
                    realY = py;
                }
                realY = py;
                realX += 3;
            }
            realY += 47;
        }
        return image;
    }

    private int[][] getTileTexture(Vector2 tile) {
        if (tile.equals(this.center)) {
            return CURRENT_TILE;
        } else if (world.getTile(tile) != null && world.getTile(tile).getProperty("homeTile") != null) {
            return HOME_TILE;
        } else if (this.world.isExplored(tile)) {
            return EXPLORED_TILE;
        } else {
            return UNEXPLORED_TILE;
        }
    }

    private BufferedImage draw() {
        BufferedImage image = new BufferedImage(500, 500, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < 500; x++) {
            for (int y = 0; y < 500; y++) {
                if ((x - 1) % 50 == 0 || (x) % 50 == 0 || (x + 1) % 50 == 0 || (y - 1) % 50 == 0 || (y) % 50 == 0 || (y + 1) % 50 == 0) {
                    continue;
                }
                Vector2 tile = translate(x, y);
                image.setRGB(x, y, getTileColor(tile));
            }
        }
        return image;
    }

    private int getTileColor(Vector2 tile) {
        int red = 255 << 16;
        int green = 255 << 8;
        int blue = 255;
        int yellow = (255 << 16) | (255 << 8);
        if (tile.equals(this.center)) {
            return green;
        } else if (world.getTile(tile) != null && world.getTile(tile).getProperty("homeTile") != null) {
            return yellow;
        } else if (this.world.isExplored(tile)) {
            return blue;
        } else {
            return red;
        }
    }

    private Vector2 translate(int x, int y) {
        int cornerX = this.center.x - 4;
        int cornerY = this.center.y - 4;
        int scaledX = (x / 50) + cornerX;
        int scaledY = (y / 50) + cornerY;
        return new Vector2(scaledX, scaledY);
    }

    private static int[][] readImage(String file) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(MapGenerator.class.getClassLoader().getResource(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (image == null) {
            return new int[0][0];
        }
        int[][] pixels = new int[image.getWidth()][image.getHeight()];
        for (int xPixel = 0; xPixel < image.getWidth(); xPixel++) {
            for (int yPixel = 0; yPixel < image.getHeight(); yPixel++) {
                pixels[xPixel][yPixel] = image.getRGB(xPixel, yPixel);
            }
        }
        return pixels;
    }
}
