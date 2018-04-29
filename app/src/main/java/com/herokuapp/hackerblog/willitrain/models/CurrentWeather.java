package com.herokuapp.hackerblog.willitrain.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jason on 4/28/18.
 */

public class CurrentWeather {
    public String getLocationName() {
        return locationName;
    }

    String locationName;

    public String getWeatherStatus() {
        return weatherStatus;
    }

    String weatherStatus;
    public CurrentWeather(JSONObject jsonObject) throws JSONException {
        this.locationName = jsonObject.getString("name");
        JSONArray weatherArray = jsonObject.getJSONArray("weather");
        JSONObject weatherObj = weatherArray.getJSONObject(0);

        this.weatherStatus = weatherObj.getString("main");

    }
}
