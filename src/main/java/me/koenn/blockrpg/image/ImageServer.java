package me.koenn.blockrpg.image;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.util.*;

public class ImageServer implements HttpHandler {

    public static final String SESSION = UUID.randomUUID().toString();
    public static final HashMap<Long, byte[]> images = new HashMap<>();
    public static final HashMap<Long, Integer> ids = new HashMap<>();
    public static HttpServer server;

    public static int putImage(Long user, byte[] image) {
        images.put(user, image);
        if (!ids.containsKey(user)) {
            ids.put(user, 0);
        }
        ids.put(user, ids.get(user) + 1);
        return ids.get(user);
    }

    public ImageServer() {
        try {
            server = HttpServer.create(new InetSocketAddress(8080), 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        server.createContext("/image", this);
        server.start();
    }

    private static Map<String, List<String>> getQueryParams(String url) {
        try {
            Map<String, List<String>> params = new HashMap<>();
            String[] urlParts = url.split("\\?");
            if (urlParts.length > 1) {
                String query = urlParts[1];
                for (String param : query.split("&")) {
                    String[] pair = param.split("=");
                    String key = URLDecoder.decode(pair[0], "UTF-8");
                    String value = "";
                    if (pair.length > 1) {
                        value = URLDecoder.decode(pair[1], "UTF-8");
                    }

                    List<String> values = params.computeIfAbsent(key, k -> new ArrayList<>());
                    values.add(value);
                }
            }

            return params;
        } catch (UnsupportedEncodingException ex) {
            throw new AssertionError(ex);
        }
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        Map<String, List<String>> params = getQueryParams(httpExchange.getRequestURI().toASCIIString());

        long userId = Long.parseLong(params.get("discordId").get(0));
        int id = Integer.parseInt(params.get("id").get(0));
        byte[] data = images.get(userId);

        if (id != ids.get(userId)) {
            httpExchange.sendResponseHeaders(404, 0);
            httpExchange.getResponseBody().close();
            return;
        }

        if (data == null) {
            httpExchange.sendResponseHeaders(404, 0);
            httpExchange.getResponseBody().close();
            return;
        }

        Headers headers = httpExchange.getResponseHeaders();
        headers.add("Content-Type", "image/" + ImageGenerator.FORMAT);

        httpExchange.sendResponseHeaders(200, data.length);
        OutputStream stream = httpExchange.getResponseBody();
        stream.write(data, 0, data.length);
        stream.close();

        if (ids.get(userId) == Integer.MAX_VALUE) {
            ids.put(userId, 0);
        }
    }
}
