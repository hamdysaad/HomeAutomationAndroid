package com.gp.smarthome.hamdy.smarthomegp.Utilities;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Hamdy on 5/4/2017.
 */

public class ResponseParserUtils {
    private final String DATA_KEY = "data";
    private final String RESULT_KEY = "result";

    public final String response;
    public boolean result;
    public int errrorCode;
    public JSONObject data;

    public ResponseParserUtils(String response) throws JSONException {

        this.response = response;

        parse();

    }

    private void parse() {

        JSONObject jo = null;
        try {
            jo = new JSONObject(response);


            result = jo.getBoolean(RESULT_KEY);
            data = jo.getJSONObject(DATA_KEY);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
