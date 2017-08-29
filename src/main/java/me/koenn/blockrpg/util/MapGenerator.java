package me.koenn.blockrpg.util;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import me.koenn.blockrpg.BlockRPG;
import me.koenn.blockrpg.image.ImageGenerator;
import me.koenn.blockrpg.image.Texture;
import me.koenn.blockrpg.world.Vector2;
import me.koenn.blockrpg.world.World;
import net.dv8tion.jda.core.entities.User;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * <p>
 * Copyright (C) Koenn - All Rights Reserved Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential Written by Koen Willemse, June 2017
 */
public class MapGenerator {

    public static final Cache<User, String> cachedMaps = new Cache<>();

    private static final Texture HOME_TILE = new Texture("home_tile", "home_tile.png");
    private static final Texture EXPLORED_TILE = new Texture("explored_tile", "explored_tile.png");
    private static final Texture UNEXPLORED_TILE = new Texture("unexplored_tile", "unexplored_tile.png");
    private static final Texture CURRENT_TILE = new Texture("current_tile", "current_tile.png");
    private final World world;
    private final Vector2 center;

    public MapGenerator(World world, User owner) {
        this.world = world;
        this.center = BlockRPG.getInstance().getUserLocation(owner);
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

    public String generate(User owner) {
        /*try {
            if (cachedMaps.isCached(owner)) {
                return cachedMaps.get(owner);
            }
            String map = upload(drawMap());
            cachedMaps.put(owner, map);
            return map;
        } catch (IOException | ParseException e) {
            BlockRPG.getInstance().getLogger().severe("Error while generating image " + e);
            e.printStackTrace();
            return null;
        }*/
        if (cachedMaps.isCached(owner)) {
            return cachedMaps.get(owner);
        }
        long totalTime = System.currentTimeMillis();
        long drawTime = System.currentTimeMillis();
        ImageGenerator generator = new ImageGenerator(503, 503);
        int realX, realY = 3;
        for (int y = 0; y < 10; y++) {
            realX = 3;
            for (int x = 0; x < 10; x++) {
                Vector2 tile = translate(realX, realY);
                generator.draw(realX, realY, getTileTexture(tile));
                realX += 50;
            }
            realY += 50;
        }
        System.out.println("Draw time: " + (System.currentTimeMillis() - drawTime) + "ms");
        String image = generator.generate(owner);
        cachedMaps.put(owner, image);
        System.out.println("Total time: " + (System.currentTimeMillis() - totalTime) + "ms");
        return image;
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
                //int[][] texture = getTileTexture(tile);
                for (int tx = 0; tx < 47; tx++) {
                    for (int ty = 0; ty < 47; ty++) {
                        //image.setRGB(realX, realY, texture[tx][ty]);
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

    private Vector2 translate(int x, int y) {
        int cornerX = this.center.x - 4;
        int cornerY = this.center.y - 4;
        int scaledX = (x / 50) + cornerX;
        int scaledY = (y / 50) + cornerY;
        return new Vector2(scaledX, scaledY);
    }

    private Texture getTileTexture(Vector2 tile) {
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
}
