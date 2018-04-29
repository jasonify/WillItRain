package com.herokuapp.hackerblog.willitrain.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by jason on 4/28/18.
 */

public class Weather {
//    public String getLocationName() {
//        return locationName;
//    }

//    String locationName;

    public String getWeatherStatus() {
        return weatherStatus;
    }

    String weatherStatus;

    public String getDateStr() {
        return dateStr;
    }

    String dateStr;
    public Weather(JSONObject jsonObject)  {
//        this.locationName = jsonObject.getString("name");

        try {
            JSONArray weatherArray = jsonObject.getJSONArray("weather");
            JSONObject weatherObj = weatherArray.getJSONObject(0);

            try {
                dateStr = jsonObject.getString("dt_txt");

            } catch (JSONException e) {
                // e.printStackTrace();
                System.out.println("--- no dt_txt");
            }

            this.weatherStatus = weatherObj.getString("main");
        } catch (JSONException e) {
            e.printStackTrace();



        }


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
