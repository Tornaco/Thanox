package util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public final class JsonFormatter {

    private static final Gson GSON = new GsonBuilder()
            .disableHtmlEscaping()
            .setPrettyPrinting().create();

    private JsonFormatter() {
    }

    public static String format(String jsonString) {
        JsonParser parser = new JsonParser();
        JsonElement el = parser.parse(jsonString);
        return GSON.toJson(el);
    }

    public static String toPrettyJson(Object any) {
        return new Gson().toJson(any);
    }
}
