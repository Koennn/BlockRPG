package me.koenn.blockrpg.util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public final class SkinRenderer {

    public static String getSkinURL(String username) {
        return String.format("https://crafatar.com/renders/body/%s?overlay=true", username);
    }

    public static BufferedImage getSkinRender(String url) {
        try {
            return ImageIO.read(new URL(url));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
