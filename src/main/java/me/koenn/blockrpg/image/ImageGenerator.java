package me.koenn.blockrpg.image;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import me.koenn.blockrpg.BlockRPG;
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

/**
 * <p>
 * Copyright (C) Koenn - All Rights Reserved Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential Written by Koen Willemse, August 2017
 */
public class ImageGenerator {

    public static final String ENCODING = "UTF-8";
    public static final String FORMAT = "png";
    public static final String ERROR = "https://i.imgur.com/rhcd3l7.png";
    public static final String LOCAL_URL = "http://play.blockgaming.org:8080/image?discordId=%s&id=%s&session=%s";

    private final BufferedImage result;
    private final Graphics2D graphics;

    public ImageGenerator(int width, int height) {
        this.result = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        this.graphics = this.result.createGraphics();
    }

    public void draw(int x, int y, Texture texture) {
        int[][] pixels = texture.getPixels();
        for (int dx = 0; dx < pixels.length; dx++) {
            for (int dy = 0; dy < pixels[0].length; dy++) {
                if (pixels[dx][dy] == 0) {
                    continue;
                }
                this.result.setRGB(x + dx, y + dy, pixels[dx][dy]);
            }
        }
    }

    public void drawString(String string, Font font, Color color, int x, int y) {
        this.graphics.setFont(font);
        this.graphics.setColor(color);
        this.graphics.drawString(string, x, y);
    }

    public void clearSquare(int x, int y, int width, int height) {
        this.graphics.clearRect(x, y, width, height);
    }

    public String generate(User user) {
        try {
            return BlockRPG.ENABLE_LOCAL_SERVER ? uploadLocal(this.result, user) : upload(this.result);
        } catch (Exception e) {
            e.printStackTrace();
            return ERROR;
        }
    }

    private String upload(BufferedImage image) throws IOException, ParseException {
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        ImageIO.write(image, FORMAT, byteArray);
        String dataImage = Base64.encode(byteArray.toByteArray());
        String data = URLEncoder.encode("image", ENCODING) + "=" + URLEncoder.encode(dataImage, ENCODING);

        HttpURLConnection conn = (HttpURLConnection) new URL("https://api.imgur.com/3/image").openConnection();

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

        if (!((boolean) object.get("success"))) {
            BlockRPG.getLogger().fatal(String.format("Error while uploading image, received status code %s", object.get("status")));
            return ERROR;
        }

        return (String) ((JSONObject) object.get("data")).get("link");
    }

    private String uploadLocal(BufferedImage image, User user) throws IOException {
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        ImageIO.write(image, FORMAT, byteArray);
        int id = ImageServer.putImage(user.getIdLong(), byteArray.toByteArray());
        return String.format(LOCAL_URL, user.getIdLong(), id, ImageServer.SESSION);
    }
}
