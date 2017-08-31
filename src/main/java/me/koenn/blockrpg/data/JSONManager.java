package me.koenn.blockrpg.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class JSONManager {

    private static final String DATA_DIR = "data";

    private final JSONObject defaultBody;
    private File jsonFile;
    private JSONObject body;

    public JSONManager(String fileName) {
        this(fileName, new JSONObject());
    }

    public JSONManager(String fileName, JSONObject defaultBody) {
        if (!fileName.endsWith(".json")) {
            fileName += ".json";
        }
        File dataFolder = new File(DATA_DIR);
        if (!dataFolder.exists()) {
            dataFolder.mkdir();
        }

        this.defaultBody = defaultBody;
        this.jsonFile = new File(dataFolder, fileName);

        this.reload();
    }

    public void saveBodyToFile() {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            JsonElement je = new JsonParser().parse(this.body.toJSONString());
            FileWriter writer = new FileWriter(this.jsonFile);
            writer.write(gson.toJson(je));
            writer.flush();
            writer.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public JSONObject getBody() {
        return body;
    }

    public void setInBody(String key, Object value) {
        this.body.put(key, value);
        this.saveBodyToFile();
    }

    public Object getFromBody(String key) {
        return this.body.get(key);
    }

    public void reload() {
        if (!this.jsonFile.exists()) {
            try {
                this.jsonFile.createNewFile();
                this.body = this.defaultBody;
                this.saveBodyToFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.jsonFile = new File(this.jsonFile.getPath());

        Object obj;
        try {
            JSONParser parser = new JSONParser();
            obj = parser.parse(new FileReader(this.jsonFile));
            this.body = (JSONObject) obj;
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ParseException | ClassCastException ex) {
            ex.printStackTrace();
            this.body = this.defaultBody;
            this.saveBodyToFile();
        }
    }
}
