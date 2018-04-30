package com.herokuapp.hackerblog.willitrain;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.herokuapp.hackerblog.willitrain.models.Weather;
//import com.herokuapp.hackerblog.willitrain.models.LocationServices;

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
        //LocationServices currentLocation = new LocationServices();
        //LocationServices city = currentLocation;

        getJSON("Denver");

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
                    Boolean isRainining = weatherNowStr.indexOf("rain") >= 0;

                    for (int x = 0; x < list.size(); x++) {
                        Weather w = list.get(x);
                        System.out.println(w.getWeatherStatus());
                        String compareWeather = w.getWeatherStatus().toLowerCase();

                        Boolean isRainingCurrent = compareWeather.indexOf("rain") >= 0;


//                        public void sendNotification(String message, String title, Intent intent, int not_id) {
//                            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
//                                    PendingIntent.FLAG_ONE_SHOT);
//                            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//                            NotificationCompat.Builder notification;
//                            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
//                                notification
//                                        = new NotificationCompat.Builder(this)
//                                        .setSmallIcon(R.mipmap.ic_launcher_round)
//                                        .setStyle(new NotificationCompat.BigTextStyle().bigText("Rain, Rain go Away Come Again Some Other Day"))
//                                        .setContentTitle("It's Raining")
//                                        .setPriority(NotificationCompat.PRIORITY_HIGH)
//                                        .setContentText("Better get out your umbrella")
//                                        .setAutoCancel(true)
//                                        .setSound(defaultSoundUri)
//                                        .setContentIntent(pendingIntent);
//
//                            } else {
//                                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.app_icon);
//                                notification
//                                        = new NotificationCompat.Builder(this)
//                                        .setContentTitle("It's Raining")
//                                        .setSmallIcon(R.drawable.ic_launcher_background)
//                                        .setStyle(new NotificationCompat.BigTextStyle().bigText("Rain, Rain go Away Come Again Some Other Day"))
//                                        .setContentText("Better get out your umbrella")
//                                        .setAutoCancel(true)
//                                        //.setColor(Color.parseColor("#1a4994"))
//                                        .setPriority(NotificationCompat.PRIORITY_HIGH)
//                                        .setLargeIcon(bitmap)
//                                        .setSound(defaultSoundUri)
//                                        .setContentIntent(pendingIntent);
//                            }
//                            NotificationManager notificationManager =
//                                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//                            notificationManager.notify(not_id, notification.build());
//                        }
//
//                        public void initChannels(Context context) {
//                            if (Build.VERSION.SDK_INT < 26) {
//                                return;
//                            }
//                            NotificationManager notificationManager =
//                                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//                            NotificationChannel channel = new NotificationChannel("default",
//                                    "Channel name",
//                                    NotificationManager.IMPORTANCE_DEFAULT);
//                            channel.setDescription("Channel description");
//                            notificationManager.createNotificationChannel(channel);
//                        }


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

                    // GET GPS DATA

                    // GET CITY NAME FROM USER AND TEST AGAINST API
                    // PARSE RAIN DATA INTO HUMAN READABLE FORMAT / DATE

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
