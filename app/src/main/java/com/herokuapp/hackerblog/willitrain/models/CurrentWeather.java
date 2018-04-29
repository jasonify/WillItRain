package com.herokuapp.hackerblog.willitrain.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jason on 4/28/18.
 */

public class CurrentWeather {
    String locationName;
    public CurrentWeather(JSONObject jsonObject) throws JSONException {
        this.locationName = jsonObject.getString("name");
        JSONObject weatherObj = jsonObject.getJSONObject("weather");
    }
}
