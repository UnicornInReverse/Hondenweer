package com.example.maaik.helloworld;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WeatherResultActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String LOG_TAG = "LocationSensation";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_weather);

        Button btnFinish = (Button)findViewById(R.id.btn_finish);
        btnFinish.setOnClickListener(this);

        //Get coordinates from WelcomeScreenActivity
        Bundle bundle = getIntent().getExtras();
        double lon = bundle.getDouble("longitude");
        double lat = bundle.getDouble("latitude");

        Log.d(LOG_TAG, "Long = " + lon + "Lat = " + lat);

        //Send coordinates to getWeather
        getWeather(lon, lat);

    }

    public void getWeather(double lon, double lat) {

        //New weather task
        GetWeatherTask task = new GetWeatherTask(this, lon, lat);

        //Start task
        task.execute();
    }

    public void setWeather(JSONObject weather) throws JSONException {
        View view = this.findViewById(android.R.id.content);

        Log.d(LOG_TAG, "Weer " + weather);

        //Extract necessary information from result
        String area = weather.getString("name");
        JSONArray array_with_weather = weather.getJSONArray("weather");
        String weather_type = array_with_weather.getJSONObject(0).getString("main");

        Log.d(LOG_TAG, "type: "+ weather_type);

        TextView tv_area = (TextView) findViewById(R.id.tv_location);

        //Replace empty name with Middle of Nowhere
        if(area.equals("")) {
            tv_area.setText(R.string.tv_no_name);
        } else {
            tv_area.setText(area);
        }
        TextView tv_weather = (TextView) findViewById(R.id.tv_weather);
        tv_weather.setText(weather_type);

        TextView tv_message = (TextView) findViewById(R.id.tv_message);

        //Modify message, background and text colour based on weather type
        if (weather_type.equals("Drizzle") || weather_type.equals("Rain")|| weather_type.equals("Clouds") || weather_type.equals("Snow")) {
            view.setBackgroundResource(R.drawable.rain);
            tv_message.setText(R.string.message_rain);
        }
        else {
            view.setBackgroundResource(R.drawable.sun);
            tv_message.setText(R.string.message_sun);
//            tv_area.setTextColor(313233);
//            tv_message.setTextColor(313233);
        }

    }

    @Override
    public void onClick(View v) {
        finish();
    }


}
