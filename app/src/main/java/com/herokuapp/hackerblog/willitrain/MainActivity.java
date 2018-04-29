package com.herokuapp.hackerblog.willitrain;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.herokuapp.hackerblog.willitrain.models.Weather;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    JSONObject data = null;
    Weather weatherNow;
    Weather upcomingWeatherMatches;

    TextView currentWeatherTV;
    TextView upcomingWeatherTV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currentWeatherTV = (TextView) findViewById(R.id.currentWeather);
        upcomingWeatherTV = (TextView) findViewById(R.id.upcomingWeather);

        getJSON("Denver");

    }

    public void getPrediction(final String city) {
        // TODO: Fill out AsyncTask
        new AsyncTask<Void, Void, Void>() {


            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }

            @Override
            // TODO: Fill out doInBackground
            protected Void doInBackground(Void... params) {
                try {
                    URL url = new URL("http://api.openweathermap.org/data/2.5/forecast?q="+city+"&APPID=222aa7d3131cb8f944331a250feeb578");
                    System.out.println(url.toString());
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                    StringBuffer json = new StringBuffer(1024);
                    String tmp = "";

                    while((tmp = reader.readLine()) != null) {
                        json.append(tmp).append("\n");
                    }
                    reader.close();

                    JSONObject data2 = new JSONObject(json.toString());

                    ArrayList<Weather> list = Weather.fromJSONArray(data2.getJSONArray("list"));
                    System.out.println("---------------------");
                    // TODO: indexOf(""):

                    String weatherNowStr =  weatherNow.getWeatherStatus().toLowerCase();
                    Boolean isRainining = weatherNowStr.indexOf("rain") >= 0;

                    for (int x = 0; x < list.size(); x++) {
                        Weather w = list.get(x);
                        System.out.println(w.getWeatherStatus());
                        String compareWeather = w.getWeatherStatus().toLowerCase();

                        Boolean isRainingCurrent = compareWeather.indexOf("rain") >= 0;


                        // Exit if rain
                        if (isRainining != isRainingCurrent) {
                                upcomingWeatherMatches = w;
                                System.out.println(x);
                                x = list.size() + 1;

                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {

                                    upcomingWeatherTV.setText(upcomingWeatherMatches.getWeatherStatus() + " @" + upcomingWeatherMatches.getDateStr());

                                    // Stuff that updates the UI

                                }
                            });

                        }

                    }

                    if(data2.getInt("cod") != 200) {
                        System.out.println("===========Error...");
                        return null;
                    }

                } catch (Exception e) {
                    System.out.println("Exception !!!!!!");
                    System.out.println("Exception "+ e.getMessage());
                    return null;
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void Void) {
                if(data != null){
                    Log.d("Weather", data.toString());
                }
            }
        }.execute();

    }

    public void getJSON(final String city) {
        // TODO: Fill out AsyncTask
        new AsyncTask<Void, Void, Void>() {


            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }

            @Override
            // TODO: Fill out doInBackground
            protected Void doInBackground(Void... params) {
                try {
                    URL url = new URL("http://api.openweathermap.org/data/2.5/weather?q="+city+"&APPID=222aa7d3131cb8f944331a250feeb578");
                    System.out.println(url.toString());
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                    StringBuffer json = new StringBuffer(1024);
                    String tmp = "";

                    while((tmp = reader.readLine()) != null) {
                        json.append(tmp).append("\n");
                    }
                    reader.close();

                    data = new JSONObject(json.toString());
                    Weather currentWeather = new Weather(data);
                    weatherNow = currentWeather;

                    if(data.getInt("cod") != 200) {
                        System.out.println("===========Error...");
                        return null;
                    }
                    System.out.println("-------------------");
                    System.out.println(currentWeather.getWeatherStatus());

                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
//                            upcomingWeatherTV.setText(upcomingWeatherMatches.getWeatherStatus());
                            currentWeatherTV.setText(weatherNow.getWeatherStatus());

                            // Stuff that updates the UI

                        }
                    });



                    getPrediction("Denver");

                } catch (Exception e) {
                    System.out.println("Exception !!!!!!");
                    System.out.println("Exception "+ e.getMessage());
                    return null;
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void Void) {
                if(data != null){
                    Log.d("Weather", data.toString());
                }
            }
        }.execute();

    }


}
