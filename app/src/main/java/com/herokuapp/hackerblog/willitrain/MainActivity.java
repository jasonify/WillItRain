package com.herokuapp.hackerblog.willitrain;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    JSONObject data = null;
    JSONObject forecastedData = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getJSON("Denver");
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
                    final TextView outputView = (TextView) findViewById(R.id.text);
                    final TextView rainOutputView = (TextView) findViewById(R.id.rainInfo);

                    // API CALL FOR CURRENT WEATHER
                    URL currentWeatherUrl = new URL("http://api.openweathermap.org/data/2.5/weather?q="+city+"&APPID=222aa7d3131cb8f944331a250feeb578");

                    // API CALL FOR FORECASTED WEATHER
                    //URL forecastWeatherUrl = new URL("http://api.openweathermap.org/data/2.5/forecast?q="+city+"&APPID=222aa7d3131cb8f944331a250feeb578");

                    // CREATE NEW HTTPURLCONNECTION FOR CURRENTWEATHER
                    HttpURLConnection connection = (HttpURLConnection) currentWeatherUrl.openConnection();

                    // CREATE NEW HTTPURLCONNECTION FOR FORECASTEDWEATHER
                    //HttpURLConnection forecastConnection = (HttpURLConnection) forecastWeatherUrl.openConnection();

                    // CREATE BUFFEREDREADER FOR CURRENTWEATHER
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                    // CREATE BUFFEREDREADER FOR FORECASTEDWEATHER
                    //BufferedReader forecastedReader = new BufferedReader(new InputStreamReader(forecastConnection.getInputStream()));

                    // CREATE STRINGBUFFER FOR CURRENTWEATHER
                    final StringBuffer json = new StringBuffer(1024);
                    String tmp = "";

                    while((tmp = reader.readLine()) != null) {
                        json.append(tmp).append("\n");
                    }
                    reader.close();

                    // CREATE STRINGBUFFER FOR FORECASTEDWEATHER
                    //final StringBuffer forecastedJSON = new StringBuffer(1048576);
                    //String forecastedTmp = "";

                   // while((forecastedTmp = reader.readLine()) != null) {
                        //forecastedJSON.append(forecastedTmp).append("\n");
                    //}
                    //forecastedReader.close();

                    // CREATE CURRENTWEATHER DATA JSONOBJECT
                    data = new JSONObject(json.toString());

                    if(data.getInt("cod") != 200) {
                        System.out.println("Error...");
                        return null;
                    }

                    // CREATE FORECASTEDWEATHER DATA JSONOBJECT
                    //forecastedData = new JSONObject(forecastedJSON.toString());

                    //if(forecastedData.getInt("cod") != 200) {
                        //System.out.println("Error...");
                        //return null;
                    //}

                    MainActivity.this.runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            String words[] = json.toString().split("[^\\w']+");
                            //String forecastedWords[] = forecastedJSON.toString().split("[^\\w']+");
                            if (words[14].contains("broken")) {
                                outputView.setText("It's Party Cloudy");
                            }
                            if (words[14].contains("clear")) {
                                outputView.setText("It's a Clear Sky");
                            }
                            if (words[14].contains("few")) {
                                outputView.setText("It's just a little cloudy");
                            }
                            if (words[14].contains("light")) {
                                outputView.setText("It's raining");
                            }
                            if (words[14].contains("overcast")) {
                                outputView.setText("It's an overcast");
                            }
                            if (words[14].contains("scattered")) {
                                outputView.setText("It looks like scattered clouds");
                            }

                            //if (forecastedWords.toString().contains("Rain") || forecastedWords.toString().contains("Drizzle")) {
                                //rainOutputView.setText("Bad news, it'll rain within the next 16 days");
                            //} else {
                                //rainOutputView.setText("Great news, no rain in sight!");
                            //}

                        }
                    });


                } catch (Exception e) {

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
