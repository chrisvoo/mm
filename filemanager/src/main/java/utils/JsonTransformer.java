package utils;

import com.google.gson.Gson;
import spark.ResponseTransformer;

/**
 * Utility to serialize our models/responses.
 */
public class JsonTransformer implements ResponseTransformer {

    private final Gson gson = new Gson();

    @Override
    public String render(Object model) {
        return gson.toJson(model);
    }

}