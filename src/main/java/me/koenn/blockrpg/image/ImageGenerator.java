package me.koenn.blockrpg.image;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
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
 * Proprietary and confidential Written by Koen Willemse, August 2017
 */
public class ImageGenerator {

    private final BufferedImage result;

    public ImageGenerator(int width, int height) {
        this.result = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    }

    public ImageGenerator(int width, int height, boolean alpha) {
        this.result = new BufferedImage(width, height, alpha ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB);
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

    public String generate() {
        try {
            return upload(this.result);
        } catch (Exception e) {
            e.printStackTrace();
            return "https://blog.sqlauthority.com/wp-content/uploads/2015/10/errorstop.png";
        }
    }

    private String upload(BufferedImage image) throws IOException, ParseException {
        long encodeTime = System.currentTimeMillis();

        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        ImageIO.write(image, "png", byteArray);
        byte[] byteImage = byteArray.toByteArray();
        String dataImage = Base64.encode(byteImage);
        String data = URLEncoder.encode("image", "UTF-8") + "=" + URLEncoder.encode(dataImage, "UTF-8");

        System.out.println("Encode time: " + (System.currentTimeMillis() - encodeTime) + "ms");

        long uploadTime = System.currentTimeMillis();

        URL url = new URL("https://api.imgur.com/3/image");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

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

        System.out.println("Upload time: " + (System.currentTimeMillis() - uploadTime) + "ms");
        return (String) ((JSONObject) object.get("data")).get("link");
    }
}
