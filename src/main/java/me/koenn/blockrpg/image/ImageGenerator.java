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

    private final BufferedImage result;
    private final Graphics2D graphics;

    public ImageGenerator(int width, int height) {
        this.result = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        this.graphics = this.result.createGraphics();
    }

    public ImageGenerator(int width, int height, boolean alpha) {
        this.result = new BufferedImage(width, height, alpha ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB);
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

    public void drawBackground(Color color) {
        for (int x = 0; x < this.result.getWidth(); x++) {
            for (int y = 0; y < this.result.getHeight(); y++) {
                this.result.setRGB(x, y, color.getRGB());
            }
        }
    }

    public String generate(User user) {
        try {
            return uploadLocal(this.result, user);
        } catch (Exception e) {
            e.printStackTrace();
            return ERROR;
        }
    }

    private String upload(BufferedImage image, User user) throws IOException, ParseException {
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

        System.out.println(object.toJSONString());

        if (!((boolean) object.get("success"))) {
            BlockRPG.getLogger().fatal(String.format("Error while uploading image, received status code %s", object.get("status")));
            return ERROR;
        }

        return (String) ((JSONObject) object.get("data")).get("link");
    }

    private String uploadLocal(BufferedImage image, User user) throws IOException {
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        ImageIO.write(image, FORMAT, byteArray);
        ImageServer.images.put(user.getIdLong(), byteArray.toByteArray());
        String url = String.format("http://play.blockgaming.org:8080/image?discordId=%s&id=%s", user.getIdLong(), ImageServer.id++);
        System.out.println(url);
        return url;
    }
}
