package com.herokuapp.hackerblog.willitrain;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.herokuapp.hackerblog.willitrain.models.Weather;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

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
        setUpButton();

    }

    public void setUpButton() {
        final Button button = findViewById(R.id.goBtn);
        final EditText cityET = (EditText) findViewById(R.id.cityET);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                System.out.println("getting text");
                getJSON(cityET.getText().toString());

                // Code here executes on main thread after user presses button
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
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
                    Boolean isRainining = weatherNowStr.indexOf("rain") >= 0 || weatherNowStr.indexOf("drizzle") >= 0;

                    Boolean noChange = true;

                    for (int x = 0; x < list.size(); x++) {
                        Weather w = list.get(x);
                        System.out.println(w.getWeatherStatus());

                        String compareWeather = w.getWeatherStatus().toLowerCase();

                        Boolean isRainingCurrent = compareWeather.indexOf("rain") >= 0 || compareWeather.indexOf("drizzle") >= 0;

                        // Exit if rain

                        if (isRainining != isRainingCurrent) {
                            upcomingWeatherMatches = w;
                            System.out.println(x);
                            x = list.size() + 1;
                            noChange = false;
                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    String upcomingWeather = null;

                                    if (upcomingWeatherMatches.getWeatherStatus().contains("Rain") || upcomingWeatherMatches.getWeatherStatus().contains("Drizzle")) {
                                        upcomingWeather = "rain";
                                        String string = upcomingWeatherMatches.getDateStr();
                                        final String previousFormat = "yyyy-MM-dd hh:mm:ss";
                                        final String NEW_FORMAT = "dd";
                                        DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                                        try {
                                            SimpleDateFormat sdf = new SimpleDateFormat(previousFormat);
                                            Date d = sdf.parse(string);
                                            sdf.applyPattern(NEW_FORMAT);
                                            String newDateString = sdf.format(d);
                                            int daysFromToday = Integer.parseInt(newDateString);
                                            daysFromToday -= 1;
                                            if (daysFromToday == 0) {
                                                upcomingWeatherTV.setText("It looks like it will " + upcomingWeather + " today." + "\nGet that umbrella out!");
                                            } else if (daysFromToday == 1) {
                                                upcomingWeatherTV.setText("It looks like it will " + upcomingWeather + " tomorrow." + "\nYou might want to buy an umbrella.");
                                            } else {
                                                upcomingWeatherTV.setText("It will rain in " + daysFromToday + " days.");
                                            }
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                    } else {
                                        upcomingWeatherTV.setText("No rain within the next few days.");
                                    }

                                    // Stuff that updates the UI

                                }
                            });

                        }



                    }

                    if (noChange) {
                        upcomingWeatherTV.setText("There's no change in the foreseeable future");
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

    @SuppressLint("StaticFieldLeak")
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
                            currentWeatherTV.setText("Current weather condition: " + weatherNow.getWeatherStatus());

                            // Stuff that updates the UI

                        }
                    });



                    getPrediction(city);

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
