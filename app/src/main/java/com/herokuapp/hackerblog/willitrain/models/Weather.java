package com.herokuapp.hackerblog.willitrain.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by jason on 4/28/18.
 */

public class Weather {
    public String getLocationName() {
        return locationName;
    }

    String locationName;

    public String getWeatherStatus() {
        return weatherStatus;
    }

    String weatherStatus;
    public Weather(JSONObject jsonObject) throws JSONException {
        this.locationName = jsonObject.getString("name");
        JSONArray weatherArray = jsonObject.getJSONArray("weather");
        JSONObject weatherObj = weatherArray.getJSONObject(0);

        this.weatherStatus = weatherObj.getString("main");

    }

    public static ArrayList<Weather> fromJSONArray(JSONArray array) {
        ArrayList<Weather> results = new ArrayList<>();

        for (int x = 0; x < array.length(); x++) {
            try {
                results.add(new Weather(array.getJSONObject(x)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return results;
    }
}
