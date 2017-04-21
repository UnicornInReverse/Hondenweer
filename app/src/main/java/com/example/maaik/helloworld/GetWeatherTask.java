package com.example.maaik.helloworld;


import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

class GetWeatherTask extends AsyncTask<Object, Object, JSONObject> {

    private static final String LOG_TAG = "GetWeather";
    private WeakReference<WeatherResultActivity> mainActivityWeakReference;
    private double longitude = 0;
    private double latitude = 0;

    GetWeatherTask(WeatherResultActivity weatherResultActivity, double lon, double lat) {
        //Reference to WeatherResultActivity to send back the result
        mainActivityWeakReference = new WeakReference<>(weatherResultActivity);
        //Get coordinates
        this.longitude = lon;
        this.latitude = lat;

    }

    @Override
    protected JSONObject doInBackground(Object... params) {
        Log.d(LOG_TAG, "Asynchroonzwemmen " + longitude + " " + latitude);

        //Default
        JSONObject result = new JSONObject();

        HttpURLConnection con = null;

        try {
            //Build query for API  call

            URL url = new URL(
                    "http://api.openweathermap.org/data/2.5/weather?lat=" + latitude + "&lon=" + longitude + "&APPID=508c90a9a30311d3488084d48395fa5c");
            con = (HttpURLConnection) url.openConnection();

            con.setReadTimeout(10000);
            con.setConnectTimeout(15000);
            con.setRequestMethod("GET");

            con.setDoInput(true);

            //Start query
            con.connect();

            //Read results
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(con.getInputStream(), "UTF-8"));
            String payload = reader.readLine();
            reader.close();


            //Get the result
            result = new JSONObject(payload);

        } catch (IOException e) {
            Log.e(LOG_TAG, "IOException", e);
        } catch (JSONException e) {
            Log.e(LOG_TAG, "JSONException", e);
        } catch (Exception e) {
            Log.e(LOG_TAG, "Oeps stuk");
        } finally {
            if (con != null) {
                con.disconnect();
            }
        }

        return result;
    }

    // Executed when background task has finished
    @Override
    protected void onPostExecute(JSONObject result) {
        //Send result to WeatherResultActivity

        try {
            mainActivityWeakReference.get().setWeather(result);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}